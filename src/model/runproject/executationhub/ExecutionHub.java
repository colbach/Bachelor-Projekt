package model.runproject.executationhub;

import model.runproject.executioncontrol.ExecutionControl;
import model.runproject.debugger.Debugger;
import java.util.HashSet;
import logging.AdditionalLogger;
import logging.AdditionalOut;
import logging.AdvancedLogger;
import model.*;
import model.runproject.*;
import model.runproject.executioncontext.*;
import model.runproject.callbacks.OnFinishCallback;
import model.runproject.executionlogging.ExecutionLogger;
import model.runproject.executor.foreachs.ForEachAndReduceRelations;
import model.runproject.executor.foreachs.ForEachCollectPoint;
import model.runproject.stats.Stats;
import reflection.API;
import reflection.ContextCreator;
import reflection.NodeDefinition;
import reflection.SupportingContextCreator;

public class ExecutionHub implements OnFinishCallback<ExecutionContext> {

    private HashSet<ExecutionCreatorContext> executionCreatorContexts;
    private ExecutionControl executionControl;
    private final ExecutionLogger executionLogger;

    public ExecutionHub(Project project, Debugger debugger, ExecutionControl executionControl, ExecutionLogger executionLogger, API api, Stats stats) {

        executionLogger.out.println("New ExecutionHub: " + project.getNodes().length + " Nodes");

        this.executionLogger = executionLogger;
        this.executionControl = executionControl;

        // contextCreatingNodes ermitteln...
        HashSet<Node> contextCreatingNodes = new HashSet<>();
        for (Node node : project.getNodes()) {
            if (node.getDefinition() instanceof ContextCreator) {
                contextCreatingNodes.add(node);
            }
        }

        // forEachAndReduceRelations aufbauen...
        ForEachAndReduceRelations forEachAndReduceRelations = new ForEachAndReduceRelations(project.getNodes());

        // forEachCollectPoint anlegen...
        ForEachCollectPoint forEachCollectPoint = new ForEachCollectPoint(executionControl, forEachAndReduceRelations);

        // zu jedem contextCreatingNode ein ExecutionCreatorContext erzeugen...
        this.executionCreatorContexts = new HashSet<>();
        for (Node contextCreatingNode : contextCreatingNodes) {
            ExecutionCreatorContext newCreatorContext = new ExecutionCreatorContext(contextCreatingNode, debugger, executionControl, executionLogger, api, stats, this, forEachAndReduceRelations, forEachCollectPoint);
            executionCreatorContexts.add(newCreatorContext);
        }

        executionLogger.out.println("ExecutionHub: " + executionCreatorContexts.size() + " ExecutionCreatorContexts gestartet");
    }

    public ExecutionHubRunState getRunState() {
        for (ExecutionCreatorContext executionCreatorContext : executionCreatorContexts) {
            if (!executionCreatorContext.isTerminated()) {
                return ExecutionHubRunState.RUNNING;
            }
        }
        return ExecutionHubRunState.FINISHED;
    }

    @Override
    public synchronized void onFinish(ExecutionContext t) {
        executionCreatorContexts.remove(t);
        if (onlyContainsSupportContextCreators()) {
            executionLogger.out.println("ExecutionHub: finished");
            if (!executionControl.isTerminated()) {
                executionLogger.out.println("ExecutionHub: executionControl noch nicht terminiert. executionControl auf Beendet setzen");
                executionControl.setFinished("Erfolg");
            } else {
                executionLogger.out.println("ExecutionHub: executionControl bereits terminiert.");
            }
        }
    }

    private synchronized boolean onlyContainsSupportContextCreators() {
        for (ExecutionCreatorContext executionCreatorContext : executionCreatorContexts) {
            if (!(executionCreatorContext.getContextCreatingNodeDefinition() instanceof SupportingContextCreator)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Anweisung Speicher frei zu geben.
     */
    public void destory() {
        AdditionalLogger.out.println(toString() + "ExecutionHub: onDestroy() von ProjectExecution erhalten. Alle ExecutionCreatorContexte anweisen sich zu stoppen und Speicher frei zu geben sowie die Kinder anzuweisen das gleiche zu tun.");
        for (ExecutionCreatorContext executionCreatorContext : executionCreatorContexts) {
            executionCreatorContext.destory();
        }
    }
}
