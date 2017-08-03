/*
 * To change mainPanel license header, choose License Headers in Project Properties.
 * To change mainPanel template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.main;

import java.awt.Graphics2D;
import model.InOutlet;
import model.Node;
import model.runproject.debugger.Breakpoints;
import model.runproject.debugger.DebuggerRemote;
import model.runproject.executioncontrol.TerminationReason;
import settings.GeneralSettings;
import utils.structures.tuples.Quadrupel;
import static view.Constants.*;
import static view.main.MainPanelMode.*;
import view.main.additionalhelp.AdditionalHelpDrafter;
import view.main.debug.DebugOverviewAdditionalColorInfoDrafter;
import view.main.connectoverlay.ConnectOverlayDrafter;
import view.main.debug.DebugBlockedReasonDrafter;
import view.main.debug.DebugContextTableDrafter;
import view.main.debug.DebugNodeOverlayDrafter;
import view.main.rightclickmenue.RightClickMenueDrafter;
import view.main.showstate.ShowConnectionOverlayDefault;
import view.main.showstate.ShowConnectionOverlayAndDragConnectionBetweenLets;
import view.main.showstate.ShowOverviewDefault;
import view.main.showstate.ShowOverviewAndDragConnectionBetweenNodes;
import view.main.showstate.ShowRunningProject;
import view.main.showstate.ShowRunningProjectWithDebuggerDefault;
import view.main.showstate.ShowRunningProjectWithDebuggerAndInspectNode;
import view.main.showstate.ShowState;
import view.main.toolbar.Toolbar;
import view.main.toolbar.ToolbarDrafter;
import view.sharedcomponents.scrollbar.ScrollbarDrafter;
import view.main.showstate.ShowRunningProjectWithDebugger;

/**
 *
 * @author christiancolbach
 */
public class MainPanelDrafter {

    private static Breakpoints breakpoints = Breakpoints.getInstance();

    public static void drawMainPanel(Graphics2D g, MainPanel mainPanel) {
        ShowState showState = mainPanel.getShowState();

        // Hoehe und Breite...
        int width = 1000;
        int height = 1000;
        MainWindow window = mainPanel.getWindow();
        if (window != null) {
            width = window.getWidth();
            height = window.getHeight();
        }

        // Hintergrund zeichnen...
        BackgroundDrafter.drawBackground(g, mainPanel.getOffX(), mainPanel.getOffY(), 0, TOOLBAR_HEIGHT, width, height - TOOLBAR_HEIGHT);

        // Target zeichnen...
        if (showState.drawTargetEnabled()) {
            if (showState instanceof ShowOverviewDefault && ((ShowOverviewDefault) showState).getRightClickMenue() == null) {
                TargetDrafter.drawTarget(g, mainPanel.getTargetX() + mainPanel.getOffX(), mainPanel.getTargetY() + mainPanel.getOffY(), mainPanel.getTargetX(), mainPanel.getTargetY(), mainPanel.isMousePressed());
            } else {
                TargetDrafter.drawTarget(g, mainPanel.getTargetX() + mainPanel.getOffX(), mainPanel.getTargetY() + mainPanel.getOffY(), mainPanel.getTargetX(), mainPanel.getTargetY(), false);
            }
        }

        // Nodes aus Model laden...
        Node[] nodes;
        if (window != null) {
            nodes = window.getProject().getNodes();
        } else {
            nodes = new Node[0];
        }

        // Connections zeichnen...
        ConnectionDrafter.drawConnections(g, nodes, mainPanel.getOffX(), mainPanel.getOffY());

        // Nodes zeichnen...
        for (Node node : nodes) {
            try {
                if (breakpoints.is(node) /*&& startNode != node*/) {
                    NodeTagDrafter.drawNodeTag(g, node, "Haltemarke", mainPanel.getOffX(), mainPanel.getOffY(), mainPanel.getWidth(), mainPanel.getHeight());
                }
                NodeDrafter.drawNode(g, node, mainPanel.getOffX(), mainPanel.getOffY(), width, height, window.getDebuggerRemote(), mainPanel.getContextIdentifierForDebugger());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Toolbar zeichnen...
        Toolbar toolbar = mainPanel.getToolbar();
        if (toolbar != null) {
            ToolbarDrafter.drawToolbar(g, width, toolbar);
        } else {
            System.err.println("toolbar ist null!");
        }

        // Overlay zeichnen fall benoetigt...
        if (showState instanceof ShowConnectionOverlayDefault) {
            ShowConnectionOverlayDefault showConnectionOverlay = (ShowConnectionOverlayDefault) showState;
            if (showConnectionOverlay.getConnectingNodeFrom() != null && showConnectionOverlay.getConnectingNodeTo() != null) {
                ConnectOverlayDrafter.drawConnectOverlay(g, showConnectionOverlay.getConnectingNodeFrom(), showConnectionOverlay.getConnectingNodeTo(), mainPanel.getOpticalCenterX(), mainPanel.getOpticalCenterY(), showConnectionOverlay.isOverlaySwapButtonPressed());
            } else {
                System.err.println("connectingNodeFrom oder connectingNodeTo ist null!");
            }
        } else if (showState instanceof ShowConnectionOverlayAndDragConnectionBetweenLets) {
            ShowConnectionOverlayAndDragConnectionBetweenLets showConnectionOverlayAndDragConnectionBetweenLets = (ShowConnectionOverlayAndDragConnectionBetweenLets) showState;
            Quadrupel<InOutlet, Integer, Integer, Boolean> fromLet = showConnectionOverlayAndDragConnectionBetweenLets.getFromLet();
            Node connectingNodeFrom = showConnectionOverlayAndDragConnectionBetweenLets.getConnectingNodeFrom();
            Node connectingNodeTo = showConnectionOverlayAndDragConnectionBetweenLets.getConnectingNodeTo();
            if (connectingNodeFrom != null && connectingNodeTo != null && fromLet != null) {
                ConnectOverlayDrafter.drawConnectOverlay(g, connectingNodeFrom, connectingNodeTo, mainPanel.getOpticalCenterX(), mainPanel.getOpticalCenterY(), fromLet.c, fromLet.d, mainPanel.getMouseX(), mainPanel.getMouseY(), false);
            } else {
                System.err.println("connectingNodeFrom, connectingNodeTo oder fromLet ist null!");
            }
        }

        // DragConnectionLine zeichnen...
        if (showState instanceof ShowOverviewAndDragConnectionBetweenNodes) {
            ShowOverviewAndDragConnectionBetweenNodes showOverviewAndDragConnection = (ShowOverviewAndDragConnectionBetweenNodes) showState;
            Node connectingNodeFrom = showOverviewAndDragConnection.getConnectingNodeFrom();
            if (connectingNodeFrom != null) {
                DragConnectionDrafter.drawDragConnectionLineBetweenNodes(g, connectingNodeFrom.getUICenterX() + mainPanel.getOffX(), connectingNodeFrom.getUICenterY() + mainPanel.getOffY(), mainPanel.getMouseX(), mainPanel.getMouseY(), false);
                Node overNode = NodeDrafter.calcNodesForMousePosition(g, mainPanel.getOffX(), mainPanel.getOffY(), nodes, mainPanel.getMouseX(), mainPanel.getMouseY());
                if (connectingNodeFrom != overNode) {
                    NodeDrafter.drawHighlightForNode(g, overNode, mainPanel.getOffX(), mainPanel.getOffY(), width, height);
                }
            } else {
                System.err.println("connectingNodeFrom ist null!");
            }
        }

        // Project-Running-Overlay zeichnen...
        if (showState instanceof ShowRunningProject) { // Fall: Projekt lauft normal
            g.setColor(PROJECT_RUNNING_OVERLAY_COLOR);
            g.fillRect(0, TOOLBAR_HEIGHT + 1, mainPanel.getWidth(), mainPanel.getHeight() - TOOLBAR_HEIGHT);
        }
        if (showState instanceof ShowRunningProjectWithDebuggerDefault || showState instanceof ShowRunningProjectWithDebuggerAndInspectNode) { // Fall: Projekt wird debuggt
            g.setColor(DEBUG_PROJECT_RUNNING_OVERLAY_COLOR);
            g.fillRect(0, TOOLBAR_HEIGHT + 1, mainPanel.getWidth(), mainPanel.getHeight() - TOOLBAR_HEIGHT);
        }

        // Project-Finished-Overlay zeichnen...
        TerminationReason terminationReason = window.getProjectExecutionTerminationReason();
        if (terminationReason != null && terminationReason != TerminationReason.CANCELED) { // Fall: Projekt beendet und nicht vom Benutzer abgebrochen
            if (window.getProjectExecutionTerminationReason() == TerminationReason.RUNTIME_ERROR) {
                g.setColor(PROJECT_FAILED_OVERLAY_COLOR);
            } else {
                g.setColor(PROJECT_SUCCEEDED_OVERLAY_COLOR);
            }
            g.fillRect(0, TOOLBAR_HEIGHT + 1, mainPanel.getWidth(), mainPanel.getHeight() - TOOLBAR_HEIGHT);
        }

        // Message zeichnen
        String message = mainPanel.getMessage();
        if (message != null) {
            boolean messageError = mainPanel.isMessageError();
            MessageDrafter.drawMessage(g, message, mainPanel.getOpticalCenterX(), mainPanel.getHeight(), 350, messageError);
        }

        // Additional Help zeichnen...
        if (GeneralSettings.getInstance().getBoolean(GeneralSettings.VIEW_ADDITIONAL_HELP_KEY, true)) {
            int additionalHelpX = mainPanel.getWidth();
            int additionalHelpY = mainPanel.getHeight();
            if (mainPanel.getVerticalScrollbar().isVisible()) {
                additionalHelpX -= SCROLLBAR_WIDTH + 4;
            }
            if (mainPanel.getHorizontalScrollbar().isVisible()) {
                additionalHelpY -= SCROLLBAR_WIDTH + 4;
            }

            AdditionalHelpDrafter.drawAdditionalHelp(g, additionalHelpX, additionalHelpY, showState.getHelp());
        }

        // DebugOverviewAdditionalColorInfo zeichnen...
        {
            DebuggerRemote dr = window.getDebuggerRemote();
            if (dr != null && dr.isDebugging()) {
                DebugOverviewAdditionalColorInfoDrafter.drawDebugOverviewAdditionalColorInfo(g, -1, mainPanel.getHeight());
            }
        }

        // DebugNodeOverlay zeichnen...
        if (showState instanceof ShowRunningProjectWithDebuggerAndInspectNode) {
            ShowRunningProjectWithDebuggerAndInspectNode showRunningProjectWithDebuggerAndInspectNode = (ShowRunningProjectWithDebuggerAndInspectNode) showState;
            DebugNodeOverlayDrafter.drawDebugNodeOverlay(g, showRunningProjectWithDebuggerAndInspectNode.getInspectedNode(), mainPanel.getOpticalCenterX(), mainPanel.getOpticalCenterY(), window.getDebuggerRemote(), mainPanel.getContextIdentifierForDebugger());
        }

        // DebugContextTable zeichnen...
        if (showState instanceof ShowRunningProjectWithDebugger) {
            DebuggerRemote debuggerRemote = window.getDebuggerRemote();
            if (debuggerRemote == null) {
                System.err.println("debuggerRemote ist null! DebugContextTable nicht zeichnen.");
            } else {
                DebugContextTableDrafter.drawDebugContextTable(g, width - DEBUG_CONTEXT_TABLE_X_RIGHT_SPACE, DEBUG_CONTEXT_TABLE_Y, debuggerRemote, mainPanel.getContextIdentifierForDebugger());
                DebugBlockedReasonDrafter.drawDebugBlockedReason(g, -1, TOOLBAR_HEIGHT, debuggerRemote);
            }
        }

        // Scrollbars zeichnen...
        ScrollbarDrafter.drawScrollbar(g, mainPanel.getVerticalScrollbar());
        ScrollbarDrafter.drawScrollbar(g, mainPanel.getHorizontalScrollbar());

        // Right-Click-Menue...
        if (mainPanel.getRightClickMenue() != null) {
            RightClickMenueDrafter.drawLeftClickMenue(g, mainPanel.getRightClickMenue());
        }

        // DrawCounter zeichnen...
        /*drawCounterForDebugging++;
        if(mainPanel.settings.getBoolean(Settings.DEVELOPER_REDRAW_COUNTER_KEY, false)) {
            g.setColor(Color.lightGray);
            g.setFont(DEVELOPER_INFO_FONT);
            String drawCounterForDebuggingSring = String.valueOf(drawCounterForDebugging);
            g.drawString(drawCounterForDebuggingSring, getWidth() - g.getFontMetrics().stringWidth(drawCounterForDebuggingSring) - 3, 10);
        }*/
    }

}
