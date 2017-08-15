package projectrunner.executioncontext;

import projectrunner.stats.Stats;
import projectrunner.outputdata.OutletOutputData;
import projectrunner.callbacks.OnFinishCallback;
import projectrunner.executioncontrol.ExecutionControl;
import projectrunner.debugger.Debugger;
import java.util.HashSet;
import java.util.Set;
import logging.AdditionalErr;
import logging.AdditionalOut;
import logging.AdvancedLogger;
import model.*;
import projectrunner.debugger.DebuggerMapForContexts;
import projectrunner.executionlogging.ExecutionLogger;
import projectrunner.executor.foreachs.ForEachAndReduceRelations;
import projectrunner.executor.foreachs.ForEachCollectPoint;
import reflection.*;
import settings.FastAccessibleSettings;

public class ExecutionCreatorContext extends ExecutionContext {

    private final Node contextCreatingNode;
    private final HashSet<ExecutionContext> childContexts;
    private final ExecutionLogger executionLogger;
    private final ForEachAndReduceRelations forEachAndReduceRelations;
    private final ForEachCollectPoint forEachCollectPoint;

    public ExecutionCreatorContext(Node contextCreatingNode, Debugger debugger, ExecutionControl executionControl, ExecutionLogger executionLogger, API api, Stats stats, OnFinishCallback<ExecutionContext> onFinishCallback, ForEachAndReduceRelations forEachAndReduceRelations, ForEachCollectPoint forEachCollectPoint) {
        super(debugger, executionControl, executionLogger, api, stats, onFinishCallback, DebuggerMapForContexts.PARENT_IDENTIFIER_FOR_CONTEXT_CREATOR, forEachAndReduceRelations, forEachCollectPoint);

        if(FastAccessibleSettings.additionalchecks()) {
            if(!(contextCreatingNode.getDefinition() instanceof ContextCreator)) {
                throw new IllegalArgumentException("contextCreatingNode muss ContextCreator sein!");
            }
        }
        
        this.executionLogger = executionLogger;
        this.contextCreatingNode = contextCreatingNode;
        this.childContexts = new HashSet<>();
        this.forEachAndReduceRelations = forEachAndReduceRelations;
        this.forEachCollectPoint = forEachCollectPoint;

        this.submitExecutorIfNotSubmited(contextCreatingNode);
    }

    public void startNewContext(OutletOutputData outletOutputData) {

        executionLogger.out.println(toString() + ": Start New Context");

        if (outletOutputData.size() == 0) {
            executionLogger.err.println("Start New Context ohne Outlet-Data (outletOutputData.size()=0). Neuer Kontext wird keine Nodes bearbeiten!");
        }

        synchronized (childContexts) {

            // neuen Context erzeugen...
            ExecutionContext thisObject = this;
            ExecutionContext createdContext = new ExecutionContext(getDebugger(), getExecutionControl(), getExecutionLogger(), getApi(), getStats(), new OnFinishCallback<ExecutionContext>() {
                @Override
                public synchronized void onFinish(ExecutionContext executionContext) {
                    synchronized (childContexts) {
                        childContexts.remove(executionContext);
                        if (childContexts.isEmpty()) {
                            executionLogger.out.println(thisObject.toString() + ": Context finished");
                            if (isTerminated()) { // ACHTUNG: isTerminated() ist eine Ueberschriebene Methode
                                executionLogger.out.println(thisObject.toString() + ": Terminiert (Aufruf Callback)");
                                getOnFinishCallback().onFinish(thisObject);
                            }
                        }
                    }
                }
            }, getIdentifier(), forEachAndReduceRelations, forEachCollectPoint);

            // Context in childContexts eintragen...
            childContexts.add(createdContext);

            // Context starten...
            //System.out.println("****** deliever on for each context");
            createdContext.deliver(contextCreatingNode, outletOutputData);

        }
    }

    @Override
    public boolean isTerminated() {
        return super.isTerminated() && childContexts.size() == 0;
    }

    public int getRunningChildContextCount() {

        synchronized (childContexts) {
            return childContexts.size();
        }
    }

    public Set<ExecutionContext> getRunningChildContext() {

        synchronized (childContexts) {
            return (HashSet<ExecutionContext>) childContexts.clone();
        }
    }

    @Override
    public String toString() {
        return "ExecutionCreatorContext #" + getIdentifier();
    }

    /**
     * Anweisung Speicher frei zu geben.
     */
    @Override
    public void destory() {
        super.destory();
        synchronized (childContexts) {
            for (ExecutionContext childContext : childContexts) {
                childContext.destory();
            }
        }
    }

    public NodeDefinition getContextCreatingNodeDefinition() {
        return contextCreatingNode.getDefinition();
    }
}
