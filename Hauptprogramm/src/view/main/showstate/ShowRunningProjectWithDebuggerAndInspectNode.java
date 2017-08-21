package view.main.showstate;

import model.Node;
import view.main.additionalhelp.AdditionalHelpDescription;

public class ShowRunningProjectWithDebuggerAndInspectNode implements ShowState, ShowRunningProjectWithDebugger, RunningViewState {

    private String message = null;
    private boolean error = false;

    private static final AdditionalHelpDescription HELP = new AdditionalHelpDescription();
    private static final AdditionalHelpDescription HELP_WITH_MESSAGE = new AdditionalHelpDescription("ESC", "um Nachicht zu verbergen");

    private final ShowRunningProjectWithDebugger previewsState;

    private final Node inspectedNode;

    public ShowRunningProjectWithDebuggerAndInspectNode(ShowRunningProjectWithDebugger previewsState, Node inspectedNode) {
        this.previewsState = previewsState;
        this.inspectedNode = inspectedNode;
    }

    @Override
    public AdditionalHelpDescription getHelp() {
        if (message == null) {
            return HELP;
        } else {
            return HELP_WITH_MESSAGE;
        }
    }

    @Override
    public ShowState back() {
        if (message != null) {
            message = null;
            return this;
        } else {
            return previewsState;
        }
    }

    @Override
    public void setMessage(String message, boolean error) {
        this.message = message;
        this.error = error;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void resetMessage() {
        message = null;
    }
    
    @Override
    public boolean isMessageError() {
        return error;
    }

    public Node getInspectedNode() {
        return inspectedNode;
    }
    
    @Override
    public boolean drawTargetEnabled() {
        return false;
    }

    @Override
    public ShowOverviewDefault backToOverview() {
        ShowState state = previewsState;
        while(state != null && !(state instanceof ShowOverviewDefault)) {
            state = state.back();
        }
        if(state == null)
            return null;
        else
            return (ShowOverviewDefault) state;
    }

    @Override
    public String toString() {
        return "ShowRunningProjectWithDebuggerAndInspectNode{" + "message=" + message + ", error=" + error + ", previewsState=" + previewsState + ", inspectedNode=" + inspectedNode + '}';
    }
}
