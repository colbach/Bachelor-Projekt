package utils.format;

import java.awt.Color;
import java.io.File;

/**
 * Diese Klasse dient zum darstellen und kuerzen von Object-Arrays.
 */
public class ObjectArrayFormat {
    
    public static String format(Object[] content) {
        String contentString = "";
        if(content != null) {
            boolean first = true;
            for(Object c : content) {
                if(!first)
                    contentString += ", ";
                String toString;
                if(c == null) {
                    toString = "null";
                } else if(c instanceof Color) {
                    Color color = (Color) c;
                    toString = String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
                } else if(c instanceof File) {
                    toString = ((File)c).getName();
                } else {
                    toString = c.toString();
                }
                contentString += toString;
                first = false;
            }
        } else {
            contentString = "undefiniert";
        }
        return contentString;
    }
    
    public static String formatShortened(Object[] content, int symbols) {
        String contentString = format(content);
        if(contentString.length() > symbols) {
            return contentString.substring(0, symbols-3) + "...";
        } else {
            return contentString;
        }
    }
    
}
