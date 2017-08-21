package utils.format;

public class TimeFormat {
    
    public static String formatNanos(long nanos) {
        if(nanos >= 1000000000) {
            return format(nanos/1000000);
        } else if(nanos >= 2 * 1000000) {
            return (nanos/1000000) + " Millisekunden";
        
        } else {
            return nanos + " Nanosekunden";
        }
    }
    
    public static String format(long millis) {

        if (millis < 2 * 60 * 1000) { // Fall: Zeit kleiner 2 Minuten
            return millisToSeconds(millis);

        } else { // Fall: Zeit groesser 2 Minuten

            long minutes = (millis / 1000) / 60;
            long restMillis = millis - minutes * 60 * 1000;
            if (restMillis == 0) {
                return minutes + " Minuten";
            } else {
                return minutes + " Minuten und " + millisToSeconds(restMillis);
            }
        }
    }

    private static String millisToSeconds(long millis) {
        long s = millis / 1000;
        String msString = String.valueOf(millis);
        millis = millis % 1000;
        while (msString.length() < 4) {
            msString = "0" + msString;
        }
        return s + "." + millis + " Sekunden";
    }

}
