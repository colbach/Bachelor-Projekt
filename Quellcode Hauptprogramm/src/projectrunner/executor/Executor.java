package projectrunner.executor;

import projectrunner.outputdata.OutletOutputDataDestination;
import projectrunner.inputdata.InletInputDataSource;
import logging.AdditionalOut;
import logging.AdvancedLogger;
import projectrunner.executioncontrol.ExecutionControl;
import projectrunner.debugger.Debugger;
import java.util.concurrent.atomic.AtomicLong;
import model.Node;
import projectrunner.executionlogging.ExecutionLogger;
import projectrunner.stats.Stats;
import reflection.common.API;
import reflection.common.NodeDefinition;
import view.onrun.OnRunWindowManager;

public abstract class Executor {

    /**
     * Einzigartiger Bezeichner.
     */
    private final long identifier;

    /**
     * Counter zaelt Anzahl von Instanzen. Wird fuer identifier benoetigt.
     */
    private static final AtomicLong instanceCounter = new AtomicLong(0);

    private final long contextIdentifier;

    private final Node executingNode;

    private final Debugger debugger;

    private final InletInputDataSource inputSource;
    private final OutletOutputDataDestination outputDestination;

    private final ExecutionControl executionControl;

    private final ExecutionLogger executionLogger;

    private final API api;

    private final Stats stats;

    public Executor(Node executingNode, InletInputDataSource inputSource, OutletOutputDataDestination outputDestination, Debugger debugger, ExecutionControl executionControl, ExecutionLogger executionLogger, API api, Stats stats, long contextIdentifier) {
        this.identifier = instanceCounter.incrementAndGet();
        this.contextIdentifier = contextIdentifier;
        this.inputSource = inputSource;
        this.outputDestination = outputDestination;
        this.executingNode = executingNode;
        this.debugger = debugger;
        this.executionControl = executionControl;
        this.executionLogger = executionLogger;
        this.api = api;
        this.stats = stats;
        if (debugger != null) {
            debugger.registerExecutorIdentifier(identifier, executingNode, toString(), contextIdentifier);
        }

        executionLogger.out.println("New Executor: " + toString());

    }

    public Node getNode() {
        return executingNode;
    }

    public long getIdentifier() {
        return identifier;
    }

    public Stats getStats() {
        return stats;
    }

    /**
     * Gibt Debugger oder null zurueck falls debug nicht aktiviert.
     */
    public Debugger getDebugger() {
        return debugger;
    }

    protected InletInputDataSource getInputSource() {
        return inputSource;
    }

    protected OutletOutputDataDestination getOutputDestination() {
        return outputDestination;
    }

    public ExecutionControl getExecutionControl() {
        return executionControl;
    }

    public ExecutionLogger getExecutionLogger() {
        return executionLogger;
    }

    public long getContextIdentifier() {
        return contextIdentifier;
    }

    public Node getExecutingNode() {
        return executingNode;
    }

    public API getApi() {
        return api;
    }

    @Override
    public String toString() {
        return getExecutorType() + " #" + identifier + " \"" + executingNode.toString() + "\"";
    }

    public abstract void run();

    public abstract String getExecutorType();

}
