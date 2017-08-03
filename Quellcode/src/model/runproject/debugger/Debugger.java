package model.runproject.debugger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import logging.AdditionalOut;
import logging.AdvancedLogger;
import model.Inlet;
import model.Node;
import model.runproject.debugger.letdatastorage.DebuggerLetDataStorage;
import model.runproject.executionlogging.ExecutionLogger;
import utils.structures.tuples.Pair;

public class Debugger {
    
    private boolean debug;

    private final DebuggerRemote debuggerRemote;

    private final Breakpoints breakpoints;

    private final DebuggerMapForContexts debuggerMapForContexts;
    private final DebuggerMapForExecutors debuggerMapForExecutors;

    private final DebuggerLetDataStorage debuggerLetDataStorage;

    private final AtomicLong changeCounter;

    private boolean steps;
    private boolean blocked;
    private String blockedReason;
    private Long blockedContextIdentifier;
    
    /*public long nextTicketNumber;
    public long latestTicketNumber;
    public boolean breakpointTriggered = false;*/
    
    
    private final AdvancedLogger debuggerExecutionLogger;
    private final int DEBUGGER_STATE_LOGGER_SIZE = 300;
    
    private final ExecutionLogger executionLogger;

    /**
     * Node welche Exception ausgeloesst hat und wegen welcher Ausfuehrung
     * beendet wurde.
     */
    private HashSet<Long> failedExecutors;

    public Debugger(ExecutionLogger executionLogger) {
        this.debuggerMapForContexts = new DebuggerMapForContexts(executionLogger);
        this.debuggerMapForExecutors = new DebuggerMapForExecutors(executionLogger);
        this.debuggerLetDataStorage = new DebuggerLetDataStorage(executionLogger);
        this.breakpoints = Breakpoints.getInstance();
        this.debug = true;
        this.steps = false;
        this.blocked = false;
        this.changeCounter = new AtomicLong();
        this.debuggerRemote = new DebuggerRemote(this);
        this.failedExecutors = new HashSet<>();
        this.debuggerExecutionLogger = new AdvancedLogger(DEBUGGER_STATE_LOGGER_SIZE);
        this.executionLogger = executionLogger;
    }

    public synchronized void block() {
        // Blockieren wenn blocked-Flag gesetzt ist und Debugger noch laeuft...
        while (blocked && debug) {
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
    }

    private synchronized void blockAndTriggerBreakpoint(long identifier, String actualActionDescription) {
        if (debug) {

            // Normalles block()...
            block();

            // Entscheiden ob dieser OLD_Executor vieleicht einen neuen blockAndTriggerBreakpoint auslösst...
            if (steps || breakpoints.is(debuggerMapForExecutors.getNodeForExecutorIdentifier(identifier))) { // Fall: steps-Flag ist gesetzt (jeder OLD_Executor loesst blockAndTriggerBreakpoint aus. ODER auf diesen OLD_Executor ist ein Breakpoint gesetzt
                blocked = true; // blocked-Flag setzten um alle Threads warten zu lassen
                while (blocked && debug) { // Solange blockAndTriggerBreakpoint-Flag gesetzt ist und OLD_Debugger noch laeuft
                    try {
                        String executorDescription = debuggerMapForExecutors.getDescriptionForExecutorIdentifier(identifier);
                        executionLogger.out.println("Debugger: block " + executorDescription);
                        blockedReason = executorDescription + ": " + actualActionDescription;
                        blockedContextIdentifier = debuggerMapForExecutors.getParentContextIdentifierForExecutorIdentifier(identifier);
                        wait();
                    } catch (InterruptedException ex) {
                    }
                }
                executionLogger.out.println("Debugger: unblock " + debuggerMapForExecutors.getDescriptionForExecutorIdentifier(identifier));
            }
        }
    }
    
    public synchronized void releaseBlock() {
        blocked = false;
        blockedReason = null;
        blockedContextIdentifier = null;
        notifyAll();
    }

    public synchronized void step() {
        System.out.println("[Schritt]");
        executionLogger.out.println("Debugger: step");
        steps = true;
        releaseBlock();
        changeCounter.incrementAndGet();
    }

    public synchronized void continueWithDebugger() {
        System.out.println("[Fortfahren mit Debugger]");
        executionLogger.out.println("Debugger: Fortsetzen mit Debugger");
        debug = true;
        steps = false;
        releaseBlock();
        changeCounter.incrementAndGet();
    }

    public synchronized void continueWithoutDebugger() {
        System.out.println("[Fortfahren ohne Debugger]");
        executionLogger.out.println("Debugger: Fortsetzen ohne Debugger");
        debug = false;
        steps = false;
        releaseBlock();
        changeCounter.incrementAndGet();
    }

    public void registerContextIdentifier(Long identifier, String description, Long parentIdentifier) {
        debuggerMapForContexts.registerContextIdentifier(identifier, description, parentIdentifier);
        changeCounter.incrementAndGet();
    }

    public boolean isCreatorContext(Long identifier) {
        return debuggerMapForContexts.isCreatorContext(identifier);
    }

    public void setContextTerminated(Long identifier) {
        changeCounter.incrementAndGet();
        debuggerMapForContexts.setContextTerminated(identifier);
        changeCounter.incrementAndGet();
    }

    public void registerExecutorIdentifier(Long identifier, Node node, String description, Long parentContextIdentifier) {
        debuggerMapForExecutors.registerExecutorIdentifier(identifier, node, description, parentContextIdentifier);
        changeCounter.incrementAndGet();
        blockAndTriggerBreakpoint(identifier, "Executor registered");
        changeCounter.incrementAndGet();
    }

    public ExecutorState getExecutorState(Long identifier) {
        return debuggerMapForExecutors.getExecutorState(identifier);
    }

    public synchronized ExecutorState getNodeState(Node node, Long contextIdentifier) {
        return debuggerMapForExecutors.getNodeState(node, contextIdentifier);
    }

    public synchronized ContextState getContextState(Long identifier) {
        return debuggerMapForContexts.getContextState(identifier);
    }
    
    public void setExecutorState(Long identifier, ExecutorState executorState) {
        debuggerMapForExecutors.setExecutorState(identifier, executorState);
        changeCounter.incrementAndGet();
        blockAndTriggerBreakpoint(identifier, "State changed ("+executorState.toString()+")");
        changeCounter.incrementAndGet();
    }

    public ContextTable getContextTable() {
        return debuggerMapForContexts.getContextTable();
    }

    public void putLetData(Inlet inlet, Integer index, long contextIdentifier, Object[] data) {
        debuggerLetDataStorage.put(inlet, index, contextIdentifier, data);
        changeCounter.incrementAndGet();
    }

    public String getShortenedLetData(Inlet inlet, Integer index, long contextIdentifier) {
        return debuggerLetDataStorage.getShortenedData(inlet, index, contextIdentifier);
    }

    public boolean isLetDataDelivered(Inlet inlet, Integer index, long contextIdentifier) {
        return debuggerLetDataStorage.isDataDelivered(inlet, index, contextIdentifier);
    }

    public void setLetDataDelivered(Inlet inlet, Integer index, long contextIdentifier) {
        debuggerLetDataStorage.setDataDelivered(inlet, index, contextIdentifier);
        changeCounter.incrementAndGet();
    }

    public Map<Pair<Inlet, Integer>, String> getArrivedShortenedDataLimitedToNode(Node node, long contextIdentifier) {

        HashMap<Pair<Inlet, Integer>, String> map = new HashMap<>();
        for (Inlet inlet : node.getInlets()) {
            int connectedLetsCount = inlet.getConnectedLetsCount();
            for (int i = 0; i < connectedLetsCount; i++) {
                Pair<Inlet, Integer> key = new Pair<>(inlet, i);
                String shortenedData = debuggerLetDataStorage.getShortenedData(inlet, i, contextIdentifier);
                map.put(key, shortenedData);
            }
        }
        return map;
    }

    public boolean isDataDelivered(Inlet inlet, Integer index, long contextIdentifier) {
        return debuggerLetDataStorage.isDataDelivered(inlet, index, contextIdentifier);
    }

    public int getActiveThreadCount() {
        return debuggerMapForExecutors.getActiveThreadCount();
    }

    public int getCollectingThreadCount() {
        return debuggerMapForExecutors.getCollectingThreadCount();
    }

    public int getOtherActiveThreadCount() {
        return debuggerMapForExecutors.getOtherActiveThreadCount();
    }

    public int getRunningThreadCount() {
        return debuggerMapForExecutors.getRunningThreadCount();
    }

    public int getTotalThreadCount() {
        return debuggerMapForExecutors.getTotalThreadCount();
    }

    public int getFinishedThreadCount() {
        return debuggerMapForExecutors.getFinishedThreadCount();
    }

    public String getDescriptionForExecutorIdentifier(Long identifier) {
        return debuggerMapForExecutors.getDescriptionForExecutorIdentifier(identifier);
    }

    public synchronized HashMap<String, Integer> getContextCreatorDescriptionsToChildContextCount() {
        return debuggerMapForContexts.getContextCreatorDescriptionsToChildContextCount();
    }

    public synchronized HashMap<String, Integer> getContextCreatorDescriptionsToTerminatedChildContextCount() {
        return debuggerMapForContexts.getContextCreatorDescriptionsToTerminatedChildContextCount();
    }

    public synchronized HashMap<Inlet, Integer> getArrivedDataCountPerInlet(long contextIdentifier) {
        return debuggerLetDataStorage.getArrivedDataCountPerInlet(contextIdentifier);
    }

    public synchronized boolean containsContextIdentifier(long contextIdentifier) {
        return debuggerMapForContexts.containsContextIdentifier(contextIdentifier);
    }

    public Long getAnyContextIdentifier() {
        return debuggerMapForContexts.getAnyContextIdentifier();
    }

    public DebuggerRemote getRemote() {
        return debuggerRemote;
    }

    public AdvancedLogger getExecutionLogger() {
        return debuggerExecutionLogger;
    }

    boolean isDebugging() {
        return debug;
    }

    public void destroy() {
        debuggerMapForContexts.destroy();
        debuggerMapForExecutors.destroy();
    }

    public String getBlockedReason() {
        return blockedReason;
    }

    public Long getBlockedContextIdentifier() {
        return blockedContextIdentifier;
    }

    public synchronized String getDescriptionForContextIdentifier(Long identifier) {
        return debuggerMapForContexts.getDescription(identifier);
    }
    
    
    
    
}
