package reflection.nodedefinitions.images;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.Color;
import java.awt.image.BufferedImage;
import reflection.nodedefinitions.camera.*;
import reflection.customdatatypes.camera.Camera;
import reflection.customdatatypes.camera.Webcam;
import utils.images.RGBHelper;

public class PickColorFromImageNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return BufferedImage.class;
            case 1:
                return Number.class;
            case 2:
                return Number.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Bild";
            case 1:
                return "x";
            case 2:
                return "y";
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
        if (index == 0) {
            return true;
        }
        return false;
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
        return false;
    }

    @Override
    public String getName() {
        return "Farbwert extrahieren";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Grafik] Farbe auswählen pick picker";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ColorPickFromImage";
    }

    @Override
    public String getIconName() {
        return "Color-Pick-From-Image_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        BufferedImage img = (BufferedImage) io.in0(0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
        int x = ((Number) io.in0(1, 0)).intValue();
        int y = ((Number) io.in0(2, 0)).intValue();

        Color color = new Color(img.getRGB(x, y));

        io.out(0, color);
    }

}
