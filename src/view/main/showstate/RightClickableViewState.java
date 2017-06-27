package view.main.showstate;

import view.main.rightclickmenue.RightClickMenue;

public interface RightClickableViewState extends ShowState {
    
    public RightClickMenue getRightClickMenue();
    
    public void setRightClickMenue(RightClickMenue rightClickMenue);
    
    public void disposeRightClickMenue();
    
}
