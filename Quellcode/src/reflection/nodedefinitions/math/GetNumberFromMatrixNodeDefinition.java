package reflection.nodedefinitions.math;

import java.util.ArrayList;
import reflection.*;
import reflection.customdatatypes.math.Matrix;

public class GetNumberFromMatrixNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Matrix.class;
            case 1:
                return Number.class;
            case 2:
                return Number.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Matrix";
            case 1:
                return "Spalte";
            case 2:
                return "Zeile";
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
                return Number.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Wert";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        return false;
    }

    @Override
    public String getName() {
        return "Zahl aus Matrix";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Math] aus Matrix 1 Element get";
    }

    @Override
    public String getUniqueName() {
        return "buildin.NumberFromMatrix";
    }

    @Override
    public String getIconName() {
        return "Get-Element-From-Matrix_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Matrix matrix = (Matrix) io.in0(0, Matrix.NULL_MATRIX);
        Number spalte = (Number) io.in0(1, 0);
        Number zeile = (Number) io.in0(2, 0);

        io.out(0, matrix.get(zeile.intValue(), spalte.intValue()));
    }

}
