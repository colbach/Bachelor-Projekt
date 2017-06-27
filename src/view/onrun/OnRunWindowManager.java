package view.onrun;

import view.onrun.showimage.ShowBitmapWindow;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import reflection.SmartIdentifier;
import reflection.SmartIdentifierContext;
import view.onrun.shownumberarray.ShowNumberArrayWindow;

public class OnRunWindowManager {

    private static final HashSet<OnRunWindowManager> instances = new HashSet<>();

    public static HashSet<OnRunWindowManager> getInstances() {
        return new HashSet<>(instances);
    }

    public final HashMap<SmartIdentifier, ShowSomethingWindow> windows;

    public OnRunWindowManager() {
        this.windows = new HashMap<>();
        synchronized (instances) {
            instances.add(this);
        }
    }

    public synchronized void show(final Object data, SmartIdentifier smartIdentifier, SmartIdentifierContext smartIdentifierContext) throws UnsupportedShowTypeException {

        // Pruefung ob Klasse Anzeige unterstuetzt...
        if (!(data instanceof BufferedImage || data instanceof Number[])) {
            throw new UnsupportedShowTypeException(data.getClass());
        }

        // Kontext erzeugen falls nicht vorhanden...
        if (smartIdentifier == null) { // Fall: Kein Kontext
            smartIdentifier = smartIdentifierContext.createNew();
        }

        final SmartIdentifier finalSmartIdentifier = smartIdentifier; // Final-Pointer fuer anonyme Klasse

        // Alles auf Swing-Thread schieben...
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ShowSomethingWindow window;
                    synchronized (windows) {
                        window = windows.get(finalSmartIdentifier);
                    }
                    if (window != null && !window.isClosed()) { // Fall: Fenster existiert bereits
                        if (window.canShow(data)) { // Fall: Fenster kompartibel
                            window.show(data);
                        } else { // Fall: Fenster nicht kompartibel
                            ShowSomethingWindow showSomethingWindow = createShowWindow(data, finalSmartIdentifier);
                            showSomethingWindow.setLocation(window.getLocation());
                            showSomethingWindow.setSize(window.getWidth(), window.getHeight());
                            window.dispose();
                            showSomethingWindow.setVisible(true);
                        }
                    } else {
                        if (data instanceof BufferedImage || data instanceof Number[]) {
                            ShowSomethingWindow showSomethingWindow = createShowWindow(data, finalSmartIdentifier);
                            showSomethingWindow.setVisible(true);
                        } else {
                            System.err.println("Klasse zur Anzeige nicht unterstuetzt");
                        }
                    }
                } catch (UnsupportedShowTypeException unsupportedShowTypeException) { // Sollte wegen window.canShow(data) nie schief gehen
                    System.err.println(unsupportedShowTypeException.getMessage());
                }
            }
        });
    }

    private ShowSomethingWindow createShowWindow(Object data, SmartIdentifier smartIdentifier) throws UnsupportedShowTypeException {
        synchronized (windows) {
            if (data instanceof BufferedImage) {
                ShowSomethingWindow showSomethingWindow = new ShowBitmapWindow((BufferedImage) data, smartIdentifier);
                showSomethingWindow.setVisible(true);
                windows.put(smartIdentifier, showSomethingWindow);
                return showSomethingWindow;
            } else if (data instanceof Number[]) {
                ShowSomethingWindow showSomethingWindow = new ShowNumberArrayWindow((Number[]) data, smartIdentifier);
                showSomethingWindow.setVisible(true);
                windows.put(smartIdentifier, showSomethingWindow);
                return showSomethingWindow;
            } else {
                throw new UnsupportedShowTypeException(data.getClass());
            }
        }
    }

    public void dispose(SmartIdentifier smartIdentifier) {
        synchronized (windows) {
            ShowSomethingWindow get = windows.get(smartIdentifier);
            if (get == null) {
                System.err.println("Es wird kein Fenster mit der Id " + smartIdentifier + " angezeigt.");
            } else {
                get.dispose();
                windows.remove(get);
            }
        }
    }

    public void disposeAll() {
        synchronized (windows) {
            windows.values().forEach((showSomethingWindow) -> {
                showSomethingWindow.dispose();
            });
            windows.clear();
        }
    }

    public int count() {
        synchronized (windows) {
            int i = 0;
            for (ShowSomethingWindow showSomethingWindow : windows.values()) {
                if (!showSomethingWindow.isClosed()) {
                    i++;
                }
            }
            return i;
        }
    }

}
