package FileSystem;

import Clothes.Costume;
import NetStuff.TransferPackage;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import mainpkg.Main;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *Класс для дешифровки команд и их выполнения.
 */
public enum Command {

    @SuppressWarnings("unchecked")
    REMOVE((command,transferPackage) ->{
        try{
            String strData = "";
            for(Object s : command.data.toArray()) strData += s.toString();
            if(strData.length() == 0) {
                command.setData(Stream.of(new TransferPackage(-1, "Команда не выполнена.", null,
                        "Отсутствует аргумент команды!".getBytes(Main.DEFAULT_CHAR_SET))));
                return;
            }
            JSONObject jsonObject = new JSONObject(strData);
            Costume costume = new Costume(jsonObject);

            command.setData(null);
            Stream<Costume> stream = transferPackage.getData().filter(p -> !p.equals(costume));
            HashSet<Costume> collection = new HashSet<>();
            stream.sequential().collect(Collectors.toCollection(() -> collection));

            command.setData(Stream.of(new TransferPackage(1, "Команда выполнена.", null,
                    CollectionManager.getXmlFromCollection(collection).getBytes(Main.DEFAULT_CHAR_SET))));

            System.out.println("Команда выполнена.");
        }
        catch (JSONException e){
            command.setData(Stream.of(new TransferPackage(-1, "Команда не выполнена.", null,
                    "Аргумент команды неверный!".getBytes(Main.DEFAULT_CHAR_SET))));
        }

    }), ///Done

    SHOW((command,transferPackage)->{
        command.setData(null);

        HashSet<Costume> collection = new HashSet<>();

        transferPackage.getData().sequential().collect(Collectors.toCollection(() -> collection));

        String output = "";

        for(Costume p : collection){
            output += p.toString() + "\t";
        }

        command.setData(Stream.of(new TransferPackage(2, "Команда выполнена.",null, output.getBytes(Main.DEFAULT_CHAR_SET))));

        System.out.println("Команда выполнена.");
    }),  ///Done
    ADD_IF_MAX((command,transferPackage)->{
        try {

            // Now data has a Costume object in json format
            String strData = "";
            for (Object s : command.data.toArray()) strData += s.toString();
            if(strData.length() == 0) {
                command.setData(Stream.of(new TransferPackage(-1, "Команда не выполнена.", null,
                        "Отсутствует аргумент команды!".getBytes(Main.DEFAULT_CHAR_SET))));
                return;
            }
            JSONObject jsonObject = new JSONObject(strData);
            Costume costume = new Costume(jsonObject);
            command.setData(null); //Now data is null

            Object[] dataArray = transferPackage.getData().toArray();

            Costume[] costumes= new Costume[dataArray.length];

            for(int i = 0; i < dataArray.length; i++)
                costumes[i] = (Costume)dataArray[i];

            //Create collection
            HashSet<Costume> collection = new HashSet<>();

            Stream<Costume> costumeStream = Stream.of(costumes);
            Stream<Costume> dataStream = Stream.of(costumes);
            Costume maxCostume = costumeStream.max(Costume::compareTo).get();
            Stream<Costume> stream;
            if (costume.compareTo(maxCostume) > 0) {
                stream = Stream.concat(dataStream, Stream.of(costume));
            }
            else
                stream = transferPackage.getData();

            //Fill collection
            stream.sequential().collect(Collectors.toCollection(() -> collection));

            command.setData(Stream.of(new TransferPackage(3, "Команда выполнена.", null,
                    CollectionManager.getXmlFromCollection(collection).getBytes(Main.DEFAULT_CHAR_SET))));
            // Now data has Transfer Package for sending
            System.out.println("Команда выполнена.");
        }
        catch (JSONException e){
            command.setData(Stream.of(new TransferPackage(-1, "Команда не выполнена.", null,
                    "Аргумент команды неверный!".getBytes(Main.DEFAULT_CHAR_SET))));
        }
    }), ///Done
    LOAD((command,manager)->{
        command.setData(Stream.of(new TransferPackage(4, "Команда выполнена.", null, "Load collection to server".getBytes(Main.DEFAULT_CHAR_SET))));
        System.out.println("Команда выполнена.");
    }),
    INFO((command,transferPackage)->{

        HashSet<Costume> collection = new HashSet<>();

        ((Stream<Costume>)transferPackage.getData()).sequential().collect(Collectors.toCollection(() -> collection));

        try(ByteArrayOutputStream byteObject = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteObject)) {
            try {
                objectOutputStream.writeObject(CollectionManager.getCollectionFromXML(
                        new String(transferPackage.getAdditionalData(), Main.DEFAULT_CHAR_SET)
                ));
            } catch (EmptyFileException e) {
                System.err.println(e.getMessage());
            }
            objectOutputStream.flush();
            objectOutputStream.close();
            byteObject.close();
            command.setData(Stream.of(new TransferPackage(5, "Команда выполнена.", null,
                    String.format(
                            "Тип коллекции: %s \nТип элементов коллекции: %s\nДата инициализации: %s\nКоличество элементов: %s\nРазмер: %s байт\n",
                            collection.getClass().getName(),
                            "Clothes.Costume", new Date().toString(), collection.size(), byteObject.toByteArray().length
                    ).getBytes(Main.DEFAULT_CHAR_SET))));

        } catch (IOException e) {
            command.setData(Stream.of(new TransferPackage(-1, "Команда выполнена.",null,
            "Ошибка при определении размера памяти коллекции.".getBytes(Main.DEFAULT_CHAR_SET))));
        }
        System.out.println("Команда выполнена.");
    }),
    IMPORT((command,transferPackage)->{
        String path = "";
        for(Object s : command.data.toArray()) path += s.toString();
        if(path.length() == 0) {
            command.setData(Stream.of(new TransferPackage(-1, "Команда не выполнена.", null,
                    "Отсутствует аргумент команды!".getBytes(Main.DEFAULT_CHAR_SET))));
            return;
        }
        command.setData(Stream.of(new TransferPackage(6, "Команда выполнена.", null, path.getBytes(Main.DEFAULT_CHAR_SET))));
        System.out.println("Первый этап импорта пройден.");
    }),     ///Done

    I1A8S1D1F0G0H((command,transferPackage)->{
        try(ByteArrayInputStream bis = new ByteArrayInputStream(transferPackage.getAdditionalData());
            DataInputStream dis = new DataInputStream(bis)){
            HashSet<Costume> mainCollection = CollectionManager.getCollectionFromXML(dis.readUTF());

            mainCollection.addAll(CollectionManager.getCollectionFromXML(dis.readUTF()));

            command.setData(Stream.of(new TransferPackage(601, "Команда выполнена.", null,
                    CollectionManager.getXmlFromCollection(mainCollection).getBytes(Main.DEFAULT_CHAR_SET))));

        } catch (IOException | EmptyFileException e) {
            System.err.println(e.getMessage());
        }
    }),     ///Done

    ADD((command,transferPackage)->{
        try {
            String strData = "";
            for (Object s : command.data.toArray()) strData += s.toString();
            if(strData.length() == 0) {
                command.setData(Stream.of(new TransferPackage(-1, "Команда не выполнена.", null,
                        "Отсутствует аргумент команды!".getBytes(Main.DEFAULT_CHAR_SET))));
                return;
            }
            JSONObject jsonObject = new JSONObject(strData);
            Costume costume = new Costume(jsonObject);

            command.setData(null);
            HashSet<Costume> collection = new HashSet<>();
            Stream<Costume> stream = Stream.concat(transferPackage.getData(), Stream.of(costume));
            stream.sequential().collect(Collectors.toCollection(() -> collection));

            //collection.add(costume);

            String xml = CollectionManager.getXmlFromCollection(collection);

            command.setData(Stream.of(new TransferPackage(7, "Команда выполнена.", null, xml.getBytes(Main.DEFAULT_CHAR_SET))));
            System.out.println("Команда выполнена.");
        }
        catch (JSONException e){
            command.setData(Stream.of(new TransferPackage(-1, "Команда не выполнена.", null,
                    "Аргумент команды неверный!".getBytes(Main.DEFAULT_CHAR_SET))));
        }
    }),     ///Done
    START((command,transferPackage)->{

        HashSet<Costume> collection = new HashSet<>();

        ((Stream<Costume>)transferPackage.getData()).sequential().collect(Collectors.toCollection(() -> collection));

        if (collection.size() != 0 &&
                collection.size() >= 5) {
            command.setData(Stream.of(new TransferPackage(8, "Команда выполнена.", collection.stream())));
            //new Main().program.start(manager.getCollectionCase());
        }
        else{
            command.setData(Stream.of(new TransferPackage(-1, "Команда не выполнена.", null, "Причина: Количество объектов в коллекции ниже, чем минимальное. P.S. Минимальное количество равно пяти.".getBytes(Main.DEFAULT_CHAR_SET))));
         //   System.err.println("Ошибка: для запуска программы необходимо хотя бы 5 элементов в коллекции.");
        }
    }),
    EXIT((command,transferPackage)->{
        command.setData(Stream.of(new TransferPackage(9, "Команда выполнена.", null, "null".getBytes(Main.DEFAULT_CHAR_SET))));
        System.out.println("Команда выполнена.");
    }),     ///Done
    HELP((command,transferPackage)->{
        command.setData(Stream.of(new TransferPackage(11, "Команда выполнена.", null,
                ("remove {element}: удалить элемент из коллекции по его значению\n" +
                "show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add_if_max {element}: добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                "load: перечитать коллекцию из файла\n" +
                "info: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "import {String path}: добавить в коллекцию все данные из файла\n" +
                "add {element}: добавить новый элемент в коллекцию\n" +
                "start: начать выполнение программы\n" +
                "exit: выйти из программмы\n" +
                "change_def_file_path {String path}: меняет путь к файлу с коллекцией на новый.\n" +
                "trimToMin: обрезает коллекцию до минимума.\n" +
                "help: справка \n" +
                "Пример правильного формата ввода объекта json: \n" +
                "{\"topClothes\":{\"growth_sm\":170,\"size\":50,\"color\":\"White\",\"material\":\"Chlopoc\",\"is_hood\":false,\"name\":\"T-Shirt\",\"is_for_man\":true,\"hand_sm_length\":60},\"downClothes\":{\"size\":50,\"color\":\"Black\",\"material\":\"Chlopoc\",\"diametr_leg_sm\":40,\"name\":\"Trousers\",\"leg_length_sm\":70,\"is_for_man\":true},\"underwear\":{\"sex_lvl\":100,\"size\":50,\"color\":\"Red\",\"material\":\"Chlopoc\",\"name\":\"Panties\",\"is_for_man\":true},\"hat\":{\"cylinder_height_sm\":15,\"size\":50,\"color\":\"White\",\"material\":\"Len\",\"visor_length_sm\":20,\"name\":\"BaseballHat\",\"is_for_man\":true},\"shoes\":{\"is_shoelaces\":true,\"size\":38,\"color\":\"White\",\"material\":\"Leather\",\"outsole_material\":\"Rubber\",\"name\":\"Sneackers\",\"is_for_man\":true}}"
        ).getBytes())));
    }),     ///Done
    CHANGE_DEF_FILE_PATH(((command,transferPackage) -> {
        String strData = "";
        for(Object s : command.data.toArray()) strData += s.toString();
        if(strData.length() == 0) {
            command.setData(Stream.of(new TransferPackage(-1, "Команда не выполнена.", null,
                    "Отсутствует аргумент команды!".getBytes(Main.DEFAULT_CHAR_SET))));
            return;
        }
        command.setData(Stream.of(new TransferPackage(10, "Команда выполнена.", null, strData.getBytes(Main.DEFAULT_CHAR_SET))));
    })),
    TRIMTOMIN((command,transferPackage) -> {
        HashSet<Costume> collection = new HashSet<>();

        Stream<Costume> stream = transferPackage.getData().limit(5);

        stream.sequential().collect(Collectors.toCollection(() -> collection));

        command.setData(Stream.of(new TransferPackage(7, "Команда выполнена.", null,
                CollectionManager.getXmlFromCollection(collection).getBytes(Main.DEFAULT_CHAR_SET))));
        System.out.println("Команда выполнена.");
    });     ///Done

    Command(ICommand cmd){
        this.cmd = cmd;
    }

    /**Тело выполняемой команды.*/
    private ICommand cmd;

    /**Данные, с которыми оперирует команда.*/
    private Stream data;

    private TransferPackage sendingPackage;

    private void setData(Stream data) {
        this.data = data;
    }

    /**
     * Метод для дешифровки команды, представленной в строке формата json.
     * @author Валерий Бондарь
     * @param jsonInput - строка формата json
     */
    public static Command parseCmd(String jsonInput) throws IllegalArgumentException{

        String jsonRegex = "{\"topClothes\":{\"growth_sm\":(\\d+),\"size\":(\\d+),\"color\":\"(White|Black|Green|Purple|Blonde|Blue|Red|Orange|Gray|Brown)\",\"material\":\"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)\",\"is_hood\":(true|false),\"name\":\"T-Shirt\",\"is_for_man\":(true|false),\"hand_sm_length\":(\\d+)},\"downClothes\":{\"size\":(\\d+),\"color\":\"(White|Black|Green|Purple|Blonde|Blue|Red|Orange|Gray|Brown)\",\"material\":\"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)\",\"diametr_leg_sm\":(\\d+),\"name\":\"Trousers\",\"leg_length_sm\":(\\d+),\"is_for_man\":(true|false)},\"underwear\":{\"sex_lvl\":(\\d+),\"size\":(\\d+),\"color\":\"(White|Black|Green|Purple|Blonde|Blue|Red|Orange|Gray|Brown)\",\"material\":\"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)\",\"name\":\"Panties\",\"is_for_man\":(true|false)},\"hat\":{\"cylinder_height_sm\":(\\d+),\"size\":(\\d+),\"color\":\"(White|Black|Green|Purple|Blonde|Blue|Red|Orange|Gray|Brown)\",\"material\":\"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)\",\"visor_length_sm\":(\\d+),\"name\":\"BaseballHat\",\"is_for_man\":(true|false)},\"shoes\":{\"is_shoelaces\":(true|false),\"size\":(\\d+),\"color\":\"(White|Black|Green|Purple|Blonde|Blue|Red|Orange|Gray|Brown)\",\"material\":\"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)\",\"outsole_material\":\"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)\",\"name\":\"Sneackers\",\"is_for_man\":(true|false)}}";
        String dataCommandRegex = "(remove|add_if_max|import|add|change_def_file_path) \\{.}";
        String nodataCommandRegex = "show|load|info|start|exit|help";

        if(jsonInput.matches(dataCommandRegex)){
            String cmd = findMatches("(remove|add_if_max|import|add|change_def_file_path)", jsonInput).get(0).toUpperCase();
            String data;
            if(cmd.equals("IMPORT")){
                data = jsonInput.split(" ")[1].substring(1, jsonInput.split(" ")[1].length() - 2);
            } else{
                data = findMatches(jsonRegex, jsonInput).get(0);
            }
            Command command = Command.valueOf(cmd);
            command.setData(Stream.of(data));
            return command;
        }else if(jsonInput.matches(nodataCommandRegex)){
            Command command = Command.valueOf(jsonInput.toUpperCase());
            return command;
        }else{
            return null;
        }
 //        if(jsonInput.contains("{")){
//            if (jsonInput.contains("}")) {
//                Command command = null;
//                try {
//                     command = Command.valueOf(jsonInput.split(Pattern.quote("{"))[0].replace(" ", "").toUpperCase());
//                }
//                catch (IllegalArgumentException e){ }
//                if(command != null){
//                    String data = jsonInput.substring(command.toString().length() + 2, jsonInput.length() - 1);
//                    command.data = Stream.of(data);
//                }
//                return command;
//            }
//            else
//                return null;
//        }
//        else{
//            String mbCmd = jsonInput.replace(" ", "").toUpperCase();
//            Command command = null;
//            try {
//                command = Command.valueOf(mbCmd);
//            }
//            catch (IllegalArgumentException e){}
//            if(command == Command.REMOVE || command == Command.ADD || command == Command.ADD_IF_MAX ||
//                    command == Command.CHANGE_DEF_FILE_PATH || command == Command.IMPORT){
//                return null;
//            }
//            else {
//            return command;
//            }
//        }

    }

    /**
     * Метод для дешифровки команды, представленной в строке формата json.
     * @author Валерий Бондарь
     */
    @SuppressWarnings("unchecked")
    public TransferPackage start(Command command,TransferPackage transferPackage){
        try {
            this.cmd.start(command,transferPackage);
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.getMessage());
        }
        Object obj = data.findFirst().orElse(null);
        return (TransferPackage) obj ;
    }

    public static ArrayList<String> findMatches(String patterStr, String text){
        Pattern pattern = Pattern.compile(patterStr);
        Matcher matcher = pattern.matcher(text);
        ArrayList<String> collection = new ArrayList<>();
        while(matcher.find()){
            collection.add(text.substring(matcher.start(), matcher.end()));
        }
        return collection;
    }
}
