package projectrunner.executor.firstvalues;

import reflection.common.TerminatedException;
import reflection.common.API;
import projectrunner.stats.Stats;
import projectrunner.outputdata.OutletOutputData;
import projectrunner.outputdata.OutletOutputDataDestination;
import projectrunner.inputdata.InletInputDataSource;
import projectrunner.inputdata.InletInputData;
import projectrunner.executioncontrol.ExecutionControl;
import projectrunner.debugger.ExecutorState;
import projectrunner.debugger.Debugger;
import utils.structures.tuples.Pair;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import logging.*;
import logging.AdvancedLogger;
import model.*;
import projectrunner.executionlogging.ExecutionLogger;
import projectrunner.executor.Executor;
import projectrunner.executor.InOutImplementation;
import reflection.nodedefinitions.specialnodes.firstvalues.AValueNodeDefinition;
import reflection.nodedefinitions.specialnodes.firstvalues.FastestValueNodeDefinition;
import settings.FastAccessibleSettings;
import utils.structures.*;
import view.onrun.*;

/**
 * Verallgemeinerter Executor fuer AValueNodeDefinition UND
 * FastestValueNodeDefinition.
 */
public class GeneralizedFirstValueNodeExecutor extends Executor implements CollectWaitCallback {

    private InletInputData firstInletInputData = null;

    public GeneralizedFirstValueNodeExecutor(Node executingNode, InletInputDataSource inputSource, OutletOutputDataDestination outputDestination, Debugger debugger, ExecutionControl executionControl, ExecutionLogger executionLogger, API api, Stats stats, long contextIdentifier) {
        super(executingNode, inputSource, outputDestination, debugger, executionControl, executionLogger, api, stats, contextIdentifier);
    }

    @Override
    public void run() {
        Node node = getNode();
        Debugger debugger = getDebugger();
        ExecutionLogger executionLogger = getExecutionLogger();

        // Input ermitteln...
        if (debugger != null) {
            debugger.setExecutorState(getIdentifier(), ExecutorState.COLLECTING);
        }
        Pair<Inlet, Integer>[] connectedInlets = node.getConnectedInletsAndIndexs();
        NotifyValue<Boolean> cancelator = new NotifyValue<>(Boolean.FALSE);
        for (Pair<Inlet, Integer> connectedInlet : connectedInlets) {
            boolean push;
            if (node.getDefinition() instanceof AValueNodeDefinition) {
                push = false;
            } else if (node.getDefinition() instanceof FastestValueNodeDefinition) {
                push = true;
            } else {
                throw new RuntimeException("Node weder AValueNodeDefinition noch FastestValueNodeDefinition!");
            }
            CollectWaitThread collectWaitThread = new CollectWaitThread(connectedInlet, this, getInputSource(), push, cancelator);
            collectWaitThread.start();
        }

        try {

            // Output weiter geben...
            if (debugger != null) {
                debugger.setExecutorState(getIdentifier(), ExecutorState.DELIEVERING);
            }
            InOutImplementation io = new InOutImplementation(new InletInputData(), node, getExecutionControl(), getContextIdentifier()); // leer
            io.out(0, collectData());
            OutletOutputData output = io.getOutletOutputData();
            Outlet triggerOutlet = node.getOutlet(Node.TRIGGER_LABEL);
            if (triggerOutlet != null) {
                output.put(triggerOutlet, new Object[0]);
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

        }

    }

    @Override
    public String getExecutorType() {
        return "FirstValueNodeExecutor";
    }

    @Override
    public synchronized void takeCollectedInletInputData(InletInputData inletInputData) {
        if (firstInletInputData == null) { // Erster Call
            firstInletInputData = inletInputData;
            notifyAll(); // notify sollte auch reichen, notifyAll hat jedoch keinen Nachteil
        }
    }

    public synchronized Object[] collectData() {
        while (firstInletInputData == null) {
            getExecutionControl().stopTest();
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        if (FastAccessibleSettings.additionalchecks()) {
            int firstInletInputDataSize = firstInletInputData.getAllContainedInlets().size();
            if (firstInletInputDataSize != 1) {
                System.err.println("WARNUNG: firstInletInputDataSize ist " + firstInletInputDataSize + ". Dies entspricht nicht der erwarteten Laenge von 1!");
            }
        }
        return firstInletInputData.getAllData();
    }

}
