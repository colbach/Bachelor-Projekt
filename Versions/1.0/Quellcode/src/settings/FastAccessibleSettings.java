package settings;

import static settings.GeneralSettings.*;

/**
 * Diese Klasse dient rein der OPTIMIERUNG des Programcodes. Aus
 * Efficienz-Gruenden wird kein Singelon-Pattern sondern direkte Zugriffe
 * verwendet um haufig verwendete Einstellungen zu erreichen.
 */
public class FastAccessibleSettings {

    private static boolean logMore;
    private static boolean additionalchecks;
    
    static {
        GeneralSettings generalSettings = GeneralSettings.getInstance();
        FastAccessibleSettings.logMore = generalSettings.getBoolean(DEVELOPER_LOG_MORE_KEY, false);
        FastAccessibleSettings.additionalchecks = generalSettings.getBoolean(DEVELOPER_ADDITIONAL_CHECKS_KEY, DEVELOPER_ADDITIONAL_CHECKS_DEFAULT);
    }

    public static boolean additionalchecks() {
        return additionalchecks;
    }
    
    public static boolean logMore() {
        return logMore;
    }

    protected static void setLogMore(boolean logMore) {
        FastAccessibleSettings.logMore = logMore;
    }

    protected static void setAdditionalchecks(boolean additionalchecks) {
        FastAccessibleSettings.additionalchecks = additionalchecks;
    }
    
}
