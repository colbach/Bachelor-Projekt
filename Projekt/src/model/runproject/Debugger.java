package model.runproject;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import log.AdditionalOut;
import log.Logging;
import model.Inlet;
import model.Node;
import reflection.NodeDefinition;
import utils.TimeFormat;
import utils.structures.Tuple;

/**
 * Diese Klasse dient zum Debuggen, aber auch zur Ueberwachung der Ausfuehrung.
 */
public class Debugger {

    private boolean debug = true;
    
    private boolean steps = false;
    private boolean blocked = false;
    
    /**
     * Zeit seit welcher blockiert wird. ACHTUNG: != wastedNanosThroughTheDebugger da dieser die Zeit zaehlt aber keine Startzeit angibt.
     */
    private long blockTimeInNanos = -1;

    private int startedExecutorCount;
    private int finishedExecutorCount;
    private int waitingExecutorCount;
    private int workingExecutorCount;

    private final HashSet<Executor> runningExecutors;
    private final HashSet<Executor> waitingExecutors;
    private final HashSet<Executor> workingExecutors;

    private final ExecutionHub executionHub;

    private final Breakpoints breakpoints;

    /**
     * Start-Zeit.
     */
    private final long startTimeInNanos;
    
    /**
     * End-Zeit.
     */
    private long endTimeInNanos;
    
    /**
     * Zeit welcher der Debugger blockiert hat.
     */
    private long wastedNanosThroughTheDebugger;
    
    /**
     * Start-Datum.
     */
    private final Date startDate;
    
    /**
     * End-Datum.
     */
    private Date endDate;

    private final Logging debuggerStateLogger;
    private final int DEBUGGER_STATE_LOGGER_SIZE = 200;

    private String exitMessage = null;

    public HashMap<NodeDefinition, Long> runtimeInNanosPerNodeMap;
    public HashMap<NodeDefinition, Integer> runcountPerNodeMap;

    public Debugger(ExecutionHub executionHub, boolean debug) {
        this.executionHub = executionHub;
        this.runningExecutors = new HashSet<>();
        this.waitingExecutors = new HashSet<>();
        this.workingExecutors = new HashSet<>();
        this.runtimeInNanosPerNodeMap = new HashMap<>();
        this.runcountPerNodeMap = new HashMap<>();
        this.startedExecutorCount = 0;
        this.finishedExecutorCount = 0;
        this.waitingExecutorCount = 0;
        this.workingExecutorCount = 0;
        this.debug = debug;
        this.breakpoints = Breakpoints.getInstance();
        this.startTimeInNanos = System.nanoTime();
        this.startDate = new Date();
        this.endTimeInNanos = -1;
        this.wastedNanosThroughTheDebugger = 0;
        this.debuggerStateLogger = new Logging(DEBUGGER_STATE_LOGGER_SIZE);
    }

    protected void registerRuntimeInNanosForNode(Node node, long runtime) {
        synchronized (runtimeInNanosPerNodeMap) {
            Long runtimeForNode = runtimeInNanosPerNodeMap.get(node);
            if (runtimeForNode == null) {
                runtimeForNode = 0L;
            }
            runtimeForNode += runtime;
            runtimeInNanosPerNodeMap.put(node.getDefinition(), runtimeForNode);
        }
        synchronized (runcountPerNodeMap) {
            Integer runcountForNode = runcountPerNodeMap.get(node);
            if (runcountForNode == null) {
                runcountForNode = 0;
            }
            runcountForNode += 1;
            runcountPerNodeMap.put(node.getDefinition(), runcountForNode);
        }
    }

    public HashMap<NodeDefinition, Long> getRuntimeInNanosPerNodeMap() {
        synchronized (runtimeInNanosPerNodeMap) {
            return (HashMap<NodeDefinition, Long>) runtimeInNanosPerNodeMap.clone();
        }
    }

    public HashMap<NodeDefinition, Integer> getRuncountPerNodeMap() {
        synchronized (runcountPerNodeMap) {
            return (HashMap<NodeDefinition, Integer>) runcountPerNodeMap.clone();
        }
    }
    
    public synchronized void block() {
        // Blockieren wenn blocked-Flag gesetzt ist...
        while (blocked) {
            try {
                wait();
            } catch (InterruptedException ex) {}
        }
    }

    private synchronized void blockAndTriggerBreakpoint(Executor executor) {
        if (debug) {
  
            // Normalles block()...
            block();
  
            // Entscheiden ob dieser Executor vieleicht einen neuen blockAndTriggerBreakpoint auslösst...
            if (steps || breakpoints.is(executor.getExecutingNode())) { // Fall: steps-Flag ist gesetzt (jeder Executor loesst blockAndTriggerBreakpoint aus. ODER auf diesen Executor ist ein Breakpoint gesetzt
                blocked = true; // blocked-Flag setzten um alle Threads warten zu lassen
                blockTimeInNanos = System.nanoTime();
                while (blocked) { // Solange blockAndTriggerBreakpoint-Flag gesetzt ist
                    try {
                        AdditionalOut.println("Debugger: block " + executor.toString());
                        wait();
                    } catch (InterruptedException ex) {}
                }
                wastedNanosThroughTheDebugger += System.nanoTime() - blockTimeInNanos;
                AdditionalOut.println("Debugger: unblock " + executor.toString());
            }
        }
    }

    public synchronized void step() {
        debuggerStateLogger.outPrintln("[Schritt]");
        AdditionalOut.println("Debugger: step");
        steps = true;
        blocked = false;
        notifyAll();
        if (debug) {
            executionHub.incrementChangeCounterForUI();
        }
    }

    public synchronized void continueWithDebugger() {
        debuggerStateLogger.outPrintln("[Fortfahren mit Debugger]");
        AdditionalOut.println("Debugger: Fortsetzen mit Debugger");
        debug = true;
        steps = false;
        blocked = false;
        notifyAll();
        if (debug) {
            executionHub.incrementChangeCounterForUI();
        }
    }
    
    public synchronized void continueWithoutDebugger() {
        debuggerStateLogger.outPrintln("[Fortfahren ohne Debugger]");
        AdditionalOut.println("Debugger: Fortsetzen ohne Debugger");
        debug = false;
        steps = false;
        blocked = false;
        notifyAll();
        if (debug) {
            executionHub.incrementChangeCounterForUI();
        }
    }

    public synchronized void threadStarted(Executor executor) {
        debuggerStateLogger.outPrintln("Thread (" + executor + ") wurde gestartet.");
        this.startedExecutorCount++;
        boolean r = this.runningExecutors.add(executor);
        if (!r) {
            System.err.println(executor.toString() + " ist bereits in runningExecutors vorhanden.");
        }
        if (debug) {
            executionHub.incrementChangeCounterForUI();
        }
        blockAndTriggerBreakpoint(executor);
    }

    public synchronized void threadFinished(Executor executor) {
        debuggerStateLogger.outPrintln("Thread (" + executor + ") hat sich beendet.");
        this.finishedExecutorCount++;
        boolean r = this.runningExecutors.remove(executor);
        if (!r) {
            System.err.println(executor.toString() + " ist nicht in runningExecutors vorhanden.");
        }
        this.waitingExecutors.remove(executor);
        if (this.runningExecutors.isEmpty()) {
            this.endTimeInNanos = System.nanoTime();
            this.endDate = new Date();
            debuggerStateLogger.outPrintln("Ausfuehrung beendet (" + TimeFormat.format(1000000 / (this.endTimeInNanos - this.startTimeInNanos)) + ")");
            System.out.println("Ausfuehrung beendet (" + TimeFormat.format(1000000 / (this.endTimeInNanos - this.startTimeInNanos)) + " s)");
            if (exitMessage == null) {
                exitMessage = "Ausführung erfolgreich beendet";
            }
            executionHub.incrementChangeCounterForUI();
        }
        if (debug) {
            executionHub.incrementChangeCounterForUI();
        }
        blockAndTriggerBreakpoint(executor);
    }

    public synchronized void threadWait(Executor executor) {
        debuggerStateLogger.outPrintln("Thread (" + executor + ") wartet.");
        this.waitingExecutorCount++;
        boolean r = this.waitingExecutors.add(executor);
        if (!r) {
            System.err.println(executor.toString() + " ist bereits in runningExecutors vorhanden.");
        }
        if (debug) {
            executionHub.incrementChangeCounterForUI();
        }
        blockAndTriggerBreakpoint(executor);
    }

    public synchronized void threadFinishedWaiting(Executor executor) {
        debuggerStateLogger.outPrintln("Thread (" + executor + ") beendet Warten.");
        this.waitingExecutorCount--;
        boolean r = this.waitingExecutors.remove(executor);
        if (!r) {
            System.err.println(executor.toString() + " ist nicht in waitingExecutors vorhanden.");
        }
        blockAndTriggerBreakpoint(executor);
    }

    public synchronized void threadWork(Executor executor) {
        debuggerStateLogger.outPrintln("Thread (" + executor + ") arbeitet.");
        this.workingExecutorCount++;
        boolean r = this.workingExecutors.add(executor);
        if (!r) {
            System.err.println(executor.toString() + " ist bereits in runningExecutors vorhanden.");
        }
        blockAndTriggerBreakpoint(executor);
    }

    public synchronized void threadFinishedWorking(Executor executor) {
        debuggerStateLogger.outPrintln("Thread (" + executor + ") beendet Arbeit.");
        this.workingExecutorCount--;
        boolean r = this.workingExecutors.remove(executor);
        if (!r) {
            System.err.println(executor.toString() + " ist nicht in workingExecutors vorhanden.");
        }
        blockAndTriggerBreakpoint(executor);
    }

    public synchronized int getStartedthreadCount() {
        return startedExecutorCount;
    }

    public synchronized int getFinishedthreadCount() {
        return finishedExecutorCount;
    }

    /**
     * Anzahl Threads die gestartet sind und noch nicht beendet wurden.
     */
    public synchronized int getActiveThreadCount() {
        return startedExecutorCount - finishedExecutorCount;
    }

    public synchronized int getWorkingThreadCount() {
        return workingExecutorCount;
    }

    public synchronized int getWaitingThreadCount() {
        return waitingExecutorCount;
    }

    public synchronized HashSet<Node> getExecutingNodes() {
        HashSet<Node> nodes = new HashSet<>(runningExecutors.size());
        for (Executor e : runningExecutors) {
            nodes.add(e.getExecutingNode());
        }
        return nodes;
    }

    public synchronized HashSet<Node> getWaitingNodes() {
        HashSet<Node> nodes = new HashSet<>(runningExecutors.size());
        for (Executor e : waitingExecutors) {
            nodes.add(e.getExecutingNode());
        }
        return nodes;
    }

    public synchronized HashSet<Node> getNotWaitingNodes() {
        HashSet<Node> waitingNodes = getWaitingNodes();
        HashSet<Node> executingNodes = getWaitingNodes();
        HashSet<Node> nodes = new HashSet<>(executingNodes.size());
        for (Node executingNode : executingNodes) {
            if (!waitingNodes.contains(executingNode)) {
                nodes.add(executingNode);
            }
        }
        return nodes;
    }

    public synchronized boolean isNodeExecuting(Node node) {
        return getExecutingNodes().contains(node);
    }

    public synchronized boolean isNodeWaiting(Node node) {
        return getNotWaitingNodes().contains(node);
    }

    /**
     * (betreffendes Inlet, Anzahl angekommener Daten)
     */
    public synchronized HashMap<Inlet, Integer> getArrivedData() {
        HashMap<Inlet, Integer> map = new HashMap<>();
        for (Tuple<Inlet, Integer> key : executionHub.arrivedInletData.keySet()) {
            Integer v = map.get(key.l);
            if (v == null) {
                map.put(key.l, 1);
            } else {
                map.put(key.l, v + 1);
            }
        }
        return map;
    }

    public String toString() {
        return "========= Debugger-Uebersicht =========\n"
                + "Gestartete Threads:             " + getStartedthreadCount() + "\n"
                + "davon bereits beendete Threads: " + getFinishedthreadCount() + "\n"
                + "Wartende Threads:               " + getWaitingThreadCount() + "\n"
                + "Arbeitende Threads:             " + getWorkingThreadCount() + "\n"
                + "Aktive Threads:                 " + getActiveThreadCount() + "\n"
                + "---------------------------------------";

    }

    public boolean isDebugging() {
        return debug;
    }

    public synchronized void setDebugging(boolean debugging) {
        this.debug = debugging;
        executionHub.incrementChangeCounterForUI();
    }

    public boolean isFinished() {
        return this.endTimeInNanos != -1;
    }

    public synchronized long getRuntimeInNanos() {
        
        // Gesammtzeit berchnen...
        long time;
        if (this.endTimeInNanos == -1) {
            time = System.nanoTime() - this.startTimeInNanos; 
            
        } else {
            time = this.endTimeInNanos - this.startTimeInNanos;
        }
        
        // Zeit abzaelen welche Debugger blockiert hat...
        long localWastedNanosThroughTheDebugger = wastedNanosThroughTheDebugger;
        if(blocked) {
            localWastedNanosThroughTheDebugger += System.nanoTime() - blockTimeInNanos;
        }
        
        return time - localWastedNanosThroughTheDebugger;
    }
    
    public synchronized long getRuntimeInMillis() {
        return getRuntimeInNanos() / 1000000L;
    }

    public Logging getDebuggerStateLogger() {
        return debuggerStateLogger;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getExitMessage() {
        return exitMessage;
    }

}
