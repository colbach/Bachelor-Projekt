package view.main.toolbar;

import utils.Drawing;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import static view.Constants.*;
import view.main.toolbar.Toolbar;
import view.main.toolbar.ToolbarItem;

public class ToolbarDrafter {

    public static void drawToolbar(Graphics2D g, int width, Toolbar toolbar) {
        // Hintergrund zeichnen...
        g.setColor(TOOLBAR_BACKGROUND_COLOR);
        g.fillRect(0, 0, width, TOOLBAR_HEIGHT);
        g.setColor(DEFAULT_LINE_COLOR);
        g.setStroke(TOOLBAR_BORDER_LINE_STROKE);
        g.drawLine(0, TOOLBAR_HEIGHT, width, TOOLBAR_HEIGHT);
        
        // Toolbaritems zeichen...
        Drawing.enableAntialiasing(g);
        int c=0; // zaehlt sichtbare Elemente
        for (ToolbarItem item : toolbar.getToolbarItems()) { // fuer jedes Element
            
            // Zeichne Element wenn sichtbar & zaehle c hoch
            if(!item.isHidden()) {
                
                // Spezialfall: Taste gedrueckt...
                int offXY = 0; // Offest welcher auf alle X und Y Koordinaten aufgerechnet wird
                if(item.isPressedDown()) {
                    g.setColor(TOOLBAR_ITEM_PRESSED_DOWN_BACKGROUND_COLOR);
                    g.fillRect((c) * TOOLBAR_ITEM_WIDTH, 0, TOOLBAR_ITEM_WIDTH, TOOLBAR_HEIGHT);
                    offXY = TOOLBAR_ITEM_PRESSED_DOWN_OFFSET_XY;
                    g.setColor(TOOLBAR_ITEM_PRESSED_DOWN_BORDER_COLOR);
                    g.fillRect((c) * TOOLBAR_ITEM_WIDTH, 0, offXY, TOOLBAR_HEIGHT);
                    g.fillRect((c) * TOOLBAR_ITEM_WIDTH, 0, TOOLBAR_ITEM_WIDTH, offXY);
                }
                
                // Zeichne Trennlinie...
                g.setColor(BEETWEEN_TOOLBAR_ITEMS_COLOR);
                g.setStroke(TOOLBAR_BORDER_LINE_STROKE);
                g.drawLine((c + 1) * TOOLBAR_ITEM_WIDTH, 0, (c + 1) * TOOLBAR_ITEM_WIDTH, TOOLBAR_HEIGHT - 1);

                // Zeichne Icon...
                item.getImage().drawCentered(g, c * TOOLBAR_ITEM_WIDTH + TOOLBAR_ITEM_WIDTH / 2 + offXY, TOOLBAR_HEIGHT / 2 - 8 + offXY);

                // Zeichne Label...
                g.setFont(TOOLBAR_ITEM_LABEL_FONT);
                g.setColor(DEFAULT_FONT_COLOR);
                int labelWidth = g.getFontMetrics().stringWidth(item.getLabel());
                g.drawString(item.getLabel(), c * TOOLBAR_ITEM_WIDTH + TOOLBAR_ITEM_WIDTH / 2 - labelWidth / 2 + offXY, TOOLBAR_HEIGHT / 2 + 26 + offXY);
                
                // c hochzaehlen...
                c++;
            }
        }
        
        // Infobutton zeichnen...
        {
            
            if((c+1) * TOOLBAR_ITEM_WIDTH > width) { // Falls Breite nicht gross genug fuer alle Elemente ist
                width = (c+1) * TOOLBAR_ITEM_WIDTH; // Breite erweitern (wird in Fenster dann abgeschnitten)
            } 
            int offXY = 0; // Offest welcher auf alle X und Y Koordinaten aufgerechnet wird wenn Infobutton gedrueckt ist
            if(toolbar.isInfoButtonIsPressed()) { // Wenn Infobutton gedrueckt ist
                offXY = TOOLBAR_ITEM_PRESSED_DOWN_OFFSET_XY+1;
                g.setColor(TOOLBAR_ITEM_PRESSED_DOWN_BORDER_COLOR);
                for(int i=0; i<offXY; i++) {
                    g.fillOval(width - 55 + i, 20 + i, 28, 28);
                }
                g.setColor(TOOLBAR_ITEM_PRESSED_DOWN_BACKGROUND_COLOR);
                g.fillOval(width - 55 + offXY, 20 + offXY, 28, 28);
                Drawing.disableAntialiasing(g);
                g.setColor(TOOLBAR_BACKGROUND_COLOR);
                g.setStroke(TWO_PX_LINE_STROKE);
                for(int i=1; i<offXY+2; i++) {
                    g.drawOval(width - 55 - i, 20 - i, 28 + 2*i, 28 + 2*i);;
                }
            }
            Drawing.enableAntialiasing(g);
            g.setStroke(ONE_PX_LINE_STROKE);
            g.setColor(DEFAULT_LINE_COLOR);
            g.drawOval(width - 55, 20, 28, 28);
            g.drawOval(width - 43 + offXY, 26 + offXY, 4, 4);
            g.drawRect(width - 43 + offXY, 33 + offXY, 4, 8);
        }
        

    }
}
