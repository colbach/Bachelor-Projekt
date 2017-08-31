package projectrunner.executor.foreachs;

import projectrunner.outputdata.OutletOutputData;
import projectrunner.outputdata.OutletOutputDataDestination;
import projectrunner.inputdata.CollectRequest;
import projectrunner.inputdata.InletInputData;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import projectrunner.executioncontrol.ExecutionControl;
import projectrunner.debugger.Debugger;
import logging.AdvancedLogger;
import projectrunner.inputdata.InletInputDataSource;
import model.*;
import projectrunner.callbacks.OnFinishCallback;
import projectrunner.debugger.ExecutorState;
import projectrunner.executioncontext.ExecutionContext;
import projectrunner.executionlogging.ExecutionLogger;
import projectrunner.executor.Executor;
import projectrunner.executor.foreachs.ForEachAndReduceRelations;
import projectrunner.executor.foreachs.ForEachCollectPoint;
import projectrunner.stats.Stats;
import reflection.common.API;
import reflection.common.NodeDefinition;
import utils.structures.tuples.Pair;
import view.onrun.OnRunWindowManager;

public class ForEachNodeExecutor extends Executor {

    private final Node executingNode;
    private final HashSet<ExecutionContext> childContexts;
    private final ExecutionLogger executionLogger;
    private final ForEachAndReduceRelations forEachAndReduceRelations;
    private final ForEachCollectPoint forEachCollectPoint;
    private final long contextIdentifier;
    private final ExecutionContext executionContext;

    public ForEachNodeExecutor(Node executingNode, InletInputDataSource inputSource, OutletOutputDataDestination outputDestination, Debugger debugger, ExecutionControl executionControl, ExecutionLogger executionLogger, API api, Stats stats, long contextIdentifier, ForEachAndReduceRelations forEachAndReduceRelations, ForEachCollectPoint forEachCollectPoint, ExecutionContext executionContext) {
        super(executingNode, inputSource, outputDestination, debugger, executionControl, executionLogger, api, stats, contextIdentifier);

        this.executionContext = executionContext;
        this.contextIdentifier = contextIdentifier;
        this.executionLogger = executionLogger;
        this.executingNode = executingNode;
        this.childContexts = new HashSet<>();
        this.forEachAndReduceRelations = forEachAndReduceRelations;
        this.forEachCollectPoint = forEachCollectPoint;
    }

    @Override
    public void run() {
        Node forEachNode = getNode();
        Debugger debugger = getDebugger();
        if (debugger != null) {
            debugger.setExecutorState(getIdentifier(), ExecutorState.PREPARING);
        }

        // Auslöser pushen falls verbunden...
        CollectRequest triggerRequest = new CollectRequest();
        Inlet triggerInlet = forEachNode.getInlet(Node.TRIGGER_LABEL);
        if (triggerInlet != null) {
            if (triggerInlet.isConnected()) {
                int connectedLetsCount = triggerInlet.getConnectedLetsCount();
                for (int i = 0; i < connectedLetsCount; i++) {
                    triggerRequest.add(triggerInlet, i);
                }
            }
            getInputSource().push(triggerRequest);
        }

        // Objekt ermitteln...
        Object[] object = null;
        {
            CollectRequest objectRequest = new CollectRequest();
            objectRequest.add(getNode().getInlet(0), 0);
            if (debugger != null) {
                debugger.setExecutorState(getIdentifier(), ExecutorState.COLLECTING);
            }
            if (forEachNode.getInlet(0).isConnected()) {
                InletInputData inletData0Only = getInputSource().collect(objectRequest, true);
                if (debugger != null) {
                    debugger.setExecutorState(getIdentifier(), ExecutorState.DELIEVERING);
                }
                object = inletData0Only.getAllData();
            } else {
                object = new Object[]{};
            }
        }
        
        // ReduceNodes anstossen...
        for(Node reduceNode : forEachAndReduceRelations.getReduceNodesForForEachNode(forEachNode)) {
            executionContext.submitExecutorIfNotSubmited(reduceNode);
        }

        // RunKontext fuer jedes Element erzeugen...
        TreeSet<Long> childElementContextIdentifiers = new TreeSet<>();
        for(Object o : object) {
            OutletOutputData singleElementOutputData = new OutletOutputData(getNode().getOutlet(0), new Object[]{o});
            Outlet triggerOutlet = forEachNode.getOutlet(Node.TRIGGER_LABEL);
            if (triggerOutlet != null) {
                singleElementOutputData.put(triggerOutlet, new Object[0]);
            }
            Long childContextIdentifier = startNewContext(singleElementOutputData);
            childElementContextIdentifiers.add(childContextIdentifier);
        }
        forEachCollectPoint.registerchildElemtentSets(new Pair<>(contextIdentifier, forEachNode), childElementContextIdentifiers);
        
        // Auf Beendigung aller Kontexte warten...
        synchronized (childContexts) {
            while (childContexts.size() > 0 && !getExecutionControl().isCanceled()) {
                try {
                    childContexts.wait();
                } catch (InterruptedException ex) {
                }
            }
        }
        
        // Beenden
        if (debugger != null) {
            debugger.setExecutorState(getIdentifier(), ExecutorState.FINISHED);
        }

        /*
        // Output uebergeben...
        OutletOutputData output = new OutletOutputData();
        if (bool) { // Wenn true...
            output.put(forEachNode.getOutlet(0), object);
        } else { // Wenn false...
            output.put(forEachNode.getOutlet(1), object);
        }
        Outlet triggerOutlet = forEachNode.getOutlet(Node.TRIGGER_LABEL);
        if (triggerOutlet != null) {
            output.put(triggerOutlet, new Object[0]);
        } else {
            System.err.println(toString() + ": Ausloeser-Outlet nicht gefunden!");
        }
        getOutputDestination().deliver(forEachNode, output);
        if (debugger != null) {
            debugger.setExecutorState(getIdentifier(), ExecutorState.FINISHED);
        }
         */
    }

    public Long startNewContext(OutletOutputData outletOutputData) {

        getExecutionLogger().out.println(toString() + ": Start New Context For Element");

        synchronized (childContexts) {

            // neuen Context erzeugen...
            ForEachNodeExecutor thisObject = this;
            ExecutionContext createdContext = new ExecutionContext(getDebugger(), getExecutionControl(), getExecutionLogger(), getApi(), getStats(), new OnFinishCallback<ExecutionContext>() {
                @Override
                public synchronized void onFinish(ExecutionContext executionContext) {
                    synchronized (childContexts) {
                        childContexts.remove(executionContext);
                        childContexts.notifyAll(); // Grundsatzlich darf zwar nur einer warten, jedoch hat hier notifyAll() keinen Nachteil gegenueber notify()
                    }
                }
            }, getIdentifier(), forEachAndReduceRelations, forEachCollectPoint);

            // Context in childContexts eintragen...
            childContexts.add(createdContext);

            // Context starten...
            createdContext.deliver(executingNode, outletOutputData);
            
            return createdContext.getIdentifier();
        }
    }

    @Override
    public String getExecutorType() {
        return "ForEachNodeExecutor";
    }

}
