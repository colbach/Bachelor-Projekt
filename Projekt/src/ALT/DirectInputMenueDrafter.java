package ALT;

import view.main.rightclickmenue.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import model.Inlet;
import model.Node;
import static view.Constants.*;
import static utils.Drawing.*;
import utils.measurements.Area;

public class DirectInputMenueDrafter {

    public static Area calcDirectInputMenueArea(Graphics2D g, DirectInputMenue dim) {
        return DirectInputMenueDrafter.calcDirectInputMenueArea(g.getFontMetrics(RIGHT_CLICK_MENUE_FONT), dim);
    }
    
    public static Area calcDirectInputMenueArea(FontMetrics fm, DirectInputMenue dim) {
        // Hoehe...
        int height = RIGHT_CLICK_MENUE_ITEM_HEIGHT * dim.getInletCount();
        
        // Breite...
        int width = 20;
        for(int i=0; i<dim.getInletCount(); i++) {
            width = Math.max(30 + 15 + fm.stringWidth(dim.getInletName(i)), width);
        }
        
        // x, y...
        int x = 0, y = 0;
        switch (dim.getCorner()) {
            case LEFT_BOTTOM:
                x = dim.getX() - width;
                y = dim.getY();
                break;
            case RIGHT_BOTTOM:
                x = dim.getX();
                y = dim.getY();
                break;
            case LEFT_TOP:
                x = dim.getX() - width;
                y = dim.getY() - height;
                break;
            case RIGHT_TOP:
                x = dim.getX();
                y = dim.getY() - height;
                break;
        }
        return new Area(x, y, width, height);
    }

    public static void drawDirectInputMenue(Graphics2D g, DirectInputMenue dim) {
        Area area = calcDirectInputMenueArea(g, dim);

        g.setColor(RIGHT_CLICK_MENUE_BACKGROUND_COLOR);
        g.fillRect(area.getX(), area.getY(), area.getWidth(), area.getHeight());
        g.setStroke(ONE_PX_LINE_STROKE);
        g.setFont(RIGHT_CLICK_MENUE_FONT);
        for (int i = 0; i < dim.getInletCount(); i++) {
            String name = dim.getInletName(i);
            if (dim.isInletPressed(i)) {
                g.setColor(RIGHT_CLICK_MENUE_PRESSED_COLOR);
                g.fillRect(area.getX(), area.getY() + i * RIGHT_CLICK_MENUE_ITEM_HEIGHT, area.getWidth(), RIGHT_CLICK_MENUE_ITEM_HEIGHT);
            }
            g.setColor(RIGHT_CLICK_MENUE_FRONT_COLOR);
            g.drawString(name, area.getX() + 15, area.getY() + i * RIGHT_CLICK_MENUE_ITEM_HEIGHT + ((RIGHT_CLICK_MENUE_ITEM_HEIGHT / 3) * 2));
        }
        g.setColor(RIGHT_CLICK_MENUE_PRESSED_COLOR);
        for (int i = 1; i < dim.getInletCount(); i++) {
            g.drawLine(area.getX(), area.getY() + i * RIGHT_CLICK_MENUE_ITEM_HEIGHT, area.getX() + area.getWidth(), area.getY() + i * RIGHT_CLICK_MENUE_ITEM_HEIGHT);
        }
        g.setColor(RIGHT_CLICK_MENUE_FRONT_COLOR);
        g.drawRect(area.getX(), area.getY(), area.getWidth(), area.getHeight());
    }

    public static Inlet calcNodesForMousePosition(Graphics2D g, DirectInputMenue dim, int mouseX, int mouseY) {
        Area area;
        if (g == null) {
            area = DirectInputMenueDrafter.calcDirectInputMenueArea(new Canvas().getFontMetrics(RIGHT_CLICK_MENUE_FONT), dim); 
        } else {
            area = calcDirectInputMenueArea(g, dim);
        }
        if (area.isInside(mouseX, mouseY)) {
            for (int i = 0; i < dim.getInletCount(); i++) {
                if (mouseY < area.getY() + (i + 1) * RIGHT_CLICK_MENUE_ITEM_HEIGHT) {
                    return dim.getInlet(i);
                }
            }
        }
        return null;
    }
}
