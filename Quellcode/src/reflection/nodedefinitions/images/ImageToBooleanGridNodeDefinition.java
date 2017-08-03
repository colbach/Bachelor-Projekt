package reflection.nodedefinitions.images;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import reflection.*;
import reflection.customdatatypes.*;
import utils.images.BrightnessCalculator;

public class ImageToBooleanGridNodeDefinition implements NodeDefinition {

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
                return Function.class;
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
                return "(Farbe) -> Wahrheitswert";
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
                return BooleanGrid.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Raster";
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
        return "Bild zu Raster";
    }

    @Override
    public String getDescription() {
        return "Wandelt Bild in Raster um. Function wird auf jeden Pixel ausgeführt um zu entscheiden welcher Wert in Raster übernommen wird." + TAG_PREAMBLE + " True False umwandeln convert to Image raster gatter gitter";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ImageToBooleanGrid";
    }

    @Override
    public String getIconName() {
        return "Image-To-Boolean-Grid_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws FunctionNotAdoptableException {

        BufferedImage bild = (BufferedImage) io.in0(0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
        Function f = (Function) io.in0(1);
        if(f == null) {
            f = new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    return new Object[]{Boolean.TRUE};
                }
            };
        }
        
        io.terminatedTest();
        boolean[][] bs = new boolean[bild.getWidth()][bild.getHeight()];
        
        for(int x=0; x<bild.getWidth(); x++) {
            for(int y=0; y<bild.getHeight(); y++) {
                io.terminatedTest();
                bs[x][y] = f.evaluateBoolean(new Color(bild.getRGB(x, y)));
            }
        }

        io.terminatedTest();
        io.out(0, new BooleanGrid() {
            @Override
            public boolean getBoolean(int x, int y) {
                return (Boolean) bs[x][y];
            }
            @Override
            public int getWidth() {
                return bs.length;
            }
            @Override
            public int getHeight() {
                if (bs.length > 0) {
                    return bs[0].length;
                } else {
                    return 0;
                }
            }
        });
    }

}
