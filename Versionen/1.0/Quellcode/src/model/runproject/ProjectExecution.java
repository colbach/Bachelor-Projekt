package model.runproject;

import logging.AdvancedLogger;
import java.awt.Component;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import logging.AdditionalLogger;
import logging.AdditionalOut;
import model.runproject.api.APIImplementation;
import model.runproject.executioncontrol.TerminationReason;
import model.runproject.executioncontrol.ExecutionControl;
import model.runproject.debugger.Debugger;
import model.*;
import model.runproject.executationhub.ExecutionHub;
import model.runproject.executationhub.ExecutionHubRunState;
import model.runproject.callbacks.OnFinishCallback;
import model.runproject.report.Report;
import model.runproject.stats.Stats;
import settings.*;
import view.main.MainWindow;
import view.onrun.*;
import model.runproject.callbacks.OnDestroyCallback;
import model.runproject.executionlogging.ExecutionLogger;

public class ProjectExecution implements OnFinishCallback<ExecutionControl> {

    /**
     * Einzigartiger Bezeichner.
     */
    private final long identifier;

    /**
     * Counter zaelt Anzahl von Instanzen. Wird fuer identifier benoetigt.
     */
    private static final AtomicLong instanceCounter = new AtomicLong(0);

    private final Project project;

    private final Component viewOrNull;

    private ExecutionControl executionControl;

    private AdvancedLogger printer;

    private OnRunWindowManager onRunWindowManager;

    private APIImplementation api;

    private ExecutionHub executationHub;

    private long startNanoTime;
    private long startTime;

    private Stats stats;

    private final ExecutionLogger executionLogger;

    /**
     * Bericht. Wird erst nach Beaendigung erstellt.
     */
    private final ProjectExecutionResultEventListener projectExecutionEventListener;

    private final Set<OnDestroyCallback<ProjectExecution>> onDeletionCallbacks;

    /**
     * Aktueller Debugger oder null falls debug nicht aktiviert ist.
     */
    private Debugger debugger;

    protected ProjectExecution(Project project, Component viewOrNull, ProjectExecutionResultEventListener projectExecutionEventListener, boolean debug, Set<OnDestroyCallback<ProjectExecution>> onDeletionCallbacks) {
        this.identifier = instanceCounter.incrementAndGet();

        this.project = project;
        this.viewOrNull = viewOrNull;
        this.projectExecutionEventListener = projectExecutionEventListener;
        this.onDeletionCallbacks = onDeletionCallbacks;
        this.executionLogger = new ExecutionLogger(debugger);

        // Startzeit notieren...
        startNanoTime = System.nanoTime();
        startTime = System.currentTimeMillis();

        System.out.println("ProjectExecution startet: datum=\"" + Report.DATE_FORMAT.format(new Date(startTime)) + "\"");

        // Debugger erstellen...
        if (debug) { // Falls debug aktiviert
            debugger = new Debugger(executionLogger); // Debugger erstellen
        }

        // Stats erstellen...
        stats = new Stats();

        // executionPrinter erstellen...
        printer = new AdvancedLogger(GeneralSettings.getInstance().getInt(GeneralSettings.DEVELOPER_RING_STACK_SIZE_FOR_EXECUTION_LOGGER, 2000));

        // executionControl erstellen...
        executionControl = new ExecutionControl(this, executionLogger);

        // onRunWindowManager erstellen...
        onRunWindowManager = new OnRunWindowManager();

        // api erstellen...
        TemporarySmartIdentifierContextImplementation temporarySmartIdentifierContextImplementation = new TemporarySmartIdentifierContextImplementation(project.getSmartIdentifierContext());
        SharedObjectSpace sharedObjectSpace = new SharedObjectSpace(executionControl);
        api = new APIImplementation(executionControl, printer, executionLogger, onRunWindowManager, viewOrNull, project, temporarySmartIdentifierContextImplementation, sharedObjectSpace);

        // executationHub erstellen und Ausfuehrung starten...
        executationHub = new ExecutionHub(project, debugger, executionControl, executionLogger, api, stats);
    }

    /**
     * Gibt den aktuellen Debugger zuruck oder null zurueck falls debug nicht
     * aktiviert ist.
     */
    public Debugger getDebugger() {
        return debugger;
    }

    public boolean isDebuggingActivated() {
        return debugger != null;
    }

    /**
     * Gibt executionControl oder null falls die Ausfuehrung noch nicht
     * gestartet wurde.
     */
    public ExecutionControl getExecutionControl() {
        return executionControl;
    }

    public void cancelExecution(String reason) {
        executionControl.setCanceled(reason);
    }

    public boolean isTerminated() {
        return (executionControl != null && executionControl.isTerminated()) || (executationHub != null && executationHub.getRunState() == ExecutionHubRunState.FINISHED);
    }

    public TerminationReason getTerminationReason() {
        return executionControl.getTerminationReason();
    }

    public String getTerminationMessage() {
        String messageFromExecutationControl = executionControl.getTerminationMessage();
        if (messageFromExecutationControl != null) {
            return messageFromExecutationControl;
        } else {
            return "Unkekannt";
        }
    }

    /**
     * Erstellt finalen Report.
     */
    public Report createFinalReport() {
        if (isTerminated()) {
            Report report = new Report(getTerminationReason(), getTerminationMessage(), printer.getSnapshot(), startTime, executionControl.getTerminationTime(), startNanoTime, executionControl.getTerminationNanoTime(), stats);
            return report;

        } else {
            throw new RuntimeException("Report kann nur nach Ausfuehrung erstellt werden!");
        }
    }

    public ExecutionLogger getExecutionLogger() {
        return executionLogger;
    }

    public AdvancedLogger getPrinter() {
        return printer;
    }

    public long getStartTime() {
        return startTime;
    }

    @Override
    public void onFinish(ExecutionControl executionControl) {
        executionLogger.out.println(toString() + ": finished");

        // Erstelle Report...
        Report report = createFinalReport();

        System.out.println("ProjectExecution beendet: datum=\"" + Report.DATE_FORMAT.format(report.getEndDate()) + "\", runtime=\"" + report.getRuntimeFormated() + "\", terminationMessage=\"" + report.getTerminationMessage() + "\"");

        // Report uebergeben...
        projectExecutionEventListener.takeFinalReport(report);
    }

    @Override
    public String toString() {
        return "ProjectExecution #" + identifier;
    }

    public void destroy() throws NotTerminatedYetException {
        if (isTerminated()) {
            AdditionalLogger.out.println(toString() + ": Alle onDeletionCallbacks anweisen sich zu zerstoeren und Speicher frei zu geben");
            for (OnDestroyCallback<ProjectExecution> onDeletionCallback : onDeletionCallbacks) {
                onDeletionCallback.onDestroy(this);
            }
            if (debugger != null) {
                debugger.destroy();
            }
            if (executationHub != null) {
                executationHub.destory();
            }

        } else {
            throw new NotTerminatedYetException("destroy darf erst ausgefuehrt werden wenn Projekt terminiert wurde!");
        }
    }

}
