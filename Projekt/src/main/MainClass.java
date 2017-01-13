package main;

import view.main.MainWindow;
import java.awt.*;
import javax.swing.*;
import log.AdditionalOut;
import log.Logging;
import view.assets.ImageAsset;

public class MainClass {

    /**
     * Pfad zu eingebauten Nodes.
     */
    public static final String PATH_FOR_BUILDIN_NODES_CLASSES = "./ReflexiveExecutableNodes/build/classes";

    /**
     * Pfad zu Bild-Assets.
     */
    public static final String PATH_FOR_ASSETS = "./assets";

    public static void main(String[] args) {

        // Initialisiere Logger...
        Logging.getGeneralInstance().setupSystemStreams();
        
        // Preload ImageAssets...
        long nanoTime = System.nanoTime();
        ImageAsset.preload();
        long usedNanoSeconds = System.nanoTime() - nanoTime;
        AdditionalOut.println("ImageAssets vorgeladen (" + (usedNanoSeconds / 1000000) + "." + ((usedNanoSeconds / 10000) % 100) + "ms)");
        
        // Setze System Look-And-Feel...
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Oeffne Hauptfenster in eigenem Thread...
        EventQueue.invokeLater(() -> {
            try {
                // Fenster erzeugen...
                MainWindow mainWindow = new MainWindow();

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
    }

}
