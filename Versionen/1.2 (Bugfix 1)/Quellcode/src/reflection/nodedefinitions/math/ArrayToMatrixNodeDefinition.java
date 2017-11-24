package reflection.nodedefinitions.math;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.math.OneDimensionalArrayBasedMatrix;
import utils.ArrayHelper;

public class ArrayToMatrixNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Number.class;
            case 1:
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
            case 1:
                return "Spalten";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            case 1:
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
                return "Matrix";
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
        return "Array zu Matrix";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Math] [Umwandeln] Array to convert erzeugen Matrix";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ArrayToMatrix";
    }

    @Override
    public String getIconName() {
        return "Array-To-Matrix_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] werte = io.in(0, new Number[0]);
        Number spalten = (Number) io.in0(1, null);
        if(spalten == null) {
            double sqrt = Math.sqrt(werte.length);
            if(Math.abs(sqrt-((int) sqrt)) < 0.00000001) {
                spalten = (int)sqrt;
            } else {
                spalten = 1;
            }
        }

        Matrix matrix = new OneDimensionalArrayBasedMatrix(ArrayHelper.castToNumberArray(werte), spalten.intValue());

        io.out(0, matrix);
    }

}
