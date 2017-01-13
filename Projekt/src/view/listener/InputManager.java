package view.listener;

import java.util.ArrayList;

public class InputManager {

    private final ArrayList<MouseAndKeyboardListener> listeners = new ArrayList<>();
    private int lastMouseX = -1, lastMouseY = -1;
    private int mousePressedX = -1, mousePressedY = -1;

    //private static InputManager instance;
    public InputManager() {
    }

    /*public synchronized static InputManager getInstance() {
        if(instance == null) {
            instance = new InputManager();
        }
        return instance;
    }*/
    public void mouseClicked(int x, int y) {

        synchronized (listeners) {
            for (int i = 0; i < listeners.size(); i++) { // fuer alle Listener

                MouseAndKeyboardListener listener = listeners.get(i);
                if (listener.isStillListening()) { // pruefen ob Listener noch interessiert ist
                    if (listener.getClip().isInside(x, y)) {
                        if (listener.mouseClicked(x, y)) {
                            break;
                        }
                    }
                } else {
                    listeners.remove(i); // Listener aus Liste entfernen
                }
            }
        }
    }

    public void mousePressed(int x, int y, boolean left) {

        mousePressedX = x;
        mousePressedY = y;
        lastMouseX = x;
        lastMouseY = y;

        synchronized (listeners) {
            for (int i = 0; i < listeners.size(); i++) { // fuer alle Listener

                MouseAndKeyboardListener listener = listeners.get(i);
                if (listener.isStillListening()) { // pruefen ob Listener noch interessiert ist
                    if (listener.getClip().isInside(x, y)) {
                        if (left) {
                            if (listener.mousePressed(x, y)) {
                                break;
                            }
                        } else {
                            if (listener.mousePressedRight(x, y)) {
                                break;
                            }
                        }
                    }
                } else {
                    listeners.remove(i); // Listener aus Liste entfernen
                }
            }
        }
    }

    public void mouseMoved(int x, int y) {

        synchronized (listeners) {
            for (int i = 0; i < listeners.size(); i++) { // fuer alle Listener

                MouseAndKeyboardListener listener = listeners.get(i);
                if (listener.isStillListening()) { // pruefen ob Listener noch interessiert ist
                    if (listener.getClip().isInside(x, y)) {
                        if (listener.mouseMoved(lastMouseX, lastMouseY, x, y)) {
                            break;
                        }
                    }
                } else {
                    listeners.remove(i); // Listener aus Liste entfernen
                }
            }
        }
        lastMouseX = x;
        lastMouseY = y;
    }

    public void mouseReleased(int x, int y) {

        synchronized (listeners) {
            for (int i = 0; i < listeners.size(); i++) { // fuer alle Listener

                MouseAndKeyboardListener listener = listeners.get(i);
                if (listener != null && listener.isStillListening()) { // pruefen ob Listener noch interessiert ist
                    if (listener.getClip().isInside(x, y) || listener.getClip().isInside(mousePressedX, mousePressedY)) {
                        if (listener.mouseReleased(x, y, mousePressedX, mousePressedY)) {
                            break;
                        }
                    }
                } else {
                    listeners.remove(i); // Listener aus Liste entfernen
                }
            }
        }
        lastMouseX = x;
        lastMouseY = y;
    }

    public void mouseDragged(int x, int y) {

        synchronized (listeners) {
            for (int i = 0; i < listeners.size(); i++) { // fuer alle Listener                
                MouseAndKeyboardListener listener = listeners.get(i);
                if (listener.isStillListening()) { // pruefen ob Listener noch interessiert ist
                    if (listener.getClip().isInside(x, y) || listener.getClip().isInside(mousePressedX, mousePressedY)) {
                        if (listener.mouseDragged(lastMouseX, lastMouseY, x, y, mousePressedX, mousePressedY)) {
                            break;
                        }
                    }
                } else {
                    listeners.remove(i); // Listener aus Liste entfernen
                }
            }
        }
        lastMouseX = x;
        lastMouseY = y;
    }

    public void mouseMouseWheelMoved(int scrollAmount) {

        synchronized (listeners) {
            for (int i = 0; i < listeners.size(); i++) { // fuer alle Listener

                MouseAndKeyboardListener listener = listeners.get(i);
                if (listener.isStillListening()) { // pruefen ob Listener noch interessiert ist
                    if (listener.mouseMouseWheelMoved(scrollAmount)) {
                        break;
                    }
                } else {
                    listeners.remove(i); // Listener aus Liste entfernen
                }
            }
        }
    }

    public SpecialKey keyCodeToSpecialKey(int code) {
        switch (code) {
            case 17:
                return SpecialKey.CTRL;
            case 157:
                return SpecialKey.CMD;
            case 18:
                return SpecialKey.ALT;
            case 16:
                return SpecialKey.SHIFT;
            case 27:
                return SpecialKey.ESC;
            case 8:
            case 46:
                return SpecialKey.DELETE;
            case 10:
                return SpecialKey.ENTER;
            default:
                return null;
        }
    }

    // nicht verwendet
    public void keyTyped(char c, int code) {
        // nicht implementiert
    }

    public void keyPressed(char c, int code) {

        synchronized (listeners) {

            // auf SpecialKey testen...
            SpecialKey specialKey = keyCodeToSpecialKey(code);
            if (specialKey != null) {
                for (int i = 0; i < listeners.size(); i++) { // fuer alle Listener
                    boolean result = listeners.get(i).specialKeyPressed(specialKey);
                    if (result) {
                        break;
                    }
                }
            }

            // Listener informieren...
            for (int i = 0; i < listeners.size(); i++) { // fuer alle Listener
                boolean result = listeners.get(i).keyPressed(c);
                if (result) {
                    break;
                }
            }
        }
    }

    /*CTRL,   // Keycode: 17
    CMD,    // Keycode: 157
    ALT,    // Keycode: 18
    SHIFT,  // Keycode: 16
    ESC,    // Keycode: 27
    DELETE; // Keycode: 8 oder 46*/
    public void keyReleased(char c, int code) {
        synchronized (listeners) {

            // auf SpecialKey testen...
            SpecialKey specialKey = keyCodeToSpecialKey(code);
            if (specialKey != null) {
                for (int i = 0; i < listeners.size(); i++) { // fuer alle Listener
                    boolean result = listeners.get(i).specialKeyReleased(specialKey);
                    if (result) {
                        break;
                    }
                }
            }

            // Listener informieren...
            for (int i = 0; i < listeners.size(); i++) { // fuer alle Listener
                boolean result = listeners.get(i).keyPressed(c);
                if (result) {
                    break;
                }
            }
        }
    }

    public boolean addListener(MouseAndKeyboardListener e) {
        synchronized (listeners) {
            return listeners.add(e);
        }
    }

}
