import java.io.Serializable;
import java.util.*;

/**
 * Sourced from https://www.techiedelight.com/implement-multimap-java/
 * MultiMap class
 * This class allows you to create a multimap object that is serializable and contains methods
 * which retrieve information and manipulate the multimap.
 * @author Emily Chang
 * @param <K> MultiMap key
 * @param <V> MultiMap Value
 * @version complete
 */
class MultiMap<K, V> implements Serializable
{
    private Map<K, ArrayList<V>> map = new HashMap<>();

    /**
     * Adds the specified value with the specified key in this multimap.
     */
    public void put(K key, V value) {
        //if key does not exist in the map
        if (map.get(key) == null)
            //add key and empty array list that holds values to map
            map.put(key, new ArrayList<>());

        //add value to key in map
        map.get(key).add(value);
    }

    /**
     * Returns the ArrayList of values to which the specified key is mapped,
     * or null if this multimap contains no mapping for the key.
     * @param key key to map values in multimap
     * @return ArrayList of values to which the specified key is mapped
     */
    public ArrayList<V> get(Object key) {
        return map.get(key);
    }

    /**
     * Returns a Set view of the keys contained in this multimap.
     * @return Set view of the keys contained in this multimap
     */
    public Set<K> keySet() {
        return map.keySet();
    }

    /**
     * Returns boolean value measuring if this multimap contains a mapping for the specified key.
     * @param key key that is checked for existence
     * @returntrue returns true if this multimap contains a mapping for the specified key.
     */
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    /**
     * Removes the mapping for the specified key from this multimap if present.
     * @param key specified key mapping is being removed from
     * @return the ArrayList of previous values associated with key, or
     * null if there was no mapping for key.
     */
    public ArrayList<V> remove(Object key) {
        return map.remove(key);
    }

    /**
     * Returns boolean value measuring if this map contains no key-value mappings.
     * @return Returns true if this map contains no key-value mappings (is empty).
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Removes the entry for the specified key only if it is currently
     * mapped to the specified value
     * @param key key of entry being removed
     * @param value value of entry being removed
     * @return return true if entry is removed
     */
    public boolean remove(K key, V value) {
        // if the key exists
        if (map.get(key) != null)
            //remove the value from the key
            return map.get(key).remove(value);

        return false;
    }
}
