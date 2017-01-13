package log;

import static log.Logging.settings;
import settings.GeneralSettings;

/**
 * Diese Klasse ist zursaendig fuer zusaetzliche Fehler-Ausgaben. Zusaetzliche
 * Fehler-Ausgaben sind solche die fuer den Benutzer nicht von besonderer
 * Bedeutung sind, welche jedoch beim Debugging behilflich sein koennen.
 */
public class AdditionalErr {

    public static void print(String s) {
        if (settings.getBoolean(GeneralSettings.DEVELOPER_LOG_MORE_KEY, false)) {
            System.err.print("(" + s + ")");
        }
    }

    public static void println(String s) {
        if (settings.getBoolean(GeneralSettings.DEVELOPER_LOG_MORE_KEY, false)) {
            System.err.println("(" + s + ")");
        } 
    }

}
