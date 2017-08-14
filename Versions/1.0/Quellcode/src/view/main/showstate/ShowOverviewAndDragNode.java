package view.main.showstate;

import model.Node;
import view.main.additionalhelp.AdditionalHelpDescription;

public class ShowOverviewAndDragNode implements ShowState, ShowOverview {

    private String message = null;
    private boolean error = false;

    private static final AdditionalHelpDescription HELP = new AdditionalHelpDescription(
            "",
            "Loslassen um Element zu platzieren"
    );
    private static final AdditionalHelpDescription HELP_WITH_MESSAGE = new AdditionalHelpDescription(
            new String[]{
                "",
                "ESC"
            },
            new String[]{
                "Loslassen um Element zu platzieren",
                "um Nachicht zu verbergen"
            });

    private final ShowOverview previewsState;

    /**
     * Node welche gerade verschoben wird.
     */
    protected final Node draggingNode;

    public ShowOverviewAndDragNode(ShowOverview previewsState, Node draggingNode) {
        this.previewsState = previewsState;
        this.draggingNode = draggingNode;
    }

    @Override
    public AdditionalHelpDescription getHelp() {
        if (message == null) {
            return HELP;
        } else {
            return HELP_WITH_MESSAGE;
        }
    }

    public Node getDraggingNode() {
        return draggingNode;
    }

    @Override
    public ShowState back() {
        if (message != null) {
            message = null;
            return this;
        } else {
            if (previewsState instanceof RightClickableViewState) {
                ((RightClickableViewState) previewsState).setRightClickMenue(null);
            }
            return previewsState;
        }
    }

    @Override
    public void setMessage(String message, boolean error) {
        this.message = message;
        this.error = error;
    }

    @Override
    public boolean isMessageError() {
        return error;
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
    public boolean drawTargetEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "ShowOverviewAndDragNode{" + "message=" + message + ", error=" + error + ", previewsState=" + previewsState + ", draggingNode=" + draggingNode + '}';
    }

}
