package view.main.runreport;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.SimpleDateFormat;
import javax.swing.JPanel;
import model.*;
import model.runproject.*;
import utils.*;
import view.*;
import static view.Constants.*;

public class GeneralRuntimePanel extends JPanel {

    private Debugger debugger;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

    public GeneralRuntimePanel() {
    }

    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;

        if (debugger != null) {

            Drawing.enableAntialiasing(g);

            int a = 20;

            g.setColor(Color.BLACK);
            g.drawString("Gestartet: " + SIMPLE_DATE_FORMAT.format(debugger.getStartDate()), 1, a - 5);
            g.drawString("Beendet: " + SIMPLE_DATE_FORMAT.format(debugger.getEndDate()), 1, a - 5 + a);

            g.setStroke(ONE_PX_LINE_STROKE);
            int b = 210;
            g.drawLine(b - 5, 4, b, 4);
            g.drawLine(b, 4, b, a + a - 4);
            g.drawLine(b - 5, a + a - 4, b, a + a - 4);
            g.drawLine(b, a, b, a + 7);

            long millis = debugger.getRuntimeInMillis();
            g.drawString("Laufzeit: " + TimeFormat.format(millis), b + 12, a - 5 + a / 2);
            
            g.drawString("Beendigungsgrund: " + debugger.getExitMessage(), 1, a - 5 + a + a);

        } else {
            System.err.println("debugger ist null.");
            g.setColor(Color.RED);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
