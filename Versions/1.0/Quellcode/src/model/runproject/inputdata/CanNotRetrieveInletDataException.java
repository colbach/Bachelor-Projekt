package model.runproject.inputdata;

public class CanNotRetrieveInletDataException extends RuntimeException {

    public CanNotRetrieveInletDataException() {
        super("Nicht alle Daten in InletInputData vorhanden!");
    }
    
}
