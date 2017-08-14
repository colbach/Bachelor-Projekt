package view.main.debug;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import logging.AdditionalErr;
import logging.AdditionalLogger;
import model.InOutlet;
import model.Inlet;
import model.Node;
import model.runproject.debugger.*;
import utils.structures.tuples.Pair;
import static view.Constants.*;
import view.assets.ImageAsset;
import view.main.MainWindow;
import view.main.connectoverlay.VirtualLetLister;

public class DebugNodeOverlayDrafter {

    public static void drawDebugNodeOverlay(Graphics2D g, Node node, int centerX, int centerY, DebuggerRemote debugger, long contextIdentifier) {

        if (debugger == null) {
            AdditionalLogger.err.println("DebuggerRemote ist null. DebugNodeOverlay kann nicht gezeichnet werden!");

        } else {

            contextIdentifier = debugger.ajustContextIdentifier(contextIdentifier);
            g.setFont(DEBUG_NODE_OVERLAY_TITLE_FONT);
            String name = node.getName();
            int nameWidth = g.getFontMetrics().stringWidth(node.getName());
            int spaceY = 25;
            Inlet[] inlets = node.getInlets();
            int virtualLetCount = VirtualLetLister.getConnectedVirtualLetCount(inlets);
            String[] lines = new String[virtualLetCount];
            boolean[] connected = new boolean[virtualLetCount];

            // Angekommene Daten verarbeiten und in Text umwandeln...
            Map<Pair<Inlet, Integer>, String> shortenedDataMap = debugger.getArrivedShortenedDataLimitedToNode(node, contextIdentifier);
            for (int i = 0; i < virtualLetCount; i++) {
                Pair<InOutlet, Integer> virtualLet = VirtualLetLister.getCorrespondingLetForConnectedVirtualLet(inlets, i);
                Pair<Inlet, Integer> virtualInlet = new Pair<>((Inlet) virtualLet.l, virtualLet.r);
                String shortenedData = shortenedDataMap.get(virtualInlet);
                if (shortenedData != null) {
                    lines[i] = VirtualLetLister.getNameForConnectedVirtualLet(inlets, i) + ": " + shortenedData;
                    connected[i] = true;
                } else if (!virtualInlet.l.isConnected()) {
                    lines[i] = VirtualLetLister.getNameForConnectedVirtualLet(inlets, i) + ": " + "(nicht verbunden)";
                    connected[i] = false;
                } else {
                    lines[i] = VirtualLetLister.getNameForConnectedVirtualLet(inlets, i) + ": " + "(ausstehend)";
                    connected[i] = true;
                }
            }

            // Hoehe und Breite bestimmen...
            int overlayHeigth = 70 + virtualLetCount * spaceY;
            int overlayWidth = Math.max(100, nameWidth + 80);
            g.setFont(DEBUG_NODE_OVERLAY_FONT);
            FontMetrics fm = g.getFontMetrics();
            for (String line : lines) {
                overlayWidth = Math.max(overlayWidth, fm.stringWidth(line) + 40);
            }

            // Hintergrund zeichnen...
            g.setColor(DEBUG_NODE_OVERLAY_BACKGROUND_COLOR);
            g.fillRoundRect(centerX - overlayWidth / 2, centerY - overlayHeigth / 2, overlayWidth, overlayHeigth, CONNECT_OVERLAY_ROUNDING, CONNECT_OVERLAY_ROUNDING);

            // Titel zeichnen...
            g.setColor(DEBUG_NODE_OVERLAY_TEXT_COLOR);
            g.setFont(DEBUG_NODE_OVERLAY_TITLE_FONT);
            g.drawString(name, centerX - (nameWidth + 40) / 2 + 40, centerY - overlayHeigth / 2 + 30);
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
                icon.drawCenteredInverted(g, centerX - (nameWidth + 40) / 2 + 20 - 5, centerY - overlayHeigth / 2 + 25);
            } else {
                System.err.println("icon ist null!");
            }

            // Text zeichnen...
            g.setFont(DEBUG_NODE_OVERLAY_FONT);
            for (int i = 0; i < lines.length; i++) {
                if (connected[i]) {
                    g.setColor(DEBUG_NODE_OVERLAY_TEXT_COLOR);
                } else {
                    g.setColor(DEBUG_NODE_OVERLAY_INACTIVE_TEXT_COLOR);
                }
                g.drawString(lines[i], centerX - overlayWidth / 2 + 20, centerY - overlayHeigth / 2 + 70 + i * spaceY);
            }
        }
    }
}
