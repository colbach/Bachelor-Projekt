package view.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import utils.Drawing;
import utils.math.RandomHelper;

public class HeIsDeadJimDrafter {

    public static void draw(Graphics2D g, int centerX, int centerY) {

        int u = 30;

        Drawing.enableAntialiasing(g);
        g.setStroke(new BasicStroke(1.2f));
        
        g.setColor(Color.WHITE);
        g.fillRoundRect(centerX - 3 * u, centerY - 3 * u, 6 * u, 5 * u, 2 * u, 2 * u);

        g.setColor(Color.BLACK);
        g.drawRoundRect(centerX - 3 * u, centerY - 3 * u, 6 * u, 5 * u, 2 * u, 2 * u);
        
        g.drawLine(centerX - 2 * u, centerY - 2 * u, centerX - u, centerY - u);
        g.drawLine(centerX - u, centerY - 2 * u, centerX - 2 * u, centerY - u);
        
        g.drawLine(centerX + 2 * u, centerY - 2 * u, centerX + u, centerY - u);
        g.drawLine(centerX + u, centerY - 2 * u, centerX + 2 * u, centerY - u);
        
        
        
        
        int min = 0;
        int max = u/2;
        
        int m1 = RandomHelper.randomNumber(min, max);
        int m2 = RandomHelper.randomNumber(min, max);
        
        g.drawLine(centerX - 2 * u, centerY + m1 + u/2, centerX + 2 * u, centerY + m2);

    }

}
