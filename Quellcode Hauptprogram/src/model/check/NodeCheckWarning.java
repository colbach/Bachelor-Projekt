package model.check;

import model.Node;

public class NodeCheckWarning implements CheckWarning {

    private final String message;
    private final String userMessage;
    private final boolean projectIsStillRunnable;
    private final Node node;

    public NodeCheckWarning(String message, String userMessage, boolean projectIsStillRunnable, Node node) {
        this.message = message;
        this.userMessage = userMessage;
        this.projectIsStillRunnable = projectIsStillRunnable;
        this.node = node;
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
    
    public Node getNode() {
        return node;
    }
    
}
