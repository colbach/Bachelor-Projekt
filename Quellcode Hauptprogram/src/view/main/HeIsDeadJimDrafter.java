package view.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import utils.Drawing;
import utils.math.RandomHelper;

public class HeIsDeadJimDrafter {

    private final static int U = 30;
    private static boolean oval = Math.random() > 0.9;
    private final static int MIN = 0;
    private final static int MAX = U / 2;
    private static int m1 = RandomHelper.randomNumber(MIN, MAX);
    private static int m2 = RandomHelper.randomNumber(MIN, MAX);

    private static long lastCalc = 0;

    public static void draw(Graphics2D g, int centerX, int centerY) {

        Drawing.enableAntialiasing(g);
        g.setStroke(new BasicStroke(1.2f));

        g.setColor(Color.WHITE);
        g.fillRoundRect(centerX - 3 * U, centerY - 3 * U, 6 * U, 5 * U, 2 * U, 2 * U);

        g.setColor(Color.BLACK);
        g.drawRoundRect(centerX - 3 * U, centerY - 3 * U, 6 * U, 5 * U, 2 * U, 2 * U);

        g.drawLine(centerX - 2 * U, centerY - 2 * U, centerX - U, centerY - U);
        g.drawLine(centerX - U, centerY - 2 * U, centerX - 2 * U, centerY - U);

        g.drawLine(centerX + 2 * U, centerY - 2 * U, centerX + U, centerY - U);
        g.drawLine(centerX + U, centerY - 2 * U, centerX + 2 * U, centerY - U);

        if (System.currentTimeMillis() - lastCalc > 100) {
            oval = Math.random() > 0.9;
            m1 = RandomHelper.randomNumber(MIN, MAX);
            m2 = RandomHelper.randomNumber(MIN, MAX);
            lastCalc = System.currentTimeMillis();
        }

        if (oval) {
            g.fillOval(centerX - U, centerY, (int) (1.5 * U), U);

        } else {
            g.drawLine(centerX - 2 * U, centerY + m1 + U / 2, centerX + 2 * U, centerY + m2);
        }

    }

}
