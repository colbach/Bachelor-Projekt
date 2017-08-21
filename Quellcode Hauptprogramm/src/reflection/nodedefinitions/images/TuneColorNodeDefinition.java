package reflection.nodedefinitions.images;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;

public class TuneColorNodeDefinition implements NodeDefinition {

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
                return Double.class;
            case 2:
                return Double.class;
            case 3:
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
                return "R";
            case 2:
                return "G";
            case 3:
                return "B";
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
                return BufferedImage.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Resultierendes Bild";
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
        return "Farbe anpassen";
    }

    @Override
    public String getDescription() {
        return "Passt Farbwerte an. Werte müssen zwischen -1 und 1 liegen." + TAG_PREAMBLE + "";
    }

    @Override
    public String getUniqueName() {
        return "buildin.TuneColor";
    }

    @Override
    public String getIconName() {
        return "Tune-Color_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        BufferedImage sourceImage = (BufferedImage) io.in0(0, null);
        if (sourceImage == null) {
            sourceImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        }
        Double r = (Double) io.in0(1, 0D);
        Double g = (Double) io.in0(2, 0D);
        Double b = (Double) io.in0(3, 0D);

        short[] red = new short[256];
        short[] green = new short[256];
        short[] blue = new short[256];
        short[] alpha = new short[256];

        for (short i = 0; i < 256; i++) {
            red[i] = faktor(i, r);
            green[i] = faktor(i, g);
            blue[i] = faktor(i, b);
            alpha[i] = i;
        }

        short[][] data;
        if (sourceImage.getColorModel().hasAlpha()) {
            data = new short[][]{
                red, green, blue, alpha
            };
        } else {
            data = new short[][]{
                red, green, blue
            };
        }

        LookupTable lookupTable = new ShortLookupTable(0, data);
        LookupOp op = new LookupOp(lookupTable, null);
        BufferedImage destinationImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());

        destinationImage = op.filter(sourceImage, destinationImage);

        io.out(0, destinationImage);
    }

    public short faktor(short v, double faktor) {
        return (short) (Math.round(v + faktor * multi(v)) % Short.MAX_VALUE);
    }

    public short multi(short v) {
        return (short) (255 - v);
    }

}
