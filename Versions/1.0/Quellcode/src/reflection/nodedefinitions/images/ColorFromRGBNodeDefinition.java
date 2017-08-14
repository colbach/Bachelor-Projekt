package reflection.nodedefinitions.images;

import java.awt.Color;
import reflection.*;

public class ColorFromRGBNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 4;
    }

    @Override
    public Class getClassForInlet(int index) {
        return Float.class;
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "R";
            case 1:
                return "G";
            case 2:
                return "B";
            case 3:
                return "A";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int index) {
        if (index == 3) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Color.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Farbe";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "RGB-Farbe";
    }

    @Override
    public String getDescription() {
        return "Erzeugt Farbe anhand von RGB-Werten. Werte müssen zwischen 0 und 1 liegen." + TAG_PREAMBLE + "";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ColorFromRGB";
    }

    @Override
    public String getIconName() {
        return "Color-From-RGB_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Float r = (Float) io.in0(0, 0f);
        Float g = (Float) io.in0(1, 0f);
        Float b = (Float) io.in0(2, 0f);
        Float a = (Float) io.in0(3, null);

        if (r > 1 || g > 1 || b > 1 || r < 0 || g < 0 || b < 0) {
            throw new IllegalArgumentException("Werte müssen zwischen 0 und 1 liegen");
        }

        Color farbe;

        if (a == null) {
            farbe = new Color(r, g, b);
        } else {
            if (a > 1 || a < 0) {
                throw new IllegalArgumentException("Werte müssen zwischen 0 und 1 liegen");
            }
            farbe = new Color(r, g, b, a);
        }
        io.out(0, farbe);
    }

}
