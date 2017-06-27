package model.runproject.executioncontrol;

class StopException extends RuntimeException {

    public StopException() {
        super("Ausfuehrung anhalten!");
    }
    
}
