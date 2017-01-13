package view.main;

import view.sharedcomponents.scrollbar.ScrollbarDrafter;
import view.sharedcomponents.scrollbar.Direction;
import view.sharedcomponents.scrollbar.Scrollbar;
import java.awt.Color;
import view.main.connectoverlay.ConnectOverlayDrafter;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.swing.JPanel;
import model.*;
import settings.GeneralSettings;
import utils.measurements.Area;
import utils.measurements.NonArea;
import utils.structures.*;
import static view.Constants.*;
import static view.main.MainPanelMode.*;
import view.main.additionalhelp.*;
import ALT.DirectInputMenue;
import model.runproject.Breakpoints;
import view.main.rightclickmenue.RightClickMenue;
import view.main.rightclickmenue.RightClickMenueDrafter;
import view.main.toolbar.*;

public final class MainPanel extends JPanel {

    //private long drawCounterForDebugging = 0;
    protected MainPanelMode mode = SHOW_OVERVIEW;
    protected MainPanelMode preservedMode = null;
    protected MainWindow window;
    private Breakpoints breakpoints = Breakpoints.getInstance();
    protected Toolbar toolbar;
    protected int targetX = 50, targetY = 100;
    protected int mouseX = -1, mouseY = -1;
    protected boolean mousePressed = false;
    protected boolean overlaySwapButtonPressed = false;
    protected Node draggingNode = null;
    protected Node connectingNodeFrom = null, connectingNodeTo = null;
    protected int opticalCenterX, opticalCenterY;
    protected Quadrupel<InOutlet /*let*/, Integer /*index in diesem Let*/, Integer /*virtueller Index*/, Boolean /*true=links, false=rechts*/> fromLet = null, toLet = null;
    protected String message = null;
    protected boolean messageIsError = false;
    protected final GeneralSettings settings;
    private final AdditionalHelpDescription additionalHelpDescriptionForOverviewMode;
    private final AdditionalHelpDescription additionalHelpDescriptionForConnectionMode;
    private final AdditionalHelpDescription additionalHelpDescriptionForOverviewModeWhileDraggingConnection;
    private final AdditionalHelpDescription additionalHelpDescriptionForOverviewModeWhileDraggingNode;
    private final AdditionalHelpDescription additionalHelpDescriptionForOverviewModeWithMessage;
    private final AdditionalHelpDescription additionalHelpDescriptionForConnectionModeWithMessage;
    private final MainPanelMouseAndKeyboardListener mainPanelMouseAndKeyboardListener;
    private final Scrollbar verticalScrollbar, horizontalScrollbar;
    private Area areaForViewing = NonArea.getInstance();
    protected RightClickMenue rightClickMenue = null;

    /**
     * Graphics-Pointer fuer MouseEvents. Soll nicht zum zeichnen benutzt
     * werden!
     */
    protected Graphics savedGraphics = null;

    public MainPanel(MainWindow window) {
        this.mainPanelMouseAndKeyboardListener = new MainPanelMouseAndKeyboardListener(this);
        this.settings = GeneralSettings.getInstance();
        this.additionalHelpDescriptionForOverviewMode = new AdditionalHelpDescription(new String[]{"", "", "Control"}, new String[]{"Rechtsklick auf Element um mehr Optionen anzuzeigen", "Rechtsklick auf leere Fläche um Elemente zu erstellen", "gedrückt halten um Elemente zu verbinden"});
        this.additionalHelpDescriptionForConnectionMode = new AdditionalHelpDescription("ESC", "drücken um Overlay schliessen");
        this.additionalHelpDescriptionForOverviewModeWhileDraggingNode = new AdditionalHelpDescription("", "Loslassen um Element zu platzieren");
        this.additionalHelpDescriptionForConnectionModeWithMessage = new AdditionalHelpDescription("ESC", "um Nachicht zu verbergen");
        this.additionalHelpDescriptionForOverviewModeWithMessage = new AdditionalHelpDescription(new String[]{"ESC", "Control"}, new String[]{"um Nachicht zu verbergen", "gedrückt halten um Elemente zu verbinden"});
        this.additionalHelpDescriptionForOverviewModeWhileDraggingConnection = new AdditionalHelpDescription(new String[]{"", "ESC"}, new String[]{"Zum Verbinden über Element loslassen", "Zum abbrechen"});
        this.window = window;
        verticalScrollbar = new Scrollbar(2000, 0, Direction.VERTICAL, NonArea.getInstance());
        horizontalScrollbar = new Scrollbar(2000, 0, Direction.HORIZONTAL, NonArea.getInstance());
        try {
            this.toolbar = new Toolbar(window);
            updateAreaForDisplay();
            updateScrollbarRepresendedArea();
        } catch (IOException ex) {
            System.err.println("Toolbar konte nicht geladen werden!");
        }
    }

    public void updateAreaForDisplay() {
        Area area = this.window.getProject().getNeededProjectArea();
        area.setX(area.getX() - 40);
        if (area.getX() > 0) {
            area.setWidth(area.getWidth() + area.getX());
            area.setX(0);
        }
        area.setY(area.getY() - 40);
        if (area.getY() > 0) {
            area.setHeight(area.getHeight() + area.getY());
            area.setY(0);
        }
        area.setWidth(area.getWidth() + 100);
        area.setHeight(area.getHeight() + 150);
        areaForViewing = area;
        updateScrollbarRepresendedArea();
    }

    public void updateScrollbarBarAreas() {
        verticalScrollbar.setArea(new Area(getWidth() - SCROLLBAR_WIDTH - 2, TOOLBAR_HEIGHT + 2, SCROLLBAR_WIDTH, getHeight() - TOOLBAR_HEIGHT - SCROLLBAR_WIDTH - 5));
        horizontalScrollbar.setArea(new Area(2, getHeight() - SCROLLBAR_WIDTH - 2, getWidth() - SCROLLBAR_WIDTH - 5, SCROLLBAR_WIDTH));
    }

    /**
     * Passt representierte Flaeche von Scrollbar an. Wird automatisch nach
     * updateAreaForDisplay() aufgerufen.
     */
    private void updateScrollbarRepresendedArea() {
        verticalScrollbar.setRepresentedHeightOrWidth(areaForViewing.getHeight());
        horizontalScrollbar.setRepresentedHeightOrWidth(areaForViewing.getWidth());
    }

    public MainPanelMouseAndKeyboardListener getMainPanelMouseAndKeyboardListener() {
        return mainPanelMouseAndKeyboardListener;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        try {
            savedGraphics = graphics;

            long nanoTime = System.nanoTime();

            updateScrollbarBarAreas();
            updateAreaForDisplay();

            // Graphics ind Graphics2D casten...
            Graphics2D g = (Graphics2D) graphics;

            // Hoehe und Breite...
            int width = 1000;
            int height = 1000;
            if (window != null) {
                width = window.getWidth();
                height = window.getHeight();
            }

            // Zentrum aktualisieren...
            this.opticalCenterX = width / 2;
            this.opticalCenterY = height / 2 + 10;

            // Hintergrund zeichnen...
            BackgroundDrafter.drawBackground(g, getOffX(), getOffY(), 0, TOOLBAR_HEIGHT, width, height - TOOLBAR_HEIGHT);

            // Target zeichnen...
            if (this.mode == SHOW_OVERVIEW && this.rightClickMenue == null) {
                TargetDrafter.drawTarget(g, targetX + getOffX(), targetY + getOffY(), targetX, targetY, mousePressed);
            } else {
                TargetDrafter.drawTarget(g, targetX + getOffX(), targetY + getOffY(), targetX, targetY, false);
            }

            // Nodes aus Model laden...
            Node[] nodes;
            if (window != null) {
                nodes = window.getProject().getNodes();
            } else {
                nodes = new Node[0];
            }

            // Connections zeichnen...
            ConnectionDrafter.drawConnections(g, nodes, getOffX(), getOffY());

            // StartNode-Tag zeichnen...
            Node startNode = window.getProject().getStartNode();
            if (breakpoints.is(startNode)) {
                NodeTagDrafter.drawNodeTag(g, startNode, "Start & Haltemarke", getOffX(), getOffY(), getWidth(), getHeight());
            } else {
                NodeTagDrafter.drawNodeTag(g, startNode, "Start", getOffX(), getOffY(), getWidth(), getHeight());
            }

            // Nodes zeichnen...
            for (Node node : nodes) {
                try {
                    if (breakpoints.is(node) && startNode != node) {
                        NodeTagDrafter.drawNodeTag(g, node, "Haltemarke", getOffX(), getOffY(), getWidth(), getHeight());
                    }
                    NodeDrafter.drawNode(g, node, getOffX(), getOffY(), getWidth(), getHeight(), window.getDebugger());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Toolbar zeichnen...
            if (toolbar != null) {
                ToolbarDrafter.drawToolbar(g, width, toolbar);
            } else {
                System.err.println("toolbar ist null!");
            }

            // Overlay zeichnen fall benoetigt...
            if (mode == SHOW_CONNECTION_OVERLAY) {
                if (this.connectingNodeFrom != null && this.connectingNodeTo != null) {
                    ConnectOverlayDrafter.drawConnectOverlay(g, this.connectingNodeFrom, this.connectingNodeTo, opticalCenterX, opticalCenterY, overlaySwapButtonPressed);
                } else {
                    System.err.println("connectingNodeFrom oder connectingNodeTo ist null!");
                }
            } else if (mode == SHOW_CONNECTION_OVERLAY_AND_DRAG_CONNECTION_BETWEEN_LETS) {
                if (this.connectingNodeFrom != null && this.connectingNodeTo != null && this.fromLet != null) {
                    ConnectOverlayDrafter.drawConnectOverlay(g, this.connectingNodeFrom, this.connectingNodeTo, opticalCenterX, opticalCenterY, this.fromLet.c, this.fromLet.d, this.mouseX, this.mouseY, overlaySwapButtonPressed);
                } else {
                    System.err.println("connectingNodeFrom, connectingNodeTo oder fromLet ist null!");
                }
            }

            // DragConnectionLine zeichnen...
            if (mode == SHOW_OVERVIEW_AND_DRAG_CONNECTION_BETWEEN_NODES) {
                if (connectingNodeFrom != null) {
                    DragConnectionDrafter.drawDragConnectionLineBetweenNodes(g, connectingNodeFrom.getUICenterX() + getOffX(), connectingNodeFrom.getUICenterY() + getOffY(), mouseX, mouseY, false);
                    Node overNode = NodeDrafter.calcNodesForMousePosition(g, getOffX(), getOffY(), nodes, mouseX, mouseY);
                    if (connectingNodeFrom != overNode) {
                        NodeDrafter.drawHighlightForNode(g, overNode, getOffX(), getOffY(), width, height);
                    }
                } else {
                    System.err.println("connectingNodeFrom ist null!");
                }
            }

            // Message zeichnen
            if (message != null) {
                MessageDrafter.drawMessage(g, this.message, opticalCenterX, getHeight(), 350, messageIsError);
            }

            // Additional Help zeichnen...
            if (this.settings.getBoolean(GeneralSettings.VIEW_ADDITIONAL_HELP_KEY, true)) {
                int additionalHelpX = getWidth();
                int additionalHelpY = getHeight();
                if (verticalScrollbar.isVisible()) {
                    additionalHelpX -= SCROLLBAR_WIDTH + 4;
                }
                if (horizontalScrollbar.isVisible()) {
                    additionalHelpY -= SCROLLBAR_WIDTH + 4;
                }
                if (this.mode == SHOW_OVERVIEW && this.message != null) {
                    AdditionalHelpDrafter.drawAdditionalHelp(g, additionalHelpX, additionalHelpY, additionalHelpDescriptionForConnectionModeWithMessage);
                } else if (this.mode == SHOW_OVERVIEW_AND_DRAG_NODE) {
                    AdditionalHelpDrafter.drawAdditionalHelp(g, additionalHelpX, additionalHelpY, additionalHelpDescriptionForOverviewModeWhileDraggingNode);
                } else if (this.mode == SHOW_OVERVIEW) {
                    AdditionalHelpDrafter.drawAdditionalHelp(g, additionalHelpX, additionalHelpY, additionalHelpDescriptionForOverviewMode);
                } else if (this.mode == SHOW_OVERVIEW_AND_DRAG_CONNECTION_BETWEEN_NODES) {
                    AdditionalHelpDrafter.drawAdditionalHelp(g, additionalHelpX, additionalHelpY, additionalHelpDescriptionForOverviewModeWhileDraggingConnection);
                } else if (this.mode == SHOW_CONNECTION_OVERLAY && this.message != null) {
                    AdditionalHelpDrafter.drawAdditionalHelp(g, additionalHelpX, additionalHelpY, additionalHelpDescriptionForConnectionModeWithMessage);
                } else if (this.mode == SHOW_CONNECTION_OVERLAY) {
                    AdditionalHelpDrafter.drawAdditionalHelp(g, additionalHelpX, additionalHelpY, additionalHelpDescriptionForConnectionMode);
                }/* else {
                System.out.println("Keine AdditionalHelp zum Anzeigen");
            }*/
            }

            // Project-Running-Overlay zeichnen...
            if (this.mode == PROJECT_RUNNING) {
                g.setColor(PROJECT_RUNNING_OVERLAY_COLOR);
                g.fillRect(0, TOOLBAR_HEIGHT + 1, getWidth(), getHeight() - TOOLBAR_HEIGHT);
            }

            // Scrollbars zeichnen...
            ScrollbarDrafter.drawScrollbar(g, verticalScrollbar);
            ScrollbarDrafter.drawScrollbar(g, horizontalScrollbar);

            // Right-Click-Menue...
            if (rightClickMenue != null) {
                RightClickMenueDrafter.drawLeftClickMenue(g, rightClickMenue);
            }

            // DrawCounter zeichnen...
            /*drawCounterForDebugging++;
            if(this.settings.getBoolean(Settings.DEVELOPER_REDRAW_COUNTER_KEY, false)) {
                g.setColor(Color.lightGray);
                g.setFont(DEVELOPER_INFO_FONT);
                String drawCounterForDebuggingSring = String.valueOf(drawCounterForDebugging);
                g.drawString(drawCounterForDebuggingSring, getWidth() - g.getFontMetrics().stringWidth(drawCounterForDebuggingSring) - 3, 10);
            }*/
            if (this.settings.getBoolean(GeneralSettings.DEVELOPER_LOG_REDRAW_KEY, false)) {
                long usedNanoSeconds = System.nanoTime() - nanoTime;
                System.out.println("MainPanel gezeichnet (" + (usedNanoSeconds / 1000000) + "." + ((usedNanoSeconds / 10000) % 100) + "ms)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public int getOffX() {
        return -horizontalScrollbar.getOffsetForContent() - areaForViewing.getX();
    }

    public int getOffY() {
        return -verticalScrollbar.getOffsetForContent() - areaForViewing.getY() + TOOLBAR_HEIGHT;
    }

    public Scrollbar getVerticalScrollbar() {
        return verticalScrollbar;
    }

    public Scrollbar getHorizontalScrollbar() {
        return horizontalScrollbar;
    }

    /**
     * Verschiebt Target um ein kleines Stuck damit User nicht aus versehen
     * Elemente uebereinander anlegt.
     */
    public void targetPlusPlus() {
        this.targetX += 10;
        this.targetY += 10;
    }

    public void esc() {
        this.rightClickMenue = null;
        if (this.mode == SHOW_OVERVIEW_AND_DRAG_CONNECTION_BETWEEN_NODES) {
            this.mode = SHOW_OVERVIEW;
        } else if (this.message != null) {
            resetMessage();
        } else {
            this.mode = SHOW_OVERVIEW;
        }
    }

    public void enter() {
        if (this.mode == SHOW_OVERVIEW_AND_DRAG_CONNECTION_BETWEEN_NODES) {
            this.mode = SHOW_OVERVIEW;
        } else if (this.message != null) {
            resetMessage();
        }
    }

    public void resetMessage() {
        this.message = null;
        this.messageIsError = false;
    }

    public void setMessage(String message, boolean isError) {
        this.message = message;
        this.messageIsError = isError;
        if (isError) {
            System.err.println(message);
        } else {
            System.out.println(message);
        }
    }

    public void setProjektRunning(boolean running) {
        if (running) {
            if (this.mode != PROJECT_RUNNING) {
                this.preservedMode = this.mode;
                this.mode = PROJECT_RUNNING;
            }
        } else {
            if (this.mode == PROJECT_RUNNING) {
                this.mode = this.preservedMode;
                this.preservedMode = null;
            }
        }
    }
}
