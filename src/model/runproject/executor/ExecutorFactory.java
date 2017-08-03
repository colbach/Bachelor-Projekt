package model.runproject.executor;

import reflection.nodedefinitions.specialnodes.firstvalues.FirstValueNodeDefinition;
import reflection.nodedefinitions.specialnodes.ifs.IfForwardNodeDefinition;
import reflection.nodedefinitions.specialnodes.ifs.IfBackNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ForEachNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ReduceNodeDefinition;
import model.runproject.executor.contexts.*;
import model.runproject.executor.ifs.*;
import model.runproject.executor.foreachs.*;
import model.runproject.executioncontrol.*;
import model.runproject.debugger.*;
import model.runproject.inputdata.*;
import model.*;
import model.runproject.executioncontext.*;
import model.runproject.executionlogging.*;
import model.runproject.executor.firstvalues.GeneralizedFirstValueNodeExecutor;
import model.runproject.outputdata.*;
import model.runproject.stats.*;
import reflection.*;

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
