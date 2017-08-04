package generalexceptions;

public class IllegalUserActionException extends UserDisplayableException {

    public IllegalUserActionException(String message, String userMessage) {
        super(message, userMessage);
    }
    
    public IllegalUserActionException(String message) {
        super(message, message);
    }

}
