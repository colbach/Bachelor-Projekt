package view.main.connectoverlay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.InOutlet;
import model.Inlet;
import model.Node;
import model.Outlet;
import utils.structures.tuples.Quadrupel;
import utils.structures.tuples.Triple;
import utils.structures.tuples.Pair;
import view.main.DragConnectionDrafter;
import view.main.DragConnectionDrafter;
import static view.Constants.*;
import view.assets.ImageAsset;
import static view.main.connectoverlay.VirtualLetLister.*;

public class ConnectOverlayDrafter {

    private static ImageAsset swapIcon = null;
    private static ImageAsset swapIconTransparent = null;

    private static ImageAsset getSwapIcon() {
        if (swapIcon == null) {
            try {
                swapIcon = ImageAsset.getImageAssetForName(ImageAsset.SWAP_SIDES_TO_WHITE);
            } catch (IOException ex) {
                System.err.println("SwapIcon kann nicht geladen werden.");
            }
        }
        return swapIcon;
    }

    private static ImageAsset getSwapIconTransparent() {
        if (swapIconTransparent == null) {
            try {
                swapIconTransparent = ImageAsset.getImageAssetForName(ImageAsset.SWAP_SIDES_BACK_WHITE);
            } catch (IOException ex) {
                System.err.println("SwapIconTransparent kann nicht geladen werden.");
            }
        }
        return swapIconTransparent;
    }

    public static void drawConnectOverlay(Graphics2D g, Node nodeFrom, Node nodeTo, int centerX, int centerY, boolean swapPressed) {
        drawConnectOverlay(g, nodeFrom, nodeTo, centerX, centerY, -1, false, -1, -1, swapPressed);
    }

    public static void drawConnectOverlay(Graphics2D g, Node nodeFrom, Node nodeTo, int centerX, int centerY, int fromVirtualLetIndex, boolean lr, int xMouse, int yMouse, boolean swapPressed) {

        // Virtuelle Lets zaehlen...
        Outlet[] nodeFromOutlets = nodeFrom.getVisibleOutlets();
        Inlet[] nodeToInlets = nodeTo.getVisibleInlets();
        int nodeFromVirtualOutletCount = getVirtualLetCount(nodeFromOutlets);
        int nodeToVirtualInletCount = getVirtualLetCount(nodeToInlets);

        // Masse berechnen...
        int nodeFromOutletListHeigth = nodeFromVirtualOutletCount * 25;
        int nodeToInletListHeigth = nodeToVirtualInletCount * 25;
        int overlayHeigth = Math.max(nodeFromOutletListHeigth, nodeToInletListHeigth) + 55;
        int overlayWidth = CONNECT_OVERLAY_WIDTH;

        // Hintergrund zeichnen...
        g.setColor(CONNECT_OVERLAY_BACKGROUND_COLOR);
        g.fillRoundRect(centerX - overlayWidth / 2, centerY - overlayHeigth / 2, overlayWidth, overlayHeigth, CONNECT_OVERLAY_ROUNDING, CONNECT_OVERLAY_ROUNDING);

        // Ueberschrift zeichnen...
        {
            g.setFont(CONNECT_OVERLAY_TITLE_FONT);
            g.setColor(CONNECT_OVERLAY_TEXT_COLOR);
            String nodeFromName = nodeFrom.getName();
            int nodeFromNameWidth = g.getFontMetrics().stringWidth(nodeFromName);
            g.drawString(nodeFromName, centerX - CONNECT_OVERLAY_CONNECTION_SPACE / 2 - 10 - nodeFromNameWidth, centerY - overlayHeigth / 2 + 25);
            String nodeToName = nodeTo.getName();
            g.drawString(nodeToName, centerX + CONNECT_OVERLAY_CONNECTION_SPACE / 2 + 10, centerY - overlayHeigth / 2 + 25);
        }

        // Out und Inlets zeichnen...
        g.setFont(CONNECT_OVERLAY_FONT);
        g.setColor(CONNECT_OVERLAY_TEXT_COLOR);
        {
            // Links...
            for (int i = 0; i < nodeFromVirtualOutletCount; i++) {
                String label = getNameForVirtualLet(nodeFromOutlets, i);
                int labelWidth = g.getFontMetrics().stringWidth(label);
                g.drawString(label, centerX - CONNECT_OVERLAY_CONNECTION_SPACE / 2 - 10 - labelWidth, centerY - nodeFromOutletListHeigth / 2 + i * 25 + 30);
                int connectionCircleX = centerX - CONNECT_OVERLAY_CONNECTION_SPACE / 2;
                int connectionCircleY = centerY - nodeFromOutletListHeigth / 2 + i * 25 + 30 - 5;
                boolean virtualLetConnected = isVirtualLetConnected(nodeFromOutlets, i);
                drawConnectionCircle(g, connectionCircleX, connectionCircleY, virtualLetConnected);

                // Verbindung zeichnen...
                if (virtualLetConnected) { // Wenn let connected
                    // Ueberpruefung ob nodeTo mit diesem Outlet verbunden ist...
                    Pair<InOutlet, Integer> correspondingLet = getCorrespondingLetForVirtualLet(nodeFromOutlets, i);
                    InOutlet[] connectedLets = correspondingLet.l.getConnectedLets();
                    if (connectedLets.length > correspondingLet.r) {
                        Inlet connectedLet = (Inlet) connectedLets[correspondingLet.r];
                        if (connectedLet.getNode() == nodeTo) { // Ist mit diesem verbunden

                            // Virtueller Index ermitteln...
                            int inletIndex = connectedLet.getIndexForConnectedOutletInList(correspondingLet.l);
                            int virtualToIndex = VirtualLetLister.getVirtualLetIndexFor(nodeToInlets, connectedLet, inletIndex);

                            // Pfeil zeichnen...
                            int arrowCurveX1 = connectionCircleX + 10;
                            int arrowCurveY1 = connectionCircleY;
                            int arrowCurveX2 = centerX + CONNECT_OVERLAY_CONNECTION_SPACE / 2 - 10;
                            int arrowCurveY2 = centerY - nodeToInletListHeigth / 2 + virtualToIndex * 25 + 30 - 5;
                            ArrowCurveDrafter.drawArrowCurve(g, arrowCurveX1, arrowCurveY1, arrowCurveX2, arrowCurveY2);

                        }
                    }
                }

                // Draglinie zeichnen...
                if (fromVirtualLetIndex != -1 && lr == true && i == fromVirtualLetIndex) {
                    DragConnectionDrafter.drawDragConnectionLineBetweenLets(g, connectionCircleX, connectionCircleY, xMouse, yMouse, true);
                }
            }

            // Rechts...
            for (int i = 0; i < nodeToVirtualInletCount; i++) {
                String label = getNameForVirtualLet(nodeToInlets, i);
                g.drawString(label, centerX + CONNECT_OVERLAY_CONNECTION_SPACE / 2 + 10, centerY - nodeToInletListHeigth / 2 + i * 25 + 30);
                int connectionCircleX = centerX + CONNECT_OVERLAY_CONNECTION_SPACE / 2;
                int connectionCircleY = centerY - nodeToInletListHeigth / 2 + i * 25 + 30 - 5;
                drawConnectionCircle(g, connectionCircleX, connectionCircleY, isVirtualLetConnected(nodeToInlets, i));

                // Draglinie zeichnen...
                if (fromVirtualLetIndex != -1 && lr == false && i == fromVirtualLetIndex) {
                    DragConnectionDrafter.drawDragConnectionLineBetweenLets(g, connectionCircleX, connectionCircleY, xMouse, yMouse, true);
                }
            }
        }
        
        // Swap Icon zeichnen...
        g.setColor(new Color(255, 255, 255, 30));
        if (swapPressed) {
            getSwapIconTransparent().drawCentered(g, centerX, centerY - overlayHeigth / 2 + 18);
        } else {
            getSwapIcon().drawCentered(g, centerX, centerY - overlayHeigth / 2 + 18);
        }
    }
    
    public static boolean mouseMatchSwapIcon(Node nodeFrom, Node nodeTo, int centerX, int centerY, int xMouse, int yMouse) {

        // Virtuelle Lets zaehlen...
        Outlet[] nodeFromOutlets = nodeFrom.getVisibleOutlets();
        Inlet[] nodeToInlets = nodeTo.getVisibleInlets();
        int nodeFromVirtualOutletCount = getVirtualLetCount(nodeFromOutlets);
        int nodeToVirtualInletCount = getVirtualLetCount(nodeToInlets);

        // Masse berechnen...
        int nodeFromOutletListHeigth = nodeFromVirtualOutletCount * 25;
        int nodeToInletListHeigth = nodeToVirtualInletCount * 25;
        int overlayHeigth = Math.max(nodeFromOutletListHeigth, nodeToInletListHeigth) + 55;

        int xMin = centerX - 15;
        int xMax = centerX + 15;
        int yMin = centerY - overlayHeigth / 2 + 18 - 15;
        int yMax = centerY - overlayHeigth / 2 + 18 + 15;
        
        if(xMin < xMouse && xMax > xMouse && yMin < yMouse && yMax > yMouse) {
            return true;
        } else
            return false;
    }
    
    public static Quadrupel<InOutlet /*let*/, Integer /*index in diesem Let*/, Integer /*virtueller Index*/, Boolean /*true=links, false=rechts*/> calcLetForMousePosition(Node nodeFrom, Node nodeTo, int centerX, int centerY, int mouseX, int mouseY) {
        // Virtuelle Lets zaehlen...
        int overlayWidth = CONNECT_OVERLAY_WIDTH;

        if (mouseX < centerX && mouseX > centerX - overlayWidth / 2) { // Linke Seite betroffen
            Outlet[] nodeFromOutlets = nodeFrom.getVisibleOutlets();
            int nodeFromVirtualOutletCount = getVirtualLetCount(nodeFromOutlets);
            int nodeFromOutletListHeigth = nodeFromVirtualOutletCount * 25;
            for (int i = 0; i < nodeFromVirtualOutletCount; i++) {
                int connectionCircleCenterY = centerY - nodeFromOutletListHeigth / 2 + i * 25 + 30 - 5;
                if (mouseY < connectionCircleCenterY + 15 && mouseY > connectionCircleCenterY - 15) {
                    Pair<InOutlet, Integer> correspondingLet = getCorrespondingLetForVirtualLet(nodeFromOutlets, i);
                    return new Quadrupel<>(correspondingLet.l, correspondingLet.r, i, true);
                }
            }

        } else if (mouseX > centerX && mouseX < centerX + overlayWidth / 2) { // Rechte Seite betroffen
            Inlet[] nodeToInlets = nodeTo.getVisibleInlets();
            int nodeToVirtualInletCount = getVirtualLetCount(nodeToInlets);
            int nodeToInletListHeigth = nodeToVirtualInletCount * 25;
            for (int i = 0; i < nodeToVirtualInletCount; i++) {
                int connectionCircleCenterY = centerY - nodeToInletListHeigth / 2 + i * 25 + 30 - 5;
                if (mouseY < connectionCircleCenterY + 15 && mouseY > connectionCircleCenterY - 15) {
                    Pair<InOutlet, Integer> correspondingLet = getCorrespondingLetForVirtualLet(nodeToInlets, i);
                    return new Quadrupel<>(correspondingLet.l, correspondingLet.r, i, false);
                }
            }
        }

        return null; // Kein Let getroffen
    }

    private static void drawConnectionCircle(Graphics2D g, int xCenter, int yCenter, boolean connected) {

        g.setColor(CONNECT_OVERLAY_TEXT_COLOR);
        g.setStroke(ONE_PX_LINE_STROKE);
        final int RADIUS = 4;
        g.drawOval(xCenter - RADIUS, yCenter - RADIUS, RADIUS * 2, RADIUS * 2);
        if (connected) {
            g.fillOval(xCenter - RADIUS, yCenter - RADIUS, RADIUS * 2, RADIUS * 2);
        }

    }

}
