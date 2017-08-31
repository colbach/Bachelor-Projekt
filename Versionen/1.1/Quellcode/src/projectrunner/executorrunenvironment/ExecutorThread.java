package projectrunner.executorrunenvironment;

import java.lang.reflect.InvocationTargetException;
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
        } catch (Exception e) {
            if(!e.getClass().getName().equals("StopException")) {
                throw e;
            }
        }
        onFinishCallback.onFinish(this);
    }

    @Override
    public String toString() {
        return "ExecutorThread (" + executor.toString() + ")";
    }
}
