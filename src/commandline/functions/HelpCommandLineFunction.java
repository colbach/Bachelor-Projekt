package commandline.functions;

import commandline.CommandLine;
import commandline.CommandLineFooterHeaderFormat;
import commandline.CommandLineFunction;
import commandline.OutputLineLengthLimiter;
import java.util.Map;
import utils.CharacterRepeateHelper;

public class HelpCommandLineFunction implements CommandLineFunction {

    @Override
    public void execute(String[] param, CommandLine commandLine) {

        switch (param.length) {
            case 0:
                {
                    String tap = CharacterRepeateHelper.repeateSpaceCharacter(5);
                    String linebreak = "\n" + tap;
                    CommandLineFooterHeaderFormat.printFormatedHeaderLine(getName());
                    System.out.println("ALLGEMEIN");
                    System.out.println(OutputLineLengthLimiter.limitLineLength(
                            "Parameter koennen mit leerzeichen getrennt nach dem "
                                    + "Funktionsnamen angegeben werden. Leerzeichen innerhalb von Argumenten muessen mit \\ maskiert werden "
                                    + "oder zwischen Gaensefuessen geschreiben werden. "
                                    + "Mehr Informationen ueber eine bestimmte Funktion koennen wie folgt ausgegeben werden: "
                                    + "help <funktionsnamen>", "", CommandLineFooterHeaderFormat.WIDTH));
                    System.out.println("\nFUNKTIONEN");
                    for (Map.Entry<String, CommandLineFunction> entry : commandLine.getFunctions().entrySet()) {
                        String key = entry.getKey();
                        CommandLineFunction value = entry.getValue();
                        String description = value.getDescription();
                        description = tap + description.replaceAll("\n", linebreak);
                        
                        System.out.println(key);
                        System.out.println(description);
                    }       CommandLineFooterHeaderFormat.printFooterLine();
                    break;
                }
            case 1:
                {
                    CommandLineFunction function = commandLine.getFunction(param[0]);
                    if (function == null) {
                        System.out.println("Funktion " + param[0] + " nicht gefunden!");
                    }       String tap = CharacterRepeateHelper.repeateSpaceCharacter(5);
                    String linebreak = "\n" + tap;
                    CommandLineFooterHeaderFormat.printFormatedHeaderLine(getName() + " : " + function.getName());
                    String longDescription = function.getLongDescription();
                    System.out.println("BESCHREIBUNG");
                    System.out.println(OutputLineLengthLimiter.limitLineLength(longDescription, tap, CommandLineFooterHeaderFormat.WIDTH));
                    String usage = function.getUsage();
                    System.out.println("\nVERWENDUNG");
                    System.out.println(OutputLineLengthLimiter.limitLineLength(usage, tap, CommandLineFooterHeaderFormat.WIDTH));
                    CommandLineFooterHeaderFormat.printFooterLine();
                    break;
                }
            default:
                System.err.println("Zu viele Argumente.");
                break;
        }
    }

    @Override
    public String getDescription() {
        return "Gibt Hilfe aus.";
    }

    @Override
    public String getLongDescription() {
        return getDescription() + "\nWenn diese Funktion ohne Parameter aufgerufen wird eine (reduzierte) Uebersicht aller Funktionen ausgegeben.\nFalls diese Funktion mit einem Funktionsnamen als Parameter aufgerufen wird werden genaue Informationen zu genau dieser Funktion ausgegeben.";
    }

    @Override
    public String getUsage() {
        return getName() + " [<funktionsnamen>]\n"
                + "@<funktionsnamen> Funktion zu welcher mehr Informationen ausgegeben werden sollen.";
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getAliases() {
        return "info hilfe gethelp h";
    }
}
