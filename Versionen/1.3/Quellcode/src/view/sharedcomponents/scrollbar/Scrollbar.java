package view.sharedcomponents.scrollbar;

import logging.AdditionalErr;
import logging.AdditionalLogger;
import utils.measurements.Area;
import view.main.MainPanel;
import view.listener.MouseAndKeyboardListener;

public final class Scrollbar extends MouseAndKeyboardListener {
    
    private int representedSize;
    
    /**
     * Position innerhalb Scrollbar: [0, 1].
     */
    private double position;
    
    private final Direction direction;
    
    private int scrollbarSize;

    public Scrollbar(int representedHeight, float position, Direction direction, Area area) {
        super();
        this.direction = direction;
        setArea(area);
        this.representedSize = representedHeight;
        this.position = position;
    }

    public void setRepresentedHeightOrWidth(int representedHeight) {
        this.representedSize = representedHeight;
    }

    public double getPosition() {
        
        // Werte normalisieren...
        if(position < 0) {
            this.position = 0;
        } else if(position > 1) {
            this.position = 1;
        }
        
        return position;
    }

    public void setPosition(double position) {
        
        // Werte normalisieren und setzen...
        if(position < 0) {
            this.position = 0;
        } else if(position > 1) {
            this.position = 1;
        } else {
            this.position = position;
        }
    }
    
    /**
     * Berechnet Groesse der anzuzeigenden Bar.
     */
    public int getBarSize() {
        return (int)Math.min((scrollbarSize / (float)representedSize) * scrollbarSize, scrollbarSize);
    }
    
    public int getBarBegin() {
        return (int) (position * (scrollbarSize - getBarSize()));
    }
    
    /**
     * Gibt an ob Scrollbar ueberhaupt sichtbar ist.
     */
    public boolean isVisible() {
        return getBarSize() < scrollbarSize-1;
    }
    
    private int moveableDistance() {
        return scrollbarSize - getBarSize();
    }

    public Direction getDirection() {
        return direction;
    }

    public int getRepresentedSize() {
        return representedSize;
    }

    public void setRepresentedSize(int representedSize) {
        this.representedSize = representedSize;
    }

    public int getScrollbarSize() {
        return scrollbarSize;
    }
    
    public void setScrollbarSize(int scrollbarSize) {
        this.scrollbarSize = scrollbarSize;
    }
    
    /**
     * Registriert Bewegung von Bar.
     */
    public void moveBar(int movedDistance) {
        int moveableDistance = moveableDistance();
        double d = 0;
        if(moveableDistance > 0) {
            d = movedDistance / (double)moveableDistance;
        } else {
            AdditionalLogger.err.println("moveableDistance = " + moveableDistance);
        }
        setPosition(getPosition() + d);
    }
    
    public void moveContent(int distanceOnContent) {
        int moveableDistance = moveableDistance();
        double a = getRepresentedSize() - getScrollbarSize();
        double d = 0;
        if(moveableDistance > 0 && a >= 0) {
            d = distanceOnContent / a;
        } else {
            AdditionalLogger.err.println("moveableDistance = " + moveableDistance);
        }
        setPosition(getPosition() + d);
    }
    
    public void setArea(Area area) {
        // Clip von MouseAndKeyboardListener setzen...
        super.setClip(area);
        
        // size setzen
        if(direction == Direction.VERTICAL) {
            scrollbarSize = area.getHeight();
        } else if(direction == Direction.HORIZONTAL) {
            scrollbarSize = area.getWidth();
        } else {
            System.err.println("Direction muss VERTICAL oder HORIZONTAL sein!");
        }
    }
    
    public Area getArea() {
        return getClip();
    }

    @Override
    public boolean mousePressed(int x, int y) {
        return true;
    }

    @Override
    public boolean mouseDragged(int lastX, int lastY, int actualX, int actualY, int startX, int startY) {
        if(direction == Direction.VERTICAL) {
            if(!(actualY < getArea().getY() || actualY > getArea().getY() + getArea().getHeight())) { // Falls Maus noch im Y-Bereich ist
                moveBar(actualY-lastY); // bewege Cursor
            }
        } else {
            if(!(actualX < getArea().getX() || actualX > getArea().getX() + getArea().getWidth())) { // Falls Maus noch im X-Bereich ist
                moveBar(actualX-lastX);
            }
        }
        return true;
    }
    
    public int getOffsetForContent() {
        if(isVisible()) { // Wenn Scrollbar sichtbar ist
            return (int) ((representedSize - scrollbarSize) * getPosition());
        } else {
            return 0; // Keine Verschiebung
        }
    }
    
    @Override
    public boolean mouseMouseWheelMoved(int scrollAmount) {
        setPosition(getPosition() + scrollAmount * 8 * (1 / (double)getRepresentedSize()));
        return true;
    }
    
}
