package commandline.functions;

import commandline.CommandLine;
import commandline.CommandLineFooterHeaderFormat;
import commandline.CommandLineFunction;
import java.util.HashSet;
import java.util.Map;
import static utils.text.CharacterRepeateHelper.*;
import view.onrun.OnRunWindowManager;

public class CountOnRunWindowsCommandLineFunction implements CommandLineFunction {
    
    @Override
    public void execute(String[] param, CommandLine commandLine) {

        switch (param.length) {
            case 0:
                HashSet<OnRunWindowManager> instances = OnRunWindowManager.getInstances();
                int i=0;
                for(OnRunWindowManager instance : instances)
                    i += instance.count();
                System.out.println(i);
                break;
            default:
                System.err.println("Zu viele Argumente.");
//                break;
        }
    }
    
    @Override
    public String getDescription() {
        return "Gibt Anzahl von OnRunWindows aus.";
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
        return "countonrunwindows";
    }

    @Override
    public String getAliases() {
        return "countonrunwindows countwindows";
    }
}
