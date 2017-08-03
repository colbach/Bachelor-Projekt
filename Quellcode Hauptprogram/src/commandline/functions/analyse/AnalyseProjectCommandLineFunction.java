package commandline.functions.analyse;

import commandline.*;
import componenthub.*;
import java.util.*;
;
import model.Inlet;
import model.Node;
import model.Outlet;
import model.Project;
import model.check.CheckResult;
import model.check.CheckWarning;
import model.check.Checker;
import static utils.text.CharacterRepeateHelper.*;



public class AnalyseProjectCommandLineFunction implements CommandLineFunction {

    @Override
    public void execute(String[] param, CommandLine commandLine) {

        switch (param.length) {
            case 0:

                Project project = ComponentHub.getInstance().getProject();

                System.out.println("--- Analyse auf moegliche Repearaturen ---");
                try {
                    ProjectRepair.repairProject(project, false);
                } catch (Exception ex) {
                    System.err.println("Ausnahme: " + ex.getMessage());
                }

                System.out.println("");
                System.out.println("--- Project-Check durchfuehren (Moegliche Ueberschneidung mit Analyse auf moegliche Repearaturen) ---");
                try {
                    CheckResult checkProject = Checker.checkProject(project);
                    for (int i = 0; i < checkProject.size(); i++) {
                        CheckWarning get = checkProject.get(i);
                        if (get.projectIsStillRunnable()) {
                            System.out.println("Warnung: " + get.getMessage());
                        } else {
                            System.out.println("Warnung: " + get.getMessage() + "\n (Kritisch, Projekt ist vermudlich nicht ausfuehrbar)");
                        }
                    }
                    System.out.println("Abgeschlossen");
                    
                } catch (Exception ex) {
                    System.err.println("Ausnahme: " + ex.getMessage());
                }

                break;

            default:
                System.err.println("Zu viele Argumente.");
                break;
        }
    }

    @Override
    public String getDescription() {
        return "Analysiert Project auf Probleme.";
    }

    @Override
    public String getLongDescription() {
        return getDescription() + " Hierbei wird eine Analyse auf moegliche Reperaturen sowie ein Durchlauf der Check-Moduls ausgefuehrt.";
    }

    @Override
    public String getUsage() {
        return getName();
    }

    @Override
    public String getName() {
        return "analyse";
    }

    @Override
    public String getAliases() {
        return "analyseproject analise checkproject check";
    }
}
