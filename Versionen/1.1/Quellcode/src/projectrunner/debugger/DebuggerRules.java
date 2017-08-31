package projectrunner.debugger;

public class DebuggerRules {
    
    private static DebuggerRules instance = null;

    public static DebuggerRules getInstance() {
        if(instance == null)
            instance = new DebuggerRules();
        return instance;
    }

    private boolean triggerBreakpointOnNewExecutor = true;
    private boolean triggerBreakpointOnStateChangeToPreparing = true;
    private boolean triggerBreakpointOnStateChangeToCollecting = true;
    private boolean triggerBreakpointOnStateChangeToRunning = true;
    private boolean triggerBreakpointOnStateChangeToDelievering = true;
    private boolean triggerBreakpointOnStateChangeToFinished = true;

    public boolean isTriggerBreakpointOnStateChangeToPreparing() {
        return triggerBreakpointOnStateChangeToPreparing;
    }

    public void setTriggerBreakpointOnStateChangeToPreparing(boolean triggerBreakpointOnStateChangeToPreparing) {
        this.triggerBreakpointOnStateChangeToPreparing = triggerBreakpointOnStateChangeToPreparing;
    }

    public void toogleTriggerBreakpointOnStateChangeToPreparing() {
        triggerBreakpointOnStateChangeToPreparing = !triggerBreakpointOnStateChangeToPreparing;
    }

    public boolean isTriggerBreakpointOnStateChangeToCollecting() {
        return triggerBreakpointOnStateChangeToCollecting;
    }

    public void setTriggerBreakpointOnStateChangeToCollecting(boolean triggerBreakpointOnStateChangeToCollecting) {
        this.triggerBreakpointOnStateChangeToCollecting = triggerBreakpointOnStateChangeToCollecting;
    }

    public void toogleTriggerBreakpointOnStateChangeToCollecting() {
        triggerBreakpointOnStateChangeToCollecting = !triggerBreakpointOnStateChangeToCollecting;
    }

    public boolean isTriggerBreakpointOnStateChangeToRunning() {
        return triggerBreakpointOnStateChangeToRunning;
    }

    public void setTriggerBreakpointOnStateChangeToRunning(boolean triggerBreakpointOnStateChangeToRunning) {
        this.triggerBreakpointOnStateChangeToRunning = triggerBreakpointOnStateChangeToRunning;
    }
    
    public void toogleTriggerBreakpointOnStateChangeToRunning() {
        triggerBreakpointOnStateChangeToRunning = !triggerBreakpointOnStateChangeToRunning;
    }

    public boolean isTriggerBreakpointOnStateChangeToDelievering() {
        return triggerBreakpointOnStateChangeToDelievering;
    }

    public void setTriggerBreakpointOnStateChangeToDelievering(boolean triggerBreakpointOnStateChangeToDelievering) {
        this.triggerBreakpointOnStateChangeToDelievering = triggerBreakpointOnStateChangeToDelievering;
    }

    public void toogleTriggerBreakpointOnStateChangeToDelievering() {
        triggerBreakpointOnStateChangeToDelievering = !triggerBreakpointOnStateChangeToDelievering;
    }

    public boolean isTriggerBreakpointOnStateChangeToFinished() {
        return triggerBreakpointOnStateChangeToFinished;
    }
    
    public void toogleTriggerBreakpointOnStateChangeToFinished() {
        triggerBreakpointOnStateChangeToFinished = !triggerBreakpointOnStateChangeToFinished;
    }

    public void setTriggerBreakpointOnStateChangeToFinished(boolean triggerBreakpointOnStateChangeToFinished) {
        this.triggerBreakpointOnStateChangeToFinished = triggerBreakpointOnStateChangeToFinished;
    }

    public boolean isTriggerBreakpointOnNewExecutor() {
        return triggerBreakpointOnNewExecutor;
    }

    public void toogleTriggerBreakpointOnNewExecutor() {
        triggerBreakpointOnNewExecutor = !triggerBreakpointOnNewExecutor;
    }

    public void setTriggerBreakpointOnNewExecutor(boolean triggerBreakpointOnNewExecutor) {
        this.triggerBreakpointOnNewExecutor = triggerBreakpointOnNewExecutor;
    }
}
