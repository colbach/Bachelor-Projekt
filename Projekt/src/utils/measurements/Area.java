package utils.measurements;

/**
 * Representiert eine Flaeche.
 */
public class Area {

    private int x, y, width, height;

    public Area(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isInside(int x, int y) {
        return x > this.x && x < this.x + this.width && y > this.y && y < this.y + this.height;
    }

    public boolean intersect(Area other) {
        if(other instanceof InfiniteArea) { // Spiezial Fall: Unendliche Flaeche
            return true;
        } if(other instanceof NonArea) { // Spiezial Fall: Keine Flaeche
            return false;
        } else {
            return this.isInside(other.x, other.y)
                || this.isInside(other.x + other.width, other.y)
                || this.isInside(other.x, other.y + other.height)
                || this.isInside(other.x + other.width, other.y + other.height)
                || other.isInside(this.x, this.y)
                || other.isInside(this.x + this.width, this.y)
                || other.isInside(this.x, this.y + this.height)
                || other.isInside(this.x + this.width, this.y + this.height);
        }
        
    }
}
