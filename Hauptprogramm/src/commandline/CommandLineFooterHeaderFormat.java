package commandline;

import static utils.text.CharacterRepeateHelper.*;

public class CommandLineFooterHeaderFormat {
    
    public static final int WIDTH = 80;
    public static final char BAR_CHAR = '=';
    public static final String FOOTER_LINE = repeateCharacter(WIDTH, BAR_CHAR);
    
    public static String formatHeaderLine(String title) {
        
        title = " " + title + " ";
        int titleWidth = title.length();
        int bar1Width = (WIDTH/2) - (titleWidth/2);
        int bar2Width = WIDTH - titleWidth - bar1Width;
        
        return repeateCharacter(bar1Width, BAR_CHAR) + title.toUpperCase() + repeateCharacter(bar2Width, BAR_CHAR);
    }
    
    public static void printFormatedHeaderLine(String title) {
        System.out.println(formatHeaderLine(title));
    }
    
    
    public static void printFooterLine() {
        System.out.println(FOOTER_LINE);
    }
    
}
