package commandline.functions;

import commandline.CommandLine;
import commandline.CommandLineFooterHeaderFormat;
import commandline.CommandLineFunction;
import commandline.ConfimableTask;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import settings.GeneralSettings;
import static utils.text.CharacterRepeateHelper.*;

public class ResetGeneralSettingsCommandLineFunction implements CommandLineFunction {

    @Override
    public void execute(String[] param, CommandLine commandLine) {

        if (param.length == 0) {
            System.out.println("Die Generellen Einstellungen werden unwiederruflich zurueckgesetzt.");
            commandLine.putConfimableTask(new ConfimableTask() {
                @Override
                public void confirmed() {
                    GeneralSettings instance = GeneralSettings.getInstance();
                    instance.reset();
                    try {
                        instance.writeSettingsToSettingsFile();
                        commandLine.success();
                    } catch (IOException e) {
                        System.err.println("Datenbank wurde zurueck gesetzt konnte jedoch nicht geschrieben werden.");
                    }
                }

                @Override
                public void canceled() {
                }
            });

        } else {
            System.err.println("Zu viele Argumente.");
        }
    }

    @Override
    public String getDescription() {
        return "Setzt Generelle Einstellungen persistent auf die Standartwerte zuruck.";
    }

    @Override
    public String getLongDescription() {
        return getDescription() + "\nDieser Aufruf ist persistent und kann nicht wiederrufen werden!";
    }

    @Override
    public String getUsage() {
        return getName();
    }

    @Override
    public String getName() {
        return "resetgeneralsettings";
    }

    @Override
    public String getAliases() {
        return "cleargeneralsettings";
    }
}
