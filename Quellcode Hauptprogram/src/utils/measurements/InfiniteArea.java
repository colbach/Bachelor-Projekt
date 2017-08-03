package utils.measurements;

/**
 * Unendliche Flaeche.
 */
public class InfiniteArea extends Area {

    private static InfiniteArea intance;

    /**
     * Privater Parameter. Verwendung: getInstance()
     */
    private InfiniteArea() {
        super(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    public boolean isInside(int x, int y) {
        return true;
    }

    public boolean intersect(Area other) {
        return true; // true da sich jede Flaeche mit dieser schneidet
    }

    /**
     * Gibt einzige Instanz dieser Klasse zurueck.
     */
    public static synchronized InfiniteArea getInstance() {
        if (intance == null) {
            intance = new InfiniteArea();
        }
        return intance;
    }
}
