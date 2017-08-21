package view.main.showstate;

import view.main.additionalhelp.AdditionalHelpDescription;

public class ShowRunningProjectWithDebuggerDefault implements ShowState, LockableViewState, ShowRunningProjectWithDebugger, RunningViewState {

    private String message = null;
    private boolean error = false;

    private static final AdditionalHelpDescription HELP = new AdditionalHelpDescription();
    private static final AdditionalHelpDescription HELP_WITH_MESSAGE = new AdditionalHelpDescription("ESC", "um Nachicht zu verbergen");

    private final ShowState previewsState;
    private boolean backLock;

    public ShowRunningProjectWithDebuggerDefault(ShowState previewsState) {
        this.previewsState = previewsState;
        this.backLock = true;
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
            if (!backLock) {
                if(previewsState instanceof RightClickableViewState)
                    ((RightClickableViewState) previewsState).setRightClickMenue(null);
                return previewsState;
            } else {
                return this;
            }
        }
    }

    @Override
    public void lockBack() {
        backLock = true;
    }

    @Override
    public void unlockBack() {
        backLock = false;
    }

    @Override
    public void unlockAndGoBack() {
        unlockBack();
        back();
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
        return "ShowRunningProjectWithDebuggerDefault{" + "message=" + message + ", error=" + error + ", previewsState=" + previewsState + ", backLock=" + backLock + '}';
    }

}
