package view.main.showstate;

import view.main.additionalhelp.AdditionalHelpDescription;

public interface ShowState {
    
    public AdditionalHelpDescription getHelp();
    
    public ShowState back();
    
    public void setMessage(String message, boolean error);
    
    public String getMessage();
    
    public void resetMessage();
    
    public boolean isMessageError();
    
    public boolean drawTargetEnabled();
    
}
