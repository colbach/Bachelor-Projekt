package model.runproject.executioncontrol;

import java.util.HashSet;
import logging.AdditionalErr;
import logging.AdditionalOut;
import logging.AdvancedLogger;
import model.Node;
import model.runproject.inputdata.InletInputData;
import model.runproject.callbacks.OnFinishCallback;
import model.runproject.executionlogging.ExecutionLogger;

/**
 * Dies Klasse dient intern zur Steuerung der Ausfuehrung. Fuer die Steuerung
 * von aussen (Beispielsweise ueber die UI) wird ProjektExecutationRemote
 * verwendet.
 */
public class ExecutionControl {

    private ExecutionLogger executionLogger;

    private boolean canceled;

    private boolean failed;
    private Node failedNode;

    private boolean finished;

    private String terminationMessage;
    private long terminationTime = -1;
    private long terminationNanoTime = -1;
    
    private final OnFinishCallback onTermination;

    public ExecutionControl(OnFinishCallback<ExecutionControl> onTermination, ExecutionLogger executionLogger) {
        this.canceled = false;
        this.failedNode = null;
        this.onTermination = onTermination;
        this.executionLogger = executionLogger;
    }

    public synchronized void setCanceled(String terminationMessage) {
        if (isRunning()) {
            executionLogger.out.println("ExecutionControl: canceled message=\"" + terminationMessage + "\"");
            this.canceled = true;
            this.terminationMessage = terminationMessage;
            this.terminationNanoTime = System.nanoTime();
            this.terminationTime = System.currentTimeMillis();
            onTermination.onFinish(this);
        } else {
            System.err.println("setCanceled(...) kann nicht ausgefuehrt werden da ExecutionControl bereits beendet wurde!");
        }
    }

    public synchronized void setFailed(String terminationMessage) {
        if (isRunning()) {
                          
            executionLogger.err.println("ExecutionControl: failed message=\"" + terminationMessage + "\"");
            this.failed = true;
            this.terminationMessage = terminationMessage;
            this.terminationNanoTime = System.nanoTime();
            this.terminationTime = System.currentTimeMillis();
            onTermination.onFinish(this);
        } else {
            System.err.println("setFailed(...) kann nicht ausgefuehrt werden da ExecutionControl bereits beendet wurde!");
        }
    }

    public synchronized void setFailed(String terminationMessage, Node node) {
        if (isRunning()) {
            executionLogger.err.println("ExecutionControl: failed message=\"" + terminationMessage + "\", node=\"" + node.getName() + "\"");
            this.failed = true;
            this.terminationMessage = terminationMessage;
            this.failedNode = node;
            this.terminationNanoTime = System.nanoTime();
            this.terminationTime = System.currentTimeMillis();
            onTermination.onFinish(this);
        } else {
            System.err.println("setFailed(...) kann nicht ausgefuehrt werden da ExecutionControl bereits beendet wurde!");
        }
    }

    public synchronized void setFinished(String terminationMessage) {
        if (isRunning()) {
            executionLogger.out.println("ExecutionControl: finished message=\"" + terminationMessage + "\"");
            this.finished = true;
            this.terminationMessage = terminationMessage;
            this.terminationNanoTime = System.nanoTime();
            this.terminationTime = System.currentTimeMillis();
            onTermination.onFinish(this);
        } else {
            System.err.println("setFinished(...) kann nicht ausgefuehrt werden da ExecutionControl bereits beendet wurde!");
        }
    }

    public boolean isCanceled() {
        return canceled;
    }

    public boolean isFailed() {
        return failed;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isRunning() {
        return !isTerminated();
    }

    public boolean isTerminated() {
        return canceled || finished || failed;
    }

    public TerminationReason getTerminationReason() {
        if (isCanceled()) {
            return TerminationReason.CANCELED;
        } else if (isFinished()) {
            return TerminationReason.FINISHED;
        } else if (isFailed()) {
            return TerminationReason.RUNTIME_ERROR;
        } else {
            return null;
        }
    }

    public String getTerminationMessage() {
        return terminationMessage;
    }

    public void stopTest() throws StopException {
        if (isTerminated()) {
            throw new StopException(); // ACHTUNG: != NodeDefinition.StopException()
        }
    }

    public void setFailedNode(Node node) {
        failedNode = node;
    }

    public ExecutionLogger getExecutionLogger() {
        return executionLogger;
    }

    public Node getFailedNode() {
        return failedNode;
    }

    public long getTerminationTime() {
        return terminationTime;
    }

    public long getTerminationNanoTime() {
        return terminationNanoTime;
    }

}
