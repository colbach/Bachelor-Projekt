package view.main.showstate;

import view.main.additionalhelp.AdditionalHelpDescription;
import view.main.contextmenu.ContextMenu;

public class ShowOverviewDefault implements ShowState, RightClickableViewState, ShowOverview {

    private String message = null;
    private boolean error = false;
    private ContextMenu rightClickMenue = null;

    private static final AdditionalHelpDescription HELP = new AdditionalHelpDescription(
            new String[]{
                "",
                "",
                "Control"
            },
            new String[]{
                "Rechtsklick auf Element um mehr Optionen anzuzeigen",
                "Rechtsklick auf leere Fläche um Elemente zu erstellen",
                "gedrückt halten um Elemente zu verbinden"
            });
    private static final AdditionalHelpDescription HELP_WITH_MESSAGE = new AdditionalHelpDescription("ESC", "um Nachicht zu verbergen");
    private static final AdditionalHelpDescription HELP_WITH_RCM = new AdditionalHelpDescription("ESC", "Abbrechen");

    public ShowOverviewDefault() {
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
        if (rightClickMenue != null) {
            rightClickMenue = null;
        } else {
            message = null;
        }
        return this;
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
    public ContextMenu getRightClickMenue() {
        return rightClickMenue;
    }

    @Override
    public void setRightClickMenue(ContextMenu rightClickMenue) {
        this.rightClickMenue = rightClickMenue;
    }

    @Override
    public void disposeRightClickMenue() {
        rightClickMenue = null;
    }

    @Override
    public boolean drawTargetEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "ShowOverviewDefault{" + "message=" + message + ", error=" + error + ", rightClickMenue=" + rightClickMenue + '}';
    }

}
