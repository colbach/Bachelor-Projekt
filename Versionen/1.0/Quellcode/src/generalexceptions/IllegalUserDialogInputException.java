package generalexceptions;

public class IllegalUserDialogInputException extends IllegalUserActionException {

    public IllegalUserDialogInputException(String message, String userMessage) {
        super(message, userMessage);
    }
}
