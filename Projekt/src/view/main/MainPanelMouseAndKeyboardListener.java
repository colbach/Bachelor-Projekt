package view.main;

import exceptions.IllegalUserDialogInputException;
import view.main.connectoverlay.ConnectOverlayDrafter;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import log.AdditionalOut;
import log.Logging;
import model.InOutlet;
import model.Inlet;
import model.Node;
import model.Outlet;
import model.Project;
import utils.structures.Quadrupel;
import view.listener.MouseAndKeyboardListener;
import view.listener.SpecialKey;
import static view.main.MainPanelMode.*;
import static view.main.MainPanel.*;
import static view.Constants.*;
import view.dialogs.DirectInputDialogs;
import view.dialogs.ErrorDialog;
import view.main.rightclickmenue.OnNodeRightClickMenue;
import view.main.rightclickmenue.OnOverviewRightClickMenue;

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

        // Maus-Position aktualisieren...
        mainPanel.mouseX = x;
        mainPanel.mouseY = y;
        mainPanel.mousePressed = true;

        // Nachicht resetten...
        mainPanel.resetMessage();

        // mousePressed an RightClickMenue weiter geben...
        if (mainPanel.rightClickMenue != null) {
            boolean r = mainPanel.rightClickMenue.mousePressed(x, y);
            if (r) {
                return true;
            } else {
                mainPanel.rightClickMenue = null;
            }
        }

        if (mainPanel.mode == SHOW_OVERVIEW) {
            if (mainPanel.savedGraphics != null && mainPanel.window != null) {

                if (ctrlPressed) { // Node verbinden...

                    // Node ermitteln welches sich unter Maus befunden hat...
                    Node matchedNode = NodeDrafter.calcNodesForMousePosition((Graphics2D) mainPanel.savedGraphics, mainPanel.getOffX(), mainPanel.getOffY(), mainPanel.window.getProject().getNodes(), x, y);

                    if (matchedNode != null) { // Falls Maus sich ueber einem Node befunden hat

                        if (matchedNode.isDirectInputNode()) { // DirectInput-Node

                            System.err.println("DirectInputNode kann nicht zu anderem Node verbunden werden.");

                        } else { // normale Node

                            mainPanel.connectingNodeFrom = matchedNode; // connectingNodeFrom setzen
                            mainPanel.mode = SHOW_OVERVIEW_AND_DRAG_CONNECTION_BETWEEN_NODES; // mode setzen
                        }
                    }

                } else { // Node Verschieben...

                    // Node ermitteln welches sich unter Maus befunden hat...
                    Node matchedNode = NodeDrafter.calcNodesForMousePosition((Graphics2D) mainPanel.savedGraphics, mainPanel.getOffX(), mainPanel.getOffY(), mainPanel.window.getProject().getNodes(), x, y);

                    if (matchedNode != null) { // Falls Maus sich ueber einem Node befunden hat

                        if (matchedNode.isDirectInputNode()) { // DirectInput-Node

                            // Neuen Wert setzen...
                            Object[] newUserSettableContent;
                            try {
                                newUserSettableContent = DirectInputDialogs.dialogForUserSettableContent(matchedNode.getUserSettableContentType(), matchedNode.getUserSettableContent(), mainPanel.window);
                                if (newUserSettableContent != null) { // Falls Vorgang nicht vom Benutzer abgebrochen wurde
                                    matchedNode.setUserSettableContent(newUserSettableContent);
                                }
                            } catch (IllegalUserDialogInputException ex) {
                                ErrorDialog.showErrorDialog(ex, mainPanel);
                            }

                        } else { // normale Node

                            mainPanel.window.getProject().setNodeToTop(matchedNode); // Node nach oben schieben (Node wir ueber anderen Nodes angezeigt)
                            mainPanel.draggingNode = matchedNode; // draggingNode setzen
                            mainPanel.mode = SHOW_OVERVIEW_AND_DRAG_NODE; // mode setzen
                        }

                    } else { // Maus hat sich nicht ueber einem Node befunden

                        if (mainPanel.mode == SHOW_OVERVIEW) { // Im Overview-Modus
                            if (isInsideOverviewArea(x, y) && mainPanel.rightClickMenue == null) {
                                mainPanel.targetX = x - mainPanel.getOffX();
                                mainPanel.targetY = y - mainPanel.getOffY();
                            }
                        }
                    }
                }

            } else { // Fehler
                System.err.println("savedGraphics ist null!");
            }
        } else if (mainPanel.mode == SHOW_CONNECTION_OVERLAY) {

            if(ConnectOverlayDrafter.mouseMatchSwapIcon(mainPanel.connectingNodeFrom, mainPanel.connectingNodeTo, mainPanel.opticalCenterX, mainPanel.opticalCenterY, x, y)) {
                mainPanel.overlaySwapButtonPressed = true;
                Node from = mainPanel.connectingNodeFrom;
                mainPanel.connectingNodeFrom = mainPanel.connectingNodeTo;
                mainPanel.connectingNodeTo = from;
                
            } else {
            
            Quadrupel<InOutlet, Integer, Integer, Boolean> matchedLet = ConnectOverlayDrafter.calcLetForMousePosition(mainPanel.connectingNodeFrom, mainPanel.connectingNodeTo, mainPanel.opticalCenterX, mainPanel.opticalCenterY, x, y);

            if (matchedLet != null) { // Falls Maus sich ueber einem Let befunden hat

                mainPanel.fromLet = matchedLet;
                mainPanel.mode = SHOW_CONNECTION_OVERLAY_AND_DRAG_CONNECTION_BETWEEN_LETS;
            }}

        }

        return true;
    }

    @Override
    public boolean mouseDragged(int lastX, int lastY, int actualX, int actualY, int startX, int startY) {
        // Maus-Position aktualisieren...
        mainPanel.mouseX = actualX;
        mainPanel.mouseY = actualY;

        if (mainPanel.mode == SHOW_OVERVIEW) {

            if (isInsideOverviewArea(startX, startY) && mainPanel.rightClickMenue == null) {
                // neues Target setzen...
                mainPanel.targetX = actualX - mainPanel.getOffX();
                mainPanel.targetY = actualY - mainPanel.getOffY();
            }

        } else if (mainPanel.mode == SHOW_OVERVIEW_AND_DRAG_NODE) { // Wenn ConnectOverlay nicht offen ist

            // gehaltene Ndoe neu positionieren...
            if (mainPanel.draggingNode != null) {
                mainPanel.draggingNode.setUiCenterX(mainPanel.draggingNode.getUICenterX() + actualX - lastX);
                mainPanel.draggingNode.setUiCenterY(mainPanel.draggingNode.getUICenterY() + actualY - lastY);
            }
        }

        return true;
    }

    @Override
    public boolean mouseReleased(int x, int y, int pressedX, int pressedY) {
        // Maus-Position aktualisieren...
        mainPanel.mouseX = x;
        mainPanel.mouseY = y;
        mainPanel.mousePressed = false;
        mainPanel.overlaySwapButtonPressed = false;

        // mouseReleased an RightClickMenue weiter geben...
        if (mainPanel.rightClickMenue != null) {
            mainPanel.rightClickMenue.mouseReleased();
            return true;
        }

        if (mainPanel.mode == SHOW_OVERVIEW_AND_DRAG_CONNECTION_BETWEEN_NODES) { // Node verbinden...

            // Node ermitteln welches sich unter Maus befunden hat...
            Node matchedNode = NodeDrafter.calcNodesForMousePosition((Graphics2D) mainPanel.savedGraphics, mainPanel.getOffX(), mainPanel.getOffY(), mainPanel.window.getProject().getNodes(), x, y);

            if (matchedNode != null && !matchedNode.isRelativeNode() && mainPanel.connectingNodeFrom != null && mainPanel.connectingNodeFrom != matchedNode) { // Falls Maus sich ueber einem Node befunden hat
                AdditionalOut.println(mainPanel.connectingNodeFrom.getName() + " -> " + matchedNode.getName());

                mainPanel.connectingNodeTo = matchedNode;
                mainPanel.mode = SHOW_CONNECTION_OVERLAY;

            } else { // Keine Node getroffen

                mainPanel.mode = SHOW_OVERVIEW;
            }

        } else if (mainPanel.mode == SHOW_OVERVIEW) {

            if (isInsideOverviewArea(pressedX, pressedY) && mainPanel.rightClickMenue == null) {
                // neues Target setzen...
                mainPanel.targetX = x - mainPanel.getOffX();
                mainPanel.targetY = y - mainPanel.getOffY();
            }

        } else if (mainPanel.mode == SHOW_OVERVIEW_AND_DRAG_NODE) {

            // holded Node zurucksetzen...
            mainPanel.draggingNode = null;
            mainPanel.mode = SHOW_OVERVIEW;

        } else if (mainPanel.mode == SHOW_CONNECTION_OVERLAY_AND_DRAG_CONNECTION_BETWEEN_LETS) {

            Quadrupel<InOutlet, Integer, Integer, Boolean> matchedLet = ConnectOverlayDrafter.calcLetForMousePosition(mainPanel.connectingNodeFrom, mainPanel.connectingNodeTo, mainPanel.opticalCenterX, mainPanel.opticalCenterY, x, y);

            if (matchedLet != null && matchedLet.a != mainPanel.fromLet.a) { // Falls Maus sich ueber einem Let befunden hat

                mainPanel.toLet = matchedLet;

                // Versuche Lets zu verbinden...
                try {
                    AdditionalOut.println("Versuche " + mainPanel.fromLet.a + " (Index: " + mainPanel.fromLet.b + ") mit " + mainPanel.toLet.a + " (Index: " + mainPanel.toLet.b + ") zu verbinden.");

                    // Inlet und Outlet bestimmen...
                    Inlet inlet;
                    int inletIndex;
                    Outlet outlet;
                    int outletIndex;
                    if (mainPanel.fromLet.a instanceof Inlet) {
                        inlet = (Inlet) mainPanel.fromLet.a;
                        inletIndex = mainPanel.fromLet.b;
                        outlet = (Outlet) mainPanel.toLet.a; // Kann Exception beim casten verursachen wenn Typ falsch
                        outletIndex = mainPanel.toLet.b;
                    } else {
                        inlet = (Inlet) mainPanel.toLet.a; // Kann Exception beim casten verursachen wenn Typ falsch
                        inletIndex = mainPanel.toLet.b;
                        outlet = (Outlet) mainPanel.fromLet.a; // Kann Exception beim casten verursachen wenn Typ falsch
                        outletIndex = mainPanel.fromLet.b;
                    }

                    // Verbinden...
                    if (inlet.canConnectToOutlet(outlet) && outlet.canConnectToInlet(inlet)) {
                        for (Outlet potentialRelativeNodeOutlet : inlet.getConnectedLets()) {
                            Node potentialRelativeNode = potentialRelativeNodeOutlet.getNode();
                            if (potentialRelativeNode.isRelativeNode()) {
                                mainPanel.window.getProject().removeNode(potentialRelativeNode);
                            }
                        }
                        inlet.connectOutlet(outlet, inletIndex);
                        outlet.connectInlet(inlet, outletIndex);
                    } else { // Fehlermeldung
                        mainPanel.setMessage("Eingang " + inlet.getName() + " (" + inlet.getType().toString() + ") kann nicht mit Ausgang " + inlet.getName() + " (" + outlet.getType().toString() + ") verbunden werden.", true);
                    }

                } catch (Exception e) {
                    //e.printStackTrace();
                    mainPanel.setMessage("Elemente können nicht verbunden werden (" + e.getMessage() + ").", true);
                }
            } else { // Maus hat sich nicht ueber einem Let befunden

                // Falls Verbindung vorhanden diese loeschen...
                mainPanel.fromLet.a.unconnectLetBilateral(mainPanel.fromLet.b);

            }

            mainPanel.mode = SHOW_CONNECTION_OVERLAY;

        }

        return true;
    }

    @Override
    public boolean mousePressedRight(int x, int y) {

        if (mainPanel.mode == SHOW_OVERVIEW) {
            Project project = mainPanel.window.getProject();
            Node matchedNode = NodeDrafter.calcNodesForMousePosition((Graphics2D) mainPanel.savedGraphics, mainPanel.getOffX(), mainPanel.getOffY(), project.getNodes(), x, y);
            if (matchedNode != null) {
                mainPanel.rightClickMenue = new OnNodeRightClickMenue(x, y, matchedNode, project, mainPanel.window);
            } else {
                mainPanel.targetX = x - mainPanel.getOffX();
                mainPanel.targetY = y - mainPanel.getOffY();
                mainPanel.rightClickMenue = new OnOverviewRightClickMenue(x, y, mainPanel.targetX, mainPanel.targetY, project, mainPanel.window);
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
        if (mainPanel.rightClickMenue != null) {
            boolean r = mainPanel.rightClickMenue.mouseClicked(x, y);
            if (r) {
                return true;
            }
        }

        return false;
    }

}
