package reflection.nodedefinitions.math;

import java.util.ArrayList;
import reflection.*;
import reflection.customdatatypes.math.*;

public class SetNumberInMatrixNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 4;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Matrix.class;
            case 1:
                return Integer.class;
            case 2:
                return Integer.class;
            case 3:
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
            case 3:
                return "Wert";
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
        return 2;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Number.class;
            case 1:
                return Matrix.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Alter Wert";
            case 1:
                return "Matrix";
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
        return "Zahl in Matrix setzen";
    }

    @Override
    public String getDescription() {
        return "Erzeugt neue Matrix und setzt in dieser den Wert. Die uhrsprüngliche Matrix bleibt unberührt." + TAG_PREAMBLE + " [Math] in Matrix Element setzen";
    }

    @Override
    public String getUniqueName() {
        return "buildin.SetNumberToMatrix";
    }

    @Override
    public String getIconName() {
        return "Set-Element-In-Matrix_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        TwoDimensionalArrayBasedMatrix matrix = new TwoDimensionalArrayBasedMatrix((Matrix) io.in0(0, Matrix.NULL_MATRIX));
        Integer spalte = (Integer) io.in0(1, 0);
        Integer zeile = (Integer) io.in0(2, 0);
        Number number = (Number) io.in0(3, Matrix.DEFAULT_NUMBER);
        io.out(0, matrix.get(zeile, spalte));
        matrix.set(spalte, zeile, number);
        io.out(1, matrix);
    }

}
