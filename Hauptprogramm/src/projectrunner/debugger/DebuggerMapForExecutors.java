package projectrunner.debugger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import model.Node;
import projectrunner.executionlogging.ExecutionLogger;
import utils.structures.tuples.Pair;

public class DebuggerMapForExecutors {
    
    private final ExecutionLogger executionLogger;

    private final HashMap<Long, String> executorIdentifierToDescription;
    private final HashMap<Long, Long> executorIdentifierToParentContextIdentifier;
    private final HashMap<Long, ExecutorState> executorIdentifierToState;
    private final HashMap<Long, Node> executorIdentifierToNode;
    private final HashMap<Pair<Long, Node>, Long> contextIdentifierAndNodeToExecutor;
    private final HashSet<Long> activeExecutors;

    public DebuggerMapForExecutors(ExecutionLogger executionLogger) {
        this.executionLogger = executionLogger;
        this.executorIdentifierToDescription = new HashMap<>();
        this.executorIdentifierToParentContextIdentifier = new HashMap<>();
        this.executorIdentifierToState = new HashMap<>();
        this.executorIdentifierToNode = new HashMap<>();
        this.contextIdentifierAndNodeToExecutor = new HashMap<>();
        this.activeExecutors = new HashSet<>();
    }

    public synchronized void registerExecutorIdentifier(Long identifier, Node node, String description, Long parentContextIdentifier) {
        executorIdentifierToDescription.put(identifier, description);
        executorIdentifierToParentContextIdentifier.put(identifier, parentContextIdentifier);
        executorIdentifierToNode.put(identifier, node);
        contextIdentifierAndNodeToExecutor.put(new Pair<>(parentContextIdentifier, node), identifier);
        activeExecutors.add(identifier);
    }

    public synchronized int getActiveThreadCount() {
        return activeExecutors.size();
    }

    public synchronized int getTotalThreadCount() {
        return executorIdentifierToState.size();
    }
    
    public synchronized int getFinishedThreadCount() {
        return getTotalThreadCount() - getActiveThreadCount();
    }

    public synchronized int getRunningThreadCount() {
        int counter = 0;
        for (long identifier : activeExecutors) {
            if (executorIdentifierToState.get(identifier) == ExecutorState.RUNNING) {
                counter++;
            }
        }
        return counter;
    }

    public synchronized int getCollectingThreadCount() {
        int counter = 0;
        for (long identifier : activeExecutors) {
            if (executorIdentifierToState.get(identifier) == ExecutorState.COLLECTING) {
                counter++;
            }
        }
        return counter;
    }

    public synchronized int getOtherActiveThreadCount() {
        return getActiveThreadCount() - getRunningThreadCount() - getCollectingThreadCount();
    }

    public synchronized ExecutorState getExecutorState(Long identifier) {
        return executorIdentifierToState.get(identifier);
    }

    public synchronized ExecutorState getNodeState(Node node, Long contextIdentifier) {
        Long executorIdentifier = contextIdentifierAndNodeToExecutor.get(new Pair<>(contextIdentifier, node));
        ExecutorState executorState = executorIdentifierToState.get(executorIdentifier);
        if (executorState != null) {
            return executorState;
        } else {
            //executionLogger.err.println("executorIdentifier " + contextIdentifier + " nicht gefunden");
            return ExecutorState.UNKNOWN;
        }
    }

    public synchronized void setExecutorState(Long identifier, ExecutorState executorState) {
        executorIdentifierToState.put(identifier, executorState);
        if (executorState == ExecutorState.FAILED || executorState == ExecutorState.FINISHED) {
            activeExecutors.remove(identifier);
        }
    }

    public synchronized Node getNodeForExecutorIdentifier(Long identifier) {
        return executorIdentifierToNode.get(identifier);
    }

    public synchronized String getDescriptionForExecutorIdentifier(Long identifier) {
        return executorIdentifierToDescription.get(identifier);
    }
    
    public synchronized Long getParentContextIdentifierForExecutorIdentifier(Long identifier) {
        return executorIdentifierToParentContextIdentifier.get(identifier);
    }

    public synchronized void destroy() {
        executorIdentifierToDescription.clear();
        executorIdentifierToParentContextIdentifier.clear();
        executorIdentifierToState.clear();
        executorIdentifierToNode.clear();
    }

}
