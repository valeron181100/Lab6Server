package FileSystem;

import Clothes.Costume;

import java.util.HashSet;

/**
 * Класс-обёртка для коллекции HashSet, необходим для передачи коллекции в файловый менеджер по ссылке, а не по значению.
 * @author Валерий Бондарь
 */
public class CollectionCase {

    private HashSet<Costume> collection;

    public CollectionCase(){
        collection = new HashSet<>();
    }

    public HashSet<Costume> getCollection() {
        return collection;
    }

}
