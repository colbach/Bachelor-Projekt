package view.onrun.showimage;

import view.sharedcomponents.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import logging.AdditionalErr;
import logging.AdditionalLogger;
import logging.AdvancedLogger;
import settings.GeneralSettings;
import utils.Drawing;
import utils.measurements.Area;
import utils.measurements.NonArea;
import static view.Constants.*;
import view.sharedcomponents.scrollbar.*;

public class ShowBitmapPanel extends JPanel {

    private Scrollbar verticalScrollbar, horizontalScrollbar;

    private boolean scrollbarNeedsAreaUpdate = true;

    private BufferedImage bitmap = null;

    private GeneralSettings settings = GeneralSettings.getInstance();

    private double scale = 1;

    public void scrollbarNeedsAreaUpdate() {
        this.scrollbarNeedsAreaUpdate = true;
    }

    public ShowBitmapPanel() {
        this.verticalScrollbar = new Scrollbar(100, 0, Direction.VERTICAL, NonArea.getInstance());
        this.horizontalScrollbar = new Scrollbar(100, 0, Direction.HORIZONTAL, NonArea.getInstance());
    }

    public void setBitmap(BufferedImage bitmap) {
        this.bitmap = bitmap;
        updateScrollbarArea();
    }
    
    public void releaseMemory() {
        bitmap = null;
    }

    public void updateScrollbarArea() {
        if (verticalScrollbar != null && horizontalScrollbar != null) {
            if (bitmap != null) {
                scrollbarNeedsAreaUpdate = false;
                verticalScrollbar.setArea(new Area(getWidth() - SCROLLBAR_WIDTH - 2, 2, SCROLLBAR_WIDTH, getHeight() - SCROLLBAR_WIDTH - 4));
                verticalScrollbar.setRepresentedSize((int) Math.round(bitmap.getHeight() * scale));
                horizontalScrollbar.setArea(new Area(2, getHeight() - SCROLLBAR_WIDTH - 2, getWidth() - SCROLLBAR_WIDTH - 4, SCROLLBAR_WIDTH));
                horizontalScrollbar.setRepresentedSize((int) Math.round(bitmap.getWidth() * scale));
            } else {
                AdditionalLogger.err.println("ScrollbarArea kann nicht aktualisiert werden da bitmap null ist.");
            }
        } else {
            AdditionalLogger.err.println("ScrollbarArea kann nicht aktualisiert werden da verticalScrollbar oder horizontalScrollbar null ist.");
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        // Hintergrund zeichnen...
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (bitmap == null) {
            AdditionalLogger.err.println("Logger ist null. Keine Daten zum anzeigen.");

            // Warnung
            Drawing.enableAntialiasing(g);
            g.setColor(Color.BLACK);
            String text = "Keine Daten zur Anzeige vorhanden";
            int textWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 + 5);

        } else {

            g.drawImage(bitmap, -horizontalScrollbar.getOffsetForContent(), -verticalScrollbar.getOffsetForContent(), (int) Math.round(bitmap.getWidth() * scale), (int) Math.round(bitmap.getHeight() * scale), this);

            ScrollbarDrafter.drawScrollbar(g, verticalScrollbar);
            ScrollbarDrafter.drawScrollbar(g, horizontalScrollbar);

        }
    }

    public Scrollbar getVerticalScrollbar() {
        return verticalScrollbar;
    }

    public Scrollbar getHorizontalScrollbar() {
        return horizontalScrollbar;
    }

    public void resetScale() {
        scale = 0;
        updateScrollbarArea();
    }
    
    public void scaleMinusMinus() {
        scale = scale / 2;
        updateScrollbarArea();
    }
    
    public void scalePlusPlus() {
        scale = scale * 2;
        updateScrollbarArea();
    }
}
