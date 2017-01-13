package utils;

import java.awt.Graphics2D;
import java.util.ArrayList;
import static view.Constants.*;

/**
 * Hilfsmethoden die das zeichnen von Text unterstuetzen sollen.
 */
public class TextHandler {
    
    /**
     * Teilt String auf Zeilen auf.
     */
    public static String[] splitStringToLines(Graphics2D g, String s, int maxWidth) {
        ArrayList<String> lines = new ArrayList<>();
        int w = 0;
        for(String line : s.split("\n")) {
            String trimmedLine = "";
            for(String word : line.split(" ")) {
                if(g.getFontMetrics().stringWidth(trimmedLine + word) > maxWidth) {
                    lines.add(trimmedLine);
                    w = Math.max(w, g.getFontMetrics().stringWidth(trimmedLine));
                    trimmedLine = "";
                }
                trimmedLine += word + " ";
            }
            lines.add(trimmedLine);
            w = Math.max(w, g.getFontMetrics().stringWidth(trimmedLine));
        }
        return lines.toArray(new String[0]);
    }
    
    /**
     * Teilt String auf Zeilen auf.
     */
    public static String[] splitStringToLines(String s, int maxLetters) {
        ArrayList<String> lines = new ArrayList<>();
        int w = 0;
        for(String line : s.split("\n")) {
            String trimmedLine = "";
            for(String word : line.split(" ")) {
                if((trimmedLine + word).length() > maxLetters) {
                    lines.add(trimmedLine);
                    w = Math.max(w, trimmedLine.length());
                    trimmedLine = "";
                }
                trimmedLine += word + " ";
            }
            lines.add(trimmedLine);
            w = Math.max(w, trimmedLine.length());
        }
        return lines.toArray(new String[0]);
    }
}
