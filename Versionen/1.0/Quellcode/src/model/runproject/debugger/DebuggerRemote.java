package model.runproject.debugger;

import java.util.HashMap;
import java.util.Map;
import logging.AdditionalLogger;
import logging.AdditionalOut;
import logging.AdvancedLogger;
import model.Inlet;
import model.Node;
import utils.structures.tuples.Pair;

public class DebuggerRemote {

    private final Debugger debugger;

    protected DebuggerRemote(Debugger debugger) {
        this.debugger = debugger;
    }

    public void step() {
        debugger.step();
    }

    public void continueWithDebugger() {
        debugger.continueWithDebugger();
    }

    public void continueWithoutDebugger() {
        debugger.continueWithoutDebugger();
    }

    public boolean isDebugging() {
        return debugger.isDebugging();
    }

    public synchronized ExecutorState getNodeState(Node node, Long contextIdentifier) {
        return debugger.getNodeState(node, contextIdentifier);
    }

    public synchronized HashMap<Inlet, Integer> getArrivedDataCountPerInlet(long contextIdentifier) {
        return debugger.getArrivedDataCountPerInlet(contextIdentifier);
    }

    public Map<Pair<Inlet, Integer>, String> getArrivedShortenedDataLimitedToNode(Node node, long contextIdentifier) {
        return debugger.getArrivedShortenedDataLimitedToNode(node, contextIdentifier);
    }

    public synchronized int getActiveThreadCount() {
        return debugger.getActiveThreadCount();
    }

    public synchronized int getCollectingThreadCount() {
        return debugger.getCollectingThreadCount();
    }

    public synchronized int getOtherActiveThreadCount() {
        return debugger.getOtherActiveThreadCount();
    }

    public synchronized int getRunningThreadCount() {
        return debugger.getRunningThreadCount();
    }

    public synchronized int getTotalThreadCount() {
        return debugger.getTotalThreadCount();
    }

    public synchronized int getFinishedThreadCount() {
        return debugger.getFinishedThreadCount();
    }

    public synchronized HashMap<String, Integer> getContextCreatorDescriptionsToChildContextCount() {
        return debugger.getContextCreatorDescriptionsToChildContextCount();
    }

    public synchronized HashMap<String, Integer> getContextCreatorDescriptionsToTerminatedChildContextCount() {
        return debugger.getContextCreatorDescriptionsToTerminatedChildContextCount();
    }

    public ContextTable getContextTable() {
        return debugger.getContextTable();
    }

    public AdvancedLogger getExecutionLogger() {
        return debugger.getLogger();
    }

    public long ajustContextIdentifier(long contextIdentifier) {
        if (debugger.containsContextIdentifier(contextIdentifier)) {
            return contextIdentifier;
        } else {
            long anyContextCreator = debugger.getAnyContextIdentifier();
            AdditionalLogger.err.println("ContextIdentifier " + contextIdentifier + " nicht vorhanden. Ajust to " + anyContextCreator);
            return anyContextCreator;
        }
    }

    public String getBlockedReason() {
        if (debugger == null) {
            return null;
        }
        return debugger.getBlockedReason();
    }

    public Long getBlockedContextIdentifier() {
        return debugger.getBlockedContextIdentifier();
    }

    public synchronized String getDescriptionForContextIdentifier(Long identifier) {
        return debugger.getDescriptionForContextIdentifier(identifier);
    }
}
