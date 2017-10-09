package commandline.functions;

import commandline.CommandLine;
import commandline.CommandLineFooterHeaderFormat;
import commandline.CommandLineFunction;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import settings.GeneralSettings;
import static utils.text.CharacterRepeateHelper.*;

public class FileExistsCommandLineFunction implements CommandLineFunction {

    @Override
    public void execute(String[] param, CommandLine commandLine) {

        switch (param.length) {
            case 0:
                System.out.println("Zu wenige Argumente.");
                break;
            case 1:
                File file = new File(param[0]);
                if (file.exists()) {
                    if (file.isFile()) {
                        System.out.println("Datei gefunden.");
                    } else {
                        System.out.println("Verzeichniss gefunden.");
                    }
                } else {
                    System.out.println("Keine Datei/Verzeichniss gefunden.");
                }   break;
            default:
                System.err.println("Zu viele Argumente.");
                break;
        }
    }

    @Override
    public String getDescription() {
        return "Gibt an ob Datei gefunden werden kann.";
    }

    @Override
    public String getLongDescription() {
        return getDescription() + "\nDies kann gerade deshalb sinnvoll sein falls unklar ist ob/wie "
                + "diese Datei/Verzeichniss vom aktuellen Arbeits-Pfad erreichbar ist.";
    }

    @Override
    public String getUsage() {
        return getName() + " [<pfad>]\n"
                + "@<pfad> Pfad zu Datei/Verzeichniss welche gesucht werden soll";
    }

    @Override
    public String getName() {
        return "fileexists";
    }

    @Override
    public String getAliases() {
        return "folderexists directoryexists";
    }
}
