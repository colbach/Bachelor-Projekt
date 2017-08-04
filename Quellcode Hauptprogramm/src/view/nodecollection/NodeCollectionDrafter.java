package view.nodecollection;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.io.IOException;
import java.util.ArrayList;
import model.Node;
import model.resourceloading.NodeDefinitionDescription;
import model.runproject.debugger.ExecutorState;
import reflection.nodedefinitions.specialnodes.SpecialNodeDefinition;
import reflection.nodedefinitions.specialnodes.firstvalues.AValueNodeDefinition;
import reflection.nodedefinitions.specialnodes.firstvalues.FastestValueNodeDefinition;
import reflection.nodedefinitions.specialnodes.firstvalues.FirstValueNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ForEachNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ForNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ReduceNodeDefinition;
import reflection.nodedefinitions.specialnodes.ifs.IfBackNodeDefinition;
import reflection.nodedefinitions.specialnodes.ifs.IfForwardNodeDefinition;
import model.type.Type;
import reflection.ContextCreator;
import reflection.NodeDefinition;
import reflection.additionalnodedefinitioninterfaces.Experimental;
import utils.Drawing;
import static utils.Drawing.disableAntialiasing;
import static utils.Drawing.enableAntialiasing;
import utils.text.TextHandler;
import view.assets.ImageAsset;
import static view.Constants.*;
import view.main.NodeDrafter;

public class NodeCollectionDrafter {

    public static void drawInfoSection(Graphics2D g, int x, int y, int width, int height, NodeDefinition node) {

        // Hintergrund zeichnen...
        g.setColor(NODE_INFO_BACKGROUND_COLOR);
        g.fillRect(x, y, width, height);

        // Icon zeichnen...
        ImageAsset icon = null;
        String iconName = node.getIconName();
        if (iconName != null) { // Wenn eine iconName existiert
            try { // Versuche ImageAsset zu laden
                icon = ImageAsset.getImageAssetForName(iconName);
            } catch (IOException ex) {
                System.err.println(iconName + " konnte nicht geladen werden! Versuche Default.");
            }
        }
        if (icon == null) { // Laden hat nicht funktioniert oder iconName existiert nicht
            try {
                icon = ImageAsset.getImageAssetForName(DEFAULT_NODE_ICON_ASSET_NAME);
            } catch (IOException ex) {
                System.err.println("Default-Icon " + DEFAULT_NODE_ICON_ASSET_NAME + " konnte nicht geladen werden!");
            }
        }
        if (icon != null) { // Kein else da Zustand innerhalb erstem if aendern kann
            icon.drawCentered(g, x + 30, y + height / 2);
        }

        // Infotext in Zeilen gliedern...
        g.setColor(DEFAULT_FONT_COLOR);
        g.setFont(INFO_LABEL_FONT);
        String[] infoLines = TextHandler.splitStringToLines(g, NodeDefinitionDescription.getDescriptionWithoutTags(node), width - 75);

        // In-Moeglichkeiten in Zeilen gliedern...
        final String IN_TITLE = "Eingänge:";
        String inString = IN_TITLE;
        {
            if (node.getInletCount() > 0) {
                boolean first = true;
                for (int i = 0; i < node.getInletCount(); i++) {
                    Type type = new Type(node.getClassForInlet(i), node.isInletForArray(i));
                    String inletDescription = node.getNameForInlet(i) + " (" + type.toString() + ")";
                    if (first) {
                        inString += " " + inletDescription;
                    } else {
                        inString += ", " + inletDescription;
                    }
                    first = false;
                }
            } else {
                inString += " /";
            }
        }
        g.setColor(DEFAULT_FONT_COLOR);
        g.setFont(INFO_IN_OUT_CAPABILITIES_FONT);
        String[] inLines = TextHandler.splitStringToLines(g, inString, width - 75);

        // Out-Moeglichkeiten in Zeilen gliedern...
        final String OUT_TITLE = "Ausgänge:";
        String outString = OUT_TITLE;
        {
            if (node.getOutletCount() > 0) {
                boolean first = true;
                for (int i = 0; i < node.getOutletCount(); i++) {
                    Type type = new Type(node.getClassForOutlet(i), node.isOutletForArray(i));
                    String inletDescription = node.getNameForOutlet(i) + " (" + type.toString() + ")";
                    if (first) {
                        outString += " " + inletDescription;
                    } else {
                        outString += ", " + inletDescription;
                    }
                    first = false;
                }
            } else {
                outString += " /";
            }
        }
        g.setColor(DEFAULT_FONT_COLOR);
        g.setFont(INFO_IN_OUT_CAPABILITIES_FONT);
        String[] outLines = TextHandler.splitStringToLines(g, outString, width - 75);

        // Start Y berechnen...
        final int SPACE = 20;
        final int START_OFF_X = 60;
        int offY = height / 2 - ((infoLines.length + inLines.length + outLines.length) * SPACE) / 2 + SPACE / 2;

        // Infotext zeichnen...
        int h = infoLines.length * SPACE;
        g.setColor(DEFAULT_FONT_COLOR);
        g.setFont(INFO_LABEL_FONT);
        for (String line : infoLines) {
            g.drawString(line, x + START_OFF_X, y + offY);
            offY += SPACE;
        }

        // In-Moeglichkeiten zeichnen...
        g.setFont(INFO_IN_OUT_TITLE_FONT);
        g.drawString(IN_TITLE, x + START_OFF_X, y + offY);
        int inTitleWidth = g.getFontMetrics().stringWidth(IN_TITLE);
        g.setFont(INFO_IN_OUT_CAPABILITIES_FONT);
        g.drawString(inLines[0].substring(IN_TITLE.length()), x + START_OFF_X + inTitleWidth, y + offY);
        offY += SPACE;
        for (int i = 1; i < inLines.length; i++) {
            g.drawString(inLines[i], x + START_OFF_X, y + offY);
            offY += SPACE;
        }

        // Out-Moeglichkeiten zeichnen...
        g.setFont(INFO_IN_OUT_TITLE_FONT);
        g.drawString(OUT_TITLE, x + START_OFF_X, y + offY);
        int outTitleWidth = g.getFontMetrics().stringWidth(OUT_TITLE);
        g.setFont(INFO_IN_OUT_CAPABILITIES_FONT);
        g.drawString(outLines[0].substring(OUT_TITLE.length()), x + START_OFF_X + outTitleWidth, y + offY);
        offY += SPACE;
        for (int i = 1; i < outLines.length; i++) {
            g.drawString(outLines[i], x + START_OFF_X, y + offY);
            offY += SPACE;
        }
    }

    /**
     * Gibt die Breite einer zu Zeischnenden NodePreview zurueck.
     */
    public static int calcNeededNodePreviewWidth(Graphics2D g, NodeDefinition node) {

        if (node instanceof SpecialNodeDefinition) { // Spezielle Nodes
            return 50;

        } else { // andere Node

            // Breite von Label ermitteln...
            Drawing.enableAntialiasing(g);
            g.setFont(NODE_LABEL_FONT);
            int labelWidth = g.getFontMetrics().stringWidth(node.getName());

            return Math.max(labelWidth, 40) + 25;
        }
    }

    /**
     * Zeichnet Vorschau von Node.
     */
    public static void drawNodePreview(Graphics2D g, int x, int y, NodeDefinition node, boolean highlighted, boolean focusMode) {

        // Breite ermitteln...
        int nodePreviewWidth = calcNeededNodePreviewWidth(g, node);

        // Hintergrund zeichenen...
        Drawing.enableAntialiasing(g);

        if (node instanceof IfForwardNodeDefinition || node instanceof IfBackNodeDefinition) { // If-Node
            drawIfNodePreview(g, node, x + nodePreviewWidth / 2, y + MINIMUM_NODE_HEIGHT / 2, nodePreviewWidth - 2, MINIMUM_NODE_HEIGHT - 4, highlighted, focusMode);

        } else if (node instanceof ForNodeDefinition) { // If-Node
            drawForNodePreview(g, node, x + nodePreviewWidth / 2, y + MINIMUM_NODE_HEIGHT / 2, nodePreviewWidth - 2, MINIMUM_NODE_HEIGHT - 4, highlighted, focusMode);

        } else if (node instanceof FirstValueNodeDefinition) { // FirstValue-Node
            drawFirstValueNodePreview(g, node, x + nodePreviewWidth / 2, y + MINIMUM_NODE_HEIGHT / 2, nodePreviewWidth - 2, MINIMUM_NODE_HEIGHT - 4, highlighted, focusMode);

        } else {
            if (highlighted) {
                g.setColor(HIGHLIGHTED_NODE_BACKGROUND_COLOR);
            } else {
                if (focusMode) {
                    g.setColor(NODE_BACKGROUND_COLOR_WHEN_FOCUSING);
                } else {
                    g.setColor(NODE_BACKGROUND_COLOR);
                }
            }

            if (node instanceof ContextCreator) {
                g.fillRect(x + 2, y + 2, nodePreviewWidth - 4, MINIMUM_NODE_HEIGHT - 4);
            } else {
                g.fillRoundRect(x, y, nodePreviewWidth, MINIMUM_NODE_HEIGHT, NODE_EDGE_ROUNDING, NODE_EDGE_ROUNDING);
            }
            g.setColor(NODE_BORDER_COLOR);
            if (highlighted) {
                g.setStroke(HIGHLIGHTED_NODE_BORDERS_LINE_STROKE);
            } else {
                g.setStroke(NODE_BORDERS_LINE_STROKE);
            }
            if (node instanceof ContextCreator) {
                g.drawRect(x + 2, y + 2, nodePreviewWidth - 4, MINIMUM_NODE_HEIGHT - 4);
            } else {
                g.drawRoundRect(x, y, nodePreviewWidth, MINIMUM_NODE_HEIGHT, NODE_EDGE_ROUNDING, NODE_EDGE_ROUNDING);
            }

            // Text zeichnen...
            g.setColor(NODE_LABEL_COLOR);
            g.setFont(NODE_LABEL_FONT);
            int labelWidth = g.getFontMetrics().stringWidth(node.getName());
            g.drawString(node.getName(), x + nodePreviewWidth / 2 - labelWidth / 2, y + MINIMUM_NODE_HEIGHT - 12);

            // Icon zeichnen...
            ImageAsset icon = null;
            String iconName = node.getIconName();
            if (iconName != null) { // Wenn eine iconName existiert
                try { // Versuche ImageAsset zu laden
                    icon = ImageAsset.getImageAssetForName(iconName);
                } catch (IOException ex) {
                    System.err.println(iconName + " konnte nicht geladen werden! Versuche Default.");
                }
            }
            if (icon == null) { // Laden hat nicht funktioniert oder iconName existiert nicht
                try {
                    icon = ImageAsset.getImageAssetForName(DEFAULT_NODE_ICON_ASSET_NAME);
                } catch (IOException ex) {
                    System.err.println("Default-Icon " + DEFAULT_NODE_ICON_ASSET_NAME + " konnte nicht geladen werden!");
                }
            }
            if (icon != null) { // Kein else da Zustand innerhalb erstem if aendern kann
                int centerYIcon = y + MINIMUM_NODE_HEIGHT / 2 - 10;
                if (node instanceof Experimental) {
                    centerYIcon += 5;
                }
                icon.drawCentered(g, x + nodePreviewWidth / 2, centerYIcon);
            }

            // Experimental-Label zeichen...
            if (node instanceof Experimental) {
                try {
                    ImageAsset experimentalLabelImageAsset = ImageAsset.getImageAssetForName("ui/Experimental-Label.png");
                    experimentalLabelImageAsset.drawCentered(g, x + nodePreviewWidth / 2, y + MINIMUM_NODE_HEIGHT / 2 - 27);

                } catch (IOException ex) {
                    System.err.println("Experimental-Label konnte nicht geladen werden.");
                }
            }
        }
    }

    /**
     * Zeichnet Vorschau von Nodes.
     */
    public static void drawNodesPreview(Graphics2D g, int x, int y, int drawWidth, int drawHeight, NodeDefinition[] nodes, NodeDefinition highlightedDefinition) {

        int highlightedDefinitionX = -1, highlightedDefinitionY = -1; // Position von hervorgehobenen Definition um diese spaeter zu zeichnen

        // Berechnen Position und Zeichnen der einzelnen Nodes...
        int offX = SPACE_BETWEEN_NODE_PREVIEWS;
        int offY = SPACE_BETWEEN_NODE_PREVIEWS;
        
        for (NodeDefinition node : nodes) { // fuer alle Nodes
            int neededNodePreviewWidth = calcNeededNodePreviewWidth(g, node);
            
            if (offX + neededNodePreviewWidth + SPACE_BETWEEN_NODE_PREVIEWS - 1 > drawWidth) { // Wenn Nodes ueber Zeile hinausgehen
                offY += MINIMUM_NODE_HEIGHT + SPACE_BETWEEN_NODE_PREVIEWS;
                offX = SPACE_BETWEEN_NODE_PREVIEWS;
            }
            if (highlightedDefinition == node) {
                highlightedDefinitionX = x + offX;
                highlightedDefinitionY = y + offY;
            }

            if (y + offY + MINIMUM_NODE_HEIGHT >= 0 && y + offY <= drawHeight) { // Wenn im sichtbaren Bereich
                drawNodePreview(g, x + offX, y + offY, node, highlightedDefinition == node, highlightedDefinition != null);
            }
            offX += neededNodePreviewWidth + SPACE_BETWEEN_NODE_PREVIEWS;
        }
    }

    public static void drawIfNodePreview(Graphics2D g, NodeDefinition definition, int centerX, int centerY, int nodeWidth, int nodeHeigth, boolean highlighted, boolean focusMode) {

        Drawing.enableAntialiasing(g);

        // Hintergrund zeichnen...
        if (highlighted) {
            g.setColor(HIGHLIGHTED_NODE_BACKGROUND_COLOR);
        } else {
            if (focusMode) {
                g.setColor(NODE_BACKGROUND_COLOR_WHEN_FOCUSING);
            } else {
                g.setColor(NODE_BACKGROUND_COLOR);
            }
        }

        int[] xs = new int[]{
            centerX - nodeWidth / 2 - 1,
            centerX,
            centerX + nodeWidth / 2 + 1,
            centerX + nodeWidth / 2 + 1,
            centerX,
            centerX - nodeWidth / 2 - 1
        };
        int[] ys = new int[]{
            centerY - nodeHeigth / 2 + 5,
            centerY - nodeHeigth / 2 - 2,
            centerY - nodeHeigth / 2 + 5,
            centerY + nodeHeigth / 2 - 5,
            centerY + nodeHeigth / 2 + 2,
            centerY + nodeHeigth / 2 - 5
        };
        g.fillPolygon(xs, ys, xs.length);
        g.setStroke(ONE_POINT_FIVE_PX_LINE_STROKE);
        g.setColor(NODE_BORDER_COLOR);
        if (highlighted) {
            g.setStroke(HIGHLIGHTED_NODE_BORDERS_LINE_STROKE);
        } else {
            g.setStroke(NODE_BORDERS_LINE_STROKE);
        }
        g.drawPolygon(xs, ys, xs.length);

        // Front zeichnen...
        if (definition instanceof IfForwardNodeDefinition) {
            g.setColor(DEFAULT_LINE_COLOR);
            CubicCurve2D.Double c1 = new CubicCurve2D.Double(centerX - 18, centerY, centerX - 10, centerY, centerX + 4, centerY, centerX + 15, centerY - 15);
            g.draw(c1);
            CubicCurve2D.Double c2 = new CubicCurve2D.Double(centerX - 18, centerY, centerX - 10, centerY, centerX + 4, centerY, centerX + 15, centerY + 15);
            g.draw(c2);
            g.fillPolygon(new int[]{centerX + 18, centerX + 18, centerX + 10}, new int[]{centerY - 18, centerY - 10, centerY - 18}, 3);
            g.fillPolygon(new int[]{centerX + 18, centerX + 18, centerX + 10}, new int[]{centerY + 18, centerY + 10, centerY + 18}, 3);
            g.setFont(new Font("Arial", Font.ITALIC, 20));
            g.drawString("?", centerX - 10, centerY - 5);

        } else if (definition instanceof IfBackNodeDefinition) {
            g.setColor(DEFAULT_LINE_COLOR);
            CubicCurve2D.Double c1 = new CubicCurve2D.Double(centerX - 18, centerY - 15, centerX - 10, centerY, centerX + 4, centerY, centerX + 15, centerY);
            g.draw(c1);
            CubicCurve2D.Double c2 = new CubicCurve2D.Double(centerX - 18, centerY + 15, centerX - 10, centerY, centerX + 4, centerY, centerX + 15, centerY);
            g.draw(c2);
            g.fillPolygon(new int[]{centerX + 18, centerX + 12, centerX + 12}, new int[]{centerY, centerY - 6, centerY + 6}, 3);
            g.setFont(new Font("Arial", Font.ITALIC, 20));
            g.drawString("?", centerX - 8, centerY - 6);

        } else {
            System.err.println("node ist kein IfForwardNode!");
        }
    }

    public static void drawForNodePreview(Graphics2D g, NodeDefinition definition, int centerX, int centerY, int nodeWidth, int nodeHeigth, boolean highlighted, boolean focusMode) {

        Drawing.enableAntialiasing(g);

        // Hintergrund zeichnen...
        if (highlighted) {
            g.setColor(HIGHLIGHTED_NODE_BACKGROUND_COLOR);
        } else {
            if (focusMode) {
                g.setColor(NODE_BACKGROUND_COLOR_WHEN_FOCUSING);
            } else {
                g.setColor(NODE_BACKGROUND_COLOR);
            }
        }

        int[] xs = new int[]{
            centerX - nodeWidth / 2 - 1,
            centerX,
            centerX + nodeWidth / 2 + 1,
            centerX + nodeWidth / 2 + 1,
            centerX,
            centerX - nodeWidth / 2 - 1
        };
        int[] ys = new int[]{
            centerY - nodeHeigth / 2 + 5,
            centerY - nodeHeigth / 2 - 2,
            centerY - nodeHeigth / 2 + 5,
            centerY + nodeHeigth / 2 - 5,
            centerY + nodeHeigth / 2 + 2,
            centerY + nodeHeigth / 2 - 5
        };
        g.fillPolygon(xs, ys, xs.length);
        g.setStroke(ONE_POINT_FIVE_PX_LINE_STROKE);
        g.setColor(NODE_BORDER_COLOR);
        g.drawPolygon(xs, ys, xs.length);

        // Front zeichnen...
        if (definition instanceof ForEachNodeDefinition) {
            g.setColor(DEFAULT_LINE_COLOR);
            g.setStroke(ONE_POINT_FIVE_PX_LINE_STROKE);

            g.drawLine(centerX - 16, centerY - 15 - 1, centerX + 12, centerY - 15 - 1);
            g.drawLine(centerX - 6, centerY - 1, centerX + 12, centerY - 1);
            g.drawLine(centerX - 6, centerY + 15 - 1, centerX + 12, centerY + 15 - 1);

            g.drawLine(centerX - 6, centerY - 15 - 1, centerX - 6, centerY + 15 - 1);

            g.fillPolygon(new int[]{centerX + 18, centerX + 12, centerX + 12}, new int[]{centerY - 15, centerY - 6 - 15, centerY + 6 - 15}, 3);
            g.fillPolygon(new int[]{centerX + 18, centerX + 12, centerX + 12}, new int[]{centerY, centerY - 6, centerY + 6}, 3);
            g.fillPolygon(new int[]{centerX + 18, centerX + 12, centerX + 12}, new int[]{centerY + 15, centerY - 6 + 15, centerY + 6 + 15}, 3);

        } else if (definition instanceof ReduceNodeDefinition) {
            g.setColor(DEFAULT_LINE_COLOR);
            g.setStroke(ONE_POINT_FIVE_PX_LINE_STROKE);

            int lineCenterX = centerX;
            g.drawLine(centerX - 16, centerY - 15 - 1, centerX + 12, centerY - 15 - 1);
            g.drawLine(centerX - 16, centerY - 1, lineCenterX, centerY - 1);
            g.drawLine(centerX - 16, centerY + 15 - 1, lineCenterX, centerY + 15 - 1);

            g.drawLine(lineCenterX, centerY - 15 - 1, lineCenterX, centerY + 15 - 1);

            g.fillPolygon(new int[]{centerX + 18, centerX + 12, centerX + 12}, new int[]{centerY - 15, centerY - 6 - 15, centerY + 6 - 15}, 3);

        } else {
            System.err.println("node ist kein IfForwardNode!");
        }
    }

    public static void drawFirstValueNodePreview(Graphics2D g, NodeDefinition definition, int centerX, int centerY, int nodeWidth, int nodeHeigth, boolean highlighted, boolean focusMode) {

        Drawing.enableAntialiasing(g);

        // Hintergrund zeichnen...
        if (highlighted) {
            g.setColor(HIGHLIGHTED_NODE_BACKGROUND_COLOR);
        } else {
            if (focusMode) {
                g.setColor(NODE_BACKGROUND_COLOR_WHEN_FOCUSING);
            } else {
                g.setColor(NODE_BACKGROUND_COLOR);
            }
        }

        int[] xs = new int[]{
            centerX - nodeWidth / 2 - 1,
            centerX,
            centerX + nodeWidth / 2 + 1,
            centerX + nodeWidth / 2 + 1,
            centerX,
            centerX - nodeWidth / 2 - 1
        };
        int[] ys = new int[]{
            centerY - nodeHeigth / 2 + 5,
            centerY - nodeHeigth / 2 - 2,
            centerY - nodeHeigth / 2 + 5,
            centerY + nodeHeigth / 2 - 5,
            centerY + nodeHeigth / 2 + 2,
            centerY + nodeHeigth / 2 - 5
        };
        g.fillPolygon(xs, ys, xs.length);
        g.setStroke(ONE_POINT_FIVE_PX_LINE_STROKE);
        g.setColor(NODE_BORDER_COLOR);
        g.drawPolygon(xs, ys, xs.length);

        // Front zeichnen...
        g.setColor(DEFAULT_LINE_COLOR);
        disableAntialiasing(g);
        if (definition instanceof FastestValueNodeDefinition) {
            g.setStroke(ONE_PX_LINE_STROKE);
            for (int x = centerX - 18; x < centerX - 5; x += 2) {
                for (int y = centerY - nodeHeigth / 2 + 15 + 1; y < centerY + nodeHeigth / 2 - 15 + 1; y += 2) {
                    g.drawLine(x, y, x, y);
                }
            }
        } else if (definition instanceof AValueNodeDefinition) {
            enableAntialiasing(g);
            g.setStroke(ONE_POINT_FIVE_PX_LINE_STROKE);
            int x1 = centerX - 14;
            int x2 = centerX - 5;
            int y1 = centerY - nodeHeigth / 2 + 15 + 1;
            int y2 = centerY + nodeHeigth / 2 - 15 + 1;
            g.drawLine(x1, y1, x1, y2);
            g.drawLine(x2, y1, x2, y2);
        }
        enableAntialiasing(g);
        g.setStroke(ONE_POINT_FIVE_PX_LINE_STROKE);
        g.drawLine(centerX - 5, centerY, centerX + 12, centerY);
        g.fillPolygon(new int[]{centerX + 18, centerX + 12, centerX + 12}, new int[]{centerY, centerY - 6, centerY + 6}, 3);
    }

    public static int calcNeededHeightForNodesPreview(Graphics2D g, int width, NodeDefinition[] nodes) {
        // Berechnen Position und Zeichnen der einzelnen Nodes...
        int offX = SPACE_BETWEEN_NODE_PREVIEWS;
        int offY = SPACE_BETWEEN_NODE_PREVIEWS;
        for (NodeDefinition node : nodes) { // fuer alle Nodes
            int neededNodePreviewWidth = calcNeededNodePreviewWidth(g, node);
            if (offX + neededNodePreviewWidth + SPACE_BETWEEN_NODE_PREVIEWS - 1 > width) { // Wenn Nodes ueber Zeile hinausgehen
                offY += MINIMUM_NODE_HEIGHT + SPACE_BETWEEN_NODE_PREVIEWS;
                offX = SPACE_BETWEEN_NODE_PREVIEWS;
            }
            offX += neededNodePreviewWidth + SPACE_BETWEEN_NODE_PREVIEWS;
        }
        return offY + MINIMUM_NODE_HEIGHT + SPACE_BETWEEN_NODE_PREVIEWS;

    }

    public static NodeDefinition calcNodesForMousePosition(Graphics2D g, int x, int y, int width, NodeDefinition[] nodes, int mouseX, int mouseY) {

        // Berechnen Position und Zeichnen der einzelnen Nodes...
        int offX = SPACE_BETWEEN_NODE_PREVIEWS;
        int offY = SPACE_BETWEEN_NODE_PREVIEWS;
        for (NodeDefinition node : nodes) { // fuer alle Nodes
            int neededNodePreviewWidth = calcNeededNodePreviewWidth(g, node);
            if (offX + neededNodePreviewWidth + SPACE_BETWEEN_NODE_PREVIEWS - 1 > width) { // Wenn Nodes ueber Zeile hinausgehen
                offY += MINIMUM_NODE_HEIGHT + SPACE_BETWEEN_NODE_PREVIEWS;
                offX = SPACE_BETWEEN_NODE_PREVIEWS;
            }
            if (mouseX > x + offX && mouseY > y + offY && mouseX < x + offX + calcNeededNodePreviewWidth(g, node) && mouseY < y + offY + MINIMUM_NODE_HEIGHT) {
                return node;
            }
            offX += neededNodePreviewWidth + SPACE_BETWEEN_NODE_PREVIEWS;
        }
        return null;
    }

}
