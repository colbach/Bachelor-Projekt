package reflection.nodedefinitions.images;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import reflection.nodedefinitions.camera.*;
import reflection.*;
import reflection.nodedefinitionsupport.camera.Camera;
import reflection.nodedefinitionsupport.camera.Webcam;

public class NewImageNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Color.class;
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
                return "Farbe";
            case 1:
                return "Breite";
            case 2:
                return "Höhe";
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
                return false;
            default:
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
        return "Neues Bild";
    }

    @Override
    public String getDescription() {
        return "Erzeugt neues Bild." + TAG_PREAMBLE + " [Grafik] new neu neues Bild image clear blank blankes farbig color";
    }

    @Override
    public String getUniqueName() {
        return "buildin.NewImage";
    }

    @Override
    public String getIconName() {
        return "New-Image_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Color c = (Color) io.in0(0, Color.WHITE);
        int w = ((Number) io.in0(1, 1)).intValue();
        int h = ((Number) io.in0(2, 1)).intValue();

        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = newImage.getGraphics();
        graphics.setColor(c);
        graphics.fillRect(0, 0, w, h);
        
        io.terminatedTest();
        io.out(0, newImage);
    }

}
