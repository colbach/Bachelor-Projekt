package utils.structures;

public class FutureValue<T> {

    private T value;
    private boolean setted;

    public FutureValue() {
        this.value = null;
    }

    /**
     * Wartet bis Wert gesetzt wurde und gibt diesen dann zuruck.
     * ACHTUNG: Wert kann null sein!
     */
    public synchronized T get() {
        while (!setted) {
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        return value;
    }

    /**
     * Wert setzen.
     * ACHTUNG: Diese Methode darf nur genau 1x aufgerufen werden.
     */
    public synchronized void set(T value) {
        if(setted)
            throw new RuntimeException("Wert wurde bereits gesetzt.");
        this.value = value;
        setted = true;
        this.notifyAll();
    }

    @Override
    public synchronized String toString() {
        if (value == null) {
            return "null";
        } else {
            return value.toString();
        }
    }
}
