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

public class PickAvgColorFromImageNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return BufferedImage.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Bild";
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
        return "Durchschnittlicher Farbwert";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Grafik] Farbe auswählen pick picker";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ColorPickAvgFromImage";
    }

    @Override
    public String getIconName() {
        return "Avg-Color-Pick_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        BufferedImage img = (BufferedImage) io.in0(0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
        
        long sumR = 0, sumG = 0, sumB = 0, i = 0;
        
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                Color c = new Color(img.getRGB(x, y));
                sumR += c.getRed();
                sumG += c.getGreen();
                sumB += c.getBlue();
                i++;
            }
        }
        
        int r = (int) (sumR/(float)i);
        int g = (int) (sumG/(float)i);
        int b = (int) (sumB/(float)i);
        
        Color color = new Color(r, g, b);
        
        io.out(0, color);
    }

}
