package ALT;

import model.Inlet;
import model.Node;
import view.main.rightclickmenue.*;
import view.listener.MouseAndKeyboardListener;
import view.main.MainWindow;

public class DirectInputMenue {
    
    private final Node node;
    private final int x, y;
    private final Corner corner;
    private final MainWindow mainWindow;
    private int pressedDown = -1;
    
    public DirectInputMenue(Node node, int x, int y, int viewWidth, int viewHeight, MainWindow mainWindow) {
        this.node = node;
        this.x = x;
        this.y = y;
        this.mainWindow = mainWindow;
        if(x < viewWidth/2) {
            if(y <viewHeight/2) {
                this.corner = Corner.RIGHT_BOTTOM;
            } else {
                this.corner = Corner.RIGHT_TOP;
            }
        } else {
            if(y < viewHeight/2) {
                this.corner = Corner.LEFT_BOTTOM;
            } else {
                this.corner = Corner.LEFT_TOP;
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getInletCount() {
        return node.getInletCount();
    }
    
    public String getInletName(int i) {
        return node.getInlet(i).getName();
    }
    
    public Inlet getInlet(int i) {
        return node.getInlet(i);
    }
    
    public boolean isInletUsed(int i) {
        return node.getInlet(i).isConnected();
    }

    public boolean isInletPressed(int i) {
        return i == pressedDown;
    }
    
    public Corner getCorner() {
        return corner;
    }

    public boolean mousePressed(int x, int y) {
        /*RightClickMenueItem matched = RightClickMenueDrafter.calcNodesForMousePosition(null, this, x, y);
        if(matched != null) {
            matched.setPressed(true);
            return true;
        }
        else return false;*/
        return false;
    }

    public boolean mouseClicked(int x, int y) {
        /*RightClickMenueItem matched = RightClickMenueDrafter.calcNodesForMousePosition(null, this, x, y);
        if(matched != null) {
            matched.clicked();
            mainWindow.removeRightClickMenue();
            return true;
        }
        else return false;*/
        return false;
    }

    public boolean mouseReleased() {
        /*for(RightClickMenueItem item : items)
            item.setPressed(false);
        return false;*/
        return false;
    }
    
}
