package view.main;

import java.awt.Graphics2D;
import utils.text.TextHandler;
import static view.Constants.*;

public class MessageDrafter {

    public static void drawMessage(Graphics2D g, String message, int centerX, int bottomY, int width, boolean isError) {

        g.setFont(MESSAGE_FONT);
        String[] lines = TextHandler.splitStringToLines(g, message, width - MESSAGE_PADDING_Y * 2);
        int height = lines.length * MESSAGE_LINE_HEIGHT + 2 * MESSAGE_PADDING_Y;

        if (isError) {
            g.setColor(MESSAGE_ERROR_BACKGROUND_COLOR);
        } else {
            g.setColor(MESSAGE_BACKGROUND_COLOR);
        }
        g.fillRect(centerX - width / 2, bottomY - height, width, height + 2);

        g.setColor(DEFAULT_LINE_COLOR);
        g.setStroke(TOOLBAR_BORDER_LINE_STROKE);
        g.drawRect(centerX - width / 2, bottomY - height, width, height + 2);

        g.setColor(DEFAULT_FONT_COLOR);
        int offY = MESSAGE_LINE_HEIGHT;
        for (String line : lines) {
            int lineWidth = g.getFontMetrics().stringWidth(line);
            g.drawString(line, centerX - lineWidth / 2, bottomY - height + MESSAGE_PADDING_X + offY + 3);
            offY += MESSAGE_LINE_HEIGHT;
        }

    }

}
