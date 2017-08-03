package view.main;

import java.awt.Color;
import java.awt.Graphics2D;
import settings.GeneralSettings;
import static settings.GeneralSettings.*;
import static view.Constants.*;

public class BackgroundDrafter {

    private static GeneralSettings settings = GeneralSettings.getInstance();

    public static void drawBackground(Graphics2D g, int offX, int offY, int startX, int startY, int width, int height) {

        boolean blueprint = false;
        String backgroundValue = settings.getString(VIEW_BACKGROUND_KEY, VIEW_BACKGROUND_VALUE_DEFAULT);
        if (backgroundValue.trim().equalsIgnoreCase(VIEW_BACKGROUND_VALUE_BLUEPRINT)) {
            blueprint = true;
        }

        if (blueprint) {
            g.setColor(BLUEPRINT_BACKGROUND_COLOR);
        } else {
            g.setColor(DEFAULT_BACKGROUND_COLOR);
        }
        g.fillRect(startX, startY, width, height);
        if (blueprint) {
            g.setColor(BLUEPRINT_BACKGROUND_GRID_COLOR);
        } else {
            g.setColor(DEFAULT_BACKGROUND_GRID_COLOR);
        }
        drawGrid(g, offX, offY, startX, startY, width, height);
    }

    private static void drawGrid(Graphics2D g, int offX, int offY, int startX, int startY, int width, int height) {

        // Vertikale Linien
        for (int i = 0; i < width; i++) {
            if (i - offX == 0) {
                g.setStroke(FOUR_PX_LINE_STROKE);
            } else if ((i - offX) % 250 == 0) {
                g.setStroke(TREE_PX_LINE_STROKE);
            } else if ((i - offX) % 50 == 0) {
                g.setStroke(TWO_PX_LINE_STROKE);
            } else if ((i - offX) % 10 == 0) {
                g.setStroke(ONE_PX_LINE_STROKE);
            } else {
                continue; // naechste Iteration
            }
            g.drawLine(startX + i, startY, startX + i, startY + height);
        }

        // Horizontale Linien
        for (int i = 0; i < height; i++) {
            if (i - offY == 0) {
                g.setStroke(FOUR_PX_LINE_STROKE);
            } else if ((i - offY) % 250 == 0) {
                g.setStroke(TREE_PX_LINE_STROKE);
            } else if ((i - offY) % 50 == 0) {
                g.setStroke(TWO_PX_LINE_STROKE);
            } else if ((i - offY) % 10 == 0) {
                g.setStroke(ONE_PX_LINE_STROKE);
            } else {
                continue; // naechste Iteration
            }
            g.drawLine(startX, startY + i, startX + width, startY + i);
        }

    }

}
