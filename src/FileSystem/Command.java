package FileSystem;

import Clothes.Costume;
import NetStuff.Server.TransferPackage;
import NetStuff.Server.User;
import mainpkg.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
            SocketAddress adress = command.getAddress();

            User user = transferPackage.getUser();

            Stream<Pair<Costume, String>> userStream = command.getObjectsHashSet().stream().filter(p -> p.getValue().equals(user.getLogin()) && !p.getKey().equals(costume));
            Stream<Pair<Costume, String>> otherStream = command.getObjectsHashSet().stream().filter(p -> !p.getValue().equals(user.getLogin()));
            HashSet<Pair<Costume, String>> userCostumes = new HashSet<>();
            HashSet<Pair<Costume, String>> otherCostumes = new HashSet<>();
            userStream.sequential().collect(Collectors.toCollection(() -> userCostumes));
            otherStream.sequential().collect(Collectors.toCollection(()->otherCostumes));
            userCostumes.addAll(otherCostumes);
            command.getObjectsHashSet().clear();
            command.getObjectsHashSet().addAll(userCostumes);

            Main.writeCollection(Main.getObjectsHashSet());

            HashSet<Costume> costumes = new HashSet<>();
            userCostumes.forEach(p-> costumes.add(p.getKey()));
            command.setData(Stream.of(new TransferPackage(1, "Команда выполнена.", null)));
            System.out.println("Команда выполнена.");
        }
        catch (JSONException e){
            command.setData(Stream.of(new TransferPackage(-1, "Команда не выполнена.", null,
                    "Аргумент команды неверный!".getBytes(Main.DEFAULT_CHAR_SET))));
        }

    }),

    SHOW((command,transferPackage)->{
        command.setData(null);
        User user = transferPackage.getUser();
        ConcurrentHashMap<User, SocketAddress> onlineUsers = UsersVariables.onlineUsers;
        ConcurrentHashMap<User, SocketAddress> users = UsersVariables.users;

        Set<Pair<Costume, String>> userStream = command.getObjectsHashSet().stream().filter(p -> p.getValue().equals(user.getLogin())).collect(Collectors.toSet());
        final String[] output = {""};

        userStream.forEach(p -> output[0] += p.getKey().toString() + "\t");

        command.setData(Stream.of(new TransferPackage(2, "Команда выполнена.",null, output[0].getBytes(Main.DEFAULT_CHAR_SET))));

        System.out.println("Команда выполнена.");
    }),
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

            //Create collection

            User user = transferPackage.getUser();

            Stream<Pair<Costume, String>> userStream = command.getObjectsHashSet().stream().filter(p -> p.getValue().equals(user.getLogin()));
            Pair<Costume, String> maxCostume = command.getObjectsHashSet().stream().filter(p -> p.getValue().equals(user.getLogin())).max(Comparator.comparing(p->p.getKey().hashCode())).get();
            Stream<Pair<Costume, String>> otherStream= command.getObjectsHashSet().stream().filter(p -> !p.getValue().equals(user.getLogin()));



            Stream<Pair<Costume, String>> stream;
            if (costume.compareTo(maxCostume.getKey()) > 0) {
                stream = Stream.concat(userStream, Stream.of(new Pair<Costume,String>(costume, user.getLogin())));
            }
            else
                stream = userStream;

            HashSet<Pair<Costume, String>> userCostumes = new HashSet<>();
            HashSet<Pair<Costume, String>> otherCostumes = new HashSet<>();
            stream.sequential().collect(Collectors.toCollection(() -> userCostumes));
            otherStream.sequential().collect(Collectors.toCollection(()->otherCostumes));
            userCostumes.addAll(otherCostumes);
            command.getObjectsHashSet().clear();
            command.getObjectsHashSet().addAll(userCostumes);

            Main.writeCollection(Main.getObjectsHashSet());

            command.setData(Stream.of(new TransferPackage(3, "Команда выполнена.", null)));
            // Now data has Transfer Package for sending
            System.out.println("Команда выполнена.");
        }
        catch (JSONException e){
            command.setData(Stream.of(new TransferPackage(-1, "Команда не выполнена.", null,
                    "Аргумент команды неверный!".getBytes(Main.DEFAULT_CHAR_SET))));
        }
    }),
    LOAD((command,transferPackage)->{
        User user = transferPackage.getUser();
        Stream<Pair<Costume, String>> concatStream = Stream.concat(command.getObjectsHashSet().stream(), transferPackage.getData().map(p -> new Pair<>(p, user.getLogin())));
        HashSet<Pair<Costume, String>> set = new HashSet<>(concatStream.collect(Collectors.toSet()));
        command.getObjectsHashSet().addAll(set);
        command.setData(Stream.of(new TransferPackage(4, "Команда выполнена.", null, "Load collection to server".getBytes(Main.DEFAULT_CHAR_SET))));
        Main.writeCollection(Main.getObjectsHashSet());
        System.out.println("Команда выполнена.");
    }),
    INFO((command,transferPackage)->{

        HashSet<Costume> collection = new HashSet<>();

        User user = transferPackage.getUser();

        Stream<Pair<Costume, String>> userStream = command.getObjectsHashSet().stream().filter(p -> p.getValue().equals(user.getLogin()));

        userStream.map(Pair::getKey).collect(Collectors.toCollection(()->collection));

        try(ByteArrayOutputStream byteObject = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteObject)) {

            objectOutputStream.writeObject(collection);
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
    }),

    @SuppressWarnings("unchecked")
    I1A8S1D1F0G0H((command,transferPackage)->{
        System.err.println("I am hereeee");

        User user = transferPackage.getUser();

        try(ByteArrayInputStream bis = new ByteArrayInputStream(transferPackage.getAdditionalData());
            ObjectInputStream dis = new ObjectInputStream(bis)){
            HashSet<Costume> mainCollection = (HashSet<Costume>)dis.readObject();
            HashSet<Pair<Costume,String>> collection = new HashSet<>();
            mainCollection.forEach(p -> collection.add(new Pair<>(p, user.getLogin())));
            command.getObjectsHashSet().addAll(collection);
            command.setData(Stream.of(new TransferPackage(601, "Команда выполнена.", null)));

        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }),

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

            User user = transferPackage.getUser();

            Stream<Pair<Costume, String>> userStream = command.getObjectsHashSet().stream().filter(p -> p.getValue().equals(user.getLogin()));
            Stream<Pair<Costume, String>> otherStream= command.getObjectsHashSet().stream().filter(p -> !p.getValue().equals(user.getLogin()));
            Stream<Pair<Costume, String>> stream = Stream.concat(userStream, Stream.of(new Pair<>(costume, user.getLogin())));

            HashSet<Pair<Costume, String>> userCostumes = new HashSet<>();
            HashSet<Pair<Costume, String>> otherCostumes = new HashSet<>();
            stream.sequential().collect(Collectors.toCollection(() -> userCostumes));
            otherStream.sequential().collect(Collectors.toCollection(()->otherCostumes));
            userCostumes.addAll(otherCostumes);
            command.getObjectsHashSet().clear();
            command.getObjectsHashSet().addAll(userCostumes);

            Main.writeCollection(Main.getObjectsHashSet());

            command.setData(Stream.of(new TransferPackage(7, "Команда выполнена.", null)));
            System.out.println("Команда выполнена.");
        }
        catch (JSONException e){
            command.setData(Stream.of(new TransferPackage(-1, "Команда не выполнена.", null,
                    "Аргумент команды неверный!".getBytes(Main.DEFAULT_CHAR_SET))));
        }
    }),
    START((command,transferPackage)->{

        User user = transferPackage.getUser();

        HashSet<Costume> collection = new HashSet<>();

        HashSet<Pair<Costume,String>> pairs = new HashSet<>();

        command.getObjectsHashSet().stream().filter(p -> p.getValue().equals(user.getLogin())).collect(Collectors.toCollection(() -> pairs));

        pairs.forEach(p -> collection.add(p.getKey()));

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
    }),
    HELP((command,transferPackage)->{
        command.setData(Stream.of(new TransferPackage(11, "Команда выполнена.", null,
                ("В программе реализован многострочный ввод, стоп-символом является знак '$'(Например: cmd {...}$)\n" +
                "remove {element}: удалить элемент из коллекции по его значению\n" +
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
                "login {loginStr} {passwordStr}: залогиннится на сервере.(Пароль должен быть не менее 8 символов) \n" +
                "save: сохранить коллекцию с сервера\n" +
                "Пример правильного формата ввода объекта json: \n" +
                "{\"topClothes\":{\"growth_sm\":170,\"size\":50,\"color\":\"White\",\"material\":\"Chlopoc\",\"is_hood\":false,\"name\":\"T-Shirt\",\"is_for_man\":true,\"hand_sm_length\":60},\"downClothes\":{\"size\":50,\"color\":\"Black\",\"material\":\"Chlopoc\",\"diametr_leg_sm\":40,\"name\":\"Trousers\",\"leg_length_sm\":70,\"is_for_man\":true},\"underwear\":{\"sex_lvl\":100,\"size\":50,\"color\":\"Red\",\"material\":\"Chlopoc\",\"name\":\"Panties\",\"is_for_man\":true},\"hat\":{\"cylinder_height_sm\":15,\"size\":50,\"color\":\"White\",\"material\":\"Len\",\"visor_length_sm\":20,\"name\":\"BaseballHat\",\"is_for_man\":true},\"shoes\":{\"is_shoelaces\":true,\"size\":38,\"color\":\"White\",\"material\":\"Leather\",\"outsole_material\":\"Rubber\",\"name\":\"Sneackers\",\"is_for_man\":true}}"
        ).getBytes())));
    }),
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
        HashSet<Pair<Costume, String>> collection = new HashSet<>();

        User user = transferPackage.getUser();


        Stream<Pair<Costume, String>> limitStream = command.getObjectsHashSet().stream().filter(p -> p.getValue().equals(user.getLogin())).limit(5);

        limitStream.sequential().collect(Collectors.toCollection(() -> collection));

        HashSet<Pair<Costume, String>> others = new HashSet<>();
        command.getObjectsHashSet().stream().filter(p -> !p.getValue().equals(user.getLogin())).collect(Collectors.toCollection(()->others));

        command.getObjectsHashSet().clear();
        command.getObjectsHashSet().addAll(collection);
        command.getObjectsHashSet().addAll(others);

        Main.writeCollection(Main.getObjectsHashSet());

        command.setData(Stream.of(new TransferPackage(7, "Команда выполнена.", null)));
        System.out.println("Команда выполнена.");
    }),
    LOGIN(((command, transferPackage) -> {
        String logPas = (String)command.data.findFirst().get();
        String[] lpArgs = logPas.split("\\|");
        User user = new User(lpArgs[0], lpArgs[1]);
        user.setLoggedIn(true);
        final boolean[] isAlreadyExistNickname = {false};

        final boolean[] isUserOnline = {false};
        UsersVariables.onlineUsers.forEach((k,v)->{
            if (transferPackage.getUser().equals(k)){
                isUserOnline[0] = true;
            }
        });

        UsersVariables.users.forEach((k,v)->{
           if (user.getLogin().equals(k.getLogin())){
               isAlreadyExistNickname[0] = true;
           }
        });
        if(!isUserOnline[0]) {
            if (UsersVariables.users.containsKey(user)) {
                UsersVariables.onlineUsers.put(user, command.getAddress());
                command.setData(Stream.of(new TransferPackage(110, "Команда выполнена.", null,
                        new byte[]{2})));
            } else {
                if (isAlreadyExistNickname[0]) {
                    command.setData(Stream.of(new TransferPackage(-1, "Неверный пароль!", null)));
                } else {
                    UsersVariables.users.put(user, command.getAddress());
                    UsersVariables.onlineUsers.put(user, command.getAddress());
                    command.setData(Stream.of(new TransferPackage(110, "Команда выполнена.", null,
                            new byte[]{1})));
                }
            }
        }
        else{

            for(Map.Entry<User, SocketAddress> entry : UsersVariables.onlineUsers.entrySet()){
                User k = entry.getKey();
                SocketAddress v = entry.getValue();
                    if (user.getLogin().equals(k.getLogin())){
                        command.setData(Stream.of(new TransferPackage(-1, "Вы уже авторизированы!", null)));
                        break;
                    }else {
                        UsersVariables.onlineUsers.remove(k);
                        UsersVariables.onlineUsers.put(user, command.getAddress());
                        List<Pair<Costume, String>> colRmItems = Main.getObjectsHashSet().stream().filter(p -> !p.getValue().equals(k.getLogin())).collect(Collectors.toList());
                        Main.getObjectsHashSet().clear();
                        Main.getObjectsHashSet().addAll(colRmItems);
                        System.out.println("Пользователь " + k.getLogin() + " был отключён!");
                        if (UsersVariables.users.containsKey(user)) {
                            UsersVariables.onlineUsers.put(user, command.getAddress());
                            command.setData(Stream.of(new TransferPackage(110, "Команда выполнена.", null,
                                    new byte[]{2})));
                            break;
                        } else {
                            if (isAlreadyExistNickname[0]) {
                                command.setData(Stream.of(new TransferPackage(-1, "Неверный пароль!", null)));
                                break;
                            } else {
                                UsersVariables.users.put(user, command.getAddress());
                                UsersVariables.onlineUsers.put(user, command.getAddress());
                                command.setData(Stream.of(new TransferPackage(110, "Команда выполнена.", null,
                                        new byte[]{1})));
                                break;
                            }
                        }
                }
            }
        }
        UsersVariables.saveUsers();
        System.out.println(UsersVariables.users.size());
        System.out.println("Online: " + UsersVariables.onlineUsers.size());
        UsersVariables.onlineUsers.forEach((k,v)-> System.out.println(k.toString()));
    })),
    SAVE(((command, transferPackage) -> {
        HashSet<Costume> collection = new HashSet<>();

        User user = transferPackage.getUser();

        Stream<Pair<Costume, String>> userStream = command.getObjectsHashSet().stream().filter(p -> p.getValue().equals(user.getLogin()));
        userStream.map(Pair::getKey).collect(Collectors.toCollection(()->collection));

        command.setData(Stream.of(new TransferPackage(12, "Команда выполнена.", null,
                CollectionManager.getBytesFromCollection(collection))));
    }));

    Command(ICommand cmd){
        this.cmd = cmd;
    }

    /**Тело выполняемой команды.*/
    private ICommand cmd;

    /**Данные, с которыми оперирует команда.*/
    private Stream data;

    /**Адресс пользователя, заприсившего выполнения команды*/
    private SocketAddress address;

    private void setData(Stream data) {
        this.data = data;
    }

    public Set<Pair<Costume, String>> getObjectsHashSet() {
        return Main.getObjectsHashSet();
    }


    /**
     * Метод для дешифровки команды, представленной в строке формата json.
     * @author Валерий Бондарь
     * @param jsonInput - строка формата json
     */
    public static Command parseCmd(String jsonInput) throws IllegalArgumentException{

        String jsonRegex = "\\{\"topClothes\":\\{\"growth_sm\":(\\d+),\"size\":(\\d+),\"color\":\"(White|Black|Green|Purple|Blonde|Blue|Red|Orange|Gray|Brown)\",\"material\":\"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)\",\"is_hood\":(true|false),\"name\":\"(.+)\",\"is_for_man\":(true|false),\"hand_sm_length\":(\\d+)},\"downClothes\":\\{\"size\":(\\d+),\"color\":\"(White|Black|Green|Purple|Blonde|Blue|Red|Orange|Gray|Brown)\",\"material\":\"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)\",\"diametr_leg_sm\":(\\d+),\"name\":\"(.+)\",\"leg_length_sm\":(\\d+),\"is_for_man\":(true|false)},\"underwear\":\\{\"sex_lvl\":(\\d+),\"size\":(\\d+),\"color\":\"(White|Black|Green|Purple|Blonde|Blue|Red|Orange|Gray|Brown)\",\"material\":\"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)\",\"name\":\"(.+)\",\"is_for_man\":(true|false)},\"hat\":\\{\"cylinder_height_sm\":(\\d+),\"size\":(\\d+),\"color\":\"(White|Black|Green|Purple|Blonde|Blue|Red|Orange|Gray|Brown)\",\"material\":\"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)\",\"visor_length_sm\":(\\d+),\"name\":\"(.+)\",\"is_for_man\":(true|false)},\"shoes\":\\{\"is_shoelaces\":(true|false),\"size\":(\\d+),\"color\":\"(White|Black|Green|Purple|Blonde|Blue|Red|Orange|Gray|Brown)\",\"material\":\"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)\",\"outsole_material\":\"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)\",\"name\":\"(.+)\",\"is_for_man\":(true|false)}}";
        String dataCommandRegex = "(remove|add_if_max|import|add|change_def_file_path) \\{.+}";
        String nodataCommandRegex = "show|load|info|start|exit|help|save|I1A8S1D1F0G0H|trimToMin";
        String loginRegex = "login \\{.+} \\{.+}";

        if(jsonInput.matches(dataCommandRegex)){
            String cmd = findMatches("(remove|add_if_max|import|add|change_def_file_path)", jsonInput).get(0).toUpperCase();
            String data;
            if(cmd.equals("IMPORT") || cmd.equals("CHANGE_DEF_FILE_PATH")){
                data = jsonInput.split(" ")[1].substring(1, jsonInput.split(" ")[1].length() - 1);
            } else{
                ArrayList<String> list = findMatches(jsonRegex, jsonInput);
                if (list.size() != 0)
                    data = list.get(0);
                else
                    return null;
            }
            Command command = Command.valueOf(cmd);
            command.setData(Stream.of(data));
            return command;
        }else if(jsonInput.matches(nodataCommandRegex)){
            Command command = Command.valueOf(jsonInput.toUpperCase());
            return command;
        }else if(jsonInput.matches(loginRegex)){
            String[] args = jsonInput.split(" ");
            Command command = Command.LOGIN;
            command.setData(Stream.of(
                    args[1].substring(1, args[1].length() - 1) + "|" + args[2].substring(1, args[2].length() - 1)
            ));
            return command;
        }else {
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


    public void setAddress(SocketAddress address) {
        this.address = address;
    }

    public SocketAddress getAddress() {
        return address;
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
