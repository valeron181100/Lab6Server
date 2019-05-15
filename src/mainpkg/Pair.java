package mainpkg;

import java.io.Serializable;

public class Pair<K,V> implements Serializable {
    private K key;
    private V value;
    public Pair(K key, V value){
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    @Override
    public int hashCode() {
        return key.hashCode() + value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pair<K,V> other = (Pair<K,V>) obj;
        if (!this.key.equals(other.key))
            return false;
        if (!this.value.equals(other.value))
            return false;
        return true;
    }
}
