package view.main.runreport;

import utils.format.TimeFormat;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;
import model.*;
import model.runproject.debugger.Debugger;
import model.runproject.report.Report;
import utils.*;
import view.*;
import static view.Constants.*;

public class GeneralRuntimePanel extends JPanel {

    private Report report;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

    public GeneralRuntimePanel() {
    }

    public void setReport(Report report) {
        this.report = report;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;

        if (report != null) {

            Drawing.enableAntialiasing(g);

            int a = 20;

            g.setColor(Color.BLACK);

            String startString = "nicht verfügbar";
            Date startDate = report.getStartDate();
            if (startDate != null) {
                startString = SIMPLE_DATE_FORMAT.format(startDate);
            }
            g.drawString("Gestartet: " + startString, 1, a - 5);

            String endString = "nicht verfügbar";
            Date endDate = report.getEndDate();
            if (endDate != null) {
                endString = SIMPLE_DATE_FORMAT.format(endDate);
            }
            g.drawString("Beendet: " + endString, 1, a - 5 + a);

            g.setStroke(ONE_PX_LINE_STROKE);
            int b = 210;
            g.drawLine(b - 5, 4, b, 4);
            g.drawLine(b, 4, b, a + a - 4);
            g.drawLine(b - 5, a + a - 4, b, a + a - 4);
            g.drawLine(b, a, b, a + 7);

            long millis = report.getRuntimeInMillis();
            g.drawString("Laufzeit: " + TimeFormat.format(millis), b + 12, a - 5 + a / 2);

            g.drawString("Beendigungsgrund: " + report.getTerminationMessage(), 1, a - 5 + a + a);

        } else {
            System.err.println("debugger ist null.");
            g.setColor(Color.RED);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
