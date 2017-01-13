package utils;

public class TimeFormat {
    
    public static String format(long millis) {

        if (millis < 2 * 60 * 1000) { // Fall: Zeit kleiner 2 Minuten
            return formatSeconds(millis);

        } else { // Fall: Zeit groesser 2 Minuten

            long minutes = (millis / 1000) / 60;
            long restMillis = millis - minutes * 60 * 1000;
            if (restMillis == 0) {
                return minutes + " Minuten";
            } else {
                return minutes + " Minuten und " + formatSeconds(restMillis);
            }
        }
    }

    private static String formatSeconds(long millis) {
        long s = millis / 1000;
        String msString = String.valueOf(millis);
        millis = millis % 1000;
        while (msString.length() < 4) {
            msString = "0" + msString;
        }
        return s + "." + millis + " Sekunden";
    }

}
