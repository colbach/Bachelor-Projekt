package model.runproject;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import logging.AdditionalLogger;
import model.Project;
import model.runproject.callbacks.*;

public class ProjectRunner implements OnDestroyCallback<ProjectExecution> {

    public static final int ALLOWED_CONCURRENT_PROJECT_EXECUTATIONS = 1;

    private static ProjectRunner instance;

    private final ArrayList<ProjectExecution> projectExecutations;
    private int totalProjectExecutations;

    public static ProjectRunner getInstance() {
        if (instance == null) {
            instance = new ProjectRunner();
        }
        return instance;
    }

    private ProjectRunner() {
        projectExecutations = new ArrayList<>();
        totalProjectExecutations = 0;
    }
    
    public synchronized int getProjectExecutationCount() {
        return projectExecutations.size();
    }
    
    public synchronized int getTotalProjectExecutationCount() {
        return totalProjectExecutations;
    }

    public synchronized ProjectExecutionRemote executeProject(Project project, Component viewOrNull, ProjectExecutionResultEventListener executionListener, OnDestroyCallback<ProjectExecution> onDestroyCallback, boolean debug) throws ToManyConcurrentProjectExecutions, Exception {

        // Kontrolle ob evt bereits zu viele ProjectExecutions laufen (momentan nur 1 erlaubt)...
        if (projectExecutations.size() + 1 > ALLOWED_CONCURRENT_PROJECT_EXECUTATIONS) {
            throw new ToManyConcurrentProjectExecutions(ALLOWED_CONCURRENT_PROJECT_EXECUTATIONS);
        }

        // ProjectExecution erstellen, starten und zu projectExecutations hinzufuegen...
        HashSet<OnDestroyCallback<ProjectExecution>> onDeletionCallbacks = new HashSet<>();
        onDeletionCallbacks.add(this);
        onDeletionCallbacks.add(onDestroyCallback);
        ProjectExecution projectExecutation = new ProjectExecution(project, viewOrNull, executionListener, debug, onDeletionCallbacks);
        projectExecutations.add(projectExecutation);
        totalProjectExecutations++;
        
        // ProjectExecutionRemote erstellen...
        ProjectExecutionRemote projectExecutationRemote = new ProjectExecutionRemote(projectExecutation);

        return projectExecutationRemote;

    }

    @Override
    public synchronized void onDestroy(ProjectExecution projectExecution) {
        AdditionalLogger.out.println(toString() + "ProjectRunner: onDestroy() von ProjectExecution erhalten. ProjectExecution aus projectExecutations entfernen. Actuelle Ausfuehrungen: " + getProjectExecutationCount() + ", Total: " + getTotalProjectExecutationCount() + ".");
        projectExecutations.remove(projectExecution);
    }
    
    public synchronized void cancelAllProjectExecution() {
        for(ProjectExecution projectExecution : projectExecutations) {
            projectExecution.cancelExecution("Genereller Abbruch");
        }
    }
}
