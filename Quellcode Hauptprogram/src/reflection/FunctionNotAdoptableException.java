package reflection;

public class FunctionNotAdoptableException extends Exception {

    public FunctionNotAdoptableException() {
        super("Parameter nicht fuer Funktion geeignet!");
    }

    public FunctionNotAdoptableException(String message) {
        super("Parameter nicht fuer Funktion geeignet!\n(" + message + ")");
    }
}
