package commandline.functions.getterandsetter;

import commandline.Aliases;
import commandline.CommandLine;
import commandline.CommandLineFooterHeaderFormat;
import commandline.CommandLineFunction;
import commandline.functions.getterandsetter.Getter;
import commandline.functions.getterandsetter.GetterAndSetterResource;
import commandline.functions.getterandsetter.GetterAndSetterResource;
import java.util.Map;
import static utils.text.CharacterRepeateHelper.*;

public class SetGetCommandLineFunction implements CommandLineFunction {

    private static final int EXIT_CODE = 0;

    @Override
    public void execute(String[] param, CommandLine commandLine) {

        if (param.length == 0) {
            System.out.println(commandLine.getGetterAndSetterResource().getInfosSeparatedByLineBreaks());

        } else {
            System.err.println("Keine Argumente erlaubt.");
        }
    }

    @Override
    public String getDescription() {
        return "Listet Bezeichner fuer die funktionen set und get auf.";
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
        return "setget";
    }

    @Override
    public String getAliases() {
        return "listsetget sg getset gs listgetset";
    }
}
