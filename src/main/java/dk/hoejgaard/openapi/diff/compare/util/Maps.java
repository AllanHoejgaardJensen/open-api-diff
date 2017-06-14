package dk.hoejgaard.openapi.diff.compare.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Compares two maps and detects what has been added and what has been removed from these maps.
 *
 * The metaphor used is the diffEm operation from tools like git in order to detect a simple overview
 * over differences between these maps.
 *
 * The maps are considered to be a reference map, and the map to check (the subject) to the reference.
 *
 * The differences found are mapped into 3 maps:
 *  <ol>
 *      <li>common - the keys shared between the reference and the subject maps</li>
 *      <li>removed - the keys from the reference map not found in the subject map</li>
 *      <li>added - additional keys found that was not part of the reference map </li>
 *  </ol>
 *
 * @param <K> key
 * @param <V> value
 */
public class Maps<K, V> {

    private Map<K, V> added;
    private Map<K, V> removed;
    private List<K> common;

    private Maps() {
        this.common = new ArrayList<>();
    }

    /**
     * compares two maps and returns whats common, what has been added and what has been removed.
     * @param <K> the key for the contained type in the Map
     * @param <V> the value for the contained type in the Map
     * @param reference the original map
     * @param subject the subject map for comparison
     * @return a Maps object holding the new added stuff, the old removed stuff and the common stuff
     */
    public static <K, V> Maps<K, V> diff(Map<K, V> reference, Map<K, V> subject) {
        Maps<K, V> keys = new Maps<>();
        keys.removed = new LinkedHashMap<>();
        if (isSimpleInput(reference, subject, keys)) {
            keys.added = new LinkedHashMap<>();
            return keys;
        }
        keys.added = new LinkedHashMap<>(subject);
        for (Entry<K, V> entry : reference.entrySet()) {
            K existingKey = entry.getKey();
            V existingValue = entry.getValue();
            if (subject.containsKey(existingKey)) {
                keys.added.remove(existingKey);
                keys.common.add(existingKey);
            } else {
                keys.removed.put(existingKey, existingValue);
            }
        }
        return keys;
    }

    /**
     * @return Map containing the added keys compared to the reference
     */
    public Map<K, V> getAdded() {
        return added;
    }

    /**
     * @return Map containing the removed keys compared to the reference
     */
    public Map<K, V> getRemoved() {
        return removed;
    }

    /**
     * @return Map containing the keys that exists in the reference and the subject
     */
    public List<K> getCommon() {
        return common;
    }

    /**
     * the simplest case is where the reference and/or the subject is not initialized
     */
    private static <K, V> boolean isSimpleInput(Map<K, V> reference, Map<K, V> subject, Maps<K, V> instance) {
        if (null == reference && null == subject) return true;
        if (null == reference) {
            instance.added = subject;
            return true;
        }
        if (null == subject) {
            instance.removed = reference;
            return true;
        }
        return false;
    }

    public static <K, V> Map<K, V> intersection(Map<K, V> a, Map<K, V> b) {
        Set<K> keysA;
        keysA = a.keySet();
        Set<K> keysB = b.keySet();
        List<K> commonKeys = keysA.stream()
            .filter(keysB::contains)
            .collect(Collectors.toList());
        Map<K, V> keys = new LinkedHashMap<>();
        for (K key: commonKeys) {
            keys.put(key, a.get(key));
        }
        return keys;
    }

}
