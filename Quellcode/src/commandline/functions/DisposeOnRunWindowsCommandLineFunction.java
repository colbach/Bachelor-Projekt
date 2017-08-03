package commandline.functions;

import commandline.CommandLine;
import commandline.CommandLineFooterHeaderFormat;
import commandline.CommandLineFunction;
import java.util.HashSet;
import java.util.Map;
import static utils.text.CharacterRepeateHelper.*;
import view.onrun.OnRunWindowManager;

public class DisposeOnRunWindowsCommandLineFunction implements CommandLineFunction {
    
    @Override
    public void execute(String[] param, CommandLine commandLine) {

        switch (param.length) {
            case 0:
                HashSet<OnRunWindowManager> instances = OnRunWindowManager.getInstances();
                for(OnRunWindowManager instance : instances)
                    instance.disposeAll();
                break;
            default:
                System.err.println("Zu viele Argumente.");
                break;
        }
    }
    
    @Override
    public String getDescription() {
        return "Schliesst alle OnRunWindows.";
    }

    @Override
    public String getLongDescription() {
        return getDescription() + " OnRunWindows sind Fenster die durch Ausfuehrung von Projekt zur Anzeige von Daten geoeffnet wurden.";
    }

    @Override
    public String getUsage() {
        return getName();
    }

    @Override
    public String getName() {
        return "disposeonrunwindows";
    }

    @Override
    public String getAliases() {
        return "closeonrunwindows closewindows disposewindows";
    }
}
