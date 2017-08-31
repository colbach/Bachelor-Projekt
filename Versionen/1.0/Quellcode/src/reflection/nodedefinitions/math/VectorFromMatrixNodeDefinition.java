package reflection.nodedefinitions.math;

import java.util.ArrayList;
import reflection.*;
import reflection.customdatatypes.math.ArrayBasedVector;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.math.Vector;

public class VectorFromMatrixNodeDefinition implements NodeDefinition {

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
        switch (index) {
            case 0:
                return false;
            case 1:
                return true;
            case 2:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int index) {
        if (index == 0) {
            return true;
        }
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
                return Vector.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Vektor";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Vektor aus Matrix";
    }

    @Override
    public String getDescription() {
        return "Erstellt aus Zeile(n) oder Spalte(n) von Matrix einen Vektor. Sind mehrere angegeben werden zuerst Spalten-Vektoren dann Zeilen-Vektoren ermittelt." + TAG_PREAMBLE + " [Math] Matrix Vektor Vector";
    }

    @Override
    public String getUniqueName() {
        return "buildin.VectorFromMatrix";
    }

    @Override
    public String getIconName() {
        return "Vector-To-Matrix_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Matrix matrix = (Matrix) io.in0(0, Matrix.NULL_MATRIX);
        Object[] cols = io.in(1, new Object[0]);
        Object[] rows = io.in(2, new Object[0]);

        ArrayList<Vector> vektors = new ArrayList<>();
        for (Object col : cols) {
            int coli = ((Number) col).intValue();
            Vector Vector = new ArrayBasedVector(matrix.getColum(coli));
        }
        for (Object row : rows) {
            int rowi = ((Number) row).intValue();
            Vector Vector = new ArrayBasedVector(matrix.getRow(rowi));
        }

        io.out(0, vektors.toArray());
    }

}
