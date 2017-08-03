package reflection.nodedefinitions;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class _TestElementD implements NodeDefinition {


    @Override
    public boolean isInletEngaged(int inletIndex) {
        return false;
    }
    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        return BufferedImage.class;
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        return "In " + inletIndex;
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        return false;
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int outletIndex) {
        return BufferedImage.class;
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        return "Out " + outletIndex;
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return false;
    }

    @Override
    public String getName() {
        return "Testbaustein D";
    }

    @Override
    public String getDescription() {
        return "XXXX???";
    }

    @Override
    public String getUniqueName() {
        return "BuildIn.TestD";
    }

    @Override
    public String getIconName() {
        return null;
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {
        
        BufferedImage sourceImage = (BufferedImage) io.in0(0, null);
        
        
        
        
        
        
        
        BufferedImage dstImage = null;
        int s = 15;
        float[] blurKernel = generateGaussianKernel(s);
        Kernel kernel = new Kernel(s, s, blurKernel);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
        dstImage = op.filter(sourceImage, null);
        
        
        
        io.out(0, dstImage);
        
        
        
        
        
        
        
        
    }
    
     /**
     * Builds the Gaussian kernel that will be used to convolve the image.
     * Source: http://www.math.colostate.edu/~benoit/software.html Directlink:
     * http://www.math.colostate.edu/~benoit/Java/microscopy/SmootherFilter.java
     *
     * DISCLAIMER OF WARRANTY This source code is provided "as is" and without
     * any express or implied warranties whatsoever. The user must assume the
     * entire risk of using the source code.
     *
     * @return the convolution kernel
     */
    private float[] generateGaussianKernel(int size) {
        float[] data;
        double gVal;
        float total;

        // Gaussian kernel generieren
        data = new float[size * size];
        total = 0.0f;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                gVal = (1 / (2 * Math.PI)) + Math.exp(-Math.pow(x - 3, 2) - Math.pow(y - 3, 2));
                data[(y * size) + x] = (float) gVal;
                total += data[(y * size) + x];
            }
        }

        // Normalisieren
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                data[(y * size) + x] /= total;
            }
        }

        return data;

    }

    
}
