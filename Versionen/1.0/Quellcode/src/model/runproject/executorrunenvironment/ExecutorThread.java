package model.runproject.executorrunenvironment;

import model.runproject.callbacks.OnFinishCallback;
import model.runproject.executor.Executor;
import model.runproject.executioncontrol.StopException;

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
