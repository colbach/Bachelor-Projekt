package view.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;
import reflection.NodeDefinition;
import utils.Drawing;
import static view.Constants.*;
import static utils.Drawing.*;
import utils.structures.Quadrupel;
import utils.structures.Tuple;
import view.assets.ImageAsset;
import static view.nodecollection.NodeCollectionDrafter.calcNeededNodePreviewWidth;

public class ConnectionDrafter {

    public static void drawConnections(Graphics2D g, Node[] nodes, int offX, int offY) {

        for (Node node : nodes) {
            Set<Node> overOutletsConnectedNodes = node.getOverOutletsConnectedNodes();
            for (Node overOutletsConnectedNode : overOutletsConnectedNodes) {
                drawConnections(g, node, overOutletsConnectedNode, offX, offY);
            }
        }
    }

    public static void drawConnections(Graphics2D g, Node from, Node to, int offX, int offY) {

        Outlet[] fromOutlets = from.getOutlets();
        Inlet[] toInlets = to.getInlets();
        int connectedFromOutlets = from.getConnectedOutletCount();
        int connectedToInlets = to.getConnectedInletCount();
        int fromWidth = NodeDrafter.calcNeededNodeWidth(g, from);
        int toWidth = NodeDrafter.calcNeededNodeWidth(g, to);

        int position1 = 0;
        for (int i1 = 0; i1 < fromOutlets.length; i1++) {
            if (fromOutlets[i1].isConnected()) {
                int position2 = 0;
                for (int i2 = 0; i2 < toInlets.length; i2++) {
                    if (toInlets[i2].isConnected()) {
                        Outlet fromOutlet = fromOutlets[i1];
                        Inlet toInlet = toInlets[i2];
                        if (fromOutlet.isConnectedTo(toInlet)) {
                            int fromX = NodeDrafter.calcCenterXForNode(g, from) + fromWidth / 2 + 2;
                            int fromY = NodeDrafter.calcCenterYForNode(g, from) - ((connectedFromOutlets - 1) * NODE_Y_DISTANCE_BETWEEN_LETS) / 2 + position1 * NODE_Y_DISTANCE_BETWEEN_LETS + 1;
                            String fromName = fromOutlets[i1].getName();
                            int toX = NodeDrafter.calcCenterXForNode(g, to) - toWidth / 2 - 2;
                            int toY = NodeDrafter.calcCenterYForNode(g, to) - ((connectedToInlets - 1) * NODE_Y_DISTANCE_BETWEEN_LETS) / 2 + position2 * NODE_Y_DISTANCE_BETWEEN_LETS + 1;
                            String toName = toInlets[i2].getName();

                            drawConnection(g, fromX, fromY, fromName, toX, toY, toName, offX, offY);
                        }
                        position2++;
                    }
                }
                position1++;
            }
        }

    }

    public static void drawConnection(Graphics2D g, int fromX, int fromY, String fromName, int toX, int toY, String toName, int offX, int offY) {
        if (fromName == null) {
            fromName = "";
        }
        if (toName == null) {
            toName = "";
        }

        // Text zeichnen...
        g.setFont(NODE_CONNECTION_NAME_FONT);
        g.setColor(DEFAULT_LINE_COLOR);
        Drawing.enableAntialiasing(g);
        int fromNameWidth = g.getFontMetrics().stringWidth(fromName);
        int toNameWidth = g.getFontMetrics().stringWidth(toName);
        g.drawString(fromName, fromX + 5 + offX, fromY - 2 + offY);
        g.drawString(toName, toX - 5 - toNameWidth + offX - 5, toY - 2 + offY);

        // Pfeil zeichnen...
        g.setColor(DEFAULT_LINE_COLOR);
        g.setStroke(UNSELECTED_CONNECTIONS_LINE_STROKE);
        enableAntialiasing(g);

        if (fromY == toY) { // Fall: Linie gerade
            g.drawLine(fromX + offX, fromY + offY, toX + offX - 5, toY + offY);

        } else { // Normalfall

            int fromStartLength = Math.max(fromNameWidth - 2, CONNECTION_ARROW_START_END_LENGTH);
            int toEndLength = Math.max(toNameWidth - 2, CONNECTION_ARROW_START_END_LENGTH);
            int fromXStar = fromX + fromStartLength;
            int toXStar = toX - toEndLength - 5;
            g.drawLine(fromX + offX, fromY + offY, fromXStar + offX, fromY + offY); // Anfang
            //g.drawLine(fromXStar + offX, fromY + offY, toXStar + offX, toY + offY); // Gerade Linie
            g.drawLine(toXStar + offX, toY + offY, toX + offX - 3, toY + offY); // Ende
            CubicCurve2D.Double cc = new CubicCurve2D.Double(fromXStar + offX, fromY + offY, fromXStar + offX + 100, fromY + offY, toXStar + offX - 100, toY + offY, toXStar + offX, toY + offY);
            g.draw(cc); // Bersier Kurve zeichnen
        }

        g.fillPolygon( // Pfeil zeichnen
                new int[]{toX + 1 + offX, toX + 1 - 5 + offX, toX + 1 - 5 + offX},
                new int[]{toY + offY, toY - 5 + offY, toY + 5 + offY},
                3);

    }
}
