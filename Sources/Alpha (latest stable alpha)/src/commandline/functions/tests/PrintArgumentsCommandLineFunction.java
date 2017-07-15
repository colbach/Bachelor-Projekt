package commandline.functions.tests;

import commandline.CommandLine;
import commandline.CommandLineFooterHeaderFormat;
import commandline.CommandLineFunction;
import java.util.Arrays;
import java.util.Map;
import static utils.CharacterRepeateHelper.*;

public class PrintArgumentsCommandLineFunction implements CommandLineFunction {
    
    private static final int EXIT_CODE = 0;

    @Override
    public void execute(String[] param, CommandLine commandLine) {
        int symbols = 0;
        for(int i=0; i<param.length; i++) {
            symbols += param[i].length();
            System.out.println("Argument " + i + ": \"" + param[i] + "\" (" + param[i].length() + " Zeichen)");
        }
        System.out.println("(Insgesammt " + param.length + " Argumente und " + symbols + " Zeichen)");
    }
    
    @Override
    public String getDescription() {
        return "Testmethode gibt angegebene Argumente aus.";
    }

    @Override
    public String getLongDescription() {
        return getDescription() + "\nDiese Methode soll dazu dienen zu testen wie Argumente geparst wurden.";
    }

    @Override
    public String getUsage() {
        return getName() + " [<arg1>, <arg2>, ...]\n"
                + "@<arg1>, <arg2>, ... Argumente welche ausgegeben werden sollen.";
    }

    @Override
    public String getName() {
        return "printargument";
    }

    @Override
    public String getAliases() {
        return "printargs printarg arg args";
    }
}
