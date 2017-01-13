package model.check;

public class CheckWarning {
    
    private final boolean critical;
    private final String message;
    private final Object perpetrator;

    public CheckWarning(boolean critical, String message, Object perpetrator) {
        this.critical = critical;
        this.message = message;
        this.perpetrator = perpetrator;
    }

    public boolean isCritical() {
        return critical;
    }

    public String getMessage() {
        return message;
    }

    public Object getPerpetrator() {
        return perpetrator;
    }
    
}
