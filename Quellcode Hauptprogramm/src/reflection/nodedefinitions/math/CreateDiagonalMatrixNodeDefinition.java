package reflection.nodedefinitions.math;

import reflection.*;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.math.TwoDimensionalArrayBasedMatrix;
import utils.ArrayHelper;
import utils.math.MatrixAndVectorOperations;

public class CreateDiagonalMatrixNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Number.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Werte";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return true;
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
        return "" + TAG_PREAMBLE + " [Math] Matrix Diagonalmatrix diagonale erstellen";
    }

    @Override
    public String getUniqueName() {
        return "buildin.CreateDiagonalMatrix";
    }

    @Override
    public String getIconName() {
        return "Diagonal-Matrix_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] ns = io.in(0, new Number[0]);

        Matrix diag = new TwoDimensionalArrayBasedMatrix(ArrayHelper.castToNumberArray(ns));

        io.out(0, diag);
    }

}
