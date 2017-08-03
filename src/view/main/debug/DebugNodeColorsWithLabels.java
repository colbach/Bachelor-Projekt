package view.main.debug;

import java.awt.Color;
import static view.Constants.*;

public class DebugNodeColorsWithLabels {
    
    private static final Color[] COLORS = new Color[] {
        DEBUGGER_NODE_PREPARING_BACKGROUND_COLOR,
        DEBUGGER_NODE_COLLECTING_BACKGROUND_COLOR,
        DEBUGGER_NODE_RUNNING_BACKGROUND_COLOR,
        DEBUGGER_NODE_DELIEVERING_BACKGROUND_COLOR,
        DEBUGGER_NODE_FINISHED_BACKGROUND_COLOR,
        DEBUGGER_NODE_FAILED_BACKGROUND_COLOR,
        DEBUGGER_NODE_UNKNOWN_BACKGROUND_COLOR
    };
    
    private static final String[] LABELS = new String[] {
        "Vorbereiten",
        "Warteen auf Eingänge",
        "Arbeitet",
        "Liefern an Ausgänge aus",
        "Beendet",
        "Fehlgeschlagen (Fatal)",
        "Unbekannter Zustand"
    };
    
    public static Color getColor(int i) {
        return COLORS[i];
    }    
    
    public static String getLabel(int i) {
        return LABELS[i];
    }
    
    public static int getCount() {
        return COLORS.length;
    }
    
}
