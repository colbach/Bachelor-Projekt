package view.main.showstate;

import view.main.contextmenu.ContextMenu;

public interface RightClickableViewState extends ShowState {
    
    public ContextMenu getRightClickMenue();
    
    public void setRightClickMenue(ContextMenu rightClickMenue);
    
    public void disposeRightClickMenue();
    
}
