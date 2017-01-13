package view.main.rightclickmenue;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import model.Node;
import static view.Constants.*;
import static utils.Drawing.*;
import utils.measurements.Area;

public class RightClickMenueDrafter {

    public static Area calcLeftClickMenueArea(Graphics2D g, RightClickMenue lcm) {
        return calcLeftClickMenueArea(g.getFontMetrics(RIGHT_CLICK_MENUE_FONT), lcm);
    }
    
    public static Area calcLeftClickMenueArea(FontMetrics fm, RightClickMenue lcm) {
        // Hoehe...
        int height = RIGHT_CLICK_MENUE_ITEM_HEIGHT * lcm.getItemCount();
        
        // Breite...
        int width = 20;
        for(int i=0; i<lcm.getItemCount(); i++) {
            width = Math.max(30 + fm.stringWidth(lcm.getItem(i).getLabel()), width);
        }
        if(lcm.hasCicles()) {
            width += 15;
        }
        
        // x, y...
        int x = 0, y = 0;
        switch (lcm.getCorner()) {
            case LEFT_BOTTOM:
                x = lcm.getX() - width;
                y = lcm.getY();
                break;
            case RIGHT_BOTTOM:
                x = lcm.getX();
                y = lcm.getY();
                break;
            case LEFT_TOP:
                x = lcm.getX() - width;
                y = lcm.getY() - height;
                break;
            case RIGHT_TOP:
                x = lcm.getX();
                y = lcm.getY() - height;
                break;
        }
        return new Area(x, y, width, height);
    }

    public static void drawLeftClickMenue(Graphics2D g, RightClickMenue lcm) {
        Area area = calcLeftClickMenueArea(g, lcm);

        g.setColor(RIGHT_CLICK_MENUE_BACKGROUND_COLOR);
        g.fillRect(area.getX(), area.getY(), area.getWidth(), area.getHeight());
        g.setStroke(ONE_PX_LINE_STROKE);
        g.setFont(RIGHT_CLICK_MENUE_FONT);
        for (int i = 0; i < lcm.getItemCount(); i++) {
            RightClickMenueItem item = lcm.getItem(i);
            if (item.isPressed()) {
                g.setColor(RIGHT_CLICK_MENUE_PRESSED_COLOR);
                g.fillRect(area.getX(), area.getY() + i * RIGHT_CLICK_MENUE_ITEM_HEIGHT, area.getWidth(), RIGHT_CLICK_MENUE_ITEM_HEIGHT);
            }
            g.setColor(RIGHT_CLICK_MENUE_FRONT_COLOR);
            int labelX = area.getX() + 15;
            if(lcm.hasCicles()) {
                drawConnectionCircle(g, area.getX()+15+5, area.getY() + i * RIGHT_CLICK_MENUE_ITEM_HEIGHT + (RIGHT_CLICK_MENUE_ITEM_HEIGHT / 2), lcm.isFilledCicle(i));
                labelX += 15;
            }
            String label = item.getLabel();
            if(lcm.isStroked(i)) {
                int strokeLength = g.getFontMetrics().stringWidth(label);
                g.setStroke(ONE_PX_LINE_STROKE);
                g.drawLine(labelX, area.getY() + i * RIGHT_CLICK_MENUE_ITEM_HEIGHT + (RIGHT_CLICK_MENUE_ITEM_HEIGHT / 2), labelX+strokeLength, area.getY() + i * RIGHT_CLICK_MENUE_ITEM_HEIGHT + (RIGHT_CLICK_MENUE_ITEM_HEIGHT / 2));
            }
            g.drawString(label, labelX, area.getY() + i * RIGHT_CLICK_MENUE_ITEM_HEIGHT + ((RIGHT_CLICK_MENUE_ITEM_HEIGHT / 3) * 2));
        }
        g.setColor(RIGHT_CLICK_MENUE_PRESSED_COLOR);
        for (int i = 1; i < lcm.getItemCount(); i++) {
            g.drawLine(area.getX(), area.getY() + i * RIGHT_CLICK_MENUE_ITEM_HEIGHT, area.getX() + area.getWidth(), area.getY() + i * RIGHT_CLICK_MENUE_ITEM_HEIGHT);
        }
        g.setColor(RIGHT_CLICK_MENUE_FRONT_COLOR);
        g.drawRect(area.getX(), area.getY(), area.getWidth(), area.getHeight());
    }

    /**
     * Berechnet LeftClickMenueItem welcher sich unter Mouse befindet. Falls
     * kein Graphics2D vorhanden kann null eingesetzt werden. Dies erzeugt ein
     * (Headless) Canvas welches das benoetigte Graphics2D bereit stellt.
     */
    public static RightClickMenueItem calcNodesForMousePosition(Graphics2D g, RightClickMenue lcm, int mouseX, int mouseY) {
        Area area;
        if (g == null) {
            area = calcLeftClickMenueArea(new Canvas().getFontMetrics(RIGHT_CLICK_MENUE_FONT), lcm); 
        } else {
            area = calcLeftClickMenueArea(g, lcm);
        }
        if (area.isInside(mouseX, mouseY)) {
            for (int i = 0; i < lcm.getItemCount(); i++) {
                if (mouseY < area.getY() + (i + 1) * RIGHT_CLICK_MENUE_ITEM_HEIGHT) {
                    return lcm.getItem(i);
                }
            }
        }
        return null;
    }
    
    private static void drawConnectionCircle(Graphics2D g, int xCenter, int yCenter, boolean connected) {

        g.setColor(RIGHT_CLICK_MENUE_FRONT_COLOR);
        g.setStroke(ONE_PX_LINE_STROKE);
        final int RADIUS = 4;
        g.drawOval(xCenter - RADIUS, yCenter - RADIUS, RADIUS * 2, RADIUS * 2);
        if (connected) {
            g.fillOval(xCenter - RADIUS, yCenter - RADIUS, RADIUS * 2, RADIUS * 2);
        }

    }
}
