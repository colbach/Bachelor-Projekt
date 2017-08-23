package reflection.nodedefinitions.images;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static main.MainClass.PATH_FOR_ASSETS;

public class ColorFromCMYKNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 5;
    }

    @Override
    public Class getClassForInlet(int index) {
        return Float.class;
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "C";
            case 1:
                return "M";
            case 2:
                return "Y";
            case 3:
                return "K";
            case 4:
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
        if (index == 4) {
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
        return "CMYK-Farbe";
    }

    @Override
    public String getDescription() {
        return "Erzeugt Farbe anhand von CMYK-Werten. Werte müssen zwischen 0 und 1 liegen. ACHTUNG: Es handelt sich hierbei nur um eine sehr grobe Annäherung. Es werden in der aktuellen Implementierung KEINE Farbprofile verwendet." + TAG_PREAMBLE + "[Grafik]";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ColorFromCMYK";
    }

    @Override
    public String getIconName() {
        return "Color-From-CMYK_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws IOException {
        
        System.out.println("******** "  + io.in0(0, 0f).getClass());

        Float c = (Float)(((Float) io.in0(0, 0f)) * 255f);
        Float m = (Float)(((Float) io.in0(1, 0f)) * 255f);
        Float y = (Float)(((Float) io.in0(2, 0f)) * 255f);
        Float k = (Float)(((Float) io.in0(3, 0f)) * 255f);
        Float a = (Float)(((Float) io.in0(3, 0f)) * 255f);

        if (c > 255 || m > 255 || y > 255 || y > 255 || a > 255 || c < 0 || m < 0 || y < 0 || y < 0 || a < 0) {
            throw new IllegalArgumentException("Werte müssen zwischen 0 und 1 liegen");
        }

        int r = (int) (255 - Math.min(255, ((c / 255) * (255 - k) + k)));
        int g = (int) (255 - Math.min(255, ((m / 255) * (255 - k) + k)));
        int b = (int) (255 - Math.min(255, ((y / 255) * (255 - k) + k)));

        io.out(0, new Color(r, g, b, a.intValue()));
    }

}
