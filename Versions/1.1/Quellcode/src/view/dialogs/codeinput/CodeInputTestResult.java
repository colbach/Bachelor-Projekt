package view.dialogs.codeinput;

public class CodeInputTestResult {
    
    private final String message;
    private final boolean passed;

    public CodeInputTestResult(String message, boolean passed) {
        this.message = message;
        this.passed = passed;
    }

    public String getMessage() {
        return message;
    }

    public boolean isPassed() {
        return passed;
    }
    
}
