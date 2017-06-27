package view.main;

import java.awt.Graphics2D;
import settings.GeneralSettings;
import static view.Constants.*;

public class TargetDrafter {

    public static void drawTarget(Graphics2D g, int xCenter, int yCenter, int targetX, int targetY, boolean pressed) {
        int targetSize;
        if (pressed) {
            targetSize = 10;
            g.setColor(TARGET_COLOR_PRESSED);
        } else {
            targetSize = 8;
            g.setColor(TARGET_COLOR);
        }
        g.setStroke(ONE_PX_LINE_STROKE);
        g.fillOval(xCenter - targetSize / 2, yCenter - targetSize / 2, targetSize, targetSize);
        if (pressed) {
            g.setColor(TARGET_BORDER_COLOR_PRESSED);
        } else {
            g.setColor(TARGET_BORDER_COLOR);
        }
        g.drawOval(xCenter - targetSize / 2, yCenter - targetSize / 2, targetSize, targetSize);

        // Beschriftung
        if (GeneralSettings.getInstance().getBoolean(GeneralSettings.DEVELOPER_TARGET_COORDINATES_KEY, false)) {
            g.setFont(TARGET_POSITION_FONT);
            String positionString = "(" + targetX + ", " + targetY + ")";
            int positionStringWidth = g.getFontMetrics().stringWidth(positionString);
            g.drawString(positionString, xCenter - positionStringWidth - 5, yCenter - 5);
        }
    }

}
