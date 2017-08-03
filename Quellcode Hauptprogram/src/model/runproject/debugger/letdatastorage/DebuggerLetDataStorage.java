package model.runproject.debugger.letdatastorage;

import java.util.HashMap;
import logging.AdditionalErr;
import model.Inlet;
import model.runproject.executionlogging.ExecutionLogger;
import utils.OccurrencesCounter;
import utils.format.ObjectArrayFormat;
import utils.structures.tuples.Pair;

public class DebuggerLetDataStorage {
    
    private final ExecutionLogger executionLogger;
    
    private final HashMap<DebuggerLetDataStorageKey, DebuggerLetDataStorageData> dataStorage;
    private final HashMap<Long, OccurrencesCounter<Inlet>> contextToInletOccurrencesCounter;

    public DebuggerLetDataStorage(ExecutionLogger executionLogger) {
        this.executionLogger = executionLogger;
        this.dataStorage = new HashMap<>();
        this.contextToInletOccurrencesCounter = new HashMap<>();
    }

    public synchronized void put(Inlet inlet, Integer index, long contextIdentifier, Object[] data) {
        put(new DebuggerLetDataStorageKey(inlet, index, contextIdentifier), data);
    }

    private synchronized void put(DebuggerLetDataStorageKey key, Object[] data) {
        dataStorage.put(key, new DebuggerLetDataStorageData(data));
        Long contextIdentifier = key.getContextIdentifier();
        OccurrencesCounter<Inlet> inletOccurrencesCounter = contextToInletOccurrencesCounter.get(contextIdentifier);
        if(inletOccurrencesCounter != null) {
            inletOccurrencesCounter.add(key.getInlet());
            //contextToInletOccurrencesCounter.put(contextIdentifier, inletOccurrencesCounter); // nicht noetig da Pointer
        } else {
            inletOccurrencesCounter = new OccurrencesCounter<>();
            inletOccurrencesCounter.add(key.getInlet());
            contextToInletOccurrencesCounter.put(contextIdentifier, inletOccurrencesCounter);
        }
    }
    
    public synchronized String getShortenedData(Inlet inlet, Integer index, long contextIdentifier) {
        return getShortenedData(new DebuggerLetDataStorageKey(inlet, index, contextIdentifier));
    }
    
    private synchronized String getShortenedData(DebuggerLetDataStorageKey key) {
        DebuggerLetDataStorageData get = dataStorage.get(key);
        if(get != null) {
            return get.getShortenedData();
        } else {
            executionLogger.err.println("DebuggerLetDataStorageKey nicht gefunden. return \"null\"");
            return "null";
        }
    }
    
    public synchronized boolean isDataDelivered(Inlet inlet, Integer index, long contextIdentifier) {
        return isDataDelivered(new DebuggerLetDataStorageKey(inlet, index, contextIdentifier));
    }
    
    private synchronized boolean isDataDelivered(DebuggerLetDataStorageKey debuggerLetDataStorageKey) {
        DebuggerLetDataStorageData get = dataStorage.get(debuggerLetDataStorageKey);
        if(get != null) {
            return get.isDelivered();
        } else {
            executionLogger.err.println("DebuggerLetDataStorageKey nicht gefunden. return false");
            return false;
        }
    }
    
    public synchronized void setDataDelivered(Inlet inlet, Integer index, long contextIdentifier) {
        setDataDelivered(new DebuggerLetDataStorageKey(inlet, index, contextIdentifier));
    }
    
    private synchronized void setDataDelivered(DebuggerLetDataStorageKey debuggerLetDataStorageKey) {
        DebuggerLetDataStorageData get = dataStorage.get(debuggerLetDataStorageKey);
        if(get != null) {
            get.setDelivered();
        } else {
            executionLogger.err.println("DebuggerLetDataStorageKey nicht gefunden.");
        }
    }
    
    public synchronized HashMap<Inlet, Integer> getArrivedDataCountPerInlet(long contextIdentifier) {
        OccurrencesCounter<Inlet> inletOccurrencesCounter = contextToInletOccurrencesCounter.get(contextIdentifier);
        if(inletOccurrencesCounter != null) {
            return inletOccurrencesCounter.getClonedMap();
        } else {
            return new HashMap<>();
        }
    }

}
