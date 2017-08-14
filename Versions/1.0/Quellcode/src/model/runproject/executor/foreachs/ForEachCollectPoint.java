package model.runproject.executor.foreachs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import model.Node;
import model.runproject.executioncontrol.ExecutionControl;
import utils.structures.tuples.Pair;

public class ForEachCollectPoint {

    private final HashMap<Pair</*childElementContextIdentifier*/Long, /*Reduce*/ Node>, Object[]> collectedForEachDataForReduceNodes;
    private final HashMap<Pair</*contextIdentifier*/Long, /*ForEach*/ Node>, Set</*childElementContextIdentifier */Long>> childElemtentSets;
    private final ExecutionControl executionControl;
    private final ForEachAndReduceRelations forEachAndReduceRelations;

    public ForEachCollectPoint(ExecutionControl executionControl, ForEachAndReduceRelations forEachAndReduceRelations) {
        this.executionControl = executionControl;
        this.forEachAndReduceRelations = forEachAndReduceRelations;
        childElemtentSets = new HashMap<>();
        collectedForEachDataForReduceNodes = new HashMap<>();
    }

    public synchronized void registerchildElemtentSets(Pair</*contextIdentifier*/Long, /*ForEach*/ Node> forEachKeys, Set<Long> childElementContextIdentifiers) {
        childElemtentSets.put(forEachKeys, childElementContextIdentifiers);
        notifyAll();
    }

    public synchronized void deliverForEachData(Long childElementContextIdentifier, Node reduceNode, Object[] data) {
        collectedForEachDataForReduceNodes.put(new Pair<>(childElementContextIdentifier, reduceNode), data);
        notifyAll();
    }

    private boolean canRetrieveForEachData(Pair</*contextIdentifier*/Long, /*ForEach*/ Node> forEachKey, Node reduceNode) {
        Set<Long> get = childElemtentSets.get(forEachKey);
        if (get != null) {
            for (Long childElementContextIdentifier : get) {
                Pair</*childElementContextIdentifier*/Long, /*Reduce*/ Node> key = new Pair<>(childElementContextIdentifier, reduceNode);
                if (!collectedForEachDataForReduceNodes.containsKey(key)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Diese Methode blockiert bis alle Verfuegbar.
     */
    public synchronized Object[] collectForEachData(long contextIdentifier, Node reduceNode) {
        Node forEachNode = forEachAndReduceRelations.getForEachForReduceNode(reduceNode);
        Pair</*contextIdentifier*/Long, /*ForEach*/ Node> forEachKey = new Pair<>(contextIdentifier, forEachNode);
        while (!canRetrieveForEachData(forEachKey, reduceNode)) {
            try {
                wait();
            } catch (InterruptedException ex) {
                executionControl.stopTest();
            }
        }
        Set<Long> childElemtentSet = childElemtentSets.get(forEachKey);
        TreeMap<Long, Object[]> dataMap = new TreeMap<>();
        for (Long childElementContextIdentifier : childElemtentSet) {
            Pair</*childElementContextIdentifier*/Long, /*Reduce*/ Node> key = new Pair<>(childElementContextIdentifier, reduceNode);
            Object[] data = collectedForEachDataForReduceNodes.remove(key);
            dataMap.put(childElementContextIdentifier, data);
        }
        ArrayList<Object> combinedData = new ArrayList<>();
        for (Object[] os : dataMap.values()) {
            for (Object o : os) {
                combinedData.add(o);
            }
        }
        return combinedData.toArray();
    }
}
