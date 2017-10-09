package projectrunner;

import projectrunner.debugger.Debugger;
import projectrunner.debugger.DebuggerRemote;
import projectrunner.executioncontrol.TerminationReason;
import logging.AdvancedLogger;
import projectrunner.executationhub.ExecutionHubRunState;
import projectrunner.executionlogging.ExecutionLogger;

/**
 * Dies Klasse dient zur Steuerung der Ausfuehrung von aussen.
 */
public class ProjectExecutionRemote {

    private final ProjectExecution projectExecution;

    public ProjectExecutionRemote(ProjectExecution projectExecutation) {
        this.projectExecution = projectExecutation;
    }

    /**
     * Bricht Ausfuehrung vom Projekt ab.
     */
    public void cancelExecution() {
        projectExecution.cancelExecution("Abbruch");
    }

    /**
     * Gibt zuruek ob Debugger verfuegbar ist.
     */
    public boolean isDebuggingActivated() {
        return projectExecution.isDebuggingActivated();
    }

    /**
     * Gibt nur Debugger zuruek falls Projekt um Debugmodus gestartet wurde.
     */
    public DebuggerRemote getDebuggerRemote() {
        Debugger debugger = projectExecution.getDebugger();
        if (debugger == null) {
            System.err.println("Debugger ist nicht aktiviert. return null.");
            return null;
        } else {
            return debugger.getRemote();
        }
    }

    public ExecutionLogger getExecutionLogger() {
        return projectExecution.getExecutionLogger();
    }

    public AdvancedLogger getPrinter() {
        return projectExecution.getPrinter();
    }

    /**
     * Gibt an ob Ausfuehrung bereits beendet ist.
     */
    public boolean isTerminated() {
        return projectExecution.isTerminated();
    }

    /**
     * Gibt Beendigungsgrund an.
     */
    public TerminationReason getTerminationReason() {
        return projectExecution.getTerminationReason();
    }

    /**
     * Gibt aktuelle Laufzeit zuruek.
     */
    public long getRuntime() {
        if (isTerminated()) {
            return projectExecution.getExecutionControl().getTerminationTime() - projectExecution.getStartTime();
        } else {
            return System.currentTimeMillis() - projectExecution.getStartTime();
        }
    }

    /**
     * Zerstoert ProjectExecution und ruft OnDestroyCallbacks auf.
     */
    public void destroyProjectExecution() throws NotTerminatedYetException {
        projectExecution.destroy();
    }
}
