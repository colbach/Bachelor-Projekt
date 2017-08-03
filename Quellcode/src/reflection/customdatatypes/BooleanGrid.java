package reflection.customdatatypes;

public abstract class BooleanGrid {

    public abstract boolean getBoolean(int x, int y);

    public abstract int getWidth();

    public abstract int getHeight();
    
    @Override
    public String toString() {
        return getWidth() + "x" + getHeight() + " Boolean Grid";
    }

}
