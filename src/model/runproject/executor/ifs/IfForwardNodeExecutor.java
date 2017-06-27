package model.runproject.executor.ifs;

import model.runproject.executioncontrol.ExecutionControl;
import model.runproject.debugger.Debugger;
import logging.AdvancedLogger;
import model.runproject.inputdata.InletInputDataSource;
import model.*;
import model.runproject.*;
import model.runproject.debugger.ExecutorState;
import model.runproject.executionlogging.ExecutionLogger;
import model.runproject.executor.Executor;
import model.runproject.inputdata.*;
import model.runproject.outputdata.*;
import model.runproject.stats.Stats;
import reflection.API;
import reflection.NodeDefinition;
import view.onrun.OnRunWindowManager;

public class IfForwardNodeExecutor extends Executor {

    public IfForwardNodeExecutor(Node executingNode, InletInputDataSource inputSource, OutletOutputDataDestination outputDestination, Debugger debugger, ExecutionControl executionControl, ExecutionLogger executionLogger, API api, Stats stats, long contextIdentifier) {
        super(executingNode, inputSource, outputDestination, debugger, executionControl, executionLogger, api, stats, contextIdentifier);
    }

    @Override
    public void run() {
        Node node = getNode();
        Debugger debugger = getDebugger();
        if (debugger != null) {
            debugger.setExecutorState(getIdentifier(), ExecutorState.PREPARING);
        }

        // Auslöser pushen falls verbunden...
        CollectRequest triggerRequest = new CollectRequest();
        Inlet triggerInlet = node.getInlet(Node.TRIGGER_LABEL);
        if (triggerInlet != null) {
            if (triggerInlet.isConnected()) {
                int connectedLetsCount = triggerInlet.getConnectedLetsCount();
                for (int i = 0; i < connectedLetsCount; i++) {
                    triggerRequest.add(triggerInlet, i);
                }
            }
            getInputSource().push(triggerRequest);
        }

        // Bool ermitteln...   
        Boolean bool = true;
        {
            CollectRequest boolRequest = new CollectRequest();
            boolRequest.add(getNode().getInlet(1), 0);
            if (debugger != null) {
                debugger.setExecutorState(getIdentifier(), ExecutorState.COLLECTING);
            }
            InletInputData inletData1Only = getInputSource().collect(boolRequest, true);
            if (debugger != null) {
                debugger.setExecutorState(getIdentifier(), ExecutorState.PREPARING);
            }
            for (Object o : inletData1Only.getAllData()) { // Alle muessen Wahr sein
                Boolean b = (Boolean) o;
                if (!b) {
                    bool = false;
                }
            }
        }

        // Objekt ermitteln...
        Object[] object = null;
        {
            CollectRequest objectRequest = new CollectRequest();
            objectRequest.add(getNode().getInlet(0), 0);
            if (debugger != null) {
                debugger.setExecutorState(getIdentifier(), ExecutorState.COLLECTING);
            }
            if (node.getInlet(0).isConnected()) {
                InletInputData inletData0Only = getInputSource().collect(objectRequest, true);
                if (debugger != null) {
                    debugger.setExecutorState(getIdentifier(), ExecutorState.DELIEVERING);
                }
                object = inletData0Only.getAllData();
            } else {
                object = new Object[]{};
            }
        }

        // Output uebergeben...
        OutletOutputData output = new OutletOutputData();
        if (bool) { // Wenn true...
            output.put(node.getOutlet(0), object);
        } else { // Wenn false...
            output.put(node.getOutlet(1), object);
        }
        Outlet triggerOutlet = node.getOutlet(Node.TRIGGER_LABEL);
        if (triggerOutlet != null) {
            output.put(triggerOutlet, new Object[0]);
        } else {
            System.err.println(toString() + ": Ausloeser-Outlet nicht gefunden!");
        }
        getOutputDestination().deliver(node, output);
        if (debugger != null) {
            debugger.setExecutorState(getIdentifier(), ExecutorState.FINISHED);
        }
    }

    @Override
    public String getExecutorType() {
        return "IfForwardNodeExecutor";
    }

}
