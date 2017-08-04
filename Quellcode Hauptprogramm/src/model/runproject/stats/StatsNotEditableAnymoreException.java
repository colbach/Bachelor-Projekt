package model.runproject.stats;

public class StatsNotEditableAnymoreException extends RuntimeException {

    public StatsNotEditableAnymoreException() {
        super("Statsobjekt wurde bereits abgeschlossen. Aenderungen koennen nicht mehr vorgenommen werden.");
    }
    
}
