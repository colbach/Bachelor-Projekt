package reflection.nodedefinitions.math;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.util.ArrayList;
import reflection.customdatatypes.math.Matrix;

public class MatrixToArrayNodeDefinition implements NodeDefinition {

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
        return 3;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Number.class;
            case 1:
                return Number.class;
            case 2:
                return Number.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Werte";
            case 1:
                return "Spalten";
            case 2:
                return "Zeilen";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            case 1:
                return false;
            case 2:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Matrix zu Array";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Math] [Umwandeln] Array to convert erzeugen Matrix";
    }

    @Override
    public String getUniqueName() {
        return "buildin.MatrixToArray";
    }

    @Override
    public String getIconName() {
        return "Matrix-To-Array_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Matrix matrix = (Matrix) io.in0(0, Matrix.NULL_MATRIX);

        ArrayList<Number> werte = new ArrayList<>();
        for (int r = 0; r < matrix.getRowCount(); r++) {
            for (int c = 0; c < matrix.getColumCount(); c++) {
                werte.add(matrix.get(r, c));
            }
        }
        Number spalten = matrix.getColumCount();
        Number zeilen = matrix.getRowCount();

        io.out(0, werte.toArray());
        io.out(1, spalten);
        io.out(2, zeilen);
    }

}
