package commandline.functions;

import commandline.*;
import java.util.Map;
import projectrunner.ProjectRunner;
import static utils.text.CharacterRepeateHelper.*;

public class CancelCommandLineFunction implements CommandLineFunction {

    private static final int EXIT_CODE = 0;

    @Override
    public void execute(String[] param, CommandLine commandLine) {

        switch (param.length) {
            case 0:
                ProjectRunner.getInstance().cancelAllProjectExecution();
                break;
                
            default:
                System.err.println("Diese Funktion nimmt keine Argumente.");
                break;
        }
    }

    @Override
    public String getDescription() {
        return "Bricht Ausfuehrung von Projekt ab.";
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
        return "cancel";
    }

    @Override
    public String getAliases() {
        return "stop";
    }
}
