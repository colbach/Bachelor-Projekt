package view.main.toolbar;

import exceptions.IllegalUserActionException;
import java.awt.Point;
import view.main.MainWindow;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import log.Logging;
import model.runproject.Debugger;
import utils.measurements.*;
import view.assets.*;
import static view.Constants.*;
import static view.assets.ImageAsset.*;
import view.listener.*;
import view.generallog.LogWindow;
import view.main.runreport.RunReportWindow;
import view.settings.SettingsWindow;

public final class Toolbar extends MouseAndKeyboardListener {

    /**
     * Liste mit Toolbarelementen.
     */
    private final ArrayList<ToolbarItem> toolbarItems = new ArrayList<>();

    /**
     * Gibt zuruek ob Projekt gerade ausgefuehrt wird oder nicht.
     */
    public boolean isProjectRunning() {
        return mainWindow.isProjectExecutionRunning();
    }

    /**
     * Gibt zuruek ob Projekt ausgefuehrt wurde ünd beendet wurde.
     */
    public boolean isProjectFinished() {
        return mainWindow.isProjectExecutionFinished();
    }

    /**
     * Gibt an ob Info-Button gerade runtergedruekt ist.
     */
    private boolean infoButtonIsPressed = false;

    /**
     * Zugehoeriges Hauptfenster.
     */
    private final MainWindow mainWindow;

    private boolean debuggingTools = false;

    private boolean isDebuggingOn() {
        Debugger debugger = mainWindow.getDebugger();
        if (debugger != null) {
            return debugger.isDebugging();
        }
        return false;
    }

    private void startProject() {
        try {
            mainWindow.startProjectExecution(false);
        } catch (IllegalUserActionException ex) {
            System.err.println(ex.getMessage());
            JOptionPane.showMessageDialog(mainWindow,
                    ex.getUserMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            JOptionPane.showMessageDialog(mainWindow,
                    ex.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public Toolbar(MainWindow mainWindow) throws IOException {

        // Genutzte Flaeche bei Superklasse MouseAndKeyboardListener registirieren...
        super(new Area(0, 0, Integer.MAX_VALUE, TOOLBAR_HEIGHT));

        this.mainWindow = mainWindow;

        // Toolbarelemente erzeugen...
        addToolbarItem(new ToolbarItem(true, "D. Verlassen", ImageAsset.getImageAssetForName(CLOSE_ICON)) {
            @Override
            public void clicked() {
                debuggingTools = false;
            }

            @Override
            public boolean isHidden() {
                return !debuggingTools || isProjectRunning();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Fertig", ImageAsset.getImageAssetForName(CLOSE_ICON)) {
            @Override
            public void clicked() {
                mainWindow.resetExecutionHub();
            }

            @Override
            public boolean isHidden() {
                return !isProjectFinished();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Start", ImageAsset.getImageAssetForName(START_ICON)) {
            @Override
            public void clicked() {
                startProject();
            }

            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning() || isProjectFinished();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Restart", ImageAsset.getImageAssetForName(START_ICON)) {
            @Override
            public void clicked() {
                startProject();
            }

            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning() || !isProjectFinished();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Bericht", ImageAsset.getImageAssetForName(REPORT)) {
            @Override
            public void clicked() {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {

                        Debugger debugger = mainWindow.getDebugger();

                        if (debugger != null) {

                            // Fenster erzeugen...
                            RunReportWindow nodeInfoWindow = new RunReportWindow(debugger);

                            // Fenster mittig zu Hauptfenster positionieren...
                            Point mainWindowLocation = mainWindow.getLocation();
                            nodeInfoWindow.setLocation(
                                    new Point(
                                            (int) (mainWindowLocation.getX() + (mainWindow.getWidth() / 2) - (nodeInfoWindow.getWidth() / 2)),
                                            (int) (mainWindowLocation.getY() + (mainWindow.getHeight() / 2) - (nodeInfoWindow.getHeight() / 2))
                                    )
                            );
                            // Fenster soll immer im Vordergrund bleiben...
                            nodeInfoWindow.setAlwaysOnTop(true);

                            // Fenster anzeigen...
                            nodeInfoWindow.setVisible(true);

                        } else {
                            System.err.println("debugger ist null.");
                        }
                    }
                });
            }

            @Override
            public boolean isHidden() {
                return isProjectRunning() || !isProjectFinished();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Debug", ImageAsset.getImageAssetForName(DEBUG_ICON)) {
            @Override
            public void clicked() {
                try {
                    mainWindow.startProjectExecution(true);
                } catch (IllegalUserActionException ex) {
                    System.err.println(ex.getMessage());
                    JOptionPane.showMessageDialog(mainWindow,
                            ex.getUserMessage(),
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    JOptionPane.showMessageDialog(mainWindow,
                            ex.getMessage(),
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public boolean isHidden() {
                return !debuggingTools || isProjectRunning();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Stop", ImageAsset.getImageAssetForName(STOP_ICON)) {
            @Override
            public void clicked() {
                try {
                    mainWindow.cancelProjectExecution();
                } catch (IllegalUserActionException ex) {
                    System.err.println(ex.getMessage());
                    JOptionPane.showMessageDialog(mainWindow,
                            ex.getUserMessage(),
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    JOptionPane.showMessageDialog(mainWindow,
                            ex.getMessage(),
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            public boolean isHidden() {
                return !isProjectRunning();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Schritt", ImageAsset.getImageAssetForName(CONTINUE_STEP_ICON)) {
            @Override
            public void clicked() {
                Debugger debugger = mainWindow.getDebugger();
                if (debugger != null) {
                    debugger.step();
                }
            }

            public boolean isHidden() {
                return !debuggingTools || !isProjectRunning() || !isDebuggingOn();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Fortsetzen", ImageAsset.getImageAssetForName(CONTINUE_DEBUG_ICON)) {
            @Override
            public void clicked() {
                Debugger debugger = mainWindow.getDebugger();
                if (debugger != null) {
                    debugger.continueWithDebugger();
                }
            }

            public boolean isHidden() {
                return !debuggingTools || !isProjectRunning() || !isDebuggingOn();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Weiter ohne D.", ImageAsset.getImageAssetForName(CONTINUE_ABORT_DEBUG_ICON)) {
            @Override
            public void clicked() {
                Debugger debugger = mainWindow.getDebugger();
                if (debugger != null) {
                    debugger.continueWithoutDebugger();
                }
            }

            public boolean isHidden() {
                return !debuggingTools || !isProjectRunning() || !isDebuggingOn();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Weiter mit D.", ImageAsset.getImageAssetForName(RECONTINUE_DEBUG_ICON)) {
            @Override
            public void clicked() {
                Debugger debugger = mainWindow.getDebugger();
                if (debugger != null) {
                    debugger.continueWithDebugger();
                }
            }

            public boolean isHidden() {
                return !debuggingTools || !isProjectRunning() || isDebuggingOn();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Debugger", ImageAsset.getImageAssetForName(BUG_ICON)) {
            @Override
            public void clicked() {
                debuggingTools = true;
            }

            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectFinished();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Monitor", ImageAsset.getImageAssetForName(DEBUG_WINDOW_ICON)) {
            @Override
            public void clicked() {
                mainWindow.openDebugMonitorWindow();
            }

            @Override
            public boolean isHidden() {
                return !debuggingTools;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Auto-Replay") {
            private final ImageAsset on = ImageAsset.getImageAssetForName(ON_ICON);
            private final ImageAsset off = ImageAsset.getImageAssetForName(OFF_ICON);
            private boolean state = true;

            @Override
            public void clicked() {
                System.out.println("Auto-Repeat");
                state = !state;
            }

            @Override
            public ImageAsset getImage() {
                return state ? on : off;
            }

            @Override
            public boolean isHidden() {
                return true;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Elemente", ImageAsset.getImageAssetForName(MANAGEMENT_ICON)) {
            @Override
            public void clicked() {
                if (mainWindow != null) {
                    mainWindow.openNodeCollectionOverviewWindow();
                }
            }

            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning() || isProjectFinished();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Projekt", ImageAsset.getImageAssetForName(PROJECT_ICON)) {
            @Override
            public void clicked() {
                if (mainWindow != null) {
                    mainWindow.openProjectWindow();
                }
            }

            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning() || isProjectFinished();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Log", ImageAsset.getImageAssetForName(MONITOR_ICON)) {
            @Override
            public void clicked() {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        new LogWindow().setVisible(true);
                    }
                });
            }
        });
        addToolbarItem(new ToolbarItem(true, "Speichern", ImageAsset.getImageAssetForName(DISK_ICON)) {
            @Override
            public void clicked() {
                System.out.println("Speichern");
            }

            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning() || isProjectFinished();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Laden", ImageAsset.getImageAssetForName(FOLDER_ICON)) {
            @Override
            public void clicked() {
                System.out.println("Laden");
            }

            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning() || isProjectFinished();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Neues Projekt", ImageAsset.getImageAssetForName(PAPER_ICON)) {
            @Override
            public void clicked() {
                System.out.println("Neues Projekt");
            }

            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning() || isProjectFinished();
            }
        });
        addToolbarItem(new ToolbarItem(true, "Einstellungen", ImageAsset.getImageAssetForName(PREFERENCES_ICON)) {
            @Override
            public void clicked() {
                if (mainWindow != null) {
                    mainWindow.openSettingsWindow();
                }
            }

            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning();
            }
        });

    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public boolean addToolbarItem(ToolbarItem e) {
        return toolbarItems.add(e);
    }

    public ArrayList<ToolbarItem> getToolbarItems() {
        return toolbarItems;
    }

    /**
     * Gibt durch Maus getroffenes ToolbarItem zurueck oder null wenn keines
     * getroffen ist
     *
     * @param x
     * @return
     */
    private ToolbarItem identifyMatchedItemByMouseX(int x) {
        // getroffenes Element suchen...
        int c = 0; // zaehlt sichtbare Elemente
        for (ToolbarItem item : toolbarItems) { // fuer jedes Element

            if (!item.isHidden()) { // Wenn Element sichtbar ist

                // c hochzaehlen...
                c++;

                // Bereich testen (y kann ignoriert werden wegen Clip)...
                if (x < c * TOOLBAR_ITEM_WIDTH) {
                    return item;
                }
            }
        }

        return null; // kein Element getroffen
    }

    @Override
    public boolean mouseClicked(int x, int y) {

        return true; // immer True weil Toolbar das einzige in diesem Clip-Bereich ist.
    }

    @Override
    public boolean mousePressed(int x, int y) {

        // Passendes Item ermitteln...
        ToolbarItem item = identifyMatchedItemByMouseX(x);

        if (item != null) { // Wenn Item getroffen
            if (item.isEnabled() && item.isClickable()) {
                item.setPressedDown(true); // Item "druecken"
            }
        } else { // Kein Element getroffen
            infoButtonIsPressed = true;
        }

        return true; // immer True weil Toolbar das einzige in diesem Clip-Bereich ist.
    }

    @Override
    public boolean mouseReleased(int x, int y, int pressedX, int pressedY) {

        // Passendes Item ermitteln...
        ToolbarItem matched = identifyMatchedItemByMouseX(x);

        if (matched != null) { // Wenn Item getroffen
            if (matched.isEnabled() && matched.isClickable() && matched.isPressedDown()) {
                matched.clicked(); // Item klicken
            }
        }

        // Gedruekte Taste bei allen loesen...
        toolbarItems.stream().forEach((item) -> {
            item.setPressedDown(false); // Item "loslassen"
        });
        infoButtonIsPressed = false; // Infobutton loesen

        return true;
    }

    public boolean isInfoButtonIsPressed() {
        return infoButtonIsPressed;
    }

    @Override
    public boolean mouseDragged(int lastX, int lastY, int actualX, int actualY, int startX, int startY) {
        return true;
    }

}
