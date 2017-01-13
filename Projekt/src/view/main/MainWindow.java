package view.main;

import view.main.runreport.RunReportWindow;
import model.runproject.ExecutionHub;
import model.runproject.Debugger;
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
import exceptions.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import view.debug.DebugMonitorWindow;
import static view.main.MainPanelMode.PROJECT_RUNNING;

/**
 * Hauptfenster.
 */
public class MainWindow extends javax.swing.JFrame implements ActionListener {

    private int mouseX, mouseY;
    private long redrawCounter;
    private ExecutionHub executionHub = null;
    private final Timer timer;
    private static long executionHubchangeCounter = -1;

    /**
     * Event fuer Timer.
     */
    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        ExecutionHub eh = this.executionHub; // Umgeht Syncronisations-Problem das dadurch entsteht dass executionHub nicht syncronisiert wird

        if (eh != null) { // Projekt wird oder wurde ausgefuehrt
            long result = eh.needsRedrawOfUI(executionHubchangeCounter);
            if (result != -1) { // Falls sich was geaendert hat
                executionHubchangeCounter = result;
                this.repaint(); // neu zeichnen
            }
        }
    }

    public void updateTitle() {
        String title = "";
        if (settings.getBoolean(GeneralSettings.DEVELOPER_MOUSE_COORDINATES_KEY, false)) {
            title += "Mausposition = [" + mouseX + ", " + mouseY + "]";
            if (settings.getBoolean(GeneralSettings.DEVELOPER_REDRAW_COUNTER_KEY, false)) {
                title += ", ";
            }
        }
        if (settings.getBoolean(GeneralSettings.DEVELOPER_REDRAW_COUNTER_KEY, false)) {
            title += "Redraw = " + redrawCounter;
            if (settings.getBoolean(GeneralSettings.DEVELOPER_NODE_COUNT_KEY, false) && getProject() != null) {
                title += ", ";
            }
        }
        if (settings.getBoolean(GeneralSettings.DEVELOPER_NODE_COUNT_KEY, false) && getProject() != null) {
            title += "Elemente = " + getProject().getNodeCount();
        }
        setTitle(title);
    }

    private InputManager inputmanager = new InputManager();
    private GeneralSettings settings = GeneralSettings.getInstance();

    private Project project = new Project();

    private NodeCollectionWindow nodeCollectionWindow = null;

    /**
     * Erzeugt Hauptfenster.
     */
    public MainWindow() {

        try {
            // Initialisiere Komponenten...
            mainPanel = new view.main.MainPanel(this);
            mainPanel.setFocusCycleRoot(true);
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

            // Toolbar beim InputManager registrieren
            inputmanager.addListener(mainPanel.getHorizontalScrollbar());
            inputmanager.addListener(mainPanel.getVerticalScrollbar());
            inputmanager.addListener(mainPanel.getToolbar());
            inputmanager.addListener(mainPanel.getMainPanelMouseAndKeyboardListener());

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
        this.timer = new Timer(200, this);
        this.timer.start();
    }

    public void openProjectWindow() {

        MainWindow thisWindow = this;

        // NodeCollectionOverviewPanel oeffnen...
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                // Fenster erzeugen...
                ProjectWindow nodeCollectionWindow = new ProjectWindow(thisWindow, project);

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
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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

    private void mainPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMouseClicked
        inputmanager.mouseClicked(evt.getX(), evt.getY());
        repaint();
    }

    private void mainPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMouseDragged
        inputmanager.mouseDragged(evt.getX(), evt.getY());
        repaint();
    }

    private void mainPanelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMouseMoved
        inputmanager.mouseMoved(evt.getX(), evt.getY());
        //repaint();
    }

    private void mainPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMousePressed
        inputmanager.mousePressed(evt.getX(), evt.getY(), evt.getButton() == MouseEvent.BUTTON1);
        repaint();
    }

    private void mainPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMouseReleased
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
        updateTitle();
        super.repaint();
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    public void removeRightClickMenue() {
        mainPanel.rightClickMenue = null;
    }

    public ExecutionHub getExecutionHub() {
        return executionHub;
    }

    public void startProjectExecution(boolean debug) throws IllegalUserActionException {

        if (this.executionHub != null && !this.executionHub.isFinished()) { // Falls Projekt bereits ausgefuehrt wird
            throw new IllegalUserActionException("executionHub ist nicht null", "Project wird bereits ausgeführt.");

        } else {
            Node startNode = project.getStartNode();
            if (startNode == null) {
                throw new IllegalUserActionException("Versuch Project Auszufuehren ohne definierten Startnode.", "Bitte definieren sie ein Start-Element bevor sie das Projekt ausführen.");
            }
            executionHub = new ExecutionHub(startNode, debug);
            setProjektRunning(true);
        }

    }

    public void cancelProjectExecution() throws IllegalUserActionException {
        if (this.executionHub == null) {
            throw new IllegalUserActionException("executionHub ist null", "Project wird nicht ausgeführt und kann somit nicht gestoppt werden.");
        } else {
            executionHub.cancel();
            setProjektRunning(false);
        }

    }

    public void resetExecutionHub() {
        executionHub = null;
        setProjektRunning(false);
    }

    public boolean isProjectExecutionRunning() {
        return this.executionHub != null && !this.executionHub.isFinished();
    }

    /**
     * Gibt true zuruek wenn Projekt ausgefuehrt wurde ohne dass
     * resetExecutionHub() aufgerufen wurde.
     */
    public boolean isProjectExecutionFinished() {
        return this.executionHub != null && this.executionHub.isFinished();
    }

    /**
     * Gibt Debugger oder null zuruek wenn im Moment kein Debugger zur
     * verfuegung steht.
     *
     * @return
     */
    public Debugger getDebugger() {
        if (this.executionHub == null) {
            return null;
        } else {
            return executionHub.getDebugger();
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

    public void setProjektRunning(boolean running) {
        mainPanel.setProjektRunning(running);
    }

}
