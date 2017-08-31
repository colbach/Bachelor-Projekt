package reflection.nodedefinitions.math;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import reflection.customdatatypes.math.Matrix;
import utils.math.MatrixAndVectorOperations;

public class InvertMatrixNodeDefinition implements NodeDefinition {

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
                return Matrix.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Inverse";
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
        return "Inverse Matrix";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Math] Matrix invertieren umdrehen umkehren";
    }

    @Override
    public String getUniqueName() {
        return "buildin.InvertMatrix";
    }

    @Override
    public String getIconName() {
        return "Invert-Matrix_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Matrix matrix = (Matrix) io.in0(0, Matrix.NULL_MATRIX);

        Matrix inverse = MatrixAndVectorOperations.invert(matrix);

        io.out(0, inverse);
    }

}
