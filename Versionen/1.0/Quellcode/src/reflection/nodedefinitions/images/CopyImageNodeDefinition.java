package reflection.nodedefinitions.images;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import reflection.nodedefinitions.camera.*;
import reflection.*;
import reflection.customdatatypes.camera.Camera;
import reflection.customdatatypes.camera.Webcam;

public class CopyImageNodeDefinition implements NodeDefinition {

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
        return false;
    }

    @Override
    public boolean isInletEngaged(int index) {
        switch (index) {
            case 0:
                return true;
            default:
                return false;
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
                return BufferedImage.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Original";
            case 1:
                return "Kopie";
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
        return "Bild kopieren";
    }

    @Override
    public String getDescription() {
        return "Stellt eine tiefe Kopie eines Bildes her." + TAG_PREAMBLE + " [Grafik] copy clone duplicate dublicate dublizieren duplizieren";
    }

    @Override
    public String getUniqueName() {
        return "buildin.CopyImage";
    }

    @Override
    public String getIconName() {
        return "Copy-Image_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        BufferedImage original = (BufferedImage) io.in0(0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));

        io.terminatedTest();
        BufferedImage copy = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        copy.getGraphics().drawImage(original, 0, 0, null);

        io.terminatedTest();
        
        io.out(0, original);
        io.out(0, copy);
    }

}
