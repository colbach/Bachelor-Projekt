package log;

import static log.Logging.settings;
import settings.GeneralSettings;

/**
 * Diese Klasse ist zursaendig fuer zusaetzliche Ausgaben. Zusaetzliche Ausgaben
 * sind solche die fuer den Benutzer nicht von besonderer Bedeutung sind, welche
 * jedoch beim Debugging behilflich sein koennen.
 */
public class AdditionalOut {

    protected final static GeneralSettings settings = GeneralSettings.getInstance();

    public static void println(String s) {
        if (settings.getBoolean(GeneralSettings.DEVELOPER_LOG_MORE_KEY, false)) {
            System.out.println("(" + s + ")");
        }
    }
}
