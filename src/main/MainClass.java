package main;

import commandline.CommandLinePrompt;
import commandline.CommandLineThread;
import componenthub.ComponentHub;
import view.main.MainWindow;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Scanner;
import javax.swing.*;
import logging.AdditionalLogger;
import logging.AdditionalOut;
import logging.AdvancedLogger;
import model.Project;
import model.VoidType;
import model.resourceloading.InstanceLoader;
import model.resourceloading.nodedefinitionslibrary.BuildInNodeDefinitionsLibrary;
import model.runproject.ProjectRunner;
import model.runproject.debugger.Breakpoints;
import model.runproject.debugger.DebuggerRules;
import model.type.TypeNameTranslation;
import settings.GeneralSettings;
import static settings.GeneralSettings.*;
import settings.LastProjectMemory;
import startuptasks.StartupTask;
import startuptasks.StartupTaskBatchRunner;
import utils.measurements.InfiniteArea;
import utils.measurements.NonArea;
import view.assets.ImageAsset;
import view.onrun.OnRunWindowManager;

public class MainClass {

    /**
     * Pfad zu eingebauten Nodes.
     */
    public static final String PATH_FOR_BUILDIN_NODE_CLASSES = "./build/classes/";
    public static final String BUILDIN_NODE_CLASSES_CLASSNAME_PREFIX = "reflection.nodedefinitions.";
    //public static final String PATH_FOR_BUILDIN_NODE_CLASSES = "./ReflexiveExecutableNodes/build/classes";

    /**
     * Pfad zu Bild-Assets.
     */
    public static final String PATH_FOR_ASSETS = "./assets";

    /**
     * Programm-Version.
     */
    public static final String VERSION_NAME = "Pre 1.0";

    /**
     * Startzeit.
     */
    public static final Date START_TIME = new Date();

    public static boolean guiEnabled, promptEnabled;

    public static void main(String[] args) {

        // Initialisiere Logger...
        AdvancedLogger.getGeneralInstance().setupSystemStreams();

        guiEnabled = GeneralSettings.getInstance().getBoolean(GeneralSettings.START_GUI_ON_STARTUP_KEY, GeneralSettings.START_GUI_ON_STARTUP_DEFAULT_VALUE);
        promptEnabled = GeneralSettings.getInstance().getBoolean(GeneralSettings.START_PROMPT_ON_STARTUP_KEY, GeneralSettings.START_PROMPT_ON_STARTUP_DEFAULT_VALUE);
        for (String arg : args) { // args ueberpruefen...
            if (arg.equalsIgnoreCase("gui") || arg.equalsIgnoreCase("guienabled")) {
                guiEnabled = true;
                args = new String[0]; // args resseten da nicht mehr benoetigt
            } else if (arg.equalsIgnoreCase("prompt") || arg.equalsIgnoreCase("promptenabled")) {
                promptEnabled = true;
                args = new String[0]; // args resseten da nicht mehr benoetigt
            } else if (arg.equalsIgnoreCase("guidisabled")) {
                guiEnabled = false;
                args = new String[0]; // args resseten da nicht mehr benoetigt
            } else if (arg.equalsIgnoreCase("promptdisabled")) {
                promptEnabled = false;
                args = new String[0]; // args resseten da nicht mehr benoetigt
            }
        }
        final String[] finalArgs = args;

        if (!guiEnabled && !promptEnabled) { // Falls weder GUI noch Prompt gestartet werden soll...
            System.out.println("\"gui\" eingeben um grafische Benutzeroberflaeche zu oeffnen oder \"prompt\" um Prompt zu starten");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("gui")) {
                guiEnabled = true;
            } else if (line.equalsIgnoreCase("prompt")) {
                promptEnabled = true;
            } else {
                System.out.println("Keine gueltige Eingabe getroffen. Beende Programm.");
                System.exit(0);
            }
        }

        StartupTaskBatchRunner tasks = new StartupTaskBatchRunner(new StartupTask[]{
            new StartupTask(100, "Schriften vorbereiten", true) { // UI laden...
                @Override
                public void run() {
                    BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
                    Graphics g = bi.getGraphics();
                    g.setColor(Color.BLACK);
                    g.getFontMetrics().stringWidth("Ԉ");
                }
            },
            new StartupTask(5, "Look-And-Feel laden", guiEnabled) { // LAF laden...
                @Override
                public void run() {
                    // Setze System Look-And-Feel...
                    if (GeneralSettings.getInstance().getBoolean(VIEW_USE_NIMBUS_LAF_KEY, false)) { // Nimbus-LAF
                        try {
                            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                    break;
                                }
                            }
                        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else { // System-LAF
                        try {
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            },
            new StartupTask(10, "Allgemeine Instanzen laden", true) { // Allgemeine Instanzen laden...
                @Override
                public void run() {
                    // (Bei Singleon reciht es aus diese 1x abzufragen damit diese Initialisiert sind)
                    // (Manche sind bereits geladen)
                    Breakpoints.getInstance();
                    DebuggerRules.getInstance();
                    AdvancedLogger.getGeneralInstance();
                    ComponentHub.getInstance();
                    GeneralSettings.getInstance();
                    InfiniteArea.getInstance();
                    NonArea.getInstance();
                    ProjectRunner.getInstance();
                    VoidType.getInstance();
                    TypeNameTranslation.getGermanInstance();
                }
            },
            new StartupTask(100, "Grafiken laden", guiEnabled) {
                @Override
                public void run() { // Preload ImageAssets...
                    long nanoTime = System.nanoTime();
                    ImageAsset.preload(this);
                    long usedNanoSeconds = System.nanoTime() - nanoTime;
                }
            },
            new StartupTask(100, "Elemente laden", true) {
                @Override
                public void run() { // BuildInNodeDefinitionsLibrary vorladen...
                    BuildInNodeDefinitionsLibrary.getInstance();
                }
            },
            new StartupTask(100, "Projekt laden", true) { // Projekt oeffnen...
                @Override
                public void run() {
                    addProgress(0.00, "Nach letztem Projekt suchen.");
                    Project project = LastProjectMemory.getLastOrNewProject(this);
                    ComponentHub.getInstance().setProject(project);
                }
            },
            new StartupTask(5, "Kommandozeile starten", promptEnabled) { // Starte Prompt...
                @Override
                public void run() {
                    addProgress(0.00, "Kommandozeile initialisieren.");
                    CommandLineThread commandLineThread = CommandLineThread.launchInstance();
                    addProgress(0.50, "Kommandozeile ausführen.");
                    if (finalArgs.length != 0 && commandLineThread != null) {
                        CommandLinePrompt commandLinePrompt = commandLineThread.getCommandLinePrompt();
                        if (commandLinePrompt != null) {
                            commandLinePrompt.execute(finalArgs);
                        }
                    }
                }
            },
            new StartupTask(5, "UI laden", guiEnabled) { // UI laden...
                @Override
                public void run() {
                    MainWindow gui = ComponentHub.getInstance().getGUIBlocking();
                    gui.loadFully();
                    gui.repaint();
                }
            }
        });

        // Oeffne Hauptfenster in eigenem Thread...
        EventQueue.invokeLater(() -> {
            try {
                // Fenster erzeugen...
                MainWindow mainWindow = new MainWindow(tasks);

                // In ComponentHub setzen...
                ComponentHub componentHub = ComponentHub.getInstance();
                componentHub.setGUI(mainWindow);

                // Fenster mittig plazieren...
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                mainWindow.setLocation(dim.width / 2 - mainWindow.getSize().width / 2, dim.height / 2 - mainWindow.getSize().height / 2);

                // Fenster anzeigen...
                mainWindow.setVisible(true);

                // Setze DefaultCloseOperation...
                mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Programm soll terminieren wenn dieses Fenster geschlossen wird
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        tasks.start();
    }

}
