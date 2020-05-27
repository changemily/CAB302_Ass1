import java.io.Serializable;
import java.util.*;

/**
 * Sourced from https://www.techiedelight.com/implement-multimap-java/
 * MultiMap class
 * This class implements a multimap object and methods which retrieve info and manipulate the multimap
 * @author Emily Chang
 * @param <K> MultiMap key
 * @param <V> MultiMap Value
 * @version complete
 */
class MultiMap<K, V> implements Serializable
{
    private Map<K, ArrayList<V>> map = new HashMap<>();

    /**
     * Add the specified value with the specified key in this multimap.
     */
    public void put(K key, V value) {
        if (map.get(key) == null)
            map.put(key, new ArrayList<>());

        map.get(key).add(value);
    }

    /**
     * Returns the ArrayList of values to which the specified key is mapped,
     * or null if this multimap contains no mapping for the key.
     */
    public ArrayList<V> get(Object key) {
        return map.get(key);
    }

    /**
     * Returns a Set view of the keys contained in this multimap.
     */
    public Set<K> keySet() {
        return map.keySet();
    }

    /**
     * Returns a Set view of the mappings contained in this multimap.
     */
    public Set<Map.Entry<K, ArrayList<V>>> entrySet() {
        return map.entrySet();
    }

    /**
     * Returns true if this multimap contains a mapping for the specified key.
     */
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    /**
     * Removes the mapping for the specified key from this multimap if present.
     * and returns the ArrayList of previous values associated with key, or
     * null if there was no mapping for key.
     */
    public ArrayList<V> remove(Object key) {
        return map.remove(key);
    }


    /**
     * Returns true if this map contains no key-value mappings.
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Removes the entry for the specified key only if it is currently
     * mapped to the specified value and return true if removed
     */
    public boolean remove(K key, V value) {
        if (map.get(key) != null) // key exists
            return map.get(key).remove(value);

        return false;
    }
}
