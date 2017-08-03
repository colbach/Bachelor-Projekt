package model.runproject;

import java.util.HashMap;
import model.runproject.executioncontrol.ExecutionControl;

public class SharedObjectSpace {

    private HashMap<String, Object> sharedObjects;
    private ExecutionControl executionControl;

    public SharedObjectSpace(ExecutionControl executionControl) {
        sharedObjects = new HashMap<>();
        this.executionControl = executionControl;
    }

    public synchronized Object getObject(String key) {
        while (!sharedObjects.containsKey(key)) {
            executionControl.stopTest();
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        return sharedObjects.get(key);
    }

    public synchronized Object getObjectBlocking(String key) {
        return sharedObjects.get(key);
    }

    public synchronized void putObject(String key, Object value) {
        sharedObjects.put(key, value);
        notifyAll();
    }

    public synchronized Object putIfNotAlreadyExistsObject(String key, Object object) {
        if (!sharedObjects.containsKey(key)) {
            putObject(key, object);
        }
        return getObject(key);
    }

    public synchronized void removeObject(String key) {
        sharedObjects.remove(key);
    }

}
