package view.main.contextmenu;

public abstract class RightClickMenueItem {
    
    private final String label;
    private boolean pressed;
    private boolean clickable;

    public RightClickMenueItem(String label, boolean clickable) {
        this.label = label;
        this.clickable = clickable;
    }
    
    public RightClickMenueItem(String label) {
        this.label = label;
        this.clickable = true;
    }
    
    /**
     * Wird bei Klick aufgerufen.
     */
    public abstract void clicked();

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public String getLabel() {
        return label;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}
