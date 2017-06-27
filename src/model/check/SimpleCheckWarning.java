package model.check;

public class SimpleCheckWarning implements CheckWarning {

    private final String message;
    private final String userMessage;
    private final boolean projectIsStillRunnable;

    public SimpleCheckWarning(String message, String userMessage, boolean projectIsStillRunnable) {
        this.message = message;
        this.userMessage = userMessage;
        this.projectIsStillRunnable = projectIsStillRunnable;
    }
    
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getUserMessage() {
        return userMessage;
    }

    @Override
    public boolean projectIsStillRunnable() {
        return projectIsStillRunnable;
    }
    
}
