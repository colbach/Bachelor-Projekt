/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandline;

import generalexceptions.IllegalUserActionException;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import logging.AdditionalLogger;
import model.Project;
import model.check.CheckResult;
import model.check.CheckWarning;
import model.check.Checker;
import model.runproject.ProjectExecution;
import model.runproject.ProjectExecutionRemote;
import model.runproject.ProjectExecutionResultEventListener;
import model.runproject.ProjectRunner;
import model.runproject.callbacks.OnDestroyCallback;
import model.runproject.report.Report;
import settings.GeneralSettings;
import view.dialogs.CheckDialog;
import view.dialogs.CheckDialogResult;

/**
 *
 * @author christiancolbach
 */
public class CommandLineProjectExcecutor {

    public static synchronized void startProjectExecution(final CommandLine commandLine, final Project project) {

        CheckResult checkResult;
        if (GeneralSettings.getInstance().getBoolean(GeneralSettings.CHECK_PROJECTS_BEFOR_RUN_KEY, true)) {
            checkResult = Checker.checkProject(project);
        } else {
            checkResult = new CheckResult();
        }
        if (checkResult.size() != 0) {
            System.out.println("Das Projekt enthaellt folgende Probleme:");
        }
        for (int i = 0; i < checkResult.size(); i++) {
            CheckWarning get = checkResult.get(i);
            String line = "";
            if (get.projectIsStillRunnable()) {
                String userMessage = get.getUserMessage();
                String message = get.getMessage();
                if (userMessage.equals(message)) {
                    line += "   !!! " + get.getUserMessage() + " !!!";
                } else {
                    line += "   !!! " + get.getUserMessage() + " (" + get.getMessage() + ") !!!";
                }
            } else {
                String userMessage = get.getUserMessage();
                String message = get.getMessage();
                if (userMessage.equals(message)) {
                    line += "   " + get.getUserMessage();
                } else {
                    line += "   " + get.getUserMessage() + " (" + get.getMessage() + ")";
                }
            }
            System.out.println(line);
        }
        if (!checkResult.projectIsStillRunnable()) {
            System.out.println("Projekt enthaellt kritische Probleme und kann deswegen nicht ausgefuehrt werden.");
            return;

        } else if (checkResult.size() != 0) {
            System.out.println("Das Projekt enthaellt Probleme. soll trotzdem versucht werden das Projekt austzufuehren?");
            commandLine.putConfimableTask(new ConfimableTask() {
                @Override
                public void confirmed() {
                    try {
                        ProjectExecutionRemote projectExecutionRemote = ProjectRunner.getInstance().executeProject(project, null, new ProjectExecutionResultEventListener() {
                            @Override
                            public void takeFinalReport(Report report) {
                                generalTakeFinalReport(report);
                            }

                            @Override
                            public void debugViewNeedsUpdate() {
                            }
                        }, (ProjectExecution t) -> {
                            generalOnDestroy(t);
                        }, true);
                        commandLine.setLatestProjectExecutionRemote(projectExecutionRemote);
                    } catch (TooManyListenersException ex) {
                        System.out.println(ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }

                @Override
                public void canceled() {
                }
            });
        } else {
            try {
                ProjectExecutionRemote projectExecutionRemote = ProjectRunner.getInstance().executeProject(project, null, new ProjectExecutionResultEventListener() {
                    @Override
                    public void takeFinalReport(Report report) {
                        generalTakeFinalReport(report);
                    }
                    @Override
                    public void debugViewNeedsUpdate() {
                    }
                }, (ProjectExecution t) -> {
                    generalOnDestroy(t);
                }, true);
                commandLine.setLatestProjectExecutionRemote(projectExecutionRemote);
            } catch (TooManyListenersException ex) {
                System.out.println(ex.getMessage());
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private static void generalTakeFinalReport(Report report) {
        System.out.println(report.toString());
    }

    private static void generalOnDestroy(ProjectExecution t) {
        AdditionalLogger.out.println("CommandLineProjectExcecutor hat onDestroy fuer projectExecution erhalten.");
    }

}
