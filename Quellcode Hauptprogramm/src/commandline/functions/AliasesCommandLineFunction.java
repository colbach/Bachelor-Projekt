package commandline.functions;

import commandline.Aliases;
import commandline.CommandLine;
import commandline.CommandLineFooterHeaderFormat;
import commandline.CommandLineFunction;
import java.util.Map;
import static utils.text.CharacterRepeateHelper.*;

public class AliasesCommandLineFunction implements CommandLineFunction {

    @Override
    public void execute(String[] param, CommandLine commandLine) {
        
        Aliases aliases = commandLine.getAliases();

        String col1Title = "Alias";
        String col2Title = "Zeigt auf";

        int col1Width = maximumStringLength(aliases.getAliasMap().keySet());
        if (col1Width < col1Title.length()) {
            col1Width = col1Title.length();
        }

        int col2Width = maximumStringLength(aliases.getAliasMap().values());
        if (col2Width < col2Title.length()) {
            col2Width = col2Title.length();
        }
        
        CommandLineFooterHeaderFormat.printFormatedHeaderLine(getName());

        System.out.println(fillUpWithSpaces(col1Title, col1Width) + " | " + col2Title);
        System.out.println(repeateCharacter(col1Width, '-') + "-+-" + repeateCharacter(col2Width, '-'));

        for (Map.Entry<String, String> entry : aliases.getAliasMap().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (containsSomethingOf(key, param) || containsSomethingOf(value, param)) {
                System.out.println(fillUpWithSpaces(key, col1Width) + " | " + value);
            }
        }
        
        CommandLineFooterHeaderFormat.printFooterLine();

    }

    private boolean containsSomethingOf(String string, String[] searchStrings) {
        if (searchStrings == null || searchStrings.length == 0) {
            return true;
        }
        for (String searchString : searchStrings) {
            if (string.contains(searchString)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Gibt alle Aliases als Tabelle aus.";
    }

    @Override
    public String getLongDescription() {
        return getDescription() + " Optional kann als Parameter ein Name angegeben werden nach dem gefiltert werden soll.";
    }

    @Override
    public String getUsage() {
        return getName() + " [<filterstring>]\n"
                + "@<filterstring> ein beliebiger String nach welchem die Ergebnisse gefiltert werden sollen.";
    }

    @Override
    public String getName() {
        return "aliases";
    }

    @Override
    public String getAliases() {
        return "aliases alias verknuepfungen verknuepfung verknüpfungen verknüpfung";
    }
}
