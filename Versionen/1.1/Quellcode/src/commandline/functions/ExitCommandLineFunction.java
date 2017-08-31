package commandline.functions;

import commandline.CommandLine;
import commandline.CommandLineFooterHeaderFormat;
import commandline.CommandLineFunction;
import java.util.Map;
import static utils.text.CharacterRepeateHelper.*;

public class ExitCommandLineFunction implements CommandLineFunction {
    
    private static final int EXIT_CODE = 0;

    @Override
    public void execute(String[] param, CommandLine commandLine) {

        switch (param.length) {
            case 0:
                System.exit(EXIT_CODE);
                break;
            case 1:
                System.exit(Integer.parseInt(param[0]));
                break;
            default:
                System.err.println("Zu viele Argumente.");
                break;
        }
    }
    
    @Override
    public String getDescription() {
        return "Beendet Programm.";
    }

    @Override
    public String getLongDescription() {
        return getDescription() + "\nOptional kann als Parameter der Statuscode mit dem das Programm beendet werden soll angegeben werden.";
    }

    @Override
    public String getUsage() {
        return getName() + " [<statuscode>]\n"
                + "@<statuscode> der Statuscode mit dem das Programm beendet werden soll (Standart " + EXIT_CODE + ")";
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getAliases() {
        return "verlassen";
    }
}
