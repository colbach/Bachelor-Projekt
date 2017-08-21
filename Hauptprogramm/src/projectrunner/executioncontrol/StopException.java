package projectrunner.executioncontrol;

public class StopException extends RuntimeException {

    public StopException() {
        super("Ausfuehrung anhalten!");
    }
    
}
