package projectrunner.executor.contexts;

import projectrunner.outputdata.OutletOutputData;
import projectrunner.inputdata.InletInputData;
import projectrunner.executor.InOutImplementation;
import projectrunner.executioncontrol.ExecutionControl;
import projectrunner.executioncontext.ExecutionCreatorContext;
import java.util.HashMap;
import model.*;
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
