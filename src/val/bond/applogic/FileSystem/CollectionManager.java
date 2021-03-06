package val.bond.applogic.FileSystem;

import val.bond.applogic.Clothes.Costume;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.util.HashSet;

/**
 * Класс-менеджер коллекции.
 * @author Валерий Бондарь
 */
public class CollectionManager {
    /**
     * Получает коллекцию из XML строки.
     * @author Валерий Бондарь
     * @return HashSet\<Costume\>
     */
    public static HashSet<Costume> getCollectionFromXML(String xml) throws EmptyFileException{
            HashSet<Costume> costumes = new HashSet<>();

        if (xml.length() == 0) {
            throw new EmptyFileException();
        } else {
            JSONArray jsonArray;
            jsonArray = XML.toJSONObject(xml).getJSONArray("array");
            jsonArray.forEach(p -> costumes.add(new Costume((JSONObject) p)));
        }

            return costumes;
    }


    public static byte[] getBytesFromCollection(HashSet<Costume> costumes){
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos)){
            oos.writeObject(costumes);
            return baos.toByteArray();
        } catch (IOException e) {
            System.err.println("Ошибка: не удалось сериализовать коллекцию");
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static HashSet<Costume> getCollectionFromBytes(byte[] bytes){
        try(ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais)){
            return (HashSet<Costume>)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка: не удалось сериализовать коллекцию");
        }
        return null;
    }

    /**
     * Получает XML строку из коллекции.
     * @author Валерий Бондарь
     * @return HashSet\<Costume\>
     */
    public static String getXmlFromCollection(HashSet<Costume> collection){
        JSONArray array = new JSONArray();
        collection.forEach(p -> array.put(p.getJson()));
        return XML.toString(array);
    }


}

