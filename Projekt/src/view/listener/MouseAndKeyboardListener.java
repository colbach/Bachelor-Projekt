package view.listener;

import utils.measurements.Area;
import utils.measurements.InfiniteArea;

public abstract class MouseAndKeyboardListener {

    /**
     * Beschraenkt Flaeche innerhalb welcher auf Werte gehoert wird.
     */
    public Area clip;

    public MouseAndKeyboardListener(Area clip) {
        this.clip = clip;
    }

    public MouseAndKeyboardListener() {
        this(InfiniteArea.getInstance());
    }
    
    
    /**
     * Wird bei Maus-Klick aufgerufen. Muss true zurueckgeben wenn kein weiterer
     * MouseAndKeyboardListener das Event empfangen soll.
     */
    public boolean mouseClicked(int x, int y) {
        return false;
    }

    /**
     * Wird bei gedruecktem-Ziehen der Maus aufgerufen. Muss true zurueckgeben
     * wenn kein weiterer MouseAndKeyboardListener das Event empfangen soll.
     */
    public boolean mouseDragged(int lastX, int lastY, int actualX, int actualY, int startX, int startY) {
        return false;
    }

    /**
     * Wird bei gedruecktem-Ziehen der Maus aufgerufen. Muss true zurueckgeben
     * wenn kein weiterer MouseAndKeyboardListener das Event empfangen soll.
     */
    public boolean mouseMoved(int lastX, int lastY, int actualX, int actualY) {
        return false;
    }

    /**
     * Wird aufgerufen wenn Maus gedrueckt wird. Muss true zurueckgeben wenn
     * kein weiterer MouseAndKeyboardListener das Event empfangen soll.
     */
    public boolean mousePressed(int x, int y) {
        return false;
    }
    
    /**
     * Wird aufgerufen wenn Maus rechts gedrueckt wird. Muss true zurueckgeben wenn
     * kein weiterer MouseAndKeyboardListener das Event empfangen soll.
     */
    public boolean mousePressedRight(int x, int y) {
        return false;
    }

    /**
     * Wird aufgerufen wenn Maus losgelassen wird. Muss true zurueckgeben wenn
     * kein weiterer MouseAndKeyboardListener das Event empfangen soll.
     */
    public boolean mouseReleased(int x, int y, int pressedX, int pressedY) {
        return false;
    }

    /**
     * Wird aufgerufen wenn Taste auf Tastatur gedrueckt wird. Muss true
     * zurueckgeben wenn kein weiterer MouseAndKeyboardListener das Event
     * empfangen soll.
     */
    public boolean keyPressed(char key) {
        return false;
    }
    
    /**
     * Wird aufgerufen wenn Taste auf Tastatur losgelassen wird. Muss true
     * zurueckgeben wenn kein weiterer MouseAndKeyboardListener das Event
     * empfangen soll.
     */
    public boolean keyReleased(char key) {
        return false;
    }

    /**
     * Wird beim Runterdruecken von Specialtaste aufgerufen. Muss true zurueckgeben wenn
     * kein weiterer MouseAndKeyboardListener das Event empfangen soll.
     */
    public boolean specialKeyPressed(SpecialKey specialKey) {
        return false;
    }
    
    /**
     * Wird beim Loslassen von Specialtaste aufgerufen. Muss true zurueckgeben wenn
     * kein weiterer MouseAndKeyboardListener das Event empfangen soll.
     */
    public boolean specialKeyReleased(SpecialKey specialKey) {
        return false;
    }
    
    /**
     * Diese Methode wird vor jedem aufruf anderer Methoden aufgerufen. Wenn
     * diese Methode false zuruek gibt entfernt der Manager diesen Listener aus
     * der verwalteten Liste an Listenern.
     */
    public boolean isStillListening() {
        return true;
    }

    /**
     * Gibt Bereich zurueck innerhalb welchem Listener auf Eingaben hoert.
     */
    public final /* nicht ueberschreibbar */ Area getClip() {
        return clip;
    }

    /**
     * Setzt Bereich innerhalb welchem Listener auf Eingaben hoert.
     */
    public void setClip(Area clip) {
        this.clip = clip;
    }

    /**
     * Wird bei gedrehtem Maus-Rad aufgerufen. Muss true zurueckgeben
     * wenn kein weiterer MouseAndKeyboardListener das Event empfangen soll.
     */
    public boolean mouseMouseWheelMoved(int scrollAmount) {
        return false;
    }
    
}
