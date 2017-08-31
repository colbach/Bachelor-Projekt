package reflection.nodedefinitions.images;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.image.BufferedImage;
import reflection.nodedefinitions.camera.*;
import reflection.customdatatypes.camera.Camera;
import reflection.customdatatypes.camera.Webcam;

public class ImageSizeNodeDefinition implements NodeDefinition {

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

        return false;
    }

    @Override
    public int getOutletCount() {
        return 2;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Integer.class;
            case 1:
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Breite";
            case 1:
                return "Höhe";
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
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Bild Maße";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Grafik] Image Bildgrösse Bildgröße Bild Grösse Größe Photo Maße Masse Size Dimension";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ImageSize";
    }

    @Override
    public String getIconName() {
        return "Image-Size_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        BufferedImage img = (BufferedImage) io.in0(0, null);
        Integer breite;
        Integer hhe;
        if (img == null) {
            breite = 0;
            hhe = 0;
        } else {
            breite = img.getWidth();
            hhe = img.getHeight();
        }

        io.out(0, breite);
        io.out(1, hhe);
    }

}
