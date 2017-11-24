package view.sharedcomponents.scrollbar;

import java.awt.Color;
import java.awt.Graphics2D;
import utils.measurements.*;
import static view.Constants.*;

public class ScrollbarDrafter {
    
    public static void drawScrollbar(Graphics2D g, Scrollbar scrollbar) {
        Area area = scrollbar.getArea();
        
        // Kontrolle ob gezeichnet werden muss...
        if(!scrollbar.isVisible()) { // Falls Scrollbar nicht sichtbar ist
            //System.out.println("Scrollbar wird nicht angezeigt da nicht sichtbar.");
            return; // Abbrechen
        }
        
        // Scrollbar zeichnen (Vertikal)...
        if(scrollbar.getDirection() == Direction.VERTICAL) {
            g.setColor(SCROLLBAR_BACKGROUND_COLOR);
            g.fillRect(area.getX(), area.getY(), area.getWidth(), area.getHeight());
            g.setColor(SCROLLBAR_COLOR);
            g.fillRect(area.getX(), area.getY() + scrollbar.getBarBegin(), area.getWidth(), scrollbar.getBarSize());
            g.setColor(SCROLLBAR_BORDER_COLOR);
            g.setStroke(ONE_PX_LINE_STROKE);
            g.drawRect(area.getX(), area.getY() + scrollbar.getBarBegin(), area.getWidth(), scrollbar.getBarSize());
        } else {
            g.setColor(SCROLLBAR_BACKGROUND_COLOR);
            g.fillRect(area.getX(), area.getY(), area.getWidth(), area.getHeight());
            g.setColor(SCROLLBAR_COLOR);
            g.fillRect(area.getX() + scrollbar.getBarBegin(), area.getY(), scrollbar.getBarSize(), area.getHeight());
            g.setColor(SCROLLBAR_BORDER_COLOR);
            g.setStroke(ONE_PX_LINE_STROKE);
            g.drawRect(area.getX() + scrollbar.getBarBegin(), area.getY(), scrollbar.getBarSize(), area.getHeight());
        }
        
    }
    
}
