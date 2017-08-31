package projectrunner.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import logging.LogTextSource;
import projectrunner.executioncontrol.TerminationReason;
import projectrunner.stats.Stats;
import utils.format.TimeFormat;

public class Report {
    
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final TerminationReason terminationReason;
    private final String terminationMessage;
    private final LogTextSource output;
    private final long startTime;
    private final long endTime;
    private final long runtimeInNanos;
    private final Stats stats;

    public Report(TerminationReason terminationReason, String terminationMessage, LogTextSource output, long startTime, long endTime, long startNanoTime, long endNanoTime, Stats stats) {
        this.terminationReason = terminationReason;
        this.terminationMessage = terminationMessage;
        this.output = output;
        this.startTime = startTime;
        this.endTime = endTime;
        this.stats = stats;
        if (startNanoTime > 0 && endNanoTime > 0) {
            runtimeInNanos = endNanoTime - startNanoTime;
        } else {
            System.err.println("startNanoTime (" + endNanoTime + ") und/oder endNanoTime (" + endNanoTime + ") nicht korrekt.");
            runtimeInNanos = -1;
        }
    }

    public TerminationReason getTerminationReason() {
        return terminationReason;
    }
    
    public String getTerminationReasonFormated() {
        if(terminationReason == null)
            return null;
        switch(terminationReason) {
            case CANCELED: return "Abbgebrochen";
            case FINISHED: return "Erfolgreich beendet";
            case RUNTIME_ERROR: return "Laufzeitfehler";
            case START_FAILED: return "Start fehlgeschlagen";
            case UNKNOWN: return "Unbekannt";
        }
        return null;
    }

    public String getTerminationMessage() {
        return terminationMessage;
    }

    public LogTextSource getOutput() {
        return output;
    }

    public long getStartTime() {
        return startTime;
    }
    
    public Date getStartDate() {
        return new Date(startTime);
    }

    public long getEndTime() {
        return endTime;
    }

    public Date getEndDate() {
        return new Date(endTime);
    }
    
    /**
     * Gibt Laufzeit in Nanosekunden zuruek.
     */
    public long getRuntimeInNanos() {
        return runtimeInNanos;
    }
    
    /**
     * Gibt Laufzeit in Millisekunden zuruek. Diese Methode ist wie folgt implementiert: runtimeInNanos/1000000.
     */
    public long getRuntimeInMillis() {
        return runtimeInNanos/1000000;
    }

    public String getStartTimeFormated() {
        if (startTime < 0) {
            return "Unbekannt";
        } else {
            return DATE_FORMAT.format(new Date(startTime));
        }
    }

    public String getEndTimeFormated() {
        if (endTime < 0) {
            return "Unbekannt";
        } else {
            return DATE_FORMAT.format(new Date(endTime));
        }
    }

    public String getRuntimeFormated() {
        if (runtimeInNanos < 0) {
            return "Unbekannt";
        } else {
            return TimeFormat.formatNanos(getRuntimeInNanos());
        }
    }

    public Stats getStats() {
        return stats;
    }

    @Override
    public String toString() {
        return terminationMessage + " (" + getRuntimeFormated() + ")";
    }
}
