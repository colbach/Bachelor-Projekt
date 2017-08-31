package view.main;

import projectrunner.report.Report;
import projectrunner.debugger.DebuggerRemote;
import projectrunner.ProjectExecution;
import projectrunner.NotTerminatedYetException;
import projectrunner.ProjectExecutionRemote;
import projectrunner.ToManyConcurrentProjectExecutions;
import projectrunner.ProjectRunner;
import projectrunner.ProjectExecutionResultEventListener;
import main.componenthub.ComponentHub;
import generalexceptions.IllegalUserActionException;
import view.main.runreport.*;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import javax.swing.WindowConstants;
import model.*;
import settings.GeneralSettings;
import view.nodecollection.*;
import view.listener.*;
import view.project.*;
import view.settings.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import logging.AdditionalLogger;
import model.check.CheckResult;
import model.check.Checker;
import projectrunner.callbacks.OnDestroyCallback;
import projectrunner.executioncontrol.TerminationReason;
import static settings.GeneralSettings.*;
import settings.LastProjectMemory;
import main.startuptasks.StartupTaskBatchRunner;
import utils.format.TimeFormat;
import view.debug.*;
import view.dialogs.CheckDialog;
import view.dialogs.CheckDialogResult;
import view.dialogs.ErrorDialog;
import static view.main.MainPanelMode.*;
import view.main.saveandloadproject.ProjectSaver;

/**
 * Hauptfenster.
 */
public class MainWindow extends javax.swing.JFrame implements ProjectExecutionResultEventListener, OnDestroyCallback<ProjectExecution>, WindowListener, ActionListener {

    private boolean fullyLoaded = false;

    private int mouseX, mouseY;
    private long redrawCounter;

    private final ComponentHub componentHub;

    /**
     * Aktuelle ProjectExecutionRemote. Dieses Attribut darf nicht direkt
     * syncronisiert werden da Pointer null sein kann. Zum Sperren der Referenz
     * projectExecutionRemoteLock verwenden.
     */
    private ProjectExecutionRemote projectExecutionRemote = null;

    private Report finalReport = null;

    /**
     * Lock zur Sperre von ProjectExecutionRemote.
     */
    private final Object projectExecutionRemoteLock = new Object();

    private double lastStartupTaskProgress = -1;

    /**
     * Event fuer Timer.
     */
    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        double actualProgress = startupTaskBatchRunner.getActualProgress();
        if (actualProgress != lastStartupTaskProgress) {
            lastStartupTaskProgress = actualProgress;
            repaint();
        }
    }

    public void updateTitle() {
        boolean dmc = settings.getBoolean(GeneralSettings.DEVELOPER_MOUSE_COORDINATES_KEY, false);
        boolean drc = settings.getBoolean(GeneralSettings.DEVELOPER_REDRAW_COUNTER_KEY, false);
        boolean dnc = settings.getBoolean(GeneralSettings.DEVELOPER_NODE_COUNT_KEY, false);
        if (dmc || drc || dnc) {
            String title = "";
            if (dmc) {
                title += "Mausposition = [" + mouseX + ", " + mouseY + "]";
                if (settings.getBoolean(GeneralSettings.DEVELOPER_REDRAW_COUNTER_KEY, false)) {
                    title += ", ";
                }
            }
            if (drc) {
                title += "Redraw = " + redrawCounter;
                if (settings.getBoolean(GeneralSettings.DEVELOPER_NODE_COUNT_KEY, false) && getProject() != null) {
                    title += ", ";
                }
            }
            if (dnc && getProject() != null) {
                title += "Elemente = " + getProject().getNodeCount();
            }
            setTitle(title);
        }
    }

    private InputManager inputmanager;
    private GeneralSettings settings;
    private NodeCollectionWindow nodeCollectionWindow = null;

    private final Timer startUpTimer;

    private final StartupTaskBatchRunner startupTaskBatchRunner;

    /**
     * Erzeugt Hauptfenster.
     */
    public MainWindow(StartupTaskBatchRunner startupTaskBatchRunner) {
        startUpTimer = new Timer(20, this);
        startUpTimer.setRepeats(true);
        startUpTimer.start();
        this.startupTaskBatchRunner = startupTaskBatchRunner;

        componentHub = ComponentHub.getInstance();

        mainPanel = new view.main.MainPanel(this, startupTaskBatchRunner);
        mainPanel.setFocusCycleRoot(true);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1211, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 670, Short.MAX_VALUE)
        );
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();

        if(GeneralSettings.getInstance().getBoolean(DEVELOPER_AUTO_REDRAW_EVERY_SECOND_KEY, DEVELOPER_AUTO_REDRAW_EVERY_SECOND_VALUE)) {
            new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    repaint();
                }
            }).start();
        }
        
        // Initialisiere Komponenten...
        //loadFully();
    }

    /**
     * Laed alle Komponenten komplett.
     */
    public synchronized void loadFully() {
        mainPanel.loadFully();
        inputmanager = new InputManager();
        settings = GeneralSettings.getInstance();
        try {
            mainPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                public void mouseMoved(java.awt.event.MouseEvent evt) {
                    mouseX = evt.getX();
                    mouseY = evt.getY();
                    mainPanelMouseMoved(evt);
                    updateTitle();
                }

                public void mouseDragged(java.awt.event.MouseEvent evt) {
                    mouseX = evt.getX();
                    mouseY = evt.getY();
                    mainPanelMouseDragged(evt);
                    updateTitle();
                }
            });
            mainPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    mainPanelMousePressed(evt);
                }

                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    mainPanelMouseReleased(evt);
                }

                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    mainPanelMouseClicked(evt);
                }
            });
            mainPanel.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    mainPanelKeyTyped(evt);
                }

                public void keyPressed(java.awt.event.KeyEvent evt) {
                    mainPanelKeyPressed(evt);
                }

                public void keyReleased(java.awt.event.KeyEvent evt) {
                    mainPanelKeyReleased(evt);
                }
            });

            // Toolbar beim InputManager registrieren
            inputmanager.addListener(mainPanel.getHorizontalScrollbar());
            inputmanager.addListener(mainPanel.getVerticalScrollbar());
            inputmanager.addListener(mainPanel.getToolbar());
            inputmanager.addListener(mainPanel.getMainPanelMouseAndKeyboardListener());
            addWindowListener(this);

            addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    inputmanager.keyTyped(e.getKeyChar(), e.getKeyCode());
                    repaint();
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    inputmanager.keyPressed(e.getKeyChar(), e.getKeyCode());
                    repaint();
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    inputmanager.keyReleased(e.getKeyChar(), e.getKeyCode());
                    repaint();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle(componentHub.getProject().getTitle());
        //this.timer = new Timer(200, this);
        //this.timer.start();

        fullyLoaded = true;
        startUpTimer.stop();
    }

    public void openProjectWindow() {

        MainWindow thisWindow = this;

        // NodeCollectionOverviewPanel oeffnen...
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                // Fenster erzeugen...
                ProjectWindow nodeCollectionWindow = new ProjectWindow(thisWindow, componentHub.getProject());

                // // Schliessen von Fenster soll nicht Programm beenden
                nodeCollectionWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                // Fenster mittig zu Hauptfenster positionieren...
                Point mainWindowLocation = getLocation();
                nodeCollectionWindow.setLocation(
                        new Point(
                                (int) (mainWindowLocation.getX() + (getWidth() / 2) - (nodeCollectionWindow.getWidth() / 2)),
                                (int) (mainWindowLocation.getY() + (getHeight() / 2) - (nodeCollectionWindow.getHeight() / 2))
                        )
                );

                // Fenster soll immer im Vordergrund bleiben...
                nodeCollectionWindow.setAlwaysOnTop(true);

                // Fenster anzeigen...
                nodeCollectionWindow.setVisible(true);
            }
        });
    }

    public void openNodeCollectionOverviewWindow() {

        MainWindow thisWindow = this;

        // NodeCollectionOverviewPanel oeffnen...
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                // Fenster erzeugen...
                NodeCollectionWindow nodeCollectionWindow = new NodeCollectionWindow(thisWindow);

                // // Schliessen von Fenster soll nicht Programm beenden
                nodeCollectionWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                // Fenster mittig zu Hauptfenster positionieren...
                Point mainWindowLocation = getLocation();
                nodeCollectionWindow.setLocation(
                        new Point(
                                (int) (mainWindowLocation.getX() + (getWidth() / 2) - (nodeCollectionWindow.getWidth() / 2)),
                                (int) (mainWindowLocation.getY() + (getHeight() / 2) - (nodeCollectionWindow.getHeight() / 2))
                        )
                );

                // Fenster soll immer im Vordergrund bleiben...
                nodeCollectionWindow.setAlwaysOnTop(true);

                // Fenster anzeigen...
                nodeCollectionWindow.setVisible(true);
            }
        });
    }

    public void openSettingsWindow() {

        java.awt.EventQueue.invokeLater(() -> {
            new SettingsWindow(this).setVisible(true);
        });
    }

    public void openAdvancedSettingsWindow() {

        java.awt.EventQueue.invokeLater(() -> {
            new AdvancedSettingsWindow(this).setVisible(true);
        });
    }

    public Project getProject() {
        return componentHub.getProjectBlocking();
    }

    public void setProject(Project project) {
        this.componentHub.setProject(project);
    }

    public int getTargetX() {
        return mainPanel.getTargetX();
    }

    public int getTargetY() {
        return mainPanel.getTargetY();
    }

    public void targetPlusPlus() {
        mainPanel.targetPlusPlus();
    }

    private void mainPanelMouseClicked(java.awt.event.MouseEvent evt) {
        inputmanager.mouseClicked(evt.getX(), evt.getY());
        repaint();
    }

    private void mainPanelMouseDragged(java.awt.event.MouseEvent evt) {
        inputmanager.mouseDragged(evt.getX(), evt.getY());
        repaint();
    }

    private void mainPanelMouseMoved(java.awt.event.MouseEvent evt) {
        inputmanager.mouseMoved(evt.getX(), evt.getY());
        //repaint();
    }

    private void mainPanelMousePressed(java.awt.event.MouseEvent evt) {
        inputmanager.mousePressed(evt.getX(), evt.getY(), evt.getButton() == MouseEvent.BUTTON1);
        repaint();
    }

    private void mainPanelMouseReleased(java.awt.event.MouseEvent evt) {
        inputmanager.mouseReleased(evt.getX(), evt.getY());
        repaint();
    }

    private void mainPanelKeyPressed(java.awt.event.KeyEvent evt) {
        inputmanager.keyPressed(evt.getKeyChar(), evt.getKeyCode());
        repaint();
    }

    private void mainPanelKeyTyped(java.awt.event.KeyEvent evt) {
        inputmanager.keyTyped(evt.getKeyChar(), evt.getKeyCode());
        repaint();
    }

    private void mainPanelKeyReleased(java.awt.event.KeyEvent evt) {
        inputmanager.keyReleased(evt.getKeyChar(), evt.getKeyCode());
        repaint();
    }

    private view.main.MainPanel mainPanel;

    @Override
    public void repaint() {
        redrawCounter++;
        if (fullyLoaded) {
            updateTitle();
        }
        super.repaint();
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    public void removeRightClickMenue() {
        mainPanel.setRightClickMenue(null);
    }

    public synchronized void startProjectExecution(boolean debug) throws IllegalUserActionException, Exception {

        synchronized (projectExecutionRemoteLock) {

            if (projectExecutionRemote != null && !projectExecutionRemote.isTerminated()) { // Falls Projekt bereits ausgefuehrt wird
                throw new IllegalUserActionException("projectExecutationRemote.isTerminated() gibt false zurueck", "Project wird bereits ausgeführt.");

            } else {
                CheckResult checkResult;
                if (settings.getBoolean(GeneralSettings.CHECK_PROJECTS_BEFOR_RUN_KEY, true)) {
                    checkResult = Checker.checkProject(componentHub.getProject());
                } else {
                    checkResult = new CheckResult();
                }
                CheckDialogResult checkDialogResult = CheckDialog.showCheckDialog(checkResult, this);
                if (checkDialogResult == CheckDialogResult.CONTINUE) {
                    try {
                        finalReport = null;
                        projectExecutionRemote = ProjectRunner.getInstance().executeProject(componentHub.getProject(), this, this, this, debug);
                        mainPanel.setProjektRunning(true, debug);
                    } catch (ToManyConcurrentProjectExecutions ex) {
                        throw new IllegalUserActionException("Projekt wird bereits ausgefuehrt.");
                    } catch (Exception ex) {
                        throw ex;
                    }
                }
            }

        }

    }

    public void cancelProjectExecution() throws IllegalUserActionException {
        synchronized (projectExecutionRemoteLock) {
            if (projectExecutionRemote == null || projectExecutionRemote.isTerminated()) {
                throw new IllegalUserActionException("projectExecutation laeuft nicht.", "Project wird nicht ausgeführt und kann somit nicht gestoppt werden.");
            } else {
                projectExecutionRemote.cancelExecution();
            }
        }
    }

    public TerminationReason getProjectExecutionTerminationReason() {
        synchronized (projectExecutionRemoteLock) {
            if (projectExecutionRemote != null) {
                return projectExecutionRemote.getTerminationReason();
            } else {
                return null;
            }
        }
    }

    public boolean isProjectExecutionRunning() {
        synchronized (projectExecutionRemoteLock) {
            if (projectExecutionRemote != null) {
                return !projectExecutionRemote.isTerminated();
            } else {
                return false;
            }
        }
    }

    /**
     * Gibt true zuruek wenn Projekt ausgefuehrt wurde und jetzt beendet ist.
     */
    public boolean isProjectExecutionTerminated() {
        synchronized (projectExecutionRemoteLock) {
            if (projectExecutionRemote != null) {
                return projectExecutionRemote.isTerminated();
            } else {
                return false;
            }
        }
    }

    /**
     * Gibt Debugger oder null zuruek wenn im Moment kein Debugger zur
     * Verfuegung steht.
     */
    public DebuggerRemote getDebuggerRemote() {
        synchronized (projectExecutionRemoteLock) {
            if (projectExecutionRemote == null) {
                return null;
            } else {
                if (projectExecutionRemote.isDebuggingActivated()) {
                    return projectExecutionRemote.getDebuggerRemote();
                } else {
                    return null;
                }
            }
        }
    }

    public void openDebugMonitorWindow() {
        MainWindow mainWindow = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DebugMonitorWindow(mainWindow).setVisible(true);
            }
        });
    }

    public void setProjektRunning(boolean running, boolean debug) {
        mainPanel.setProjektRunning(running, debug);
    }

    public Report getFinalReport() {
        if (finalReport == null) {
            System.err.println("Finalen Report noch nicht erhalten und kann damit nicht zuruek gegeben werden! return null.");
            return null;
        } else {
            return finalReport;
        }
    }

    @Override
    public void takeFinalReport(Report report) {
        AdditionalLogger.out.println(toString() + "MainWindow: Finalen Report erhalten.");
        TerminationReason terminationReason = report.getTerminationReason();
        finalReport = report;
        mainPanel.setMessage(report.toString(), terminationReason == TerminationReason.START_FAILED || terminationReason == TerminationReason.RUNTIME_ERROR);
        mainPanel.setProjektRunning(false, false);
        repaint();
    }

    @Override
    public void debugViewNeedsUpdate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Delegate fuer ProjectExecution OnDestroyCallback.
     */
    @Override
    public void onDestroy(ProjectExecution t) {
        AdditionalLogger.out.println("MainWindow: onDestroy() von ProjectExecution erhalten. Remote entfernen da ab jetzt ungueltig.");
        synchronized (projectExecutionRemoteLock) {
            projectExecutionRemote = null;
        }
    }

    public void destroyProjectExecution() {
        synchronized (projectExecutionRemoteLock) {
            if (projectExecutionRemote != null) {
                try {
                    projectExecutionRemote.destroyProjectExecution();
                } catch (NotTerminatedYetException ntye) {
                    System.err.println(ntye.getMessage());
                } catch (Exception e) {
                    ErrorDialog.showErrorDialog(e, this);
                }
            } else {
                System.err.println("projectExecutionRemote ist null und kann deshalb nicht destroyed werden!");
            }
        }

        repaint();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (settings.getBoolean(GeneralSettings.WARN_IF_PROJECT_NOT_SAVED_KEY, true)) {
            Project project = componentHub.getProject();
            if (project != null && project.didSomethingChanged()) {
                ProjectSaver.askToSave(this, project);
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
