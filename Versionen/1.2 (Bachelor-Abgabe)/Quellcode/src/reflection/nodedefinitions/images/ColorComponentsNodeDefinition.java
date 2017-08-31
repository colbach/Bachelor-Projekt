package reflection.nodedefinitions.images;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.Color;

public class ColorComponentsNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Color.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Farbe";
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
        return 6;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Float.class;
            case 1:
                return Float.class;
            case 2:
                return Float.class;
            case 3:
                return Float.class;
            case 4:
                return Float.class;
            case 5:
                return Float.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Rot (RGB: R)";
            case 1:
                return "Grün (RGB: G)";
            case 2:
                return "Blau (RGB: B)";
            case 3:
                return "Farbton (HSB: H)";
            case 4:
                return "Farbsättigung (HSB: S)";
            case 5:
                return "Helligkeit (HSB: B)";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:
                return false;
            case 3:
                return false;
            case 4:
                return false;
            case 5:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Farbbestandteile";
    }

    @Override
    public String getDescription() {
        return "Extrahiert die einzelnen Farbkanäle aus Farbe." + TAG_PREAMBLE + "[Grafik]";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ColorComponents";
    }

    @Override
    public String getIconName() {
        return "Color_Component_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Color c = (Color) io.in0(0, Color.BLACK);

        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        float[] hsb = Color.RGBtoHSB(r, g, b, null);

        io.out(0, r / 255f);
        io.out(1, g / 255f);
        io.out(2, b / 255f);
        io.out(3, hsb[0]);
        io.out(4, hsb[1]);
        io.out(5, hsb[2]);
    }

}
