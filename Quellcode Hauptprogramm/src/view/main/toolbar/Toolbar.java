package view.main.toolbar;

import commandline.CommandLineThread;
import componenthub.ComponentHub;
import generalexceptions.IllegalUserActionException;
import java.awt.Point;
import java.io.File;
import view.main.MainWindow;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import logging.AdvancedLogger;
import model.InOutlet;
import model.Node;
import model.Project;
import model.check.CheckResult;
import model.check.Checker;
import model.resourceloading.projectserialization.ProjectStructureBuilder;
import model.runproject.debugger.*;
import model.runproject.report.Report;
import utils.measurements.*;
import view.assets.*;
import static view.Constants.*;
import static view.assets.ImageAsset.*;
import view.dialogs.ErrorDialog;
import view.listener.*;
import view.console.ConsoleWindow;
import view.main.MainPanel;
import view.main.runreport.RunReportWindow;
import view.main.saveandloadproject.ProjectLoader;
import view.main.saveandloadproject.ProjectSaver;
import view.main.showstate.RunningViewState;
import view.main.showstate.ShowState;
import view.settings.*;

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
        return mainWindow.isProjectExecutionTerminated();
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
    private boolean breakpointRules = false;
    
    private boolean isDebuggingOn() {
        return mainWindow.getDebuggerRemote() != null && mainWindow.getDebuggerRemote().isDebugging();
    }
    
    private void startProject(boolean debug) {
        
        try {
            mainWindow.startProjectExecution(debug);
        } catch (IllegalUserActionException ex) {
            System.err.println(ex.getMessage());
            ErrorDialog.showErrorDialog(ex, mainWindow);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ErrorDialog.showErrorDialog(ex, mainWindow);
        }
    }
    
    public Toolbar(MainWindow mainWindow) throws IOException {

        // Genutzte Flaeche bei Superklasse MouseAndKeyboardListener registirieren...
        super(new Area(0, 0, Integer.MAX_VALUE, TOOLBAR_HEIGHT));
        
        this.mainWindow = mainWindow;
        
        DebuggerRules debuggerRules = DebuggerRules.getInstance();

        // Toolbarelemente erzeugen...
        addToolbarItem(new ToolbarItem(true, "D. Verlassen", ImageAsset.getImageAssetForName(CLOSE_ICON)) {
            @Override
            public void clicked() {
                debuggingTools = false;
            }
            
            @Override
            public boolean isHidden() {
                return !debuggingTools || isProjectRunning() || breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Zurück", ImageAsset.getImageAssetForName("Arrow-Left_30px.png")) {
            @Override
            public void clicked() {
                debuggingTools = true;
                breakpointRules = false;
            }
            
            @Override
            public boolean isHidden() {
                return !breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Fertig", ImageAsset.getImageAssetForName(CLOSE_ICON)) {
            @Override
            public void clicked() {
                mainWindow.destroyProjectExecution();
                MainPanel mainPanel = mainWindow.getMainPanel();
                if (mainPanel != null) {
                    ShowState showState = mainPanel.getShowState();
                    if (showState != null) {
                        if (showState instanceof RunningViewState) {
                            RunningViewState runningViewState = (RunningViewState) showState;
                            mainPanel.setShowState(runningViewState.backToOverview());
                        } else {
                            System.err.println("showState implementiert RunningViewState nicht");
                        }
                    } else {
                        System.err.println("showState ist null");
                    }                    
                } else {
                    System.err.println("mainPanel ist null");
                }
            }
            
            @Override
            public boolean isHidden() {
                return !isProjectFinished() || breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Start", ImageAsset.getImageAssetForName(START_ICON)) {
            @Override
            public void clicked() {
                startProject(false);
            }
            
            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning() || isProjectFinished() || breakpointRules;
            }
        });
//        addToolbarItem(new ToolbarItem(true, "Restart", ImageAsset.getImageAssetForName(START_ICON)) {
//            @Override
//            public void clicked() {
//                mainWindow.destroyProjectExecution();
//                startProject(false);
//            }
//            
//            @Override
//            public boolean isHidden() {
//                return debuggingTools || isProjectRunning() || !isProjectFinished() || breakpointRules;
//            }
//        });
        addToolbarItem(new ToolbarItem(true, "Bericht", ImageAsset.getImageAssetForName(REPORT_ICON)) {
            @Override
            public void clicked() {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        
                        Report report = mainWindow.getFinalReport();
                        
                        if (report != null) {

                            // Fenster erzeugen...
                            RunReportWindow nodeInfoWindow = new RunReportWindow(report);

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
                return isProjectRunning() || !isProjectFinished() || breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Debug", ImageAsset.getImageAssetForName(DEBUG_ICON)) {
            @Override
            public void clicked() {
                startProject(true);
            }
            
            @Override
            public boolean isHidden() {
                return !debuggingTools || isProjectRunning() || breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Neuer Prozess") {
            
            ImageAsset on = ImageAsset.getImageAssetForName("ui/Trigger_Breakpoint_On_New_Executor_On_30px.png");
            ImageAsset off = ImageAsset.getImageAssetForName("ui/Trigger_Breakpoint_On_New_Executor_Off_30px.png");
            
            @Override
            public void clicked() {
                debuggerRules.toogleTriggerBreakpointOnNewExecutor();
            }
            
            @Override
            public boolean isHidden() {
                return !breakpointRules;
            }
            
            @Override
            public ImageAsset getImage() {
                return debuggerRules.isTriggerBreakpointOnNewExecutor() ? on : off;
            }
            
        });
        addToolbarItem(new ToolbarItem(true, "Vorbereiten") {
            
            ImageAsset on = ImageAsset.getImageAssetForName("ui/Trigger_Breakpoint_On_Preparing_On_30px.png");
            ImageAsset off = ImageAsset.getImageAssetForName("ui/Trigger_Breakpoint_On_Preparing_Off_30px.png");
            
            @Override
            public void clicked() {
                debuggerRules.toogleTriggerBreakpointOnStateChangeToPreparing();
            }
            
            public boolean isHidden() {
                return !breakpointRules;
            }
            
            @Override
            public ImageAsset getImage() {
                return debuggerRules.isTriggerBreakpointOnStateChangeToPreparing() ? on : off;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Sammeln") {
            
            ImageAsset on = ImageAsset.getImageAssetForName("ui/Trigger_Breakpoint_On_Collecting_On_30px.png");
            ImageAsset off = ImageAsset.getImageAssetForName("ui/Trigger_Breakpoint_On_Collecting_Off_30px.png");
            
            @Override
            public void clicked() {
                debuggerRules.toogleTriggerBreakpointOnStateChangeToCollecting();
            }
            
            public boolean isHidden() {
                return !breakpointRules;
            }
            
            @Override
            public ImageAsset getImage() {
                return debuggerRules.isTriggerBreakpointOnStateChangeToCollecting() ? on : off;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Arbeiten") {
            
            ImageAsset on = ImageAsset.getImageAssetForName("ui/Trigger_Breakpoint_On_Working_On_30px.png");
            ImageAsset off = ImageAsset.getImageAssetForName("ui/Trigger_Breakpoint_On_Working_Off_30px.png");
            
            @Override
            public void clicked() {
                debuggerRules.toogleTriggerBreakpointOnStateChangeToRunning();
            }

            public boolean isHidden() {
                return !breakpointRules;
            }
            
            @Override
            public ImageAsset getImage() {
                return debuggerRules.isTriggerBreakpointOnStateChangeToRunning() ? on : off;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Ausliefern") {
            
            ImageAsset on = ImageAsset.getImageAssetForName("ui/Trigger_Breakpoint_On_Delievering_On_30px.png");
            ImageAsset off = ImageAsset.getImageAssetForName("ui/Trigger_Breakpoint_On_Delievering_Off_30px.png");
            
            @Override
            public void clicked() {
                debuggerRules.toogleTriggerBreakpointOnStateChangeToDelievering();
            }
            
            public boolean isHidden() {
                return !breakpointRules;
            }
            
            @Override
            public ImageAsset getImage() {
                return debuggerRules.isTriggerBreakpointOnStateChangeToDelievering() ? on : off;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Beendet") {
            
            ImageAsset on = ImageAsset.getImageAssetForName("ui/Trigger_Breakpoint_On_Finished_On_30px.png");
            ImageAsset off = ImageAsset.getImageAssetForName("ui/Trigger_Breakpoint_On_Finished_Off_30px.png");
            
            @Override
            public void clicked() {
                debuggerRules.toogleTriggerBreakpointOnStateChangeToFinished();
            }
            
            public boolean isHidden() {
                return !breakpointRules;
            }
            
            @Override
            public ImageAsset getImage() {
                return debuggerRules.isTriggerBreakpointOnStateChangeToFinished() ? on : off;
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
                return !isProjectRunning() || breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Schritt", ImageAsset.getImageAssetForName(CONTINUE_STEP_ICON)) {
            @Override
            public void clicked() {
                DebuggerRemote debugger = mainWindow.getDebuggerRemote();
                if (debugger != null) {
                    debugger.step();
                }
            }
            
            public boolean isHidden() {
                return !debuggingTools || !isProjectRunning() || !isDebuggingOn() || breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Fortsetzen", ImageAsset.getImageAssetForName(CONTINUE_DEBUG_ICON)) {
            @Override
            public void clicked() {
                DebuggerRemote debugger = mainWindow.getDebuggerRemote();
                if (debugger != null) {
                    debugger.continueWithDebugger();
                }
            }
            
            public boolean isHidden() {
                return !debuggingTools || !isProjectRunning() || !isDebuggingOn() || breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Weiter ohne D.", ImageAsset.getImageAssetForName(CONTINUE_ABORT_DEBUG_ICON)) {
            @Override
            public void clicked() {
                DebuggerRemote debugger = mainWindow.getDebuggerRemote();
                if (debugger != null) {
                    debugger.continueWithoutDebugger();
                }
            }
            
            public boolean isHidden() {
                return !debuggingTools || !isProjectRunning() || !isDebuggingOn() || breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Weiter mit D.", ImageAsset.getImageAssetForName(RECONTINUE_DEBUG_ICON)) {
            @Override
            public void clicked() {
                DebuggerRemote debugger = mainWindow.getDebuggerRemote();
                if (debugger != null) {
                    debugger.continueWithDebugger();
                }
            }
            
            public boolean isHidden() {
                return !debuggingTools || !isProjectRunning() || isDebuggingOn() || breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Br. Auslöser", ImageAsset.getImageAssetForName("Breakpoint_Rules_30px.png")) {
            
            @Override
            public void clicked() {
                breakpointRules = true;
                debuggingTools = false;
            }
            
            @Override
            public boolean isHidden() {
                return !debuggingTools || breakpointRules;
            }
            
        });
        addToolbarItem(new ToolbarItem(true, "Debugger", ImageAsset.getImageAssetForName(BUG_ICON)) {
            @Override
            public void clicked() {
                debuggingTools = true;
            }
            
            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning() || isProjectFinished() || breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Monitor", ImageAsset.getImageAssetForName(DEBUG_WINDOW_ICON)) {
            @Override
            public void clicked() {
                mainWindow.openDebugMonitorWindow();
            }
            
            @Override
            public boolean isHidden() {
                return !debuggingTools || breakpointRules;
            }
        });
        /*addToolbarItem(new ToolbarItem(true, "Auto-Replay") {
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
        });*/
        addToolbarItem(new ToolbarItem(true, "Elemente", ImageAsset.getImageAssetForName(MANAGEMENT_ICON)) {
            @Override
            public void clicked() {
                if (mainWindow != null) {
                    mainWindow.openNodeCollectionOverviewWindow();
                }
            }
            
            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning() || isProjectFinished() || breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Projekt-Info", ImageAsset.getImageAssetForName(PROJECT_ICON)) {
            @Override
            public void clicked() {
                if (mainWindow != null) {
                    mainWindow.openProjectWindow();
                }
            }
            
            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning() || isProjectFinished() || breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, CommandLineThread.getCommandLinePrompt() == null ? "Log" : "Konsole", ImageAsset.getImageAssetForName(MONITOR_ICON)) {
            @Override
            public void clicked() {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        new ConsoleWindow().setVisible(true);
                    }
                });
            }
            
            @Override
            public boolean isHidden() {
                return breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Speichern", ImageAsset.getImageAssetForName(DISK_ICON)) {
            @Override
            public void clicked() {
                ProjectSaver.save(mainWindow, getMainWindow().getProject());
            }
            
            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning() || isProjectFinished() || !getMainWindow().getProject().isProjectLocationSet() || !getMainWindow().getProject().didSomethingChanged() || breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Speichern als", ImageAsset.getImageAssetForName(TWO_DISK_ICON)) {
            @Override
            public void clicked() {
                ProjectSaver.saveAs(mainWindow, mainWindow.getProject());
                String title = getMainWindow().getProject().getTitle();
                getMainWindow().setTitle(title);
            }
            
            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning() || isProjectFinished() || breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Laden", ImageAsset.getImageAssetForName(FOLDER_ICON)) {
            @Override
            public void clicked() {
                Project project = ProjectLoader.load(mainWindow);
                if (project != null) {
                    getMainWindow().setProject(project);
                    getMainWindow().setTitle(project.getTitle());
                }
            }
            
            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning() || isProjectFinished() || breakpointRules;
            }
        });
        addToolbarItem(new ToolbarItem(true, "Neues Projekt", ImageAsset.getImageAssetForName(PAPER_ICON)) {
            @Override
            public void clicked() {
                Project project = getMainWindow().getProject();
                if (project != null && project.didSomethingChanged()) {
                    ProjectSaver.askToSave(getMainWindow(), project);
                }
                Project newProject = new Project();
                getMainWindow().setProject(newProject);
                getMainWindow().setTitle(newProject.getTitle());
            }
            
            @Override
            public boolean isHidden() {
                return debuggingTools || isProjectRunning() || isProjectFinished() || breakpointRules;
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
                return debuggingTools || isProjectRunning() || breakpointRules;
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
        } else { // Kein Element getroffen
            infoButtonClicked();
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
    
    private static String email;
    
    {
        email = "@";
        char cee = (char) ((((int) ('a')) + 1) + 1);
        char eff = (char) 102;
        email = eff + "raq" + email + cee + "olba." + cee + 'h';
    }
    
    private void infoButtonClicked() {
        JOptionPane.showMessageDialog(mainWindow, "Christian Colbach 2016-2017\nBei technischen Fragen: " + email, "Informationen", JOptionPane.PLAIN_MESSAGE);
    }
    
}
