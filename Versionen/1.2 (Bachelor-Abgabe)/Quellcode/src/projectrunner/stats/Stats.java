package projectrunner.stats;

import java.util.*;
import model.*;

public class Stats {

    private volatile boolean finished = false;

    public HashMap<Node, Long> runtimeInNanosPerNodeMap;
    public HashMap<Node, Integer> runcountPerNodeMap;

    public final HashMap<Node, Integer> createdContextCountPerContextCreator;
    public final HashMap<Node, Integer> finishedContextCountPerContextCreator;

    public Stats() {
        this.runtimeInNanosPerNodeMap = new HashMap<>();
        this.runcountPerNodeMap = new HashMap<>();
        this.createdContextCountPerContextCreator = new HashMap<>();
        this.finishedContextCountPerContextCreator = new HashMap<>();
    }

    public void registerRuntimeInNanosForNode(Node node, long runtime) {
        if (finished) {
            throw new StatsNotEditableAnymoreException();
        } else {
            synchronized (runtimeInNanosPerNodeMap) {
                Long runtimeForNode = runtimeInNanosPerNodeMap.get(node);
                if (runtimeForNode == null) {
                    runtimeForNode = 0L;
                }
                runtimeForNode += runtime;
                runtimeInNanosPerNodeMap.put(node, runtimeForNode);
            }
            synchronized (runcountPerNodeMap) {
                Integer runcountForNode = runcountPerNodeMap.get(node);
                if (runcountForNode == null) {
                    runcountForNode = 0;
                }
                runcountForNode += 1;
                runcountPerNodeMap.put(node, runcountForNode);
            }
        }
    }

    public Map<Node, Long> getRuntimeInNanosPerNodeMap() {
        if (finished) {
            throw new StatsNotEditableAnymoreException();
        } else {
            synchronized (runtimeInNanosPerNodeMap) {
                return (HashMap<Node, Long>) runtimeInNanosPerNodeMap.clone();
            }
        }
    }

    public Map<Node, Integer> getRuncountPerNodeMap() {
        if (finished) {
            throw new StatsNotEditableAnymoreException();
        } else {
            synchronized (runcountPerNodeMap) {
                return (HashMap<Node, Integer>) runcountPerNodeMap.clone();
            }
        }
    }

    public Map<Node, Integer> getCreatedContextCountPerContextCreator() {
        if (finished) {
            throw new StatsNotEditableAnymoreException();
        } else {
            synchronized (createdContextCountPerContextCreator) {
                return (HashMap<Node, Integer>) createdContextCountPerContextCreator.clone();
            }
        }
    }

    public Map<Node, Integer> getFinishedContextCountPerContextCreator() {
        if (finished) {
            throw new StatsNotEditableAnymoreException();
        } else {
            synchronized (finishedContextCountPerContextCreator) {
                return (HashMap<Node, Integer>) finishedContextCountPerContextCreator.clone();
            }
        }
    }

    /**
     * Finalisiert dieses Stats-Opjekt. Nach diesem
     */
    public synchronized void finish() {
        if (!finished) {
            finished = true;
        }

    }

    public boolean isFinished() {
        return finished;
    }
}
