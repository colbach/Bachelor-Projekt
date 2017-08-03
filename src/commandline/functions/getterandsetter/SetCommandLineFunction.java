package commandline.functions.getterandsetter;

import commandline.Aliases;
import commandline.CommandLine;
import commandline.CommandLineFooterHeaderFormat;
import commandline.CommandLineFunction;
import commandline.functions.getterandsetter.Getter;
import commandline.functions.getterandsetter.GetterAndSetterResource;
import commandline.functions.getterandsetter.GetterAndSetterResource;
import commandline.functions.getterandsetter.Setter;
import java.util.Map;
import static utils.CharacterRepeateHelper.*;

public class SetCommandLineFunction implements CommandLineFunction {

    private static final int EXIT_CODE = 0;

    @Override
    public void execute(String[] param, CommandLine commandLine) {

        if (param.length == 0) {
            System.out.println(commandLine.getGetterAndSetterResource().getSetInfosSeparatedByLineBreaks());

        } else if (param.length == 1) {
            System.err.println("Zu wenige Argumente.");
        
        } else if (param.length == 2) {
            Setter setter = commandLine.getGetterAndSetterResource().getSetter(param[0]);
            if (setter == null) {
                setter = commandLine.getGetterAndSetterResource().getSetter(commandLine.getAliases().get(param[0]));
                if (setter == null) {
                    System.out.println("Setter fuer " + param[0] + " nicht gefunden!");
                } else {
                    set(setter, param[0]);
                }
            } else {
                set(setter, param[0]);
            }

        } else {
            System.err.println("Zu viele Argumente.");
        }
    }

    public void set(Setter setter, String value) {
        try {
            setter.set(value);
            System.out.println("Wert wurde gesetzt");
            if (setter.isPersistent()) {
                System.out.println("Diese Aenderung ist persistent");
            }
        } catch (Exception e) {
            System.err.println("Wert konnte nicht gesetzt werden (" + e.getMessage() + ")");
        }
    }

    @Override
    public String getDescription() {
        return "Setzt Wert.";
    }

    @Override
    public String getLongDescription() {
        return getDescription() + " Falls diese Funktion ohne Argument aufgerufen wird werden alle verfuegbaren Bezeichner aufgelistet";
    }

    @Override
    public String getUsage() {
        return getName() + " [<bezeichner> <wert>]\n"
                + "@<Bezeichner> Bezeichner von Wert welcher gesetzt werden soll.\n"
                + "@<wert> Neuer Wert.";
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getAliases() {
        return "s ste setter seter";
    }
}
