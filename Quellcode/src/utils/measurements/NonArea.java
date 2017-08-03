package utils.measurements;

/**
 * Keine Flaeche.
 */
public class NonArea extends Area {

    private static NonArea intance;

    /**
     * Privater Parameter. Verwendung: getInstance()
     */
    private NonArea() {
        super(0, 0, 0, 0);
    }
    
    public boolean isInside(int x, int y) {
        return false;
    }

    public boolean intersect(Area other) {
        return false; // true da sich jede Flaeche mit dieser schneidet
    }

    /**
     * Gibt einzige Instanz dieser Klasse zurueck.
     */
    public static synchronized NonArea getInstance() {
        if (intance == null) {
            intance = new NonArea();
        }
        return intance;
    }
}
