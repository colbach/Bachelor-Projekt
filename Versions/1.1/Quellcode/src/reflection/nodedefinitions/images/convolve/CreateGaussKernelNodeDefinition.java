package reflection.nodedefinitions.images.convolve;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.math.PrimitiveFloatWrappingMatrix;

public class CreateGaussKernelNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Grösse";
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
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Matrix.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Gauss Kernel";
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
        return "Gauss Kernel erzeugen";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + "";
    }

    @Override
    public String getUniqueName() {
        return "buildin.CreateGaussKernel";
    }

    @Override
    public String getIconName() {
        return "Gauss-Kernel_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Integer size = (Integer) io.in0(0,3);

        // Gaussian kernel generieren
        float[][] kernel = new float[size][size];
        double value;
        float sum = 0.0f;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                value = (1 / (2 * Math.PI)) + Math.exp(-Math.pow(x - 3, 2) - Math.pow(y - 3, 2));
                kernel[y][x] = (float) value;
                sum += kernel[y][x];
            }
        }

        // Normalisieren
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                kernel[y][x] /= sum;
            }
        }
        
        io.out(0, new PrimitiveFloatWrappingMatrix(kernel));
    }
    
}
