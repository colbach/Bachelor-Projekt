package view.main.showstate;

import model.InOutlet;
import model.Node;
import utils.structures.Quadrupel;

public interface ShowConnectionOverlay extends ShowState {
    
    public Node getConnectingNodeFrom();

    public Node getConnectingNodeTo();
    
    public boolean isOverlaySwapButtonPressed();

    public void setOverlaySwapButtonPressed(boolean overlaySwapButtonPressed);
    
    public void swapNodes();
    
}
