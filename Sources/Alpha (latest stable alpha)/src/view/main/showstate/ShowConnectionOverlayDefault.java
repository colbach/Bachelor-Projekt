package view.main.showstate;

import model.InOutlet;
import model.Node;
import utils.structures.Quadrupel;
import view.main.additionalhelp.AdditionalHelpDescription;

public class ShowConnectionOverlayDefault implements ShowState, ShowConnectionOverlay {

    private String message = null;
    private boolean error = false;

    private AdditionalHelpDescription HELP = new AdditionalHelpDescription("ESC", "drücken um Overlay schliessen");

    private final ShowOverview previewsState;

    private Node connectingNodeFrom, connectingNodeTo;

    private boolean overlaySwapButtonPressed;

    public ShowConnectionOverlayDefault(ShowOverview previewsState, Node connectingNodeFrom, Node connectingNodeTo) {
        this.previewsState = previewsState;
        this.connectingNodeFrom = connectingNodeFrom;
        this.connectingNodeTo = connectingNodeTo;
        this.overlaySwapButtonPressed = false;
    }

    @Override
    public AdditionalHelpDescription getHelp() {
        return HELP;
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

    @Override
    public Node getConnectingNodeFrom() {
        return connectingNodeFrom;
    }

    @Override
    public Node getConnectingNodeTo() {
        return connectingNodeTo;
    }

    @Override
    public void swapNodes() {
        Node tmp = connectingNodeFrom;
        this.connectingNodeFrom = connectingNodeTo;
        this.connectingNodeTo = tmp;
    }

    @Override
    public boolean isOverlaySwapButtonPressed() {
        return overlaySwapButtonPressed;
    }

    @Override
    public void setOverlaySwapButtonPressed(boolean overlaySwapButtonPressed) {
        this.overlaySwapButtonPressed = overlaySwapButtonPressed;
    }

    @Override
    public boolean drawTargetEnabled() {
        return false;
    }

    @Override
    public String toString() {
        return "ShowConnectionOverlayDefault{" + "message=" + message + ", error=" + error + ", HELP=" + HELP + ", previewsState=" + previewsState + ", connectingNodeFrom=" + connectingNodeFrom + ", connectingNodeTo=" + connectingNodeTo + ", overlaySwapButtonPressed=" + overlaySwapButtonPressed + '}';
    }

}
