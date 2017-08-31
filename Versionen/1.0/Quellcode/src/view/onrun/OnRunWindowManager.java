package view.onrun;

import view.onrun.showimage.ShowBitmapWindow;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import reflection.customdatatypes.SmartIdentifier;
import reflection.SmartIdentifierContext;
import reflection.customdatatypes.math.MathObject;
import reflection.customdatatypes.math.NumberMathObject;
import view.onrun.showmathobject.ShowMathObjectWindow;
import view.onrun.shownumberarray.ShowNumberArrayWindow;
import view.onrun.showtext.ShowTextWindow;

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
        if (!(data instanceof BufferedImage || data instanceof Number[] || data instanceof MathObject || data instanceof Number || data instanceof String)) {
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
                        ShowSomethingWindow showSomethingWindow = createShowWindow(data, finalSmartIdentifier);
                        showSomethingWindow.setVisible(true);
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
                ShowSomethingWindow showSomethingWindow = new ShowBitmapWindow((BufferedImage) data, smartIdentifier, this);
                showSomethingWindow.setVisible(true);
                windows.put(smartIdentifier, showSomethingWindow);
                return showSomethingWindow;
            } else if (data instanceof Number[]) {
                ShowSomethingWindow showSomethingWindow = new ShowNumberArrayWindow((Number[]) data, smartIdentifier, this);
                showSomethingWindow.setVisible(true);
                windows.put(smartIdentifier, showSomethingWindow);
                return showSomethingWindow;
            } else if (data instanceof Number || data instanceof MathObject) {
                if (data instanceof Number) {
                    data = new NumberMathObject((Number) data);
                }
                ShowSomethingWindow showSomethingWindow = new ShowMathObjectWindow((MathObject) data, smartIdentifier, this);
                showSomethingWindow.setVisible(true);
                windows.put(smartIdentifier, showSomethingWindow);
                return showSomethingWindow;
            } else if (data instanceof String) {
                ShowSomethingWindow showSomethingWindow = new ShowTextWindow((String) data, smartIdentifier, this);
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
                get.releaseMemory();
                windows.remove(get);
            }
        }
    }

    public void disposeAll() {
        synchronized (windows) {
            windows.values().forEach((showSomethingWindow) -> {
                showSomethingWindow.dispose();
                showSomethingWindow.releaseMemory();
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
