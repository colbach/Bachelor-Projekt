package reflection.nodedefinitions.images;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class CropNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 5;
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
            case 3:
                return Number.class;
            case 4:
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
                return "links";
            case 2:
                return "rechts";
            case 3:
                return "unten";
            case 4:
                return "oben";
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
        return 3;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return BufferedImage.class;
            case 1:
                return Integer.class;
            case 2:
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Skaliertes Bild";
            case 1:
                return "angepasste Breite";
            case 2:
                return "angepasste Höhe";
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
        return "Bild weiden";
    }

    @Override
    public String getDescription() {
        return "Weidet (positive Werte) oder Beschneidet (negative Werte) Bild absolut oder relativ zur Ursprungsgrösse." + TAG_PREAMBLE + " [Grafik] Image Bild Grösse Größe Photo beschneiden erweitern weiden resize arbeitsfläche arbeitsfläsche";
    }

    @Override
    public String getUniqueName() {
        return "buildin.CropImage";
    }

    @Override
    public String getIconName() {
        return "Crop-Image_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        BufferedImage image = (BufferedImage) io.in0(0, null);

        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        int outWidth = -1;
        int outHeight = -1;

        Boolean relative = (Boolean) io.in0(1, false);
        Boolean verhaeltnissBewahren = (Boolean) io.in0(2, false);
        Number inWidth = (Number) io.in0(3, null);
        Number inHeight = (Number) io.in0(4, null);

        if (inWidth == null && inHeight == null) {
            io.out(0, image);
            io.out(1, originalWidth);
            io.out(2, originalHeight);

        } else {

            if (relative) {
                if (inWidth != null) {
                    outWidth = (int) (originalWidth * inWidth.doubleValue());
                }
                if (inHeight != null) {
                    outHeight = (int) (originalHeight * inHeight.doubleValue());
                }
            } else {
                if (inWidth != null) {
                    outWidth = inWidth.intValue();
                }
                if (inHeight != null) {
                    outHeight = inHeight.intValue();
                }
            }
            if (inWidth == null) {
                if (verhaeltnissBewahren) {
                    outWidth = (int) (originalWidth * (outHeight / (double) originalHeight));
                } else {
                    outWidth = originalWidth;
                }
            }
            if (inHeight == null) {
                if (verhaeltnissBewahren) {
                    outHeight = (int) (originalHeight * (outWidth / (double) originalWidth));
                } else {
                    outHeight = originalHeight;
                }
            }

//            System.out.println("outWidth = " + outWidth);
//            System.out.println("outHeight = " + outHeight);
            BufferedImage outImage = new BufferedImage(outWidth, outHeight, image.getType());
            Graphics g = outImage.getGraphics();
            g.drawImage(image, 0, 0, outWidth, outHeight, null);

            io.out(0, outImage);
            io.out(1, outWidth);
            io.out(2, outHeight);
        }
    }

}
