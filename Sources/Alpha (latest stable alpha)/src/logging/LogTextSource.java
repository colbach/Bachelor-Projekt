package logging;

/**
 * Diese Klasse definiert eine readonly Textquelle fuer einen Logverlauf.
 */
public interface LogTextSource {

    /**
     * Gibt Zeile i.
     */
    public String get(int i);
    
    /**
     * Gibt anzahl verfuegbarer Zeilen.
     */
    public int getAvailableCount();

    /**
     * Gibt an ob Zeile i als Fehler (Rot) dargestellt werden soll.
     */
    public boolean isError(int i);

}
