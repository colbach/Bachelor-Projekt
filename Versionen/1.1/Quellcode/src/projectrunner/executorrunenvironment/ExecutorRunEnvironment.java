package projectrunner.executorrunenvironment;

import projectrunner.callbacks.OnFinishCallback;
import java.util.HashSet;
import projectrunner.executor.Executor;

public class ExecutorRunEnvironment implements OnFinishCallback<ExecutorThread> {

    private final HashSet<ExecutorThread> executorThreads;
    private final OnFinishCallback<ExecutorRunEnvironment> onFinishCallback;
    private boolean destroyed;

    public ExecutorRunEnvironment(OnFinishCallback<ExecutorRunEnvironment> onFinishCallback) {
        this.executorThreads = new HashSet<>();
        this.onFinishCallback = onFinishCallback;
        this.destroyed = false;
    }

    public synchronized void submit(Executor executor) {

        if (!destroyed) {
            // executorThread erzeugen, starten & hinzufuegen...
            ExecutorThread executorThread = new ExecutorThread(executor, this);
            executorThread.start();
            executorThreads.add(executorThread);
        }
    }

    public synchronized boolean isTerminated() {
        if (destroyed) {
            return true;
        } else {
            for (ExecutorThread executorThread : executorThreads) {
                if (executorThread.isAlive()) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public synchronized void onFinish(ExecutorThread executorThread) {
        if (!destroyed) {
            executorThreads.remove(executorThread);
            if (executorThreads.isEmpty()) {
                onFinishCallback.onFinish(this);
            }
        }
    }

    public synchronized void destroy() {
        for (ExecutorThread executorThread : executorThreads) { // Alle Threads unterbrechen...
            executorThread.interrupt();
        }
        executorThreads.clear();
        destroyed = true;
    }
}
