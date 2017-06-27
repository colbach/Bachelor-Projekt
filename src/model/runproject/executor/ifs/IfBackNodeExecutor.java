package model.runproject.executor.ifs;

import model.runproject.executioncontrol.ExecutionControl;
import model.runproject.debugger.Debugger;
import model.runproject.inputdata.*;
import model.*;
import model.runproject.*;
import model.runproject.outputdata.*;
import utils.structures.*;
import java.util.HashMap;
import logging.AdvancedLogger;
import model.runproject.debugger.ExecutorState;
import model.runproject.executionlogging.ExecutionLogger;
import model.runproject.executor.Executor;
import model.runproject.stats.Stats;
import reflection.API;
import reflection.NodeDefinition;
import view.onrun.OnRunWindowManager;

public class IfBackNodeExecutor extends Executor {

    public IfBackNodeExecutor(Node executingNode, InletInputDataSource inputSource, OutletOutputDataDestination outputDestination, Debugger debugger, ExecutionControl executionControl, ExecutionLogger executionLogger, API api, Stats stats, long contextIdentifier) {
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
        CollectRequest boolRequest = new CollectRequest();
        boolRequest.add(getNode().getInlet(2), 0);
        if (debugger != null) {
            debugger.setExecutorState(getIdentifier(), ExecutorState.FINISHED);
        }
        InletInputData inletData2Only = getInputSource().collect(boolRequest, true);
        if (debugger != null) {
            debugger.setExecutorState(getIdentifier(), ExecutorState.PREPARING);
        }
        Boolean bool = true;
        for (Object o : inletData2Only.getAllData()) { // Alle muessen Wahr sein
            Boolean b = (Boolean) o;
            if (!b) {
                bool = false;
            }
        }

        // Input ermitteln...
        CollectRequest inRequest = new CollectRequest();

        if (bool) { // Wenn true...
            for (int i = 0; i < node.getInlet(0).getConnectedLetsCount(); i++) {
                inRequest.add(getNode().getInlet(0), i);
            }

        } else { // Wenn false...
            for (int i = 0; i < node.getInlet(1).getConnectedLetsCount(); i++) {
                inRequest.add(getNode().getInlet(1), i);
            }
        }
        if (debugger != null) {
            debugger.setExecutorState(getIdentifier(), ExecutorState.COLLECTING);
        }
        InletInputData inData = getInputSource().collect(inRequest, true);
        if (debugger != null) {
            debugger.setExecutorState(getIdentifier(), ExecutorState.DELIEVERING);
        }
        Object[] trueData = inData.getAllData();

        // Output uebergeben...
        OutletOutputData output = new OutletOutputData(node.getOutlet(0), trueData);
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
        return "IfBackNodeExecutor";
    }

}
