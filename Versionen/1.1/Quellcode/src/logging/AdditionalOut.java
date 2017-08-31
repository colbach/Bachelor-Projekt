package logging;

import java.util.logging.Logger;
import static logging.AdvancedLogger.settings;
import settings.FastAccessibleSettings;
import settings.GeneralSettings;

/**
 * Diese Klasse ist zursaendig fuer zusaetzliche Ausgaben. Zusaetzliche Ausgaben
 * sind solche die fuer den Benutzer nicht von besonderer Bedeutung sind, welche
 * jedoch beim Debugging behilflich sein koennen.
 */
public class AdditionalOut {

    protected AdditionalOut() {
    }
    
    public void println(Object s) {
        if (FastAccessibleSettings.logMore()) {
            System.out.println("(" + s.toString() + ")");
        }
    }
}
