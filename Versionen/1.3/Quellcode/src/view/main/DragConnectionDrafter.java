package view.main;

import java.awt.Graphics2D;
import static view.Constants.*;

public class DragConnectionDrafter {
    
    public static void drawDragConnectionLineBetweenNodes(Graphics2D g, int x1, int y1, int x2, int y2, boolean inverted) {
        
        // Zeichne Hintergrund-Linie (Border)...
        g.setColor(DRAG_CONNECTION_LINE_COLOR_BACK);
        g.setStroke(SIX_PX_LINE_STROKE);
        g.fillOval(x1-6, y1-6, 12, 12);
        g.fillOval(x2-6, y2-6, 12, 12);
        g.drawLine(x1, y1, x2, y2);
        
        // Zeichne eigentliche Linie...
        g.setColor(DRAG_CONNECTION_LINE_COLOR_FRONT);
        g.setStroke(FOUR_PX_LINE_STROKE);
        g.fillOval(x1-4, y1-4, 8, 8);
        g.fillOval(x2-4, y2-4, 8, 8);
        g.drawLine(x1, y1, x2, y2);
        
    }
    
    public static void drawDragConnectionLineBetweenLets(Graphics2D g, int x1, int y1, int x2, int y2, boolean inverted) {
        
        // Zeichne eigentliche Linie...
        g.setColor(CONNECT_OVERLAY_TEXT_COLOR);
        g.setStroke(FOUR_PX_LINE_STROKE);
        g.fillOval(x1-4, y1-4, 8, 8);
        g.fillOval(x2-4, y2-4, 8, 8);
        g.drawLine(x1, y1, x2, y2);
        
    }
    
}
