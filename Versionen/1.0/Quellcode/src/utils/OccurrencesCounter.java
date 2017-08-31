package utils;

import java.util.HashMap;

/**
 * Diese Klasse dient zum zaehlen von der Haufigkeit von Objekten.
 */
public class OccurrencesCounter<T> {
    
    private final HashMap<T, Integer> map;

    public OccurrencesCounter() {
        this.map = new HashMap<>();
    }
    
    public void add(T t) {
        Integer count = map.get(t);
        if(count == null) {
            map.put(t, 1);
        } else {
            map.put(t, count + 1);
        }
    }

    public HashMap<T, Integer> getMap() {
        return map;
    }
    
    public HashMap<T, Integer> getClonedMap() {
        return (HashMap<T, Integer>) map.clone();
    }
}
