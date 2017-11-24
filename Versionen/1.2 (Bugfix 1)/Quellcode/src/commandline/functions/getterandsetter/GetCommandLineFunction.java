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

public class GetCommandLineFunction implements CommandLineFunction {

    private static final int EXIT_CODE = 0;

    @Override
    public void execute(String[] param, CommandLine commandLine) {

        switch (param.length) {
            case 0:
                System.out.println(commandLine.getGetterAndSetterResource().getGetInfosSeparatedByLineBreaks());
                break;
            case 1:
                Getter getter = commandLine.getGetterAndSetterResource().getGetter(param[0]);
                if (getter == null) {
                    getter = commandLine.getGetterAndSetterResource().getGetter(commandLine.getAliases().get(param[0]));
                    if (getter == null) {
                        System.out.println("Getter fuer " + param[0] + " nicht gefunden!");
                    } else {
                        System.out.println(getter.get());
                    }
                } else {
                    System.out.println(getter.get());
                }   break;
            default:
                System.err.println("Zu viele Argumente.");
                break;
        }
    }

    @Override
    public String getDescription() {
        return "Gibt Wert aus.";
    }

    @Override
    public String getLongDescription() {
        return getDescription() + " Falls diese Funktion ohne Argument aufgerufen wird werden alle verfuegbaren Bezeichner aufgelistet";
    }

    @Override
    public String getUsage() {
        return getName() + " [<bezeichner>]\n"
                + "@<Bezeichner> Bezeichner von Wert welcher ausgegeben werden soll.";
    }

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getAliases() {
        return "g gte getter geter print";
    }
}
