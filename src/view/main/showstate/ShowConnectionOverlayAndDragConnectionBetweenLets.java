package view.main.showstate;

import logging.AdditionalLogger;
import model.InOutlet;
import model.Node;
import utils.structures.Quadrupel;
import view.main.additionalhelp.AdditionalHelpDescription;

public class ShowConnectionOverlayAndDragConnectionBetweenLets implements ShowState, ShowConnectionOverlay {

    private String message = null;
    private boolean error = true;

    private AdditionalHelpDescription HELP = new AdditionalHelpDescription("ESC", "drücken um Overlay schliessen");

    private final ShowConnectionOverlay previewsState;

    private final Node connectingNodeFrom, connectingNodeTo;

    private final Quadrupel<InOutlet /*let*/, Integer /*index in diesem Let*/, Integer /*virtueller Index*/, Boolean /*true=links, false=rechts*/> fromLet;

    public ShowConnectionOverlayAndDragConnectionBetweenLets(ShowConnectionOverlay previewsState, Quadrupel<InOutlet, Integer, Integer, Boolean> fromLet) {
        this.previewsState = previewsState;
        this.connectingNodeFrom = previewsState.getConnectingNodeFrom();
        this.connectingNodeTo = previewsState.getConnectingNodeTo();
        this.fromLet = fromLet;
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
    public Node getConnectingNodeFrom() {
        return connectingNodeFrom;
    }

    @Override
    public Node getConnectingNodeTo() {
        return connectingNodeTo;
    }

    @Override
    public boolean drawTargetEnabled() {
        return false;
    }

    public Quadrupel<InOutlet, Integer, Integer, Boolean> getFromLet() {
        return fromLet;
    }
//
//    public Quadrupel<InOutlet, Integer, Integer, Boolean> getToLet() {
//        return toLet;
//    }
//
//    public void setToLet(Quadrupel<InOutlet, Integer, Integer, Boolean> toLet) {
//        this.toLet = toLet;
//    }

    @Override
    public boolean isOverlaySwapButtonPressed() {
        return false;
    }

    @Override
    public void setOverlaySwapButtonPressed(boolean overlaySwapButtonPressed) {
        // Mach nichts...
    }

    @Override
    public void swapNodes() {
        AdditionalLogger.err.println("ignoriert, da swapNodes() nicht von ShowConnectionOverlayAndDragConnectionBetweenLets unterstuetzt.");
        // Mach nichts...
    }

    @Override
    public String toString() {
        return "ShowConnectionOverlayAndDragConnectionBetweenLets{" + "message=" + message + ", error=" + error + ", HELP=" + HELP + ", previewsState=" + previewsState + ", connectingNodeFrom=" + connectingNodeFrom + ", connectingNodeTo=" + connectingNodeTo + ", fromLet=" + fromLet + '}';
    }

}
