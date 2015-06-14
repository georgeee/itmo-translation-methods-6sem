package ru.georgeee.itmo.sem6.translation.bunny.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {
    public static <K, V> void addToMap(Map<K, List<V>> map, K key, V value) {
        List<V> list = map.get(key);
        if (list == null) {
            map.put(key, list = new ArrayList<V>());
        }
        list.add(value);
    }
}
