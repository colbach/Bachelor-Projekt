package view.main;

import java.awt.Graphics2D;
import model.Node;
import static view.Constants.*;
import static utils.Drawing.*;

public class NodeTagDrafter {

    public static void drawNodeTag(Graphics2D g, Node node, String tag, int offX, int offY, int drawWidth, int drawHeight) {
        if (node != null) {

            int nodeWidth = NodeDrafter.calcNeededNodeWidth(g, node);
            int nodeHeight = NodeDrafter.calcNeededNodeHeight(g, node);
            int nodeCenterX = NodeDrafter.calcCenterXForNode(g, node) + offX;
            int nodeCenterY = NodeDrafter.calcCenterYForNode(g, node) + offY;

            // Poligon zeichnen...
            g.setFont(NODE_TAG_FONT);
            int tagWidth = g.getFontMetrics().stringWidth(tag);
            int width = tagWidth + 14;
            int height = 25;
            g.setColor(NODE_BACKGROUND_COLOR);
            g.setStroke(NODE_BORDERS_LINE_STROKE);
            g.fillRoundRect(nodeCenterX-width/2, nodeCenterY+nodeHeight/2-5, width, height, 15, 15);
            g.setColor(NODE_BORDER_COLOR);
            g.drawRoundRect(nodeCenterX-width/2, nodeCenterY+nodeHeight/2-5, width, height, 15, 15);
            
            // String zeichnen...
            g.setColor(NODE_LABEL_COLOR);
            g.drawString(tag, nodeCenterX - tagWidth / 2 - 1, nodeCenterY + nodeHeight / 2 + height - 10);
        }
    }

}
