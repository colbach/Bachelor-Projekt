package view.main;

import reflection.nodedefinitions.specialnodes.SpecialNodeDefinition;
import reflection.nodedefinitions.specialnodes.firstvalues.FirstValueNodeDefinition;
import reflection.nodedefinitions.specialnodes.ifs.IfForwardNodeDefinition;
import reflection.nodedefinitions.specialnodes.ifs.IfBackNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ForEachNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ForNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ReduceNodeDefinition;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.io.IOException;
import java.util.HashMap;
import logging.AdditionalErr;
import logging.AdditionalLogger;
import logging.AdditionalOut;
import logging.AdvancedLogger;
import model.*;
import model.directinput.DirectInputNodeDefinition;
import model.runproject.debugger.ContextState;
import model.runproject.debugger.Debugger;
import model.runproject.debugger.DebuggerRemote;
import model.runproject.debugger.ExecutorState;
import reflection.nodedefinitions.specialnodes.firstvalues.AValueNodeDefinition;
import reflection.nodedefinitions.specialnodes.firstvalues.FastestValueNodeDefinition;
import reflection.*;
import utils.Drawing;
import static view.Constants.*;
import static utils.Drawing.*;
import view.assets.ImageAsset;
import static view.nodecollection.NodeCollectionDrafter.calcNeededNodePreviewWidth;

public class NodeDrafter {

    /**
     * Gibt die Breite einer zu Zeischnenden Node zurueck.
     */
    public static int calcNeededNodeWidth(Graphics2D g, Node node) {
        long cccc = System.currentTimeMillis();
        if (node.getDefinition() instanceof DirectInputNodeDefinition) { // DirectInput-Node
            g.setFont(DIRECT_INPUT_FONT);
            String content = node.getShortenedUserSettableContent();
            if (content == null) {
                content = "";
            }
            int contentWidth = g.getFontMetrics().stringWidth(content); // Boese zeile. Ohne Optimierung braucht dieser Aufruf bei ersten Durchlauf 1-2s (Optimierung bei Startup)
            return Math.max(40, contentWidth + 10);

        } else if (node.getDefinition() instanceof SpecialNodeDefinition) {
            return 50;

        } else { // Normale Node
            // Breite von Label ermitteln...
            Drawing.enableAntialiasing(g);
            g.setFont(NODE_LABEL_FONT);
            int labelWidth = g.getFontMetrics().stringWidth(node.getName());

            return Math.max(labelWidth, 40) + 25 + 10;
        }
    }

    /**
     * Gibt die Breite einer zu Zeischnenden Node zurueck.
     */
    public static int calcNeededNodeHeight(Graphics2D g, Node node) {

        if (node.getDefinition() instanceof DirectInputNodeDefinition) { // DirectInput-Node
            return 15;

        } else if (node.getDefinition() instanceof SpecialNodeDefinition) {
            int heightForInlets = node.getConnectedInletCount() * NODE_Y_DISTANCE_BETWEEN_LETS;
            return Math.max(heightForInlets + 8/*wegen Kanten*/, 60);

        } else { // Normale Node
            int heightForInlets = node.getConnectedInletCount() * NODE_Y_DISTANCE_BETWEEN_LETS;
            int heightForOutlets = node.getConnectedOutletCount() * NODE_Y_DISTANCE_BETWEEN_LETS;
            return Math.max(Math.max(heightForInlets, heightForOutlets) + 8/*wegen Kanten*/, MINIMUM_NODE_HEIGHT);
        }
    }

    public static void drawHighlightForNode(Graphics2D g, Node node, int offX, int offY, int drawWidth, int drawHeight) {
        if (node != null) {
            // Masse und Positionen ermitteln...
            int centerX = offX + node.getUICenterX();
            int centerY = offY + node.getUICenterY();
            int nodeWidth = calcNeededNodeWidth(g, node);
            int nodeHeigth = calcNeededNodeHeight(g, node);

            // Hintergrund zeichenen...
            Drawing.enableAntialiasing(g);

            g.setColor(NODE_BORDER_COLOR);
            g.setStroke(TREE_PX_LINE_STROKE);
            final int add = 5;
            g.drawRoundRect(centerX - nodeWidth / 2 - add, centerY - nodeHeigth / 2 - add, nodeWidth + add * 2, nodeHeigth + add * 2, NODE_EDGE_ROUNDING + 2, NODE_EDGE_ROUNDING + 2);

        }
    }

    public static void drawNode(Graphics2D g, Node node, int offX, int offY, int drawWidth, int drawHeight, DebuggerRemote debugger, long contextIdentifier) {

        if (node != null) {
            if (debugger != null) {
                contextIdentifier = debugger.ajustContextIdentifier(contextIdentifier);
            }

            if (node.getDefinition() instanceof DirectInputNodeDefinition) { // DirectInput-Node
                drawDirectInputNode(g, node, offX, offY, drawWidth, drawHeight, debugger, contextIdentifier);

            } else if (node.getDefinition() instanceof IfForwardNodeDefinition || node.getDefinition() instanceof IfBackNodeDefinition) { // Wenn-If-Node
                drawIfNode(g, node, offX, offY, drawWidth, drawHeight, debugger, contextIdentifier);

            } else if (node.getDefinition() instanceof ForNodeDefinition) { // Wenn-For-Each-Node
                drawForNode(g, node, offX, offY, drawWidth, drawHeight, debugger, contextIdentifier);

            } else if (node.getDefinition() instanceof FirstValueNodeDefinition) { // First-Value-Node...
                drawFirstValueNode(g, node, offX, offY, drawWidth, drawHeight, debugger, contextIdentifier);

            } else { // Normale Node

                // Masse und Positionen ermitteln...
                int centerX = offX + node.getUICenterX();
                int centerY = offY + node.getUICenterY();
                int nodeWidth = calcNeededNodeWidth(g, node);
                int nodeHeigth = calcNeededNodeHeight(g, node);

                // Hintergrund zeichenen...
                Drawing.enableAntialiasing(g);

                g.setColor(NODE_BACKGROUND_COLOR);
                if (debugger != null && debugger.isDebugging()) {
                    ExecutorState executorState = debugger.getNodeState(node, contextIdentifier);
                    switch (executorState) {
                        case COLLECTING:
                            g.setColor(DEBUGGER_NODE_COLLECTING_BACKGROUND_COLOR);
                            break;
                        case DELIEVERING:
                            g.setColor(DEBUGGER_NODE_DELIEVERING_BACKGROUND_COLOR);
                            break;
                        case FAILED:
                            g.setColor(DEBUGGER_NODE_FAILED_BACKGROUND_COLOR);
                            break;
                        case FINISHED:
                            g.setColor(DEBUGGER_NODE_FINISHED_BACKGROUND_COLOR);
                            break;
                        case PREPARING:
                            g.setColor(DEBUGGER_NODE_PREPARING_BACKGROUND_COLOR);
                            break;
                        case RUNNING:
                            g.setColor(DEBUGGER_NODE_RUNNING_BACKGROUND_COLOR);
                            break;
                        case UNKNOWN:
                            g.setColor(DEBUGGER_NODE_UNKNOWN_BACKGROUND_COLOR);
                            break;
                    }
                }

                if (node.isRunContextCreator()) {
                    g.fillRect(centerX - nodeWidth / 2, centerY - nodeHeigth / 2, nodeWidth, nodeHeigth);
                } else {
                    g.fillRoundRect(centerX - nodeWidth / 2, centerY - nodeHeigth / 2, nodeWidth, nodeHeigth, NODE_EDGE_ROUNDING, NODE_EDGE_ROUNDING);
                }
                g.setColor(NODE_BORDER_COLOR);
                g.setStroke(NODE_BORDERS_LINE_STROKE);
                if (node.isRunContextCreator()) {
                    g.drawRect(centerX - nodeWidth / 2, centerY - nodeHeigth / 2, nodeWidth, nodeHeigth);
                } else {
                    g.drawRoundRect(centerX - nodeWidth / 2, centerY - nodeHeigth / 2, nodeWidth, nodeHeigth, NODE_EDGE_ROUNDING, NODE_EDGE_ROUNDING);
                }
                // Text zeichnen...
                g.setColor(NODE_LABEL_COLOR);
                g.setFont(NODE_LABEL_FONT);
                int labelWidth = g.getFontMetrics().stringWidth(node.getName());
                g.drawString(node.getName(), centerX - nodeWidth / 2 + nodeWidth / 2 - labelWidth / 2, centerY + MINIMUM_NODE_HEIGHT / 2 - 12);

                // Icon zeichnen...
                ImageAsset icon = null;
                String iconName = node.getIconName();
                if (iconName != null) { // Wenn eine iconName existiert
                    try { // Versuche ImageAsset zu laden
                        icon = ImageAsset.getImageAssetForName(iconName);
                    } catch (IOException ex) {
                        AdditionalLogger.err.println(iconName + " konnte nicht geladen werden! Versuche Default.");
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
                    icon.drawCentered(g, centerX, centerY - 10);
                } else {
                    System.err.println("icon ist null!");
                }

                // Inlets zeichnen...
                {
                    g.setColor(DEFAULT_LINE_COLOR);
                    int inletCount = node.getConnectedInletCount();
                    for (int i = 0; i < inletCount; i++) {
                        g.fillOval(centerX - nodeWidth / 2 + NODE_X_DISTANCE_BETWEEN_LET_AND_BORDER, centerY - ((inletCount - 1) * NODE_Y_DISTANCE_BETWEEN_LETS) / 2 + i * NODE_Y_DISTANCE_BETWEEN_LETS - 1, 5, 5);
                    }
                }

                // Outlets zeichnen...
                {
                    g.setColor(DEFAULT_LINE_COLOR);
                    int oultetCount = node.getConnectedOutletCount();
                    for (int i = 0; i < oultetCount; i++) {
                        g.fillOval(centerX - nodeWidth / 2 + nodeWidth - NODE_X_DISTANCE_BETWEEN_LET_AND_BORDER - 5, centerY - ((oultetCount - 1) * NODE_Y_DISTANCE_BETWEEN_LETS) / 2 + i * NODE_Y_DISTANCE_BETWEEN_LETS - 1, 5, 5);
                    }
                }

                // Debug-Marken zeichnen...
                if (debugger != null && debugger.isDebugging()) { // OLD_Debugger-Modus
                    // Inlets...
                    {
                        Inlet[] connectedInlets = node.getConnectedInlets();
                        HashMap<Inlet, Integer> arrivedData = debugger.getArrivedDataCountPerInlet(contextIdentifier);
                        for (int i = 0; i < connectedInlets.length; i++) {
                            if (arrivedData.containsKey(connectedInlets[i])) {
                                int it = arrivedData.get(connectedInlets[i]);
                                for (int c = 0; c < it; c++) {
                                    int pX = (-c * 5) + centerX - 10 - nodeWidth / 2 + NODE_X_DISTANCE_BETWEEN_LET_AND_BORDER;
                                    int pY = centerY - ((connectedInlets.length - 1) * NODE_Y_DISTANCE_BETWEEN_LETS) / 2 + i * NODE_Y_DISTANCE_BETWEEN_LETS + 1;
                                    int v = 20;
                                    g.setColor(DEBUGGER_INLET_BACKGROUND_COLOR);
                                    g.fillOval(pX - v / 2, pY - v / 2, 20, 20);
                                    g.setColor(DEBUGGER_INLET_BORDER_COLOR);
                                    g.drawOval(pX - v / 2, pY - v / 2, 20, 20);
                                }
                            }
                        }
                    }
                }

            }
        } else {
            System.err.println("Node darf nicht null sein!");
        }
    }

    public static void drawIfNode(Graphics2D g, Node node, int offX, int offY, int drawWidth, int drawHeight, DebuggerRemote debugger, long contextIdentifier) {
        ExecutorState executorState = null;
        if (debugger != null && debugger.isDebugging()) {
            executorState = debugger.getNodeState(node, contextIdentifier);
        }
        drawIfNode(g, node.getDefinition(), node.getUICenterX(), node.getUICenterY(), calcNeededNodeWidth(g, node), calcNeededNodeHeight(g, node), offX, offY, contextIdentifier, executorState);
    }

    public static void drawForNode(Graphics2D g, Node node, int offX, int offY, int drawWidth, int drawHeight, DebuggerRemote debugger, long contextIdentifier) {
        ExecutorState executorState = null;
        if (debugger != null && debugger.isDebugging()) {
            executorState = debugger.getNodeState(node, contextIdentifier);
        }
        drawForEachNode(g, node.getDefinition(), node.getUICenterX(), node.getUICenterY(), calcNeededNodeWidth(g, node), calcNeededNodeHeight(g, node), offX, offY, contextIdentifier, executorState);
    }

    public static void drawFirstValueNode(Graphics2D g, Node node, int offX, int offY, int drawWidth, int drawHeight, DebuggerRemote debugger, long contextIdentifier) {
        ExecutorState executorState = null;
        if (debugger != null && debugger.isDebugging()) {
            executorState = debugger.getNodeState(node, contextIdentifier);
        }
        drawFirstValueNode(g, node.getDefinition(), node.getUICenterX(), node.getUICenterY(), calcNeededNodeWidth(g, node), calcNeededNodeHeight(g, node), offX, offY, contextIdentifier, executorState);
    }

    public static void drawFirstValueNode(Graphics2D g, NodeDefinition definition, int uiCenterX, int uiCenterY, int nodeWidth, int nodeHeigth, int offX, int offY, long contextIdentifier, ExecutorState executorState) {

        Drawing.enableAntialiasing(g);
        int centerX = offX + uiCenterX;
        int centerY = offY + uiCenterY;

        // Hintergrund zeichnen...
        g.setColor(NODE_BACKGROUND_COLOR);
        if (executorState != null) {
            switch (executorState) {
                case COLLECTING:
                    g.setColor(DEBUGGER_NODE_COLLECTING_BACKGROUND_COLOR);
                    break;
                case DELIEVERING:
                    g.setColor(DEBUGGER_NODE_DELIEVERING_BACKGROUND_COLOR);
                    break;
                case FAILED:
                    g.setColor(DEBUGGER_NODE_FAILED_BACKGROUND_COLOR);
                    break;
                case FINISHED:
                    g.setColor(DEBUGGER_NODE_FINISHED_BACKGROUND_COLOR);
                    break;
                case PREPARING:
                    g.setColor(DEBUGGER_NODE_PREPARING_BACKGROUND_COLOR);
                    break;
                case RUNNING:
                    g.setColor(DEBUGGER_NODE_RUNNING_BACKGROUND_COLOR);
                    break;
                case UNKNOWN:
                    g.setColor(DEBUGGER_NODE_UNKNOWN_BACKGROUND_COLOR);
                    break;
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

    public static void drawIfNode(Graphics2D g, NodeDefinition definition, int uiCenterX, int uiCenterY, int nodeWidth, int nodeHeigth, int offX, int offY, long contextIdentifier, ExecutorState executorState) {

        Drawing.enableAntialiasing(g);
        int centerX = offX + uiCenterX;
        int centerY = offY + uiCenterY;

        // Hintergrund zeichnen...
        g.setColor(NODE_BACKGROUND_COLOR);
        if (executorState != null) {
            switch (executorState) {
                case COLLECTING:
                    g.setColor(DEBUGGER_NODE_COLLECTING_BACKGROUND_COLOR);
                    break;
                case DELIEVERING:
                    g.setColor(DEBUGGER_NODE_DELIEVERING_BACKGROUND_COLOR);
                    break;
                case FAILED:
                    g.setColor(DEBUGGER_NODE_FAILED_BACKGROUND_COLOR);
                    break;
                case FINISHED:
                    g.setColor(DEBUGGER_NODE_FINISHED_BACKGROUND_COLOR);
                    break;
                case PREPARING:
                    g.setColor(DEBUGGER_NODE_PREPARING_BACKGROUND_COLOR);
                    break;
                case RUNNING:
                    g.setColor(DEBUGGER_NODE_RUNNING_BACKGROUND_COLOR);
                    break;
                case UNKNOWN:
                    g.setColor(DEBUGGER_NODE_UNKNOWN_BACKGROUND_COLOR);
                    break;
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
        if (definition instanceof IfForwardNodeDefinition) {
            g.setColor(DEFAULT_LINE_COLOR);
            g.setStroke(ONE_POINT_FIVE_PX_LINE_STROKE);
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
            g.setStroke(ONE_POINT_FIVE_PX_LINE_STROKE);
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

    public static void drawForEachNode(Graphics2D g, NodeDefinition definition, int uiCenterX, int uiCenterY, int nodeWidth, int nodeHeigth, int offX, int offY, long contextIdentifier, ExecutorState executorState) {

        Drawing.enableAntialiasing(g);
        int centerX = offX + uiCenterX;
        int centerY = offY + uiCenterY;

        // Hintergrund zeichnen...
        g.setColor(NODE_BACKGROUND_COLOR);
        if (executorState != null) {
            switch (executorState) {
                case COLLECTING:
                    g.setColor(DEBUGGER_NODE_COLLECTING_BACKGROUND_COLOR);
                    break;
                case DELIEVERING:
                    g.setColor(DEBUGGER_NODE_DELIEVERING_BACKGROUND_COLOR);
                    break;
                case FAILED:
                    g.setColor(DEBUGGER_NODE_FAILED_BACKGROUND_COLOR);
                    break;
                case FINISHED:
                    g.setColor(DEBUGGER_NODE_FINISHED_BACKGROUND_COLOR);
                    break;
                case PREPARING:
                    g.setColor(DEBUGGER_NODE_PREPARING_BACKGROUND_COLOR);
                    break;
                case RUNNING:
                    g.setColor(DEBUGGER_NODE_RUNNING_BACKGROUND_COLOR);
                    break;
                case UNKNOWN:
                    g.setColor(DEBUGGER_NODE_UNKNOWN_BACKGROUND_COLOR);
                    break;
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

    private static void drawDirectInputNode(Graphics2D g, Node node, int offX, int offY, int drawWidth, int drawHeight, DebuggerRemote debugger, long contextIdentifier) {

        if (node.getDefinition() instanceof DirectInputNodeDefinition) {
            int xCenter = offX + calcCenterXForRelativeNode(g, node);
            int yCenter = offY + calcCenterYForRelativeNode(g, node);
            int width = calcNeededNodeWidth(g, node);
            int height = calcNeededNodeHeight(g, node);

            // Poligon zeichnen...
            int[] poligonX = new int[]{
                xCenter - width / 2, xCenter - width / 2, xCenter + width / 2 - height / 2 + 1, xCenter + width / 2 + 1, xCenter + width / 2 - height / 2 + 1
            };
            int[] poligonY = new int[]{
                yCenter + height / 2 + 1, yCenter - height / 2 + 1, yCenter - height / 2 + 1, yCenter + 1, yCenter + height / 2 + 1
            };
            g.setColor(NODE_BACKGROUND_COLOR);
            if (debugger != null && debugger.isDebugging()) {
                ExecutorState executorState = debugger.getNodeState(node, contextIdentifier);
                switch (executorState) {
                    case COLLECTING:
                        g.setColor(DEBUGGER_NODE_COLLECTING_BACKGROUND_COLOR);
                        break;
                    case DELIEVERING:
                        g.setColor(DEBUGGER_NODE_DELIEVERING_BACKGROUND_COLOR);
                        break;
                    case FAILED:
                        g.setColor(DEBUGGER_NODE_FAILED_BACKGROUND_COLOR);
                        break;
                    case FINISHED:
                        g.setColor(DEBUGGER_NODE_FINISHED_BACKGROUND_COLOR);
                        break;
                    case PREPARING:
                        g.setColor(DEBUGGER_NODE_PREPARING_BACKGROUND_COLOR);
                        break;
                    case RUNNING:
                        g.setColor(DEBUGGER_NODE_RUNNING_BACKGROUND_COLOR);
                        break;
                    case UNKNOWN:
                        g.setColor(DEBUGGER_NODE_UNKNOWN_BACKGROUND_COLOR);
                        break;
                }
            }
            g.fillPolygon(poligonX, poligonY, poligonX.length);
            g.setColor(NODE_BORDER_COLOR);
            g.setStroke(NODE_BORDERS_LINE_STROKE);
            g.drawPolygon(poligonX, poligonY, poligonX.length);

            // Inhalt zeichnen...
            g.setColor(NODE_LABEL_COLOR);
            g.setFont(DIRECT_INPUT_FONT);
            String content = node.getShortenedUserSettableContent();
            if (content == null) {
                content = "";
            }
            int contentWidth = g.getFontMetrics().stringWidth(content);
            g.drawString(content, xCenter - contentWidth / 2 - 3, yCenter + 5);

        } else {
            System.err.println("node ist kein DirectInputNode!");
        }
    }

    public static Node calcNodesForMousePosition(Graphics2D g, int offX, int offY, Node[] nodes, int mouseX, int mouseY) {

        for (int i = nodes.length - 1; i >= 0; i--) {
            int nodeWidth = calcNeededNodeWidth(g, nodes[i]);
            int nodeHeigth = calcNeededNodeHeight(g, nodes[i]);
            int nodeCenterX = calcCenterXForNode(g, nodes[i]);
            int nodeCenterY = calcCenterYForNode(g, nodes[i]);
            if (nodeCenterX - nodeWidth / 2 < mouseX - offX && nodeCenterX + nodeWidth / 2 > mouseX - offX && nodeCenterY - nodeHeigth / 2 < mouseY - offY && nodeCenterY + nodeHeigth / 2 > mouseY - offY) {
                AdditionalLogger.out.println("Node getroffen: " + nodes[i].getName());
                return nodes[i];
            }
        }
        AdditionalLogger.out.println("Keine Node auf dieser Position (" + mouseX + ", " + mouseY + ") gefunden");
        return null;
    }

    public static int calcCenterXForNode(Graphics2D g, Node node) {
        if (node.isRelativeNode()) {
            return calcCenterXForRelativeNode(g, node);
        } else {
            return node.getUICenterX();
        }
    }

    public static int calcCenterXForRelativeNode(Graphics2D g, Node node) {
        if (node.isRelativeNode()) {
            Node relativeNode = node.getRelativeNode();
            int relativeNodeWidth = calcNeededNodeWidth(g, relativeNode);
            int ownWidth = calcNeededNodeWidth(g, node);
            String connectedInletFromRelativeNodeName = node.getConnectedInletFromRelativeNode().getName();
            g.setFont(NODE_CONNECTION_NAME_FONT);
            int connectedInletFromRelativeNodeNameWidth = g.getFontMetrics().stringWidth(connectedInletFromRelativeNodeName);
            int distance = DISTANCE_BETWEEN_NODE_AND_HIS_RELATIVE_NODE;
            if (distance < connectedInletFromRelativeNodeNameWidth + 20) {
                distance = connectedInletFromRelativeNodeNameWidth + 20;
            }
            return relativeNode.getUICenterX() - relativeNodeWidth / 2 - distance - ownWidth / 2;
        } else {
            throw new IllegalArgumentException("Node muss relativ zu anderem Node stehen.");
        }
    }

    public static int calcCenterYForNode(Graphics2D g, Node node) {
        if (node.isRelativeNode()) {
            return calcCenterYForRelativeNode(g, node);
        } else {
            return node.getUICenterY();
        }
    }

    public static int calcCenterYForRelativeNode(Graphics2D g, Node node) {
        if (node.isRelativeNode()) {
            Node relativeNode = node.getRelativeNode();
            Inlet connectedInlet = node.getConnectedInletFromRelativeNode();
            int positionOfConnectedRelativeNode = 0;
            for (Inlet inletOfRelativeNode : relativeNode.getInlets()) {
                if (inletOfRelativeNode == connectedInlet) {
                    break;
                }
                if (inletOfRelativeNode.isConnected()) {
                    positionOfConnectedRelativeNode++;
                }
            }
            return relativeNode.getUICenterY() - ((relativeNode.getConnectedInletCount() - 1) * NODE_Y_DISTANCE_BETWEEN_LETS) / 2 + positionOfConnectedRelativeNode * NODE_Y_DISTANCE_BETWEEN_LETS/* + 2*/;
        } else {
            throw new IllegalArgumentException("Node muss relativ zu anderem Node stehen.");
        }
    }
}
