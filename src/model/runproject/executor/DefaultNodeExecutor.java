package model.runproject.executor;

import model.runproject.executioncontrol.*;
import model.runproject.debugger.*;
import java.util.HashMap;
import logging.*;
import logging.AdvancedLogger;
import model.runproject.inputdata.*;
import model.*;
import model.runproject.*;
import model.runproject.executionlogging.ExecutionLogger;
import model.runproject.outputdata.*;
import model.runproject.stats.*;
import reflection.*;
import utils.structures.*;
import view.onrun.*;

public class DefaultNodeExecutor extends Executor {

    public DefaultNodeExecutor(Node executingNode, InletInputDataSource inputSource, OutletOutputDataDestination outputDestination, Debugger debugger, ExecutionControl executionControl, ExecutionLogger executionLogger, API api, Stats stats, long contextIdentifier) {
        super(executingNode, inputSource, outputDestination, debugger, executionControl, executionLogger, api, stats, contextIdentifier);
    }

    @Override
    public void run() {
        Node node = getNode();
        Debugger debugger = getDebugger();
        ExecutionLogger executionLogger = getExecutionLogger();
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

        // Input ermitteln...
        Tuple<Inlet, Integer>[] connectedInlets = node.getConnectedInletsAndIndexs();
        CollectRequest collectRequest = new CollectRequest(connectedInlets);
        if (debugger != null) {
            debugger.setExecutorState(getIdentifier(), ExecutorState.COLLECTING);
        }
        InletInputData input = getInputSource().collect(collectRequest, true);
        if (debugger != null) {
            debugger.setExecutorState(getIdentifier(), ExecutorState.PREPARING);
        }

        // InOut-Implementierung befuellen...
        InOutImplementation io = new InOutImplementation(input, node, getExecutionControl(), getContextIdentifier());

        // Node ausfueren...
        executionLogger.out.println(toString() + ": run definition");
        long nanos = System.nanoTime();
        try {

            if (debugger != null) {
                debugger.setExecutorState(getIdentifier(), ExecutorState.RUNNING);
            }
            node.getDefinition().run(io, getApi());

            // Output weiter geben...
            if (debugger != null) {
                debugger.setExecutorState(getIdentifier(), ExecutorState.DELIEVERING);
            }
            OutletOutputData output = io.getOutletOutputData();
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

        } catch (TerminatedException te) {
            AdditionalLogger.out.println("TerminatedException in " + toString() + ". Anhalten.");
            
        } catch (Exception e) {
            String error = toString() + ": " + e.getMessage();
            e.printStackTrace();
            getExecutionControl().setFailed(error, node);
            if (debugger != null) {
                debugger.setExecutorState(getIdentifier(), ExecutorState.FAILED);
            }

        } finally {
            Stats stats = getStats();
            if (stats != null) {
                stats.registerRuntimeInNanosForNode(node, System.nanoTime() - nanos); // Zeit registrieren
            }
        }

    }

    @Override
    public String getExecutorType() {
        return "DefaultNodeExecutor";
    }

}
