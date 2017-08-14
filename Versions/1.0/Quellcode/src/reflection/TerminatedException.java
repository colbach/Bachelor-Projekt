package reflection;

public class TerminatedException extends RuntimeException {

    public TerminatedException() {
        super("Ausfuehrung bereits terminiert.");
    }
    
}
