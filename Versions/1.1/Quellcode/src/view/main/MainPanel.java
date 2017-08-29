package view.main;

import projectrunner.debugger.Breakpoints;
import view.main.contextmenu.ContextMenu;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.swing.JPanel;
import main.MainClass;

import model.*;
import settings.GeneralSettings;
import main.startuptasks.StartupTaskBatchRunner;
import view.main.*;
import view.main.debug.*;
import view.main.showstate.*;
import view.main.toolbar.*;
import view.main.connectoverlay.*;
import view.sharedcomponents.scrollbar.*;
import utils.measurements.*;
import utils.structures.*;

import static view.Constants.*;
import static view.main.MainPanelMode.*;

public final class MainPanel extends JPanel {

    private boolean fullyLoaded = false;
    private final StartupTaskBatchRunner startupTaskBatchRunner;

    /**
     * Aktueller State in dem sich UI gerade befindet.
     */
    private ShowState showState;

    private MainWindow window;
    private Breakpoints breakpoints;
    private long contextIdentifierForDebugger = -2L;
    private Toolbar toolbar;
    private int targetX = 50, targetY = 100;
    private int mouseX = -1, mouseY = -1;
    private boolean mousePressed = false;
    private int opticalCenterX, opticalCenterY;
    private GeneralSettings settings;
    private MainPanelMouseAndKeyboardListener mainPanelMouseAndKeyboardListener;
    private Scrollbar verticalScrollbar, horizontalScrollbar;
    private Area areaForViewing;

    /**
     * Graphics-Pointer fuer MouseEvents. Darf nicht zum zeichnen benutzt
     * werden!
     */
    protected Graphics savedGraphics = null;

    public MainPanel(MainWindow window, StartupTaskBatchRunner startupTaskBatchRunner) {
        this.window = window;
        this.startupTaskBatchRunner = startupTaskBatchRunner;
    }

    public synchronized void loadFully() {
        this.showState = new ShowOverviewDefault();
        this.areaForViewing = NonArea.getInstance();
        this.breakpoints = Breakpoints.getInstance();
        this.mainPanelMouseAndKeyboardListener = new MainPanelMouseAndKeyboardListener(this);
        this.settings = GeneralSettings.getInstance();
        this.verticalScrollbar = new Scrollbar(2000, 0, Direction.VERTICAL, NonArea.getInstance());
        this.horizontalScrollbar = new Scrollbar(2000, 0, Direction.HORIZONTAL, NonArea.getInstance());
        try {
            this.toolbar = new Toolbar(window);
            updateAreaForDisplay();
            updateScrollbarRepresendedArea();
        } catch (IOException ex) {
            System.err.println("Toolbar konte nicht geladen werden!");
        }
        fullyLoaded = true;
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

        // Hoehe und Breite...
        int width = 1000;
        int height = 1000;
        if (window != null) {
            width = window.getWidth();
            height = window.getHeight();
        }

        if (fullyLoaded) {
            try {
                savedGraphics = graphics;

                long nanoTime = System.nanoTime();

                updateScrollbarBarAreas();
                updateAreaForDisplay();

                // Graphics ind Graphics2D casten...
                Graphics2D g = (Graphics2D) graphics;

                // Zentrum aktualisieren...
                this.opticalCenterX = width / 2;
                this.opticalCenterY = height / 2 + 10;

                // Alles zeichnen...
                MainPanelDrafter.drawMainPanel(g, this);

                if (this.settings.getBoolean(GeneralSettings.DEVELOPER_LOG_REDRAW_KEY, false)) {
                    long usedNanoSeconds = System.nanoTime() - nanoTime;
                    System.out.println("MainPanel gezeichnet (" + (usedNanoSeconds / 1000000) + "." + ((usedNanoSeconds / 10000) % 100) + "ms)");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            BackgroundDrafter.drawBackground((Graphics2D) graphics, 0, 0, 0, 0, width, height);
            ToolbarDrafter.drawDummyToolbar((Graphics2D) graphics, width);

            long nanoTime = System.nanoTime();
            int barWidth = 400;
            int barHeight = 10;
            graphics.setColor(STARTUP_COLOR_BRIGHT);
            graphics.fillRect(width / 2 - barWidth / 2 - 2, height / 2 - barHeight / 2 - 2, barWidth + 4, barHeight + 4);
            graphics.setColor(STARTUP_COLOR_DARK);
            graphics.drawRect(width / 2 - barWidth / 2 - 2, height / 2 - barHeight / 2 - 2, barWidth + 4, barHeight + 4);
            graphics.fillRect(width / 2 - barWidth / 2 + 2, height / 2 - barHeight / 2 + 2, (int) (startupTaskBatchRunner.getActualProgress() * barWidth) - 4 + 1, barHeight - 4);
        }
    }

    private boolean firstLoad = true;

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

        showState = showState.back();

        // Project-Finished-Overlay schliessen...
        if (window.isProjectExecutionTerminated()) {
            window.destroyProjectExecution();
        }
    }

    public void enter() {
        showState.resetMessage();
    }

    public void resetMessage() {
        showState.resetMessage();
    }

    public void setMessage(String message, boolean isError) {
        showState.setMessage(message, isError);
        if (isError) {
            System.err.println(message);
        } else {
            System.out.println(message);
        }
    }

    public void setProjektRunning(boolean running, boolean debug) {
        if (running) {
            if (debug) {
                showState = new ShowRunningProjectWithDebuggerDefault(showState);
            } else {
                showState = new ShowRunningProject(showState);
            }
        } else {
            // debug wird in diesem fall ignoriert
            if (showState instanceof LockableViewState) {
                try {
                    ((LockableViewState) showState).unlockBack();
                } catch (ClassCastException e) {
                    System.err.println("showState kann nicht zu LockableViewState gecastet werden!");
                }

            } else {
                System.err.println("showState ist keine Instanz von LockableViewState!");
            }
        }
    }

    protected boolean isMousePressed() {
        return mousePressed;
    }

    protected MainWindow getWindow() {
        return window;
    }

    public ContextMenu getRightClickMenue() {
        ShowState actualShowState = this.showState;
        if (actualShowState instanceof RightClickableViewState) {
            RightClickableViewState rightClickableViewState = (RightClickableViewState) actualShowState;
            return rightClickableViewState.getRightClickMenue();
        } else {
            return null;
        }
    }

    public boolean setRightClickMenue(ContextMenu rightClickMenue) {
        ShowState actualShowState = this.showState;
        if (actualShowState instanceof RightClickableViewState) {
            RightClickableViewState rightClickableViewState = (RightClickableViewState) actualShowState;
            rightClickableViewState.setRightClickMenue(rightClickMenue);
            return true;
        } else {
            System.err.println("Dieser ShowState (" + actualShowState.getClass().toString() + ") unterstuetzt keine RightClickMenues.");
            return false;
        }
    }

    public ShowState getShowState() {
        return showState;
    }

    public void setShowState(ShowState showState) {
        this.showState = showState;
    }

    public long getContextIdentifierForDebugger() {
        return contextIdentifierForDebugger;
    }

    public void setContextIdentifierForDebugger(long contextIdentifierForDebugger) {
        this.contextIdentifierForDebugger = contextIdentifierForDebugger;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getOpticalCenterX() {
        return opticalCenterX;
    }

    public int getOpticalCenterY() {
        return opticalCenterY;
    }

    public String getMessage() {
        return showState.getMessage();
    }

    public boolean isMessageError() {
        return showState.isMessageError();
    }

    public Breakpoints getBreakpoints() {
        return breakpoints;
    }

    public GeneralSettings getSettings() {
        return settings;
    }

    public Area getAreaForViewing() {
        return areaForViewing;
    }

    public Graphics getSavedGraphics() {
        return savedGraphics;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public void setOpticalCenterX(int opticalCenterX) {
        this.opticalCenterX = opticalCenterX;
    }

    public void setOpticalCenterY(int opticalCenterY) {
        this.opticalCenterY = opticalCenterY;
    }

}
