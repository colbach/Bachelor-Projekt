package projectrunner.inputdata;

import utils.structures.tuples.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import model.Inlet;
import utils.*;
import utils.structures.*;

public class InletInputData {

    private final HashMap<Pair<Inlet, Integer>, Object[]> inletsWithIndexsAndData;

    public InletInputData() {
        inletsWithIndexsAndData = new HashMap<>();
    }

    public synchronized Set<Inlet> getAllContainedInlets() {
        HashSet<Inlet> containedInlets = new HashSet<>();
        for (Pair<Inlet, Integer> inletWithIndex : inletsWithIndexsAndData.keySet()) {
            containedInlets.add(inletWithIndex.l);
        }
        return containedInlets;
    }

    public synchronized int size() {
        return inletsWithIndexsAndData.size();
    }

    public synchronized boolean isEmpty() {
        return inletsWithIndexsAndData.isEmpty();
    }

    public synchronized Object[] get(Inlet key) {
        int indexSize;
        if (key != null) {
            indexSize = key.getConnectedLetsCount();
        } else {
            indexSize = 0;
        }
        Object[] rs = null;
        for (int i = 0; i < indexSize; i++) {
            Object[] os = inletsWithIndexsAndData.get(new Pair<>(key, i));
            if (os != null) {
                if (rs == null || rs.length == 0) {
                    rs = os;
                } else {
                    rs = ArrayHelper.concatObjectArrays(rs, os);
                }
            } else {
                System.err.println("ACHTUNG: Daten von Inlet " + key.getName() + " von Node " + key.getNode().getNameAndID() + " an Index " + i + " nicht in inletsAndData vorhanden.");
            }
        }
        if (rs == null) {
            return new Object[0];
        } else {
            return rs;
        }
    }

    /**
     * Gibt alle Daten zuruck welche zu diesem Inlet gehoeren. Achtung diese
     * Methode ist unsyncronisiert. inletsAndData darf nicht waehrend Aufruf
     * veraendert werden.
     */
    /* public Object[] getAllDataForInlet(Inlet inlet) {
        int length = 0;
        for (Map.Entry<Inlet, Object[]> entry : inletsWithIndexsAndData.entrySet()) {
            Inlet key = entry.getKey();
            Object[] value = entry.getValue();
            if(key == inlet) {
                length += value.length;
            }
        }
        Object[] allData = new Object[length];
        int i=0;
        for (Map.Entry<Inlet, Object[]> entry : inletsWithIndexsAndData.entrySet()) {
            Inlet key = entry.getKey();
            Object[] value = entry.getValue();
            if(key == inlet) {
                if(i < allData.length)
                allData[i] = value;
                else
                    System.err.println("inletsWithIndexsAndData wurde veraendert! Laenge von allData ueberschritten!");
            }
        }
        return allData;
    }*/
    /**
     * Gibt alle Daten zuruck. Achtung diese Methode ist unsyncronisiert.
     * inletsWithIndexsAndData darf nicht waehrend Aufruf veraendert werden.
     */
    public synchronized Object[] getAllData() {
        int length = 0;
        for (Object[] os : inletsWithIndexsAndData.values()) {
            length += os.length;
        }
        Object[] allData = new Object[length];
        int i = 0;
        for (Object[] os : inletsWithIndexsAndData.values()) {
            for (Object o : os) {
                if (i < allData.length) {
                    allData[i++] = o;
                } else {
                    System.err.println("inletsAndData wurde veraendert! Laenge von allData ueberschritten!");
                }
            }
        }
        return allData;
    }

    public synchronized boolean containsDataForInlet(Inlet inlet) {
        return inletsWithIndexsAndData.containsKey(inlet);
    }

    public synchronized void put(Pair<Inlet, Integer> inletWithIndex, Object[] data) {
        inletsWithIndexsAndData.put(inletWithIndex, data);
    }

    public synchronized void put(Inlet inlet, int index, Object[] data) {
        inletsWithIndexsAndData.put(new Pair<>(inlet, index), data);
    }

    public void putAll(Map<Pair<Inlet, Integer>, Object[]> inletsWithIndexsAndData) {
        this.inletsWithIndexsAndData.putAll(inletsWithIndexsAndData);
    }

    public synchronized boolean canRetrieveInletData(CollectRequest collectRequest) {
        for (Pair<Inlet, Integer> inletWithIndex : collectRequest.getInletsWithIndexsForCollect()) {
            if (!inletsWithIndexsAndData.containsKey(inletWithIndex)) {
                return false;
            }
        }
        return true;
    }

    public synchronized InletInputData retrieveInletData(CollectRequest collectRequest) throws CanNotRetrieveInletDataException {
        InletInputData result = new InletInputData();
        for (Pair<Inlet, Integer> inletWithIndex : collectRequest.getInletsWithIndexsForCollect()) {
            Object[] data = inletsWithIndexsAndData.remove(inletWithIndex);
            if (data != null) {
                result.put(inletWithIndex, data);
            } else {
                throw new CanNotRetrieveInletDataException();
            }
        }
        return result;
    }

    public synchronized void clear() {
        inletsWithIndexsAndData.clear();
    }

    public Map<Pair<Inlet, Integer>, Object[]> getInletsWithIndexsAndData() {
        return inletsWithIndexsAndData;
    }

    public Set<Pair<Inlet, Integer>> getInletsWithIndexs() {
        return inletsWithIndexsAndData.keySet();
    }
}
