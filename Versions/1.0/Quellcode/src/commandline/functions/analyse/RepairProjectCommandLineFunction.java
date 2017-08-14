package commandline.functions.analyse;

import commandline.*;
import componenthub.*;
import java.util.*;
;
import model.Inlet;
import model.Node;
import model.Outlet;
import model.Project;
import static utils.text.CharacterRepeateHelper.*;



public class RepairProjectCommandLineFunction implements CommandLineFunction {
    
    @Override
    public void execute(String[] param, CommandLine commandLine) {

        switch (param.length) {
            case 0:
                try {
                    ProjectRepair.repairProject(ComponentHub.getInstance().getProject(), true);
                } catch (Exception ex) {
                    System.err.println("Fehler: " + ex.getMessage());
                }
                break;
                
            default:
                System.err.println("Zu viele Argumente.");
                break;
        }
    }

    @Override
    public String getDescription() {
        return "Fuehrt Reperation von Projekt aus.";
    }

    @Override
    public String getLongDescription() {
        return getDescription() + " Dieser Prozess ist destruktiv und nicht zwangslaufig erfolgreich. Projekte sollten sicherheitshalber vor Ausfuehrung gesichert werden.";
    }

    @Override
    public String getUsage() {
        return getName();
    }

    @Override
    public String getName() {
        return "repair";
    }

    @Override
    public String getAliases() {
        return "repairproject";
    }
}
