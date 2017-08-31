package reflection.nodedefinitions.images;

import java.awt.image.BufferedImage;
import reflection.nodedefinitions.camera.*;
import reflection.*;
import reflection.customdatatypes.BooleanGrid;
import reflection.customdatatypes.camera.Camera;
import reflection.customdatatypes.camera.Webcam;

public class GridSizeNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return BooleanGrid.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Raster";
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
        return "Raster Maße";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " Raster Grid Rastergrösse Rastergröße Grid Grösse Größe Photo Maße Masse Size Dimension";
    }

    @Override
    public String getUniqueName() {
        return "buildin.GridSize";
    }

    @Override
    public String getIconName() {
        return "Grid-Size_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        BooleanGrid img = (BooleanGrid) io.in0(0, null);
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
