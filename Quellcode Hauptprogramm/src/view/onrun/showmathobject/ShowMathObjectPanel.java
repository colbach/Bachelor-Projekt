package view.onrun.showmathobject;

import view.onrun.shownumberarray.*;
import view.sharedcomponents.*;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import javax.swing.JPanel;
import logging.AdditionalErr;
import logging.AdditionalLogger;
import logging.AdvancedLogger;
import reflection.customdatatypes.math.MathObject;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.math.NumberMathObject;
import reflection.customdatatypes.math.Vector;
import settings.GeneralSettings;
import utils.math.Round;
import utils.Drawing;
import utils.measurements.Area;
import utils.measurements.NonArea;
import view.Constants;
import static view.Constants.*;
import view.sharedcomponents.scrollbar.*;

public class ShowMathObjectPanel extends JPanel {

    private MathObject m = null;

    public ShowMathObjectPanel() {
    }

    public void releaseMemory() {
        m = null;
    }

    public void setMathObject(MathObject m) {
        this.m = m;

    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        // Hintergrund zeichnen...
        g.setColor(Constants.DEFAULT_BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (m == null) {
            AdditionalLogger.err.println("Logger ist null. Keine Daten zum anzeigen.");

            // Warnung
            Drawing.enableAntialiasing(g);
            g.setColor(Color.BLACK);
            String text = "Keine Daten zur Anzeige vorhanden";
            int textWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 + 5);

        } else {

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            if (m instanceof NumberMathObject) {
                Drawing.enableAntialiasing(g);
                g.setColor(Color.BLACK);
                String text = m.toString();
                int textWidth = g.getFontMetrics().stringWidth(text);
                g.drawString(text, centerX - textWidth / 2, centerY + 5);

            } else if (m instanceof Matrix) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());

                Drawing.enableAntialiasing(g);
                g.setColor(Color.BLACK);

                Matrix matrix = (Matrix) m;

                int rows = matrix.getRowCount();
                int cols = matrix.getColumCount();

                if (rows == 0 || cols == 1) {
                    String text = "[leere Matrix]";
                    int textWidth = g.getFontMetrics().stringWidth(text);
                    g.drawString(text, centerX - textWidth / 2, centerY + 5);

                } else {

                    String[][] texts = new String[rows][cols];
                    int[] widths = new int[cols];
                    FontMetrics fontMetrics = g.getFontMetrics();

                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < cols; c++) {
                            texts[r][c] = matrix.get(r, c).toString();
                            widths[c] = Math.max(widths[c], fontMetrics.stringWidth(texts[r][c]));
                        }
                    }

                    int v = 20;
                    int matrixPixelWidth = 0;
                    for (int r = 0; r < widths.length; r++) {
                        matrixPixelWidth += widths[r];
                    }
                    matrixPixelWidth += v * widths.length - 1;

                    int u = 40;
                    int matrixPixelHeigth = rows * u - u / 2;

                    int offX = 0;
                    for (int c = 0; c < cols; c++) {
                        for (int r = 0; r < rows; r++) {
                            g.drawString(texts[r][c], centerX - matrixPixelWidth / 2 + offX + 10, centerY - matrixPixelHeigth / 2 + (u * r) + 10);
                        }
                        offX += widths[c] + v;
                    }

                    // [ ...
                    g.drawLine(centerX - matrixPixelWidth / 2 - v, centerY - matrixPixelHeigth / 2 - 20, centerX - matrixPixelWidth / 2 - v, centerY + matrixPixelHeigth / 2 + 20);
                    g.drawLine(centerX - matrixPixelWidth / 2 - v, centerY - matrixPixelHeigth / 2 - 20, centerX - matrixPixelWidth / 2 - v + 10, centerY - matrixPixelHeigth / 2 - 20);
                    g.drawLine(centerX - matrixPixelWidth / 2 - v, centerY + matrixPixelHeigth / 2 + 20, centerX - matrixPixelWidth / 2 - v + 10, centerY + matrixPixelHeigth / 2 + 20);

                    /// ] ...
                    g.drawLine(centerX + matrixPixelWidth / 2 + v, centerY - matrixPixelHeigth / 2 - 20, centerX + matrixPixelWidth / 2 + v, centerY + matrixPixelHeigth / 2 + 20);
                    g.drawLine(centerX + matrixPixelWidth / 2 + v, centerY - matrixPixelHeigth / 2 - 20, centerX + matrixPixelWidth / 2 + v - 10, centerY - matrixPixelHeigth / 2 - 20);
                    g.drawLine(centerX + matrixPixelWidth / 2 + v, centerY + matrixPixelHeigth / 2 + 20, centerX + matrixPixelWidth / 2 + v - 10, centerY + matrixPixelHeigth / 2 + 20);
                }
            } else if (m instanceof Vector) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());

                Drawing.enableAntialiasing(g);
                g.setColor(Color.BLACK);

                Vector vector = (Vector) m;

                int rows = vector.getRowCount();
                int cols = 1;

                if (rows == 0) {
                    String text = "(leerer Vektor)";
                    int textWidth = g.getFontMetrics().stringWidth(text);
                    g.drawString(text, centerX - textWidth / 2, centerY + 5);

                } else {

                    String[][] texts = new String[rows][cols];
                    int[] widths = new int[cols];
                    FontMetrics fontMetrics = g.getFontMetrics();

                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < cols; c++) {
                            texts[r][c] = vector.get(r).toString();
                            widths[c] = fontMetrics.stringWidth(texts[r][c]);
                        }
                    }

                    int v = 20;
                    int matrixPixelWidth = 0;
                    for (int r = 0; r < widths.length; r++) {
                        matrixPixelWidth += widths[r];
                    }
                    matrixPixelWidth += v * widths.length - 1;

                    int u = 40;
                    int matrixPixelHeigth = rows * u - u / 2;

                    int offX = 0;
                    for (int c = 0; c < cols; c++) {
                        for (int r = 0; r < rows; r++) {
                            g.drawString(texts[r][c], centerX - matrixPixelWidth / 2 + offX + 10, centerY - matrixPixelHeigth / 2 + (u * r) + 10);
                        }
                        offX += widths[c] + v;
                    }

                    // ( ...
                    CubicCurve2D.Double cc1 = new CubicCurve2D.Double(
                            centerX - matrixPixelWidth / 2 - v + 10, centerY - matrixPixelHeigth / 2 - 20,
                            centerX - matrixPixelWidth / 2 - v, centerY - matrixPixelHeigth / 2 - 20,
                            centerX - matrixPixelWidth / 2 - v, centerY + matrixPixelHeigth / 2 + 20,
                            centerX - matrixPixelWidth / 2 - v + 10, centerY + matrixPixelHeigth / 2 + 20
                    );

                    ((Graphics2D) g).draw(cc1);

                    // ) ...
                    CubicCurve2D.Double cc2 = new CubicCurve2D.Double(
                            centerX + matrixPixelWidth / 2 + v - 10, centerY - matrixPixelHeigth / 2 - 20,
                            centerX + matrixPixelWidth / 2 + v, centerY - matrixPixelHeigth / 2 - 20,
                            centerX + matrixPixelWidth / 2 + v, centerY + matrixPixelHeigth / 2 + 20,
                            centerX + matrixPixelWidth / 2 + v - 10, centerY + matrixPixelHeigth / 2 + 20
                    );

                    ((Graphics2D) g).draw(cc2);
                }
            }
        }
    }

}
