package view.main.additionalhelp;

import java.awt.Graphics2D;
import static view.Constants.*;

public class AdditionalHelpDrafter {

    public static void drawAdditionalHelp(Graphics2D g, int xRight, int yBottom, AdditionalHelpDescription description) {

        if (!description.isEmpty()) {
            // Abmasse berechnen...
            int maxDescriptionWidth = 20;
            for (int i = 0; i < description.getCount(); i++) {
                String key = description.getKey(i);
                String label = description.getLabel(i);
                g.setFont(ADDITIONAL_DESCRIPTION_KEY_FONT);
                int keyWidth = g.getFontMetrics().stringWidth(key);
                g.setFont(ADDITIONAL_DESCRIPTION_LABEL_FONT);
                int labelWidth = g.getFontMetrics().stringWidth(label);
                maxDescriptionWidth = Math.max(maxDescriptionWidth, keyWidth + 8 + labelWidth);
            }

            // Hintergrund zeichnen...
            g.setColor(MESSAGE_BACKGROUND_COLOR);
            g.fillRect(xRight - maxDescriptionWidth - 2 * ADDITIONAL_DESCRIPTION_PADDING_X, yBottom - description.getCount() * ADDITIONAL_DESCRIPTION_LINE_HEIGHT - 2 * ADDITIONAL_DESCRIPTION_PADDING_Y, maxDescriptionWidth + 2 * ADDITIONAL_DESCRIPTION_PADDING_X, description.getCount() * ADDITIONAL_DESCRIPTION_LINE_HEIGHT + 2 * ADDITIONAL_DESCRIPTION_PADDING_Y);
            g.setColor(DEFAULT_LINE_COLOR);
            g.setStroke(TOOLBAR_BORDER_LINE_STROKE);
            g.drawRect(xRight - maxDescriptionWidth - 2 * ADDITIONAL_DESCRIPTION_PADDING_X, yBottom - description.getCount() * ADDITIONAL_DESCRIPTION_LINE_HEIGHT - 2 * ADDITIONAL_DESCRIPTION_PADDING_Y, maxDescriptionWidth + 2 * ADDITIONAL_DESCRIPTION_PADDING_X, description.getCount() * ADDITIONAL_DESCRIPTION_LINE_HEIGHT + 2 * ADDITIONAL_DESCRIPTION_PADDING_Y);

            int offY = ADDITIONAL_DESCRIPTION_LINE_HEIGHT;
            for (int i = 0; i < description.getCount(); i++) {

                String key = description.getKey(i);
                String label = description.getLabel(i);

                // Key zeichnen...
                int keyWidth = 0;
                int offBehindKey = 0;
                if (key.length() > 0) {
                    g.setFont(ADDITIONAL_DESCRIPTION_KEY_FONT);
                    keyWidth = g.getFontMetrics().stringWidth(key);
                    offBehindKey = 6;
                    g.setColor(ADDITIONAL_DESCRIPTION_KEY_COLOR);
                    g.setStroke(ONE_PX_LINE_STROKE);
                    g.fillRoundRect(xRight - maxDescriptionWidth - ADDITIONAL_DESCRIPTION_PADDING_X, yBottom - i * ADDITIONAL_DESCRIPTION_LINE_HEIGHT - ADDITIONAL_DESCRIPTION_PADDING_Y - 17 + 4, keyWidth + 2, 12, 5, 5);
                    g.setColor(MESSAGE_BACKGROUND_COLOR);
                    g.drawString(key, xRight - maxDescriptionWidth - ADDITIONAL_DESCRIPTION_PADDING_X + 1, yBottom - i * ADDITIONAL_DESCRIPTION_LINE_HEIGHT - ADDITIONAL_DESCRIPTION_PADDING_Y - 7 + 4);
                    g.setColor(DEFAULT_LINE_COLOR);
                    g.setFont(ADDITIONAL_DESCRIPTION_LABEL_FONT);
                }

                // Label zeichnen...
                g.setColor(DEFAULT_FONT_COLOR);
                int labelWidth = g.getFontMetrics().stringWidth(label);
                g.drawString(label, xRight - maxDescriptionWidth - ADDITIONAL_DESCRIPTION_PADDING_X + keyWidth + offBehindKey, yBottom - i * ADDITIONAL_DESCRIPTION_LINE_HEIGHT - ADDITIONAL_DESCRIPTION_PADDING_Y - 7 + 4);
                offY += ADDITIONAL_DESCRIPTION_LINE_HEIGHT;
            }
        }

    }

}
