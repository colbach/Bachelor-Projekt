package view.onrun.shownumberarray;

import view.sharedcomponents.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import javax.swing.JPanel;
import logging.AdditionalErr;
import logging.AdditionalLogger;
import logging.AdvancedLogger;
import settings.GeneralSettings;
import utils.math.Round;
import utils.Drawing;
import utils.measurements.Area;
import utils.measurements.NonArea;
import view.Constants;
import static view.Constants.*;
import view.sharedcomponents.scrollbar.*;

public class ShowNumberArrayPanel extends JPanel {

    private Number[] numbers = null;
    private double minimum = Double.NaN;
    private double maximum = Double.NaN;
    private double[] numbersNormalized0To1;

    private int mouseX = -1, mouseY = -1;

    private GeneralSettings settings = GeneralSettings.getInstance();

    private boolean curve = false;

    private static DecimalFormat format = new DecimalFormat("#0.0");

    public ShowNumberArrayPanel() {
    }
    
    public void releaseMemory() {
        numbers = null;
        numbersNormalized0To1 = null;
    }

    public void setNumbers(Number[] numbers) {
        this.numbers = numbers;

        // Minimum, Maximum berechnen...
        minimum = Double.MAX_VALUE;
        maximum = Double.MIN_VALUE;
        for (Number n : numbers) {
            if (n.doubleValue() < minimum) {
                minimum = n.doubleValue();
            }
            if (n.doubleValue() > maximum) {
                maximum = n.doubleValue();
            }
        }
        if (minimum == Double.MAX_VALUE && maximum == Double.MIN_VALUE) {
            minimum = new Double(Double.MAX_VALUE);
            maximum = new Double(Double.MIN_VALUE);
        }

        // Werte normalisieren von 0 bis 1...
        numbersNormalized0To1 = new double[this.numbers.length];
        double div = maximum - minimum;
        double[] valuesNormalized0To1 = new double[numbers.length];
        for (int i = 0; i < valuesNormalized0To1.length; i++) {
            double v = numbers[i].doubleValue() - minimum;
            numbersNormalized0To1[i] = v / div;
        }

    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        // Hintergrund zeichnen...
        g.setColor(Constants.DEFAULT_BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (numbers == null) {
            AdditionalLogger.err.println("Logger ist null. Keine Daten zum anzeigen.");

            // Warnung
            Drawing.enableAntialiasing(g);
            g.setColor(Color.BLACK);
            String text = "Keine Daten zur Anzeige vorhanden";
            int textWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 + 5);

        } else {
            
            g.setColor(Color.WHITE);

            int offX = 60;
            int offY = 30;
            int width = getWidth() - 100;
            int height = getHeight() - 50;
            g.fillRect(offX, offY, width, height);
            
            Drawing.enableAntialiasing(g);
            
            g.setColor(Color.BLACK);

            g.setFont(SHOW_VALUE_ARRAY_FONT);
            g.drawString(numbers.length + " Werte in einem Bereich von " + format.format(minimum) + " bis " + format.format(maximum) + "", 2, 18);

            double steps = (double) width / numbersNormalized0To1.length;

            int lastX = -1;
            int lastSize = -1;
            for (int i = 0; i < numbersNormalized0To1.length; i++) {
                int x = (int) (steps * i) + offX;
                int size = (int) (height * numbersNormalized0To1[i]);

                if (curve) {
                    g.fillRect(x, /*height+offY*/ offY + height - size, Round.roundUp(steps), size);
                } else {
                    if (lastX != -1 && lastSize != -1) {
                        g.drawLine(lastX + Round.roundUp(steps / 2), height - lastSize + offY, x + Round.roundUp(steps / 2), height - size + offY);
                    }
                }

                lastX = x;
                lastSize = size;
            }

            g.drawLine(0, offY, offX, offY);
            g.drawLine(0, offY + height, offX, offY + height);

            g.setFont(SHOW_VALUE_ARRAY_FONT);
            g.drawString(format.format(maximum), 2, offY + 15);
            g.drawString(format.format(minimum), 2, offY + height - 5);

            if (mouseX > offX && mouseX < offX + width && mouseY > offY && mouseY < offY + height) {
                g.setColor(Color.BLACK);
                g.drawLine(0, mouseY, getWidth(), mouseY);
                g.drawLine(mouseX, 0, mouseX, getHeight());

                int diaX = mouseX - offX;
                int diaY = mouseY - offY;
                double proX = diaX / (double) width;
                double proY = (height - diaY) / (double) height;

                double div = maximum - minimum;
                double vY = proY * div;
                double vX = proX * numbersNormalized0To1.length;

                if (diaY > height / 2) {
                    g.drawString(format.format(vY), 2, mouseY - 3);
                } else {
                    g.drawString(format.format(vY), 2, mouseY + 16);
                }

                g.drawString("" + Round.roundUp(vX), mouseX + 2, offY + height + 15);
            }
        }

    }

    public void setCurve(boolean curve) {
        this.curve = curve;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

}
