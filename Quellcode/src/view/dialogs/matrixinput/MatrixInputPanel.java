package view.dialogs.matrixinput;

import view.onrun.showmathobject.*;
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
import utils.ArrayHelper;
import utils.math.Round;
import utils.Drawing;
import utils.measurements.Area;
import utils.measurements.NonArea;
import utils.structures.tuples.Pair;
import view.Constants;
import static view.Constants.*;
import view.sharedcomponents.scrollbar.*;

public class MatrixInputPanel extends JPanel {

    private MatrixBuilder matrix = null;

    private int mouseX = -1, mouseY = -1;

    private int[] latestPositionsColumStarts = new int[20];
    private int[] latestPositionsRowStarts = new int[20];

    private int selectedRow = -1, selectedColumn = -1;

    public MatrixInputPanel() {
    }

    public void setMatrixBuilder(MatrixBuilder matrix) {
        this.matrix = matrix;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        // Hintergrund zeichnen...
        g.setColor(Constants.DEFAULT_BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (matrix == null) {
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

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            Drawing.enableAntialiasing(g);
            g.setColor(Color.BLACK);

            int rows = matrix.getRowCount();
            int cols = matrix.getColumCount();

            if (rows == 0 || cols == 0) {
                String text = "[leere Matrix]";
                int textWidth = g.getFontMetrics().stringWidth(text);
                g.drawString(text, centerX - textWidth / 2, centerY + 5);

            } else {

                String[][] texts = new String[rows][cols];
                int[] widths = new int[cols];
                FontMetrics fontMetrics = g.getFontMetrics();

                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {
                        texts[r][c] = matrix.getPreparedValue(r, c);
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
                    latestPositionsColumStarts[c] = centerX - matrixPixelWidth / 2 + offX + 10;
                    for (int r = 0; r < rows; r++) {
                        latestPositionsRowStarts[r] = centerY - matrixPixelHeigth / 2 + (u * r) + 10; // Eigentlich nicht noetig es jedes mal neu zu berechnen, jedoch da nur simple Berechnungen zu vernachlaessigen
                        if (selectedColumn != -1 || selectedRow != -1) {
                            if (selectedColumn == c && selectedRow == r) {
                                g.setColor(Color.BLACK);
                            } else {
                                g.setColor(Color.GRAY);
                            }
                        }
                        g.drawString(texts[r][c], latestPositionsColumStarts[c], latestPositionsRowStarts[r]);
                    }
                    offX += widths[c] + v;
                }
                g.setColor(Color.BLACK);

                // [ ...
                g.drawLine(centerX - matrixPixelWidth / 2 - v, centerY - matrixPixelHeigth / 2 - 20, centerX - matrixPixelWidth / 2 - v, centerY + matrixPixelHeigth / 2 + 20);
                g.drawLine(centerX - matrixPixelWidth / 2 - v, centerY - matrixPixelHeigth / 2 - 20, centerX - matrixPixelWidth / 2 - v + 10, centerY - matrixPixelHeigth / 2 - 20);
                g.drawLine(centerX - matrixPixelWidth / 2 - v, centerY + matrixPixelHeigth / 2 + 20, centerX - matrixPixelWidth / 2 - v + 10, centerY + matrixPixelHeigth / 2 + 20);

                /// ] ...
                g.drawLine(centerX + matrixPixelWidth / 2 + v, centerY - matrixPixelHeigth / 2 - 20, centerX + matrixPixelWidth / 2 + v, centerY + matrixPixelHeigth / 2 + 20);
                g.drawLine(centerX + matrixPixelWidth / 2 + v, centerY - matrixPixelHeigth / 2 - 20, centerX + matrixPixelWidth / 2 + v - 10, centerY - matrixPixelHeigth / 2 - 20);
                g.drawLine(centerX + matrixPixelWidth / 2 + v, centerY + matrixPixelHeigth / 2 + 20, centerX + matrixPixelWidth / 2 + v - 10, centerY + matrixPixelHeigth / 2 + 20);
            }
        }
    }

    public Pair<Integer, Integer> select(int mouseX, int mouseY) {
        int column = -1;
        int row = -1;
        for (int c = 0; c < matrix.getColumCount(); c++) {
            if (latestPositionsColumStarts[c] - 10 > mouseX) {
                column = c - 1;
                break;
            }
        }
        if (column == -1) {
            column = matrix.getColumCount() - 1;
        }
        for (int r = 0; r < matrix.getRowCount(); r++) {
            if (latestPositionsRowStarts[r] + 10 > mouseY) {
                row = r;
                break;
            }
        }
        if (column < 0 || row < 0) {
            return null;
        } else {
            selectedRow = row;
            selectedColumn = column;
            return new Pair<>(row, column);
        }
    }

}
