package reflection.nodedefinitions.images;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.Graphics;
import java.awt.Image;
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

        BufferedImage i = (BufferedImage) io.in0(0, null);
        int l = io.inN(1, 0).intValue();
        int r = io.inN(2, 0).intValue();
        int u = io.inN(3, 0).intValue();
        int o = io.inN(4, 0).intValue();
        
        BufferedImage n = new BufferedImage(i.getWidth() + l + r, i.getHeight() + o + u, i.getType());
        
        n.getGraphics().drawImage(i, l, o, null);
        
        io.out(0, n);
        io.out(1, n.getWidth());
        io.out(2, n.getHeight());
    }

}
