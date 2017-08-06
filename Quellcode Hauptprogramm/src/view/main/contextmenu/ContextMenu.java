package view.main.contextmenu;

import view.listener.MouseAndKeyboardListener;
import view.main.MainWindow;

public class ContextMenu {
    
    protected RightClickMenueItem[] items;
    private final int x, y;
    private final Corner corner;
    private final MainWindow mainWindow;
    protected Boolean[] cicles = null;
    protected Boolean[] stroked = null;
    
    public ContextMenu(RightClickMenueItem[] items, int x, int y, int viewWidth, int viewHeight, MainWindow mainWindow) {
        this.items = items;
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
    
    public int getItemCount() {
        return items.length;
    }
    
    public RightClickMenueItem getItem(int i) {
        return items[i];
    }

    public Corner getCorner() {
        return corner;
    }

    public boolean mousePressed(int x, int y) {
        RightClickMenueItem matched = ContextMenuDrafter.calcNodesForMousePosition(null, this, x, y);
        if(matched != null) {
            if(matched.isClickable()) {
                matched.setPressed(true);
            }
            return true;
        }
        else return false;
    }

    public boolean mouseClicked(int x, int y) {
        RightClickMenueItem matched = ContextMenuDrafter.calcNodesForMousePosition(null, this, x, y);
        if(matched != null) {
            if(matched.isClickable()) {
                matched.clicked();
            }
            return true;
        }
        else return false;
    }

    public boolean mouseReleased() {
        for(RightClickMenueItem item : items)
            item.setPressed(false);
        return false;
    }

    public boolean hasCicles() {
        return cicles != null;
    }
    
    public boolean isFilledCicle(int i) {
        if(hasCicles()) {
            return cicles[i];
        } else {
            return false;
        }
    }
    
    public boolean hasStrokes() {
        return stroked != null;
    }
    
    public boolean isStroked(int i) {
        if(hasStrokes()) {
            return stroked[i];
        } else {
            return false;
        }
    }
}
