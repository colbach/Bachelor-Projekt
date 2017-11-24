package view.onrun;

import view.listener.MouseAndKeyboardListener;
import view.sharedcomponents.scrollbar.Scrollbar;

/**
 * Diese Klasse dient dazu auf einer Flaeche etwas zu ziehen und das in den
 * entsprechenden Scrollbars zu registrieren.
 */
public class DragAreaListener extends MouseAndKeyboardListener {

    private final Scrollbar verticalScrollbar, horizontalScrollbar;

    public DragAreaListener(Scrollbar verticalScrollbar, Scrollbar horizontalScrollbar) {
        this.verticalScrollbar = verticalScrollbar;
        this.horizontalScrollbar = horizontalScrollbar;
    }

    @Override
    public boolean mouseDragged(int lastX, int lastY, int actualX, int actualY, int startX, int startY) {
        int movedX = lastX - actualX;
        int movedY = lastY - actualY;
        verticalScrollbar.moveContent(movedY);
        horizontalScrollbar.moveContent(movedX);
        return true;
    }
}
