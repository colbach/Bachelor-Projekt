package view.main.showstate;

import model.Node;
import view.main.additionalhelp.AdditionalHelpDescription;

public class ShowOverviewAndDragConnectionBetweenNodes implements ShowState, ShowOverview {

    private String message = null;
    private boolean error = false;

    private static final AdditionalHelpDescription HELP = new AdditionalHelpDescription(
            new String[]{
                "",
                "ESC"
            },
            new String[]{
                "Zum Verbinden über Element loslassen",
                "Zum Abbrechen"
            }
    );
    private static final AdditionalHelpDescription HELP_WITH_MESSAGE = new AdditionalHelpDescription(
            new String[]{
                "",
                "ESC"
            },
            new String[]{
                "Zum Verbinden über Element loslassen",
                "um Nachicht zu verbergen"
            }
    );

    private final Node connectingNodeFrom;

    private final ShowOverview previewsState;

    public ShowOverviewAndDragConnectionBetweenNodes(ShowOverview previewsState, Node connectingNodeFrom) {
        this.previewsState = previewsState;
        this.connectingNodeFrom = connectingNodeFrom;
    }

    @Override
    public AdditionalHelpDescription getHelp() {
        return HELP;
    }

    public Node getConnectingNodeFrom() {
        return connectingNodeFrom;
    }

    @Override
    public ShowOverview back() {
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
        return "ShowOverviewAndDragConnectionBetweenNodes{" + "message=" + message + ", error=" + error + ", connectingNodeFrom=" + connectingNodeFrom + ", previewsState=" + previewsState + '}';
    }

}
