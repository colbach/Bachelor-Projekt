package commandline.functions;

import commandline.*;
import componenthub.*;
import java.util.Map;
import static utils.text.CharacterRepeateHelper.*;

public class RunCommandLineFunction implements CommandLineFunction {

    private static final int EXIT_CODE = 0;

    @Override
    public void execute(String[] param, CommandLine commandLine) {

        switch (param.length) {
            case 0:
                CommandLineProjectExcecutor.startProjectExecution(commandLine, ComponentHub.getInstance().getProject());
                
                break;
                
            default:
                System.err.println("Zu viele Argumente.");
                break;
        }
    }

    @Override
    public String getDescription() {
        return "Startet Ausfuehrung von Projekt.";
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }

    @Override
    public String getUsage() {
        return getName();
    }

    @Override
    public String getName() {
        return "run";
    }

    @Override
    public String getAliases() {
        return "ausfuehren ausführen start";
    }
}
