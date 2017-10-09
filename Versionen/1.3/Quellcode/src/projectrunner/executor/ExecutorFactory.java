package projectrunner.executor;

import reflection.common.API;
import projectrunner.executor.ifs.IfForwardNodeExecutor;
import projectrunner.executor.ifs.IfBackNodeExecutor;
import projectrunner.executor.foreachs.ForEachAndReduceRelations;
import projectrunner.executor.foreachs.ForEachCollectPoint;
import projectrunner.executor.foreachs.ReduceNodeExecutor;
import projectrunner.executor.foreachs.ForEachNodeExecutor;
import projectrunner.stats.Stats;
import projectrunner.outputdata.OutletOutputDataDestination;
import projectrunner.inputdata.InletInputDataSource;
import projectrunner.executor.contexts.ContextCreatingNodeExecutor;
import projectrunner.executioncontrol.ExecutionControl;
import projectrunner.executionlogging.ExecutionLogger;
import projectrunner.executioncontext.ExecutionCreatorContext;
import projectrunner.executioncontext.ExecutionContext;
import projectrunner.debugger.Debugger;
import reflection.nodedefinitions.specialnodes.firstvalues.FirstValueNodeDefinition;
import reflection.nodedefinitions.specialnodes.ifs.IfForwardNodeDefinition;
import reflection.nodedefinitions.specialnodes.ifs.IfBackNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ForEachNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ReduceNodeDefinition;
import model.*;
import projectrunner.executor.firstvalues.GeneralizedFirstValueNodeExecutor;

public class ExecutorFactory {

    public static Executor createExecutor(Node executingNode, InletInputDataSource inputSource, OutletOutputDataDestination outputDestination, Debugger debugger, ExecutionControl executionControl, ExecutionLogger executionLogger, API api, ExecutionContext executionContext, Stats stats, long contextIdentifier, ForEachAndReduceRelations forEachAndReduceRelations, ForEachCollectPoint forEachCollectPoint) {

        if (executingNode == null) {
            System.err.println("executingNode ist null! return null");
            return null;
        } else if (inputSource == null) {
            System.err.println("inputSource ist null!");
        }

        // Executor erstellen...
        if (executingNode.getDefinition() instanceof IfBackNodeDefinition) {
            return new IfBackNodeExecutor(executingNode, inputSource, outputDestination, debugger, executionControl, executionLogger, api, stats, contextIdentifier);

        } else if (executingNode.getDefinition() instanceof IfForwardNodeDefinition) {
            return new IfForwardNodeExecutor(executingNode, inputSource, outputDestination, debugger, executionControl, executionLogger, api, stats, contextIdentifier);

        } else if (executingNode.getDefinition() instanceof ForEachNodeDefinition) {
            return new ForEachNodeExecutor(executingNode, inputSource, outputDestination, debugger, executionControl, executionLogger, api, stats, contextIdentifier, forEachAndReduceRelations, forEachCollectPoint, executionContext);

        } else if (executingNode.getDefinition() instanceof ReduceNodeDefinition) {
            return new ReduceNodeExecutor(executingNode, inputSource, outputDestination, debugger, executionControl, executionLogger, api, stats, contextIdentifier, forEachCollectPoint, forEachAndReduceRelations);

        } else if (executingNode.getDefinition() instanceof FirstValueNodeDefinition) {
            return new GeneralizedFirstValueNodeExecutor(executingNode, inputSource, outputDestination, debugger, executionControl, executionLogger, api, stats, contextIdentifier);

        } else if (executingNode.isRunContextCreator()) {
            if (executionContext instanceof ExecutionCreatorContext) {
                return new ContextCreatingNodeExecutor(executingNode, inputSource, outputDestination, debugger, executionControl, executionLogger, api, (ExecutionCreatorContext) executionContext, stats, contextIdentifier);
            } else {
                throw new RuntimeException("createExecutor fuer ContextCreatingNode kann nur von ExecutionCreatorContext erstellt werden!");
            }

        } else {
            return new DefaultNodeExecutor(executingNode, inputSource, outputDestination, debugger, executionControl, executionLogger, api, stats, contextIdentifier);
        }
    }

}
