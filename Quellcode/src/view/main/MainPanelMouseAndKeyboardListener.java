package view.main;

import generalexceptions.IllegalUserDialogInputException;
import view.main.connectoverlay.ConnectOverlayDrafter;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import logging.AdditionalLogger;
import logging.AdditionalOut;
import logging.AdvancedLogger;
import model.InOutlet;
import model.Inlet;
import model.Node;
import model.Outlet;
import model.Project;
import utils.structures.tuples.Quadrupel;
import view.listener.MouseAndKeyboardListener;
import view.listener.SpecialKey;
import static view.main.MainPanelMode.*;
import static view.main.MainPanel.*;
import static view.Constants.*;
import view.dialogs.DirectInputDialogs;
import view.dialogs.ErrorDialog;
import view.main.debug.DebugContextTableDrafter;
import view.main.rightclickmenue.OnNodeRightClickMenue;
import view.main.rightclickmenue.OnOverviewRightClickMenue;
import view.main.rightclickmenue.RightClickMenue;
import view.main.showstate.RunningViewState;
import view.main.showstate.ShowConnectionOverlay;
import view.main.showstate.ShowConnectionOverlayAndDragConnectionBetweenLets;
import view.main.showstate.ShowConnectionOverlayDefault;
import view.main.showstate.ShowOverviewAndDragConnectionBetweenNodes;
import view.main.showstate.ShowOverviewAndDragNode;
import view.main.showstate.ShowOverviewDefault;
import view.main.showstate.ShowRunningProjectWithDebuggerAndInspectNode;
import view.main.showstate.ShowRunningProjectWithDebuggerDefault;
import view.main.showstate.ShowState;

/**
 * MouseAndKeyboardListener von MainPanel. Diese Klasse ist eng verwurzelt mit
 * MainPanel und dient hauptsaechlich dazu diese zu entlasten. Aus diesem Grund
 * gibt es viele direktverweise auf Elemente von MainPanel.
 */
public class MainPanelMouseAndKeyboardListener extends MouseAndKeyboardListener {

    private final MainPanel mainPanel;
    private boolean ctrlPressed = false;

    public MainPanelMouseAndKeyboardListener(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    @Override
    public boolean mousePressed(int x, int y) {
        ShowState showState = mainPanel.getShowState();
        MainWindow window = mainPanel.getWindow();

        // Maus-Position aktualisieren...
        mainPanel.setMouseX(x);
        mainPanel.setMouseY(y);
        mainPanel.setMousePressed(true);

        // Nachicht resetten...
        mainPanel.resetMessage();

        // Project-Finished-Overlay schliessen...
        if (window.isProjectExecutionTerminated()) {
            window.destroyProjectExecution();
            if (showState instanceof RunningViewState) {
                RunningViewState runningViewState = (RunningViewState) showState;
                mainPanel.setShowState(runningViewState.backToOverview());
            } else {
                System.err.println("showState implementiert RunningViewState nicht");
            }
        }

        // mousePressed an RightClickMenue weiter geben...
        RightClickMenue rightClickMenue = mainPanel.getRightClickMenue();
        if (rightClickMenue != null) {
            boolean r = rightClickMenue.mousePressed(x, y);
            if (r) {
                return true;
            } else {
                mainPanel.setRightClickMenue(null);
            }
        }

        if (showState instanceof ShowOverviewDefault) {
            ShowOverviewDefault showOverviewDefault = (ShowOverviewDefault) showState;

            if (mainPanel.savedGraphics != null && mainPanel.getWindow() != null) {

                if (ctrlPressed) { // Node verbinden...

                    // Node ermitteln welches sich unter Maus befunden hat...
                    Node matchedNode = NodeDrafter.calcNodesForMousePosition((Graphics2D) mainPanel.savedGraphics, mainPanel.getOffX(), mainPanel.getOffY(), mainPanel.getWindow().getProject().getNodes(), x, y);

                    if (matchedNode != null) { // Falls Maus sich ueber einem Node befunden hat

                        if (matchedNode.isDirectInputNode()) { // DirectInput-Node
                            System.err.println("DirectInputNode kann nicht zu anderem Node verbunden werden.");

                        } else { // normale Node
                            mainPanel.setShowState(new ShowOverviewAndDragConnectionBetweenNodes(showOverviewDefault, matchedNode));
                        }
                    }

                } else { // Node Verschieben...

                    // Node ermitteln welches sich unter Maus befunden hat...
                    Node matchedNode = NodeDrafter.calcNodesForMousePosition((Graphics2D) mainPanel.savedGraphics, mainPanel.getOffX(), mainPanel.getOffY(), mainPanel.getWindow().getProject().getNodes(), x, y);

                    if (matchedNode != null) { // Falls Maus sich ueber einem Node befunden hat

                        if (matchedNode.isDirectInputNode()) { // DirectInput-Node

                            // Neuen Wert setzen...
                            Object[] newUserSettableContent;
                            try {
                                newUserSettableContent = DirectInputDialogs.dialogForUserSettableContent(matchedNode.getRelativeNode(), matchedNode.getUserSettableContentType(), matchedNode.getUserSettableContent(), mainPanel.getWindow(), mainPanel.getWindow().getProject());
                                if (newUserSettableContent != null) { // Falls Vorgang nicht vom Benutzer abgebrochen wurde
                                    matchedNode.setUserSettableContent(newUserSettableContent);
                                }
                            } catch (IllegalUserDialogInputException ex) {
                                ErrorDialog.showErrorDialog(ex, mainPanel);
                            }

                        } else { // normale Node

                            mainPanel.getWindow().getProject().setNodeToTop(matchedNode); // Node nach oben schieben (Node wir ueber anderen Nodes angezeigt)
                            mainPanel.setShowState(new ShowOverviewAndDragNode(showOverviewDefault, matchedNode));
                        }

                    } else { // Maus hat sich nicht ueber einem Node befunden

                        if (isInsideOverviewArea(x, y) && mainPanel.getRightClickMenue() == null) {
                            mainPanel.setTargetX(x - mainPanel.getOffX());
                            mainPanel.setTargetY(y - mainPanel.getOffY());
                        }
                    }
                }

            } else { // Fehler
                System.err.println("savedGraphics ist null!");
            }

        } else if (showState instanceof ShowConnectionOverlay) {
            ShowConnectionOverlay showConnectionOverlay = (ShowConnectionOverlay) showState;
            Node connectingNodeFrom = showConnectionOverlay.getConnectingNodeFrom();
            Node connectingNodeTo = showConnectionOverlay.getConnectingNodeTo();

            if (ConnectOverlayDrafter.mouseMatchSwapIcon(connectingNodeFrom, connectingNodeTo, mainPanel.getOpticalCenterX(), mainPanel.getOpticalCenterY(), x, y)) {
                showConnectionOverlay.setOverlaySwapButtonPressed(true);
                showConnectionOverlay.swapNodes();

            } else {

                Quadrupel<InOutlet, Integer, Integer, Boolean> matchedLet = ConnectOverlayDrafter.calcLetForMousePosition(connectingNodeFrom, connectingNodeTo, mainPanel.getOpticalCenterX(), mainPanel.getOpticalCenterY(), x, y);

                if (matchedLet != null) { // Falls Maus sich ueber einem Let befunden hat

                    mainPanel.setShowState(new ShowConnectionOverlayAndDragConnectionBetweenLets(showConnectionOverlay, matchedLet));
                }
            }

        } else if (showState instanceof ShowRunningProjectWithDebuggerDefault) {
            ShowRunningProjectWithDebuggerDefault showRunningProjectWithDebuggerDefault = (ShowRunningProjectWithDebuggerDefault) showState;

            long matchedContext = DebugContextTableDrafter.calcDebugContextForMousePosition((Graphics2D) mainPanel.savedGraphics, x, y, mainPanel.getWidth() - DEBUG_CONTEXT_TABLE_X_RIGHT_SPACE, DEBUG_CONTEXT_TABLE_Y, mainPanel.getWindow().getDebuggerRemote());
            if (matchedContext >= 0) {
                mainPanel.setContextIdentifierForDebugger(matchedContext);

            } else {

                // Node ermitteln welches sich unter Maus befunden hat...
                Node matchedNode = NodeDrafter.calcNodesForMousePosition((Graphics2D) mainPanel.savedGraphics, mainPanel.getOffX(), mainPanel.getOffY(), mainPanel.getWindow().getProject().getNodes(), x, y);

                if (matchedNode != null) { // Falls Maus sich ueber einem Node befunden hat

                    mainPanel.setShowState(new ShowRunningProjectWithDebuggerAndInspectNode(showRunningProjectWithDebuggerDefault, matchedNode));
                }
            }
        }

        return true;
    }

    @Override
    public boolean mouseDragged(int lastX, int lastY, int actualX, int actualY, int startX, int startY) {
        ShowState showState = mainPanel.getShowState();

        // Maus-Position aktualisieren...
        mainPanel.setMouseX(actualX);
        mainPanel.setMouseY(actualY);

        if (showState instanceof ShowOverviewDefault) {
            ShowOverviewDefault showOverviewDefault = (ShowOverviewDefault) showState;

            if (isInsideOverviewArea(startX, startY) && mainPanel.getRightClickMenue() == null) {
                // neues Target setzen...
                mainPanel.setTargetX(actualX - mainPanel.getOffX());
                mainPanel.setTargetY(actualY - mainPanel.getOffY());
            }

        } else if (showState instanceof ShowOverviewAndDragNode) { // Wenn ConnectOverlay nicht offen ist
            ShowOverviewAndDragNode showOverviewAndDragNode = (ShowOverviewAndDragNode) showState;

            // gehaltene Ndoe neu positionieren...
            Node draggingNode = showOverviewAndDragNode.getDraggingNode();
            if (draggingNode != null) {
                draggingNode.setUiCenterX(draggingNode.getUICenterX() + actualX - lastX);
                draggingNode.setUiCenterY(draggingNode.getUICenterY() + actualY - lastY);
            }
        }

        return true;
    }

    @Override
    public boolean mouseReleased(int x, int y, int pressedX, int pressedY) {
        ShowState showState = mainPanel.getShowState();

        // Maus-Position aktualisieren...
        mainPanel.setMouseX(x);
        mainPanel.setMouseY(y);
        mainPanel.setMousePressed(false);

        if (showState instanceof ShowConnectionOverlay) {
            ShowConnectionOverlay showConnectionOverlay = (ShowConnectionOverlay) showState;
            showConnectionOverlay.setOverlaySwapButtonPressed(false);
        }

        // mouseReleased an RightClickMenue weiter geben...
        RightClickMenue rightClickMenue = mainPanel.getRightClickMenue();
        if (rightClickMenue != null) {
            rightClickMenue.mouseReleased();
            return true;
        }

        if (showState instanceof ShowOverviewAndDragConnectionBetweenNodes) { // Node verbinden...
            ShowOverviewAndDragConnectionBetweenNodes showOverviewAndDragConnectionBetweenNodes = (ShowOverviewAndDragConnectionBetweenNodes) showState;
            Node connectingNodeFrom = showOverviewAndDragConnectionBetweenNodes.getConnectingNodeFrom();

            // Node ermitteln welches sich unter Maus befunden hat...
            Node matchedNode = NodeDrafter.calcNodesForMousePosition((Graphics2D) mainPanel.savedGraphics, mainPanel.getOffX(), mainPanel.getOffY(), mainPanel.getWindow().getProject().getNodes(), x, y);

            if (matchedNode != null && !matchedNode.isRelativeNode() && connectingNodeFrom != null && connectingNodeFrom != matchedNode) { // Falls Maus sich ueber einem Node befunden hat
                AdditionalLogger.out.println(connectingNodeFrom.getName() + " -> " + matchedNode.getName());

                mainPanel.setShowState(new ShowConnectionOverlayDefault(showOverviewAndDragConnectionBetweenNodes.back(), connectingNodeFrom, matchedNode));

            } else { // Keine Node getroffen

                mainPanel.setShowState(showOverviewAndDragConnectionBetweenNodes.back());
            }

        } else if (showState instanceof ShowOverviewDefault) {

            if (isInsideOverviewArea(pressedX, pressedY) && mainPanel.getRightClickMenue() == null) {
                // neues Target setzen...
                mainPanel.setTargetX(x - mainPanel.getOffX());
                mainPanel.setTargetY(y - mainPanel.getOffY());
            }

        } else if (showState instanceof ShowOverviewAndDragNode) {
            ShowOverviewAndDragNode showOverviewAndDragNode = (ShowOverviewAndDragNode) showState;

            // holded Node zurucksetzen...
            mainPanel.setShowState(showOverviewAndDragNode.back());

        } else if (showState instanceof ShowConnectionOverlayAndDragConnectionBetweenLets) {
            ShowConnectionOverlayAndDragConnectionBetweenLets showConnectionOverlayAndDragConnectionBetweenLets = (ShowConnectionOverlayAndDragConnectionBetweenLets) showState;
            Node connectingNodeFrom = showConnectionOverlayAndDragConnectionBetweenLets.getConnectingNodeFrom();
            Node connectingNodeTo = showConnectionOverlayAndDragConnectionBetweenLets.getConnectingNodeTo();
            Quadrupel<InOutlet, Integer, Integer, Boolean> fromLet = showConnectionOverlayAndDragConnectionBetweenLets.getFromLet();

            Quadrupel<InOutlet, Integer, Integer, Boolean> matchedLet = ConnectOverlayDrafter.calcLetForMousePosition(connectingNodeFrom, connectingNodeTo, mainPanel.getOpticalCenterX(), mainPanel.getOpticalCenterY(), x, y);

            if (matchedLet != null && matchedLet.a != fromLet.a) { // Falls Maus sich ueber einem Let befunden hat

                Quadrupel<InOutlet, Integer, Integer, Boolean> toLet = matchedLet;

                // Versuche Lets zu verbinden...
                try {
                    AdditionalLogger.out.println("Versuche " + fromLet.a + " (Index: " + fromLet.b + ") mit " + toLet.a + " (Index: " + toLet.b + ") zu verbinden.");

                    // Inlet und Outlet bestimmen...
                    Inlet inlet;
                    int inletIndex;
                    Outlet outlet;
                    int outletIndex;
                    if (fromLet.a instanceof Inlet) {
                        inlet = (Inlet) fromLet.a;
                        inletIndex = fromLet.b;
                        outlet = (Outlet) toLet.a; // Kann Exception beim casten verursachen wenn Typ falsch
                        outletIndex = toLet.b;
                    } else {
                        inlet = (Inlet) toLet.a; // Kann Exception beim casten verursachen wenn Typ falsch
                        inletIndex = toLet.b;
                        outlet = (Outlet) fromLet.a; // Kann Exception beim casten verursachen wenn Typ falsch
                        outletIndex = fromLet.b;
                    }

                    // Verbinden...
                    if (inlet.canConnectToOutlet(outlet) && outlet.canConnectToInlet(inlet)) {
                        for (Outlet potentialRelativeNodeOutlet : inlet.getConnectedLets()) {
                            Node potentialRelativeNode = potentialRelativeNodeOutlet.getNode();
                            if (potentialRelativeNode.isRelativeNode()) {
                                mainPanel.getWindow().getProject().removeNode(potentialRelativeNode);
                            }
                        }
                        inlet.connectOutlet(outlet, inletIndex);
                        outlet.connectInlet(inlet, outletIndex);

                    } else {
                        // showConnectionOverlayAndDragConnectionBetweenLets beenden...
                        mainPanel.setShowState(mainPanel.getShowState().back());

                        // Fehlermeldung...
                        mainPanel.setMessage("Eingang " + inlet.getName() + " (" + inlet.getType().toString() + ") kann nicht mit Ausgang " + inlet.getName() + " (" + outlet.getType().toString() + ") verbunden werden.", true);
                    }

                } catch (Exception e) {
                    //e.printStackTrace();
                    mainPanel.setMessage("Elemente können nicht verbunden werden (" + e.getMessage() + ").", true);
                }
            } else { // Maus hat sich nicht ueber einem Let befunden
                
                // Fals es sich um DirectInput handelt diese ganz loeschen...
                InOutlet connectedLet = fromLet.a.getConnectedLet(fromLet.b);
                if(connectedLet != null && connectedLet.getNode().isDirectInputNode()) {
                    mainPanel.getWindow().getProject().removeNode(connectedLet.getNode());
                }
                        
                // Falls Verbindung vorhanden diese loeschen...
                fromLet.a.unconnectLetBilateral(fromLet.b);
                
                // showConnectionOverlayAndDragConnectionBetweenLets beenden...
                mainPanel.setShowState(mainPanel.getShowState().back());

            }

            mainPanel.setShowState(showConnectionOverlayAndDragConnectionBetweenLets.back());

        }

        return true;
    }

    @Override
    public boolean mousePressedRight(int x, int y) {
        ShowState showState = mainPanel.getShowState();

        if (showState instanceof ShowOverviewDefault) {

            Project project = mainPanel.getWindow().getProject();
            Node matchedNode = NodeDrafter.calcNodesForMousePosition((Graphics2D) mainPanel.savedGraphics, mainPanel.getOffX(), mainPanel.getOffY(), project.getNodes(), x, y);
            if (matchedNode != null) {
                mainPanel.setRightClickMenue(new OnNodeRightClickMenue(x, y, matchedNode, project, mainPanel.getWindow()));
            } else {
                mainPanel.setTargetX(x - mainPanel.getOffX());
                mainPanel.setTargetY(y - mainPanel.getOffY());
                mainPanel.setRightClickMenue(new OnOverviewRightClickMenue(x, y, mainPanel.getTargetX(), mainPanel.getTargetY(), project, mainPanel.getWindow()));
            }

            return false;
        }
        return false;
    }

    @Override
    public boolean specialKeyPressed(SpecialKey specialKey) {
        if (specialKey == SpecialKey.CTRL) {
            ctrlPressed = true;
        }
        return false;
    }

    @Override
    public boolean specialKeyReleased(SpecialKey specialKey) {
        ctrlPressed = false;
        if (specialKey == SpecialKey.ESC) {
            mainPanel.esc();
        } else if (specialKey == SpecialKey.ENTER) {
            mainPanel.enter();
        }
        return false;
    }

    public boolean isInsideOverviewArea(int x, int y) {
        // Berechnen wo Scrollbar beginnt...
        int scrollBarBeginsX;
        if (mainPanel.getVerticalScrollbar().isVisible()) {
            scrollBarBeginsX = mainPanel.getWidth() - SCROLLBAR_WIDTH - 5;
        } else {
            scrollBarBeginsX = mainPanel.getWidth();
        }
        int scrollBarBeginsY;
        if (mainPanel.getHorizontalScrollbar().isVisible()) {
            scrollBarBeginsY = mainPanel.getHeight() - SCROLLBAR_WIDTH - 5;
        } else {
            scrollBarBeginsY = mainPanel.getHeight();
        }

        if (x < scrollBarBeginsX && y < scrollBarBeginsY && y > TOOLBAR_HEIGHT) { // liegt unter Toolbar und vor Scrollbars
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseClicked(int x, int y) {

        // mouseClicked an RightClickMenue weiter geben...
        RightClickMenue rightClickMenue = mainPanel.getRightClickMenue();
        if (rightClickMenue != null) {
            boolean r = rightClickMenue.mouseClicked(x, y);
            if (r) {
                return true;
            }
        }

        return false;
    }

}
