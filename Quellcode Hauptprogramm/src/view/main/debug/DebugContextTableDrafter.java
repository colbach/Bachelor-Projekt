package view.main.debug;

import java.awt.Graphics2D;
import model.Node;
import projectrunner.debugger.ContextTable;
import projectrunner.debugger.DebuggerRemote;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import logging.AdditionalErr;
import logging.AdditionalLogger;
import model.InOutlet;
import model.Inlet;
import model.Node;
import reflection.NodeDefinition;
import utils.structures.tuples.Pair;
import view.Constants;
import static view.Constants.*;
import view.assets.ImageAsset;
import view.main.MainWindow;
import view.main.connectoverlay.VirtualLetLister;

public class DebugContextTableDrafter {

    /**
     * Gibt getroffenes Feld in ContextTable oder -2L zuruek.
     */
    public static long calcDebugContextForMousePosition(Graphics2D g, int mouseX, int mouseY, int rightX, int topY, DebuggerRemote debugger) {
        
        if (debugger == null) {
            AdditionalLogger.err.println("debugger ist null. return -2L");
            return -2L;
        }

        ContextTable contextTable = debugger.getContextTable();
        Long blockedContextIdentifier = debugger.getBlockedContextIdentifier();

        if (mouseY < topY) {
            return -2L;
        }

        int yOff = 0;
        int width = 10;
        long matchOnY = -2L;
        
        for (int i = 0; i < contextTable.size(); i++) {
            boolean creator = contextTable.isCreator(i);
            int xoff;
            if (creator) {
                xoff = 0;
                g.setFont(DEBUGGER_CONTEXT_TABLE_CONTEXT_CREATOR_FONT);
            } else {
                xoff = 10;
                g.setFont(DEBUGGER_CONTEXT_TABLE_CONTEXT_FONT);
            }
            String description = contextTable.getDescription(i);
            if(Objects.equals(contextTable.getIdentifier(i), blockedContextIdentifier)) {
                description = ">> " + description + " <<";
            }
            int descriptionWidth = g.getFontMetrics().stringWidth(description);
            width = Math.max(width, xoff + DEBUG_CONTEXT_TABLE_CELL_PADDING_X + descriptionWidth + DEBUG_CONTEXT_TABLE_CELL_PADDING_X);
        }
        
        int leftX = rightX - width;
        
        for (int i = 0; i < contextTable.size(); i++) {
            boolean creator = contextTable.isCreator(i);
            int xOff;
            if (creator) {
                xOff = 0;
                g.setFont(DEBUGGER_CONTEXT_TABLE_CONTEXT_CREATOR_FONT);
            } else {
                xOff = 10;
                g.setFont(DEBUGGER_CONTEXT_TABLE_CONTEXT_FONT);
            }
            long idc = contextTable.getIdentifier(i);
            String description = contextTable.getDescription(i);
            if(Objects.equals(contextTable.getIdentifier(i), blockedContextIdentifier)) {
                description = ">> " + description + " <<";
            }
            int descriptionSize = g.getFontMetrics().stringWidth(description);
            int topCell = topY + yOff;
            if(mouseY > topCell) {
                matchOnY = idc;
            }
            yOff += DEBUG_CONTEXT_TABLE_CELL_HEIGHT;
        }
                
        if (mouseX < leftX || mouseY < topY || mouseX > leftX + width) {
            return -2L;
        }
        
        AdditionalLogger.out.println("Context " + matchOnY + " auf dieser Position (" + mouseX + ", " + mouseY + ") in ContextTable gefunden");
                
        return matchOnY;
    }

    public static void drawDebugContextTable(Graphics2D g, int rightX, int topY, DebuggerRemote debugger, long actualContextIdentifier) {
        
        if (debugger == null) {
            AdditionalLogger.err.println("debugger ist null. zeichne nichts.");
            return;
        }
        
        actualContextIdentifier = debugger.ajustContextIdentifier(actualContextIdentifier);

        ContextTable contextTable = debugger.getContextTable();
        Long blockedContextIdentifier = debugger.getBlockedContextIdentifier();

        int width = 10;
        for (int i = 0; i < contextTable.size(); i++) {
            boolean creator = contextTable.isCreator(i);
            int xoff;
            if (creator) {
                xoff = 0;
                g.setFont(DEBUGGER_CONTEXT_TABLE_CONTEXT_CREATOR_FONT);
            } else {
                xoff = 10;
                g.setFont(DEBUGGER_CONTEXT_TABLE_CONTEXT_FONT);
            }
            String description = contextTable.getDescription(i);
            if(Objects.equals(contextTable.getIdentifier(i), blockedContextIdentifier)) {
                description = ">> " + description + " <<";
            }
            int descriptionWidth = g.getFontMetrics().stringWidth(description);
            width = Math.max(width, xoff + DEBUG_CONTEXT_TABLE_CELL_PADDING_X + descriptionWidth + DEBUG_CONTEXT_TABLE_CELL_PADDING_X);
        }
        
        int leftX = rightX - width;

        int yOff = 0;
        for (int i = 0; i < contextTable.size(); i++) {
            boolean creator = contextTable.isCreator(i);
            int xOff;
            if (creator) {
                xOff = 0;
                g.setFont(DEBUGGER_CONTEXT_TABLE_CONTEXT_CREATOR_FONT);
            } else {
                xOff = 10;
                g.setFont(DEBUGGER_CONTEXT_TABLE_CONTEXT_FONT);
            }
            String description = contextTable.getDescription(i);
            if(Objects.equals(contextTable.getIdentifier(i), blockedContextIdentifier)) {
                description = ">> " + description + " <<";
            }
            int descriptionSize = g.getFontMetrics().stringWidth(description);
            if (contextTable.getIdentifier(i).equals(actualContextIdentifier)) {
                g.setColor(DEBUGGER_CONTEXT_TABLE_ACTUAL_BACKGROUND_COLOR);
            } else {
                g.setColor(DEBUGGER_CONTEXT_TABLE_BACKGROUND_COLOR);
            }
            g.fillRect(leftX + xOff, topY + yOff, width - xOff, DEBUG_CONTEXT_TABLE_CELL_HEIGHT);
            g.setColor(DEBUGGER_CONTEXT_TABLE_BORDER_COLOR);
            g.drawRect(leftX + xOff, topY + yOff, width - xOff, DEBUG_CONTEXT_TABLE_CELL_HEIGHT);
            g.setColor(DEBUGGER_CONTEXT_TABLE_LABEL_COLOR);
            g.drawString(description, leftX + xOff + DEBUG_CONTEXT_TABLE_CELL_PADDING_X, topY + yOff + DEBUG_CONTEXT_TABLE_CELL_HEIGHT / 3 * 2);
            yOff += DEBUG_CONTEXT_TABLE_CELL_HEIGHT;
        }

    }

}
