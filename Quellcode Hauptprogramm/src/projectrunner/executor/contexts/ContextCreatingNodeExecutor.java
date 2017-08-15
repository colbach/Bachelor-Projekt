package projectrunner.executor.contexts;

import projectrunner.outputdata.OutletOutputDataDestination;
import projectrunner.inputdata.CollectRequest;
import projectrunner.inputdata.InletInputDataSource;
import projectrunner.inputdata.InletInputData;
import logging.AdditionalLogger;
import projectrunner.executioncontrol.ExecutionControl;
import projectrunner.debugger.Debugger;
import logging.AdditionalOut;
import logging.AdvancedLogger;
import model.*;
import projectrunner.debugger.ExecutorState;
import projectrunner.executioncontext.ExecutionCreatorContext;
import projectrunner.executionlogging.ExecutionLogger;
import projectrunner.executor.Executor;
import projectrunner.executor.InOutImplementation;
import projectrunner.stats.Stats;
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
