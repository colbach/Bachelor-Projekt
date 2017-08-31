package view.main.debug;

import java.awt.Color;
import java.awt.Graphics2D;
import static view.Constants.ADDITIONAL_DESCRIPTION_KEY_COLOR;
import static view.Constants.ADDITIONAL_DESCRIPTION_KEY_FONT;
import static view.Constants.ADDITIONAL_DESCRIPTION_LABEL_FONT;
import static view.Constants.ADDITIONAL_DESCRIPTION_LINE_HEIGHT;
import static view.Constants.ADDITIONAL_DESCRIPTION_PADDING_X;
import static view.Constants.ADDITIONAL_DESCRIPTION_PADDING_Y;
import static view.Constants.DEFAULT_FONT_COLOR;
import static view.Constants.DEFAULT_LINE_COLOR;
import static view.Constants.MESSAGE_BACKGROUND_COLOR;
import static view.Constants.ONE_PX_LINE_STROKE;
import static view.Constants.TOOLBAR_BORDER_LINE_STROKE;

public class DebugOverviewAdditionalColorInfoDrafter {

    public static void drawDebugOverviewAdditionalColorInfo(Graphics2D g, int xLeft, int yBottom) {

        int sampleRectSize = 10;

        // Abmasse berechnen...
        int maxDescriptionWidth = 20;
        for (int i = 0; i < DebugNodeColorsWithLabels.getCount(); i++) {
            String label = DebugNodeColorsWithLabels.getLabel(i);
            g.setFont(ADDITIONAL_DESCRIPTION_LABEL_FONT);
            int labelWidth = g.getFontMetrics().stringWidth(label);
            maxDescriptionWidth = Math.max(maxDescriptionWidth, sampleRectSize + 15 + labelWidth);
        }

        // Hintergrund zeichnen...
        g.setColor(MESSAGE_BACKGROUND_COLOR);
        g.fillRect(xLeft, yBottom - DebugNodeColorsWithLabels.getCount() * ADDITIONAL_DESCRIPTION_LINE_HEIGHT - 2 * ADDITIONAL_DESCRIPTION_PADDING_Y, maxDescriptionWidth + 2 * ADDITIONAL_DESCRIPTION_PADDING_X, DebugNodeColorsWithLabels.getCount() * ADDITIONAL_DESCRIPTION_LINE_HEIGHT + 2 * ADDITIONAL_DESCRIPTION_PADDING_Y);
        g.setColor(DEFAULT_LINE_COLOR);
        g.setStroke(TOOLBAR_BORDER_LINE_STROKE);
        g.drawRect(xLeft, yBottom - DebugNodeColorsWithLabels.getCount() * ADDITIONAL_DESCRIPTION_LINE_HEIGHT - 2 * ADDITIONAL_DESCRIPTION_PADDING_Y, maxDescriptionWidth + 2 * ADDITIONAL_DESCRIPTION_PADDING_X, DebugNodeColorsWithLabels.getCount() * ADDITIONAL_DESCRIPTION_LINE_HEIGHT + 2 * ADDITIONAL_DESCRIPTION_PADDING_Y);

        int offY = ADDITIONAL_DESCRIPTION_LINE_HEIGHT;
        for (int i = 0; i < DebugNodeColorsWithLabels.getCount(); i++) {

            Color color = DebugNodeColorsWithLabels.getColor(i);
            String label = DebugNodeColorsWithLabels.getLabel(i);

            // SampleRectSize zeichnen...
            g.setColor(color);
            g.fillRect(xLeft + 8, yBottom - DebugNodeColorsWithLabels.getCount() * ADDITIONAL_DESCRIPTION_LINE_HEIGHT - 2 * ADDITIONAL_DESCRIPTION_PADDING_Y + i * ADDITIONAL_DESCRIPTION_LINE_HEIGHT + 8, sampleRectSize, sampleRectSize);
            g.setColor(DEFAULT_FONT_COLOR);
            g.drawRect(xLeft + 8, yBottom - DebugNodeColorsWithLabels.getCount() * ADDITIONAL_DESCRIPTION_LINE_HEIGHT - 2 * ADDITIONAL_DESCRIPTION_PADDING_Y + i * ADDITIONAL_DESCRIPTION_LINE_HEIGHT + 8, sampleRectSize, sampleRectSize);
            g.drawString(label, xLeft + 8 + sampleRectSize + 8, yBottom - DebugNodeColorsWithLabels.getCount() * ADDITIONAL_DESCRIPTION_LINE_HEIGHT - 2 * ADDITIONAL_DESCRIPTION_PADDING_Y + i * ADDITIONAL_DESCRIPTION_LINE_HEIGHT + 17);
            /*int keyWidth = 0;
                int offBehindKey = 0;
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
             */
            // Label zeichnen...
            //g.setColor(DEFAULT_FONT_COLOR);
            //int labelWidth = g.getFontMetrics().stringWidth(label);
            //g.drawString(label, xRight - maxDescriptionWidth - ADDITIONAL_DESCRIPTION_PADDING_X + keyWidth + offBehindKey, yBottom - i * ADDITIONAL_DESCRIPTION_LINE_HEIGHT - ADDITIONAL_DESCRIPTION_PADDING_Y - 7 + 4);
            //offY += ADDITIONAL_DESCRIPTION_LINE_HEIGHT;
        }
    }
}
