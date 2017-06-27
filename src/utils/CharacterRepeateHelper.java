package utils;

import java.util.Collection;
import java.util.Set;
import logging.AdditionalLogger;

public class CharacterRepeateHelper {
    
    public static String repeateCharacter(final int count, char character) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<count; i++) {
            sb.append(character);
        }
        return sb.toString();
    }
    
    public static String repeateSpaceCharacter(final int count) {
        return repeateCharacter(count, ' ');
    }
    
    public static String fillUpWithSpaces(String string, int characterWidth) {
        int length = string.length();
        if(length == characterWidth) {
            return string;
        } else if(length > characterWidth) {
            AdditionalLogger.err.println("String \""+ string + "\" ist laenger als " + characterWidth + " Zeichen. String kann also nicht aufgefuellt werden! return string.");
            return string;
        } else {
            return string + repeateSpaceCharacter(characterWidth - length);
        }
    }
    
    public static int maximumStringLength(String[] strings) {
        int max = 0;
        for(String string : strings) {
            int length = string.length();
            if(length > max)
                max = length;
        }
        return max;
    }
    
    public static int maximumStringLength(Set<String> strings) {
        return maximumStringLength(strings.toArray(new String[0]));
    }
    
    public static int maximumStringLength(Collection<String> strings) {
        return maximumStringLength(strings.toArray(new String[0]));
    } 
}
