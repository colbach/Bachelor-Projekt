package model.runproject.executioncontext;

import java.util.HashMap;
import model.runproject.executorrunenvironment.ExecutorRunEnvironment;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import logging.*;
import model.*;
import model.runproject.debugger.*;
import model.runproject.executioncontrol.*;
import model.runproject.executor.*;
import model.runproject.executorrunenvironment.ExecutorThread;
import model.runproject.inputdata.*;
import model.runproject.outputdata.*;
import model.runproject.stats.*;
import reflection.*;
import settings.*;
import utils.structures.*;
import model.runproject.callbacks.OnFinishCallback;
import model.runproject.executionlogging.ExecutionLogger;
import model.runproject.executor.foreachs.ForEachAndReduceRelations;
import model.runproject.executor.foreachs.ForEachCollectPoint;
import reflection.nodedefinitions.specialnodes.fors.ForEachNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ReduceNodeDefinition;

public class ExecutionContext implements InletInputDataSource, OutletOutputDataDestination {

    /**
     * Einzigartiger Bezeichner.
     */
    private final long identifier;

    /**
     * Counter zaelt Anzahl von Instanzen. Wird fuer identifier benoetigt.
     */
    private static final AtomicLong instanceCounter = new AtomicLong(0);

    private final Debugger debugger;
    private final ExecutionControl executionControl;
    final ExecutionLogger executionLogger;
    private final API api;
    private final HashSet<Node> alreadySubmited;
    private final InletInputData inputDataStock;
    private final Stats stats;
    private final ExecutorRunEnvironment executorRunEnvironment;
    private final OnFinishCallback<ExecutionContext> onFinishCallback;
    private boolean allExecutorsFinished;
    private final ForEachAndReduceRelations forEachAndReduceRelations;
    private final ForEachCollectPoint forEachCollectPoint;

    /**
     * Konstruktor. Zum Starten des ExecutionContext muss nach dem Aufruf dieses
     * Konstrucktors mindestends einmal die Methode deliver aufgerufen werden.
     */
    public ExecutionContext(
            Debugger debugger,
            ExecutionControl executionControl,
            ExecutionLogger executionLogger,
            API api,
            Stats stats,
            OnFinishCallback<ExecutionContext> onFinishCallback,
            long parentIdentifier,
            ForEachAndReduceRelations forEachAndReduceRelations,
            ForEachCollectPoint forEachCollectPoint) {

        this.identifier = instanceCounter.incrementAndGet();
        this.forEachAndReduceRelations = forEachAndReduceRelations;
        this.forEachCollectPoint = forEachCollectPoint;
        this.debugger = debugger;
        if (debugger != null) {
            debugger.registerContextIdentifier(identifier, toString(), parentIdentifier);
        }
        this.executionControl = executionControl;
        this.executionLogger = executionLogger;
        this.api = api;
        this.allExecutorsFinished = false;
        ExecutionContext thisObject = this;
        this.executorRunEnvironment = new ExecutorRunEnvironment(new OnFinishCallback<ExecutorRunEnvironment>() {
            @Override
            public void onFinish(ExecutorRunEnvironment executorRunEnvironment1) {
                executionLogger.out.println(thisObject.toString() + ": ExecutorRunEnvironment finished");
                allExecutorsFinished = true;
                if (isTerminated()) { // ACHTUNG: isTerminated() wird von ExecutionCreatorContext ueberschrieben, man kann sich also nicht darauf verlassen dass das Resultat immer gleich allExecutorsFinished ist!
                    executionLogger.out.println(thisObject.toString() + ": Terminiert (Aufruf Callback)");
                    onFinishCallback.onFinish(thisObject);
                }
                if (debugger != null) {
                    debugger.setContextTerminated(identifier);
                }
            }
        });
        this.alreadySubmited = new HashSet<>();
        this.inputDataStock = new InletInputData();
        this.stats = stats;
        this.onFinishCallback = onFinishCallback;

        executionLogger.out.println("New ExecutionContext: " + toString());
    }

    @Override
    public final void submitExecutorIfNotSubmited(Node node) {
        executionLogger.out.println(toString() + ": Executor fuer Node \"" + node.getName() + "\" erstellen falls dieser noch nicht erstellt wurde");

        synchronized (alreadySubmited) {
            if (!alreadySubmited.contains(node)) { // Falls node nicht bereits submited wurde
                Executor executor = ExecutorFactory.createExecutor(node, this, this, debugger, executionControl, executionLogger, api, this, stats, identifier, forEachAndReduceRelations, forEachCollectPoint);
                executionLogger.out.println(toString() + ": " + executor + " in Thread-Pool submiten");
                executorRunEnvironment.submit(executor);
                alreadySubmited.add(node);
            }
        }

    }

    @Override
    public InletInputData collect(CollectRequest collectRequest, boolean push) {

        // Vorgaenger anwerfen
        if (push) {
            push(collectRequest);
        }

        // Ergebnisse sammeln...
        executionLogger.out.println(toString() + ": sammele " + collectRequest.toString());
        synchronized (inputDataStock) {
            while (!inputDataStock.canRetrieveInletData(collectRequest)) {
                try {
                    inputDataStock.wait();

                } catch (InterruptedException ex) {
                    executionControl.stopTest();
                }
            }

            // Daten sammeln...
            executionLogger.out.println(toString() + ": " + collectRequest.toString() + " gesammelt");
            InletInputData retrievedData = inputDataStock.retrieveInletData(collectRequest);

            // Auslieferung an Debugger melden...
            if (debugger != null) {
                Set<Tuple<Inlet, Integer>> inletsWithIndexs = retrievedData.getInletsWithIndexs();
                for (Tuple<Inlet, Integer> inletWithIndex : inletsWithIndexs) {
                    debugger.setLetDataDelivered(inletWithIndex.l, inletWithIndex.r, identifier);
                }
            }

            // Rueckgabe...
            return retrievedData;
        }
    }

    @Override
    public void push(CollectRequest collectRequest) {
        executionLogger.out.println(toString() + ": anwerfen " + collectRequest.toString());

        // Vorgaenger anwerfen
        HashSet<Tuple<Inlet, Integer>> inletsWithIndexs = collectRequest.getInletsWithIndexsForCollect();
        for (Tuple<Inlet, Integer> inletWithIndex : inletsWithIndexs) {
            Outlet connectedOutlet = inletWithIndex.l.getConnectedLet(inletWithIndex.r);
            Node concernedNode = connectedOutlet.getNode();
            if (!concernedNode.isRunContextCreator() && !(concernedNode.getDefinition() instanceof ForEachNodeDefinition)) {
                submitExecutorIfNotSubmited(concernedNode);
            } else {
                executionLogger.out.println(toString() + ": Node \"" + concernedNode.getName() + "\" ist ContextCreator und wird deshalb nicht erneut submitet");
            }
        }
    }

    @Override
    public void deliver(final Node node, final OutletOutputData outletOutputData) {
        executionLogger.out.println(toString() + ": erhalten " + outletOutputData.toString());

        if (outletOutputData.size() > 0) { // Falls mindestends ein Element in outletOutputData enthalten ist.

            if (FastAccessibleSettings.additionalchecks()) {

                // outlets ermitteln...
                Set<Outlet> outlets = outletOutputData.getOutlets();

                // Pruefung ob alle Outlets vom gleichen Node sind...
                for (Outlet outlet : outlets) {
                    if (node != outlet.getNode()) {
                        System.err.println("WARNUNG: Outlet aus outletOutputData hat unterschiedliche Node zu Vorgaenger (" + node.getNameAndID() + " != " + node.getNameAndID() + ")!");
                    }
                }

                // Pruefung ob alle Outlets angekommen sind...
                if (node != null) {
                    Outlet[] connectedOutlets = node.getConnectedOutlets();
                    for (Outlet connectedOutlet : connectedOutlets) {
                        if (!outlets.contains(connectedOutlet)) {
                            System.err.println("WARNUNG: Outlet " + connectedOutlet.getName() + " von Node " + node.getNameAndID() + " nicht in outletOutputData vorhanden!");
                        }
                    }
                }
            }

            // OutletOutputData in InletInputData umwandeln...
            InletInputData inputData = outputToInputData(outletOutputData);

            // Daten in Debugger eintragen...
            if (debugger != null) {
                Map<Tuple<Inlet, Integer>, Object[]> inletsWithIndexsAndData = inputData.getInletsWithIndexsAndData();
                for (Map.Entry<Tuple<Inlet, Integer>, Object[]> entry : inletsWithIndexsAndData.entrySet()) {
                    Tuple<Inlet, Integer> key = entry.getKey();
                    Object[] value = entry.getValue();
                    debugger.putLetData(key.l, key.r, identifier, value);
                }
            }

            // Daten zu inputDataStock hinzufuegen...
            synchronized (inputDataStock) {

                Map<Tuple<Inlet, Integer>, Object[]> inletsWithIndexsAndData = inputData.getInletsWithIndexsAndData();
                Map<Tuple<Inlet, Integer>, Object[]> inletsWithIndexsAndDataForReduceNode = new HashMap<>();

                inletsWithIndexsAndData.entrySet().forEach((entry) -> {
                    Tuple<Inlet, Integer> key = entry.getKey();
                    if (key.l.getNode().getDefinition() instanceof ReduceNodeDefinition) {
                        inletsWithIndexsAndDataForReduceNode.put(key, entry.getValue());
                    }
                });
                inletsWithIndexsAndDataForReduceNode.entrySet().forEach((entry) -> {
                    Tuple<Inlet, Integer> key = entry.getKey();
                    inletsWithIndexsAndData.remove(key);
                    forEachCollectPoint.deliverForEachData(identifier, key.l.getNode(), entry.getValue());
                });

                inputDataStock.putAll(inletsWithIndexsAndData);
                inputDataStock.notifyAll();
            }

            // Nodes submiten...
            for (Inlet inlet : inputData.getAllContainedInlets()) {
                submitExecutorIfNotSubmited(inlet.getNode());
            }

        } else {
            executionLogger.err.println(toString() + ": erhaltenes OutletOutputData leer!");
        }
    }

    public InletInputData outputToInputData(final OutletOutputData outletOutputData) {
        // outlets ermitteln...
        Set<Outlet> outlets = outletOutputData.getOutlets();

        // inputData erstellen...
        InletInputData inputData = new InletInputData();
        for (Outlet outlet : outlets) {
            for (Inlet inlet : outlet.getConnectedLets()) {
                int onInletIndex = inlet.getIndexForConnectedOutletInList(outlet);
                inputData.put(inlet, onInletIndex, outletOutputData.get(outlet));
            }
        }

        return inputData;
    }

    public boolean isTerminated() { // ACHTUNG: Diese Methode wird von ExecutionCreatorContext ueberschrieben, man kann sich also nicht darauf verlassen dass das Resultat immer gleich allExecutorsFinished ist!
        return allExecutorsFinished;
    }

    protected Debugger getDebugger() {
        return debugger;
    }

    protected ExecutionControl getExecutionControl() {
        return executionControl;
    }

    protected ExecutionLogger getExecutionLogger() {
        return executionLogger;
    }

    protected API getApi() {
        return api;
    }

    public Stats getStats() {
        return stats;
    }

    public long getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "ExecutionContext #" + identifier;
    }

    protected OnFinishCallback<ExecutionContext> getOnFinishCallback() {
        return onFinishCallback;
    }

    /**
     * Anweisung Speicher frei zu geben.
     */
    public void destory() {
        inputDataStock.clear();
        executorRunEnvironment.destroy();
    }
}
