package model.runproject.executor.contexts;

import logging.AdditionalLogger;
import model.runproject.executioncontrol.ExecutionControl;
import model.runproject.debugger.Debugger;
import logging.AdditionalOut;
import logging.AdvancedLogger;
import model.runproject.inputdata.*;
import model.*;
import model.runproject.debugger.ExecutorState;
import model.runproject.executioncontext.ExecutionCreatorContext;
import model.runproject.executionlogging.ExecutionLogger;
import model.runproject.executor.Executor;
import model.runproject.executor.InOutImplementation;
import model.runproject.outputdata.*;
import model.runproject.stats.Stats;
import reflection.API;
import reflection.NodeDefinition;
import reflection.TerminatedException;
import utils.structures.tuples.Pair;

public class ContextCreatingNodeExecutor extends Executor {

    private final ExecutionCreatorContext executionCreatorContext;

    public ContextCreatingNodeExecutor(Node executingNode, InletInputDataSource inputSource, OutletOutputDataDestination outputDestination, Debugger debugger, ExecutionControl executionControl, ExecutionLogger executionLogger, API api, ExecutionCreatorContext executionCreatorContext, Stats stats, long contextIdentifier) {
        super(executingNode, inputSource, outputDestination, debugger, executionControl, executionLogger, api, stats, contextIdentifier);
        this.executionCreatorContext = executionCreatorContext;
    }

    @Override
    public void run() {
        Node node = getNode();
        Debugger debugger = getDebugger();
        if (debugger != null) {
            debugger.setExecutorState(getIdentifier(), ExecutorState.PREPARING);
        }
        ExecutionLogger executionLogger = getExecutionLogger();

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
        Pair<Inlet, Integer>[] connectedInlets = node.getConnectedInletsAndIndexs();
        CollectRequest collectRequest = new CollectRequest(connectedInlets);
        if (debugger != null) {
            debugger.setExecutorState(getIdentifier(), ExecutorState.COLLECTING);
        }
        InletInputData input = getInputSource().collect(collectRequest, true);
        if (debugger != null) {
            debugger.setExecutorState(getIdentifier(), ExecutorState.PREPARING);
        }

        // InOut-Implementierung befuellen...
        InOutImplementation io = new ContextCreatorInOutImplementation(input, node, getExecutionControl(), executionCreatorContext);

        // Node ausfueren...
        executionLogger.out.println(toString() + ": run definition");
        try {
            if (debugger != null) {
                debugger.setExecutorState(getIdentifier(), ExecutorState.RUNNING);
            }
            node.getDefinition().run(io, getApi());
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
        }

    }

    @Override
    public String getExecutorType() {
        return "ContextCreatingNodeExecutor";
    }

}
