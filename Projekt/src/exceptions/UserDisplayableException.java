package exceptions;

public class UserDisplayableException extends Exception {
    
    private final String userMessage;
    
    public UserDisplayableException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
