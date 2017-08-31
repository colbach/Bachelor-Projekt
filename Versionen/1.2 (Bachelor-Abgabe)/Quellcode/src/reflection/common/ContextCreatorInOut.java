package reflection.common;

public interface ContextCreatorInOut extends InOut {

    /**
     * Startet Ausfuehrung von neuem Kontext. Alle outs muessen befuellt sein
     * bevor diese Methode aufgerufen wird. Diese Methode blockiert nicht!
     */
    public void startNewContext() throws Exception, TerminatedException;

    /**
     * Gibt die Anzahl von diesem ContextCreator erzeugten und noch laufenden
     * Contexts zurueck.
     */
    public int getRunningContextCount() throws TerminatedException;
}
