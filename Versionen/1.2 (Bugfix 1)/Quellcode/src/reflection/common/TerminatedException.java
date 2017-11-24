package reflection.common;

public class TerminatedException extends RuntimeException {

    public TerminatedException() {
        super("Ausfuehrung bereits terminiert.");
    }
    
}
