package commandline;

import java.util.ArrayList;

public class OutputLineLengthLimiter {

    public static String limitLineLength(String text, String tap, int lineLength) {
        String[] lines = text.split("\n");
        ArrayList<String> resultLines = new ArrayList<>();
        for (String line : lines) {
            String restline = line;
            while (restline.length() > lineLength - tap.length()) {
                resultLines.add(restline.substring(0, lineLength - tap.length()));
                restline = restline.substring(lineLength - tap.length());
            }
            resultLines.add(restline);
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String resultLine : resultLines) {
            if (!first) {
                sb.append("\n");
            }
            sb.append(tap);
            if(resultLine.length() > 0 && resultLine.indexOf(" ") == 0) {
                resultLine = resultLine.substring(1);
            }
            sb.append(resultLine);
            first = false;
        }
        return sb.toString();
    }

}
