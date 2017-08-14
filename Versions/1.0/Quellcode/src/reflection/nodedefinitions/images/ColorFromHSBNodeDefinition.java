package reflection.nodedefinitions.images;

import java.awt.Color;
import reflection.*;

public class ColorFromHSBNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        return Float.class;
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "H";
            case 1:
                return "S";
            case 2:
                return "B";
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
        return true;
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
        return "HSB-Farbe";
    }

    @Override
    public String getDescription() {
        return "Erzeugt Farbe anhand von HSB-Werten. Werte müssen zwischen 0 und 1 liegen." + TAG_PREAMBLE + "";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ColorFromHSB";
    }

    @Override
    public String getIconName() {
        return "Color-From-HSB_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {
        
        Float h = (Float) io.in0(0, 0f);
        Float s = (Float) io.in0(1, 0f);
        Float b = (Float) io.in0(2, 0f);
        
        if (h > 1 || s > 1 || b > 1 || h < 0 || s < 0 || b < 0) {
            throw new IllegalArgumentException("Werte müssen zwischen 0 und 1 liegen");
        }

        Color farbe = Color.getHSBColor(h, s, b);
        
        io.out(0, farbe);
    }

}
