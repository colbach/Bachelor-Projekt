package view.debug;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import javax.swing.JPanel;
import logging.AdditionalErr;
import model.runproject.debugger.DebuggerRemote;
import utils.Drawing;
import utils.measurements.Area;
import utils.measurements.NonArea;
import view.sharedcomponents.scrollbar.Direction;
import view.sharedcomponents.scrollbar.Scrollbar;
import static view.Constants.*;
import view.sharedcomponents.scrollbar.ScrollbarDrafter;

public class LiveContextCounterPanel extends JPanel {

    private final Scrollbar scrollbar;
    private String[] lines;
    private final static int OFF_PER_LINE = 18;

    public LiveContextCounterPanel() {
        this.scrollbar = new Scrollbar(0, 0, Direction.VERTICAL, NonArea.getInstance());
        this.lines = new String[0];
    }

    public void capture(DebuggerRemote debugger) {
        HashMap<String, Integer> createdContextCountPerContextCreator = debugger.getContextCreatorDescriptionsToChildContextCount();
        HashMap<String, Integer> finishedContextCountPerContextCreator = debugger.getContextCreatorDescriptionsToTerminatedChildContextCount();
        lines = new String[createdContextCountPerContextCreator.size()];
        int i = 0;
        for (String key : createdContextCountPerContextCreator.keySet()) {
            Integer created = createdContextCountPerContextCreator.get(key);
            if (created == null) {
                created = 0;
            }
            Integer finished = finishedContextCountPerContextCreator.get(key);
            if (finished == null) {
                finished = 0;
            }
            lines[i] = key + " (" + created + " erzeugt, davon " + finished + " bereits beendet)";
        }
        scrollbar.setRepresentedSize(lines.length * OFF_PER_LINE);
    }

    public void updateScrollbarArea() {
        Area actualArea = new Area(getWidth() - SCROLLBAR_WIDTH - 2, 2, SCROLLBAR_WIDTH, getHeight() - 4);
        scrollbar.setArea(actualArea);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        Drawing.enableAntialiasing(g);
        
        g.setColor(DEFAULT_FONT_COLOR);
        int startY = -scrollbar.getOffsetForContent();
        for (int i = 0; i < lines.length; i++) {
            g.drawString(lines[i], 4, startY + i * OFF_PER_LINE + 14);
        }

        ScrollbarDrafter.drawScrollbar(g, scrollbar);
    }

    public Scrollbar getScrollbar() {
        return scrollbar;
    }
    
}
