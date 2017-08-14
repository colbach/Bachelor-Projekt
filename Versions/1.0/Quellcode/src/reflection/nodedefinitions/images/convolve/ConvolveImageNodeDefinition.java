package reflection.nodedefinitions.images.convolve;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import reflection.*;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.math.OneDimensionalArrayBasedMatrix;
import utils.ArrayHelper;

public class ConvolveImageNodeDefinition implements NodeDefinition {

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
                return Matrix.class;
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
                return "Kernel";
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
                return BufferedImage.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "gefaltetes Bild";
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
        return "Faltung durchführen";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + "Convolve";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ConvolveImage";
    }

    @Override
    public String getIconName() {
        return "Convolve-Image_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        BufferedImage sourceXImage = (BufferedImage) io.in0(0, null);

        BufferedImage sourceImage = new BufferedImage(sourceXImage.getWidth(), sourceXImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        sourceImage.getGraphics().drawImage(sourceXImage, 0, 0, null);

        if (sourceImage == null) {
            sourceImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        }
        Matrix matrix = (Matrix) io.in0(1, null);
        if (matrix == null) {
            matrix = new OneDimensionalArrayBasedMatrix(new Number[]{0, 0, 0, 0, 1, 0, 0, 0, 0}, 3);
        }

        Kernel kernel = new Kernel(matrix.getColumCount(), matrix.getRowCount(), ArrayHelper.numberArrayToPrimitiveFloatArray(matrix.to1DArray()));
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
        BufferedImage dstImage = op.filter(sourceImage, null);

        io.out(0, dstImage);
    }

}
