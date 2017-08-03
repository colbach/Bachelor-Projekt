package commandline.functions;

import commandline.CommandLine;
import commandline.CommandLineFooterHeaderFormat;
import commandline.CommandLineFunction;
import java.util.Arrays;
import java.util.Map;
import static utils.text.CharacterRepeateHelper.*;

public class CancelPromptCommandLineFunction implements CommandLineFunction {
    
    private static final int EXIT_CODE = 0;

    @Override
    public void execute(String[] param, CommandLine commandLine) {

        switch (param.length) {
            case 0:
                commandLine.getCommandLinePrompt().cancelPrompt();
                break;
            default:
                System.err.println("Diese Funktion nimmt keine Argumente.");
                break;
        }
    }
    
    @Override
    public String getDescription() {
        return "Beendet Prompt.";
    }

    @Override
    public String getLongDescription() {
        return getDescription() + "\nDiese Funktion unterscheidet sich zu exit dahingehend dass mit dem Prompt nicht zwangslaufig das gesammte Programm beendet wird. Andere Prozesse (u.a. die GUI) laufen evt. weiter.";
    }

    @Override
    public String getUsage() {
        return getName();
    }

    @Override
    public String getName() {
        return "cancelprompt";
    }

    @Override
    public String getAliases() {
        return "cancelconsole cancelinput";
    }
}
