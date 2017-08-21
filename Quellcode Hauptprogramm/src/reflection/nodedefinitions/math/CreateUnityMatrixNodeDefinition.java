package reflection.nodedefinitions.math;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.math.TwoDimensionalArrayBasedMatrix;
import utils.math.MatrixAndVectorOperations;

public class CreateUnityMatrixNodeDefinition implements NodeDefinition {

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
                return "Einheitsmatrix";
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
        return "Einheitsmatrix erzeugen";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Math] Matrix Einheitsmatrix Identität unity erstellen";
    }

    @Override
    public String getUniqueName() {
        return "buildin.CreateUnityMatrix";
    }

    @Override
    public String getIconName() {
        return "Unity-Matrix_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Integer size = (Integer) io.in0(0, 1);

        Matrix unity = new TwoDimensionalArrayBasedMatrix(size);

        io.out(0, unity);
    }

}
