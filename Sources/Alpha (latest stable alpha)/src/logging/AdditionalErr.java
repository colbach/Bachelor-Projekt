package logging;

import static logging.AdvancedLogger.settings;
import settings.FastAccessibleSettings;
import settings.GeneralSettings;

/**
 * Diese Klasse ist zursaendig fuer zusaetzliche Fehler-Ausgaben. Zusaetzliche
 * Fehler-Ausgaben sind solche die fuer den Benutzer nicht von besonderer
 * Bedeutung sind, welche jedoch beim Debugging behilflich sein koennen.
 */
public class AdditionalErr {

    protected AdditionalErr() {
    }
    
    public void print(String s) {
        if (FastAccessibleSettings.logMore()) {
            System.err.print("(" + s + ")");
        }
    }

    public void println(String s) {
        if (FastAccessibleSettings.logMore()) {
            System.err.println("(" + s + ")");
        } 
    }

}
