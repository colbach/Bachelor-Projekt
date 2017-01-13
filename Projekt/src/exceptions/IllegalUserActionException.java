package exceptions;

public class IllegalUserActionException extends UserDisplayableException {

    public IllegalUserActionException(String message, String userMessage) {
        super(message, userMessage);
    }

}
