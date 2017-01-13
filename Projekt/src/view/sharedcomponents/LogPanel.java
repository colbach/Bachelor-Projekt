package view.sharedcomponents;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import log.AdditionalErr;
import log.Logging;
import settings.GeneralSettings;
import utils.Drawing;
import utils.measurements.Area;
import utils.measurements.NonArea;
import static view.Constants.*;
import view.sharedcomponents.scrollbar.*;

public class LogPanel extends JPanel {

    private Scrollbar scrollbar;
    private Logging logger;
    private int drawCounterForDebugging = 0;

    private boolean scrollbarNeedsAreaUpdate = true;
    private int lastAvailableOutputCount = -1;

    private GeneralSettings settings = GeneralSettings.getInstance();

    public void scrollbarNeedsAreaUpdate() {
        this.scrollbarNeedsAreaUpdate = true;
    }

    public LogPanel() {
        this.logger = Logging.getGeneralInstance();
        this.scrollbar = new Scrollbar((this.logger.getAvailableCount() + 1) * 15, 1, Direction.VERTICAL, NonArea.getInstance());
    }

    public LogPanel(Logging logger) {
        if (logger == null) {
            AdditionalErr.println("Logger ist null. Erzeuge LogPanel ohne Logger");
        } else {
            this.logger = logger;
            this.scrollbar = new Scrollbar((this.logger.getAvailableCount() + 1) * 15, 1, Direction.VERTICAL, NonArea.getInstance());
        }
    }

    public void setLogger(Logging logger) {
        this.logger = logger;
        this.scrollbar = new Scrollbar((this.logger.getAvailableCount() + 1) * 15, 1, Direction.VERTICAL, NonArea.getInstance());
    }

    public void updateScrollbarArea() {
        if (scrollbar != null) {
            if (logger != null) {
                scrollbarNeedsAreaUpdate = false;
                scrollbar.setArea(new Area(getWidth() - SCROLLBAR_WIDTH - 2, 2, SCROLLBAR_WIDTH, getHeight() - 4));
                scrollbar.setRepresentedSize((this.logger.getAvailableCount() + 1) * 15);
            } else {
                AdditionalErr.println("ScrollbarArea kann nicht aktualisiert werden da logger null ist.");
            }
        } else {
            AdditionalErr.println("ScrollbarArea kann nicht aktualisiert werden da scrollbar null ist.");
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        // Hintergrund zeichnen...
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (logger == null) {
            AdditionalErr.println("Logger ist null. Keine Daten zum anzeigen.");

            // Warnung
            Drawing.enableAntialiasing(g);
            g.setColor(Color.WHITE);
            String text = "Keine Daten zur Anzeige vorhanden";
            int textWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 + 5);

        } else {

            Drawing.disableAntialiasing(g);

            int availableOutputCount = logger.getAvailableCount();
            if (scrollbarNeedsAreaUpdate || availableOutputCount != lastAvailableOutputCount) {
                updateScrollbarArea();
            }
            this.lastAvailableOutputCount = availableOutputCount;

            // Text zeichnen...
            int offMinusY = 0;
            int i = 0;
            g.setFont(LOG_TEXT_FONT);
            while (/*offMinusY < getHeight() &&*/i < availableOutputCount) {
                if (logger.isError(i)) {
                    g.setColor(LOG_ERR_TEXT_COLOR);
                } else {
                    g.setColor(LOG_OUT_TEXT_COLOR);
                }
                int scrollBarOffY = scrollbar.getRepresentedHeight() - scrollbar.getOffsetForContent() - getHeight();
                g.drawString(logger.get(i), 5, getHeight() - 10 - offMinusY + scrollBarOffY);
                i++;
                offMinusY += 15;
            }

            // scrollbar zeichnen...
            ScrollbarDrafter.drawScrollbar(g, scrollbar);

            // DrawCounter zeichnen...
            drawCounterForDebugging++;
            if (this.settings.getBoolean(GeneralSettings.DEVELOPER_REDRAW_COUNTER_KEY, false)) {
                g.setColor(Color.GRAY);
                g.setFont(DEVELOPER_INFO_FONT);
                String drawCounterForDebuggingSring = String.valueOf(drawCounterForDebugging);
                g.drawString(drawCounterForDebuggingSring, getWidth() - g.getFontMetrics().stringWidth(drawCounterForDebuggingSring) - 3 - (scrollbar.isVisible() ? SCROLLBAR_WIDTH + 3 : 0), 10);
            }
        }
    }

    public Scrollbar getScrollbar() {
        return scrollbar;
    }

}
