package view.nodecollection;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import model.resourceloading.NodeDefinitionDescription;
import model.type.Type;
import reflection.NodeDefinition;
import utils.Drawing;
import utils.TextHandler;
import view.assets.ImageAsset;
import static view.Constants.*;

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
            boolean first = true;
            for(int i=0; i<node.getInletCount(); i++) {
                Type type = new Type(node.getClassForInlet(i), node.isInletForArray(i));
                String inletDescription = node.getNameForInlet(i) + " (" + type.toString() + ")";
                if(first) {
                    inString += " " + inletDescription;
                } else {
                    inString +=  ", " + inletDescription;
                }
                first = false;
            }
        }
        g.setColor(DEFAULT_FONT_COLOR);
        g.setFont(INFO_IN_OUT_CAPABILITIES_FONT);
        String[] inLines = TextHandler.splitStringToLines(g, inString, width - 75);
        
        // Out-Moeglichkeiten in Zeilen gliedern...
        final String OUT_TITLE = "Ausgänge:";
        String outString = OUT_TITLE;
        {
            boolean first = true;
            for(int i=0; i<node.getOutletCount(); i++) {
                Type type = new Type(node.getClassForOutlet(i), node.isOutletForArray(i));
                String inletDescription = node.getNameForOutlet(i) + " (" + type.toString() + ")";
                if(first) {
                    outString += " " + inletDescription;
                } else {
                    outString +=  ", " + inletDescription;
                }
                first = false;
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
        for(String line : infoLines) {
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
        for(int i=1; i<inLines.length; i++) {
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
        for(int i=1; i<outLines.length; i++) {
            g.drawString(outLines[i], x + START_OFF_X, y + offY);
            offY += SPACE;
        }
    }
    
    /**
     * Gibt die Breite einer zu Zeischnenden NodePreview zurueck.
     */
    public static int calcNeededNodePreviewWidth(Graphics2D g, NodeDefinition node) {

        // Breite von Label ermitteln...
        Drawing.enableAntialiasing(g);
        g.setFont(NODE_LABEL_FONT);
        int labelWidth = g.getFontMetrics().stringWidth(node.getName());

        return Math.max(labelWidth, 40) + 25;
    }
    
    /**
     * Zeichnet Vorschau von Node.
     */
    public static void drawNodePreview(Graphics2D g, int x, int y, NodeDefinition node, boolean highlighted, boolean focusMode) {

        // Breite ermitteln...
        int nodePreviewWidth = calcNeededNodePreviewWidth(g, node);

        // Hintergrund zeichenen...
        Drawing.enableAntialiasing(g);

        if (highlighted) {
            g.setColor(HIGHLIGHTED_NODE_BACKGROUND_COLOR);
        } else {
            if (focusMode) {
                g.setColor(NODE_BACKGROUND_COLOR_WHEN_FOCUSING);
            } else {
                g.setColor(NODE_BACKGROUND_COLOR);
            }
        }
        g.fillRoundRect(x, y, nodePreviewWidth, MINIMUM_NODE_HEIGHT, NODE_EDGE_ROUNDING, NODE_EDGE_ROUNDING);
        g.setColor(NODE_BORDER_COLOR);
        if (highlighted) {
            g.setStroke(HIGHLIGHTED_NODE_BORDERS_LINE_STROKE);
        } else {
            g.setStroke(NODE_BORDERS_LINE_STROKE);
        }
        g.drawRoundRect(x, y, nodePreviewWidth, MINIMUM_NODE_HEIGHT, NODE_EDGE_ROUNDING, NODE_EDGE_ROUNDING);

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
            icon.drawCentered(g, x + nodePreviewWidth / 2, y + MINIMUM_NODE_HEIGHT / 2 - 10);
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
            if(highlightedDefinition == node) {
                highlightedDefinitionX = x + offX;
                highlightedDefinitionY = y + offY;
                
            }
            
            if(y + offY + MINIMUM_NODE_HEIGHT >= 0 && y + offY <= drawHeight) { // Wenn im sichtbaren Bereich
                drawNodePreview(g, x + offX, y + offY, node, highlightedDefinition == node, highlightedDefinition != null);
            }
            offX += neededNodePreviewWidth + SPACE_BETWEEN_NODE_PREVIEWS;
        }
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
