package reflection.nodedefinitions.math;

import reflection.nodedefinitions.images.*;
import java.awt.image.BufferedImage;
import reflection.nodedefinitions.camera.*;
import reflection.*;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.camera.Camera;
import reflection.customdatatypes.camera.Webcam;

public class MatrixSizeNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Matrix.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Matrix";
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
                return "Anzahl Spalten";
            case 1:
                return "Anzahl Zeilen";
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
        return "Matrix Maße";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Math] Matrix Matrixgrösse Matrixgröße Maße Masse Size Dimension";
    }

    @Override
    public String getUniqueName() {
        return "buildin.MatrixSize";
    }

    @Override
    public String getIconName() {
        return "Matrix-Size_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Matrix m = (Matrix) io.in0(0, null);
        Integer cs;
        Integer rs;
        if (m == null) {
            cs = 0;
            rs = 0;
        } else {
            cs = m.getColumCount();
            rs = m.getRowCount();
        }

        io.out(0, cs);
        io.out(1, rs);
    }

}
