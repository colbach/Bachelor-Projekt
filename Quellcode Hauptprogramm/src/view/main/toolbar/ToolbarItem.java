package view.main.toolbar;

import view.assets.ImageAsset;

public abstract class ToolbarItem {

    private boolean clickable;
    private String label;
    private ImageAsset image;
    private boolean hidden = false;
    private boolean enabled = true;
    private boolean pressedDown = false;

    public ToolbarItem(boolean clickable, String label, ImageAsset image) {
        this.clickable = clickable;
        this.label = label;
        this.image = image;
    }

    /**
     * Konstruktor ohne ImageAsset. Achtung getImage() muss ueberschrieben
     * werden!
     */
    public ToolbarItem(boolean clickable, String label) {
        this.clickable = clickable;
        this.label = label;
        this.image = image;
    }

    /**
     * Wird bei Klick aufgerufen.
     */
    public abstract void clicked();

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ImageAsset getImage() {
        return image;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPressedDown() {
        return pressedDown;
    }

    public void setPressedDown(boolean pressedDown) {
        this.pressedDown = pressedDown;
    }
}
