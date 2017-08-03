package view.sharedcomponents;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import logging.LogTextSource;

public class LogPanel extends JPanel {

    private String input = "";

    private Scrollbar scrollbar;
    private LogTextSource source;
    private int drawCounterForDebugging = 0;

    private boolean scrollbarNeedsAreaUpdate = true;
    private int lastAvailableOutputCount = -1;

    private boolean black = true;

    private GeneralSettings settings = GeneralSettings.getInstance();

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void scrollbarNeedsAreaUpdate() {
        this.scrollbarNeedsAreaUpdate = true;
    }

    public LogPanel() {
        this.source = AdvancedLogger.getGeneralInstance();
        this.scrollbar = new Scrollbar((this.source.getAvailableCount() + 1) * 15, 1, Direction.VERTICAL, NonArea.getInstance());
    }

    public void setWhiteMode(boolean w) {
        black = !w;
    }

    public LogPanel(LogTextSource logger) {
        if (logger == null) {
            AdditionalLogger.err.println("Logger ist null. Erzeuge LogPanel ohne Logger");
        } else {
            this.source = logger;
            this.scrollbar = new Scrollbar((this.source.getAvailableCount() + 1) * 15, 1, Direction.VERTICAL, NonArea.getInstance());
        }
    }

    public void setSource(LogTextSource source) {
        this.source = source;
        this.scrollbar = new Scrollbar((this.source.getAvailableCount() + 1) * 15, 1, Direction.VERTICAL, NonArea.getInstance());
    }

    public void updateScrollbarArea() {
        if (scrollbar != null) {
            if (source != null) {
                scrollbarNeedsAreaUpdate = false;
                scrollbar.setArea(new Area(getWidth() - SCROLLBAR_WIDTH - 2, 2, SCROLLBAR_WIDTH, getHeight() - 4));
                scrollbar.setRepresentedSize((this.source.getAvailableCount() + 1) * 15);
            } else {
                AdditionalLogger.err.println("ScrollbarArea kann nicht aktualisiert werden da logger null ist.");
            }
        } else {
            AdditionalLogger.err.println("ScrollbarArea kann nicht aktualisiert werden da scrollbar null ist.");
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        // Hintergrund zeichnen...
        if (black) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.WHITE);
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        if (!black) {
            g.setColor(Color.BLACK);
            g.setStroke(TWO_PX_LINE_STROKE);
            g.drawRect(0, 0, getWidth(), getHeight());
        }

        if (source == null) {
            AdditionalLogger.err.println("Logger ist null. Keine Daten zum anzeigen.");

            // Warnung
            Drawing.enableAntialiasing(g);
            if (black) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.BLACK);
            }
            String text = "Keine Daten zur Anzeige vorhanden";
            int textWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 + 5);

        } else {

            Drawing.disableAntialiasing(g);

            int availableOutputCount = source.getAvailableCount();
            if (scrollbarNeedsAreaUpdate || availableOutputCount != lastAvailableOutputCount) {
                updateScrollbarArea();
            }
            this.lastAvailableOutputCount = availableOutputCount;

            // Text zeichnen...
            int offMinusY = 0;
            int i = 0;
            //if (black) {
            //    g.setFont(LOG_TEXT_FONT);
            //} else {
            g.setFont(LOG_TEXT_FONT_BOLD);
            //}
            while (/*offMinusY < getHeight() &&*/i < availableOutputCount) {
                if (source.isError(i)) {
                    g.setColor(LOG_ERR_TEXT_COLOR);
                } else {
                    if (black) {
                        g.setColor(LOG_OUT_TEXT_COLOR);
                    } else {
                        g.setColor(Color.BLACK);
                    }
                }
                int scrollBarOffY = scrollbar.getRepresentedSize() - scrollbar.getOffsetForContent() - getHeight();
                String line = source.get(i);
                if (i == 0) {
                    line += input;
                }
                g.drawString(line, 5, getHeight() - 10 - offMinusY + scrollBarOffY);
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
