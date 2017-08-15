package projectrunner.executorrunenvironment;

import projectrunner.callbacks.OnFinishCallback;
import projectrunner.executor.Executor;
import projectrunner.executioncontrol.StopException;

public class ExecutorThread extends Thread {
    
    private final Executor executor;
    private final OnFinishCallback onFinishCallback;

    public ExecutorThread(Executor executor, OnFinishCallback onFinishCallback) {
        this.executor = executor;
        this.onFinishCallback = onFinishCallback;
    }

    @Override
    public void run() {
        try {
            executor.run();
        } catch (StopException e) {
        }
        onFinishCallback.onFinish(this);
    }

    @Override
    public String toString() {
        return "ExecutorThread (" + executor.toString() + ")";
    }
}
