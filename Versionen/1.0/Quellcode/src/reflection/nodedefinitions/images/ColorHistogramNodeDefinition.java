package reflection.nodedefinitions.images;

import java.awt.image.BufferedImage;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;
import utils.images.RGBHelper;

public class ColorHistogramNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return BufferedImage.class;
            case 1:
                return Boolean.class;
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
                return "Kumulativ";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
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
    public boolean isInletEngaged(int i) {
        if (i == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int getOutletCount() {
        return 4;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Integer.class;
            case 1:
                return Integer.class;
            case 2:
                return Integer.class;
            case 3:
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Rot";
            case 1:
                return "Grün";
            case 2:
                return "Blau";
            case 3:
                return "Helligkeit";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Histogram erstellen";
    }

    @Override
    public String getDescription() {
        return "Erstellt Histogram der einzelnen Farbkanäle eines Bildes." + TAG_PREAMBLE + " [Grafik]";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ColorHistogram";
    }

    @Override
    public String getIconName() {
        return "Histogram_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        BufferedImage bi = (BufferedImage) io.in0(0);
        Boolean cumulativ = (Boolean) io.in0(1, false);

        Integer[] red = new Integer[256];
        for (int i = 0; i < red.length; i++) {
            red[i] = 0;
        }
        Integer[] green = new Integer[256];
        for (int i = 0; i < green.length; i++) {
            green[i] = 0;
        }
        Integer[] blue = new Integer[256];
        for (int i = 0; i < blue.length; i++) {
            blue[i] = 0;
        }
        Integer[] light = new Integer[256];
        for (int i = 0; i < light.length; i++) {
            light[i] = 0;
        }

        io.terminatedTest();

        if (bi != null) {

            for (int ix = 0; ix < bi.getWidth(); ix++) {
                for (int iy = 0; iy < bi.getHeight(); iy++) {
                    byte r = RGBHelper.r(bi.getRGB(ix, iy));
                    red[(r + 256) % 256] = red[(r + 256) % 256] + 1;

                    byte g = RGBHelper.g(bi.getRGB(ix, iy));
                    green[(g + 256) % 256] = green[(g + 256) % 256] + 1;

                    byte b = RGBHelper.b(bi.getRGB(ix, iy));
                    blue[(b + 256) % 256] = blue[(b + 256) % 256] + 1;

                    byte h = (byte) (256d * Math.sqrt(0.299 * Math.pow(r / 256d, 2) + 0.587 * Math.pow(g / 256d, 2) + 0.114 * Math.pow(b / 256d, 2)));
                    light[(b + 256) % 256] = light[(h + 256) % 256] + 1;
                }

                io.terminatedTest();
            }
        }

        io.out(0, red);
        io.out(1, green);
        io.out(2, blue);
        io.out(3, light);
    }

}
