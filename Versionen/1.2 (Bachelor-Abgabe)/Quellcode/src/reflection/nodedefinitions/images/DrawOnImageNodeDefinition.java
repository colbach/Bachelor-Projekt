package reflection.nodedefinitions.images;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import reflection.nodedefinitions.camera.*;
import reflection.customdatatypes.camera.Camera;
import reflection.customdatatypes.camera.Webcam;

public class DrawOnImageNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 4;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return BufferedImage.class;
            case 1:
                return BufferedImage.class;
            case 2:
                return Number.class;
            case 3:
                return Number.class;
            case 4:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Untergrund";
            case 1:
                return "Bild";
            case 2:
                return "x";
            case 3:
                return "y";
            case 4:
                return "Original bewahren";
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
            case 1:
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
                return "Neues Bild";
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
        return "Bild auf Bild zeichnen";
    }

    @Override
    public String getDescription() {
        return "Zeichnet Bild auf Untergrund." + TAG_PREAMBLE + " [Grafik] draw zeichet on auf overlay";
    }

    @Override
    public String getUniqueName() {
        return "buildin.DrawOnImage";
    }

    @Override
    public String getIconName() {
        return "Draw-On-Image_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        BufferedImage base = (BufferedImage) io.in0(0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
        BufferedImage image = (BufferedImage) io.in0(1, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
        int x = ((Number) io.in0(2, 0)).intValue();
        int y = ((Number) io.in0(3, 0)).intValue();
        Boolean copy = (Boolean) io.in0(4, false);
        
        io.terminatedTest();
        if(copy) {
            BufferedImage newBase = new BufferedImage(base.getWidth(), base.getHeight(), base.getType());
            newBase.getGraphics().drawImage(base, 0, 0, null);
            base = newBase;
        }
        
        io.terminatedTest();
        base.getGraphics().drawImage(image, x, y, null);
        
        io.terminatedTest();
        io.out(0, base);
    }

}
