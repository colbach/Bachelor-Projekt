package reflection.nodedefinitions.images;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import reflection.customdatatypes.*;

public class BooleanGridToImageNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 4;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return BooleanGrid.class;
            case 1:
                return Color.class;
            case 2:
                return Color.class;
            case 3:
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Raster";
            case 1:
                return "True-Farbe";
            case 2:
                return "False-Farbe";
            case 3:
                return "Punkt-Breite";
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
            case 2:
                return false;
            case 3:
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
                return BufferedImage.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Bild";
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
        return "Raster zu Bild";
    }

    @Override
    public String getDescription() {
        return "Wandelt Raster besetehend aus Wahrheitswerten in Bild um" + TAG_PREAMBLE + " [Grafik] [Umwandeln] True False umwandeln convert to Image raster gatter gitter Color";
    }

    @Override
    public String getUniqueName() {
        return "buildin.BooleanGridToImage";
    }

    @Override
    public String getIconName() {
        return "Boolean-Grid-To-Image_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        BooleanGrid raster = (BooleanGrid) io.in0(0, new BooleanGrid() {
            @Override
            public boolean getBoolean(int x, int y) {
                return false;
            }
            @Override
            public int getWidth() {
                return 1;
            }
            @Override
            public int getHeight() {
                return 1;
            }
        });
        
        Color truefarbe = (Color) io.in0(1, Color.BLACK);
        Color falsefarbe = (Color) io.in0(2, Color.WHITE);
        Integer punktbreite = (Integer) io.in0(3, 1);

        BufferedImage bild = new BufferedImage(raster.getWidth() * punktbreite, raster.getHeight() * punktbreite, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bild.getGraphics();
        for (int x = 0; x < raster.getWidth(); x += punktbreite) {
            for (int y = 0; y < raster.getHeight(); y += punktbreite) {
                if (raster.getBoolean(x, y)) {
                    g.setColor(truefarbe);
                } else {
                    g.setColor(falsefarbe);
                }
                g.fillRect(x, y, punktbreite, punktbreite);
            }
        }

        io.out(0, bild);
    }

}
