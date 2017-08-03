package reflection.nodedefinitions.images;

import reflection.*;
import reflection.customdatatypes.BooleanGrid;

public class BooleanGridtoArrayNodeDefinition implements NodeDefinition {

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
        return true;
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Wahrheitswerte";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Raster zu Array";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Arrays] ";
    }

    @Override
    public String getUniqueName() {
        return "buildin.BooleanGridtoArray";
    }

    @Override
    public String getIconName() {
        return "Boolean-Grid-To-Array_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        BooleanGrid raster = (BooleanGrid) io.in0(0, null);

        if (raster == null) {

            io.out(0, new Boolean[0]);

        } else {

            Boolean[] bs = new Boolean[raster.getWidth() * raster.getHeight()];
            int height = raster.getHeight();
            int width = raster.getWidth();
            for (int y = 0; y < height; y++) {
                io.terminatedTest();
                for (int x = 0; x < width; x++) {
                    bs[y * width + x] = raster.getBoolean(x, y);
                }
            }
            io.out(0, bs);
        }

    }

}
