package model.runproject.executor.contexts;

import java.util.HashMap;
import model.*;
import model.runproject.executioncontrol.*;
import model.runproject.executioncontext.*;
import model.runproject.executor.*;
import model.runproject.inputdata.*;
import model.runproject.outputdata.*;
import reflection.*;

public class ContextCreatorInOutImplementation extends InOutImplementation implements ContextCreatorInOut {

    private final ExecutionCreatorContext executionCreatorContext;

    ContextCreatorInOutImplementation(InletInputData input, Node node, ExecutionControl executionControl, ExecutionCreatorContext executionCreatorContext) {
        super(input, node, executionControl, executionCreatorContext.getIdentifier());
        this.executionCreatorContext = executionCreatorContext;
    }

    @Override
    public void startNewContext() throws Exception {
        OutletOutputData copy = new OutletOutputData(getOutletOutputData());
        Outlet triggerOutlet = getNode().getOutlet(Node.TRIGGER_LABEL);
        if (triggerOutlet != null) {
            copy.put(triggerOutlet, new Object[0]);
        } else {
            System.err.println(toString() + ": Ausloeser-Outlet nicht gefunden!");
        }
        executionCreatorContext.startNewContext(copy);
    }

    @Override
    public int getRunningContextCount() {
        return executionCreatorContext.getRunningChildContextCount();
    }

    @Override
    public String toString() {
        return "ContextCreatorInOutImplementation (" + getNode().getName() + ", " + executionCreatorContext.toString() + ")";
    }
}
