package model.runproject.executioncontrol;

public class StopException extends RuntimeException {

    public StopException() {
        super("Ausfuehrung anhalten!");
    }
    
}
