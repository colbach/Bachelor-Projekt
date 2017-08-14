package reflection.nodedefinitions.images;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import reflection.nodedefinitions.camera.*;
import reflection.*;
import reflection.customdatatypes.camera.Camera;
import reflection.customdatatypes.camera.Webcam;

public class RotateNodeDefinition implements NodeDefinition {

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
                return Double.class;
            case 2:
                return Double.class;
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
                return "Rad";
            case 2:
                return "Grad";
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
                return "Rotiertes Bild";
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
        return "Bild rotieren";
    }

    @Override
    public String getDescription() {
        return "Dreht Bild. Die ursprünglichen Abmasse können sich hierbei ändern." + TAG_PREAMBLE + " [Grafik] Image Bild rotate drehen rotieren";
    }

    @Override
    public String getUniqueName() {
        return "buildin.RotateImage";
    }

    @Override
    public String getIconName() {
        return "Rotate-Image_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        BufferedImage image = (BufferedImage) io.in0(0, null);
        Double rad = (Double) io.in0(1, (Double) 0.0d);
        rad += (((Double) io.in0(2, (Double) 0.0d)) / 180) * Math.PI;

        io.terminatedTest();
        
        double a = rad;
        double x = image.getWidth();
        double y = image.getHeight();
        double h = Math.abs(Math.cos(a - Math.PI / 2) * x) + Math.abs(Math.cos(Math.PI - a) * y);
        double b = Math.abs(Math.cos(Math.PI - a) * x) + Math.abs(Math.cos(a - Math.PI / 2) * y);

        io.terminatedTest();
        
        AffineTransform at = new AffineTransform();
        at.translate((int) (b / 2), (int) (h / 2));
        at.rotate(a);
        //at.scale(1, 1);
        at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        BufferedImage rotatedImage = new BufferedImage((int) b, (int) h, BufferedImage.TYPE_INT_ARGB);
        ((Graphics2D) rotatedImage.getGraphics()).drawImage(image, at, null);

        io.terminatedTest();
        io.out(0, rotatedImage);

    }

}
