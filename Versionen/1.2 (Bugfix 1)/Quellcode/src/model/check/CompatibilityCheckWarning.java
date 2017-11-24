package model.check;

import model.Node;

public class CompatibilityCheckWarning implements CheckWarning {

    private final String message;
    private final String userMessage;
    private final Node node;

    public CompatibilityCheckWarning(String message, String userMessage, Node node) {
        this.message = message;
        this.userMessage = userMessage;
        this.node = node;
    }
    
    @Override
    public String getMessage() {
        return "Compatibility test for " + node.getName() + " failed: " + message;
    }

    @Override
    public String getUserMessage() {
        return "Kompatibilitäts-Test für " + node.getName() + " fehlgeschlagen: " + userMessage;
    }

    @Override
    public boolean projectIsStillRunnable() {
        return true;
    }
    
    public Node getNode() {
        return node;
    }
    
}
