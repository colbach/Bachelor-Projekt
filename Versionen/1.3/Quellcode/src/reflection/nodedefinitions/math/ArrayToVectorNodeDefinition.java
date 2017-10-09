package reflection.nodedefinitions.math;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import reflection.customdatatypes.math.ArrayBasedVector;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.math.OneDimensionalArrayBasedMatrix;
import reflection.customdatatypes.math.Vector;
import utils.ArrayHelper;

public class ArrayToVectorNodeDefinition implements NodeDefinition {

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
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Array zu Vektor";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Math] [Umwandeln] Array to convert erzeugen Vector";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ArrayToVector";
    }

    @Override
    public String getIconName() {
        return "Array-To-Vector_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] werte = io.in(0, new Number[0]);

        Vector v = new ArrayBasedVector(ArrayHelper.castToNumberArray(werte));

        io.out(0, v);
    }

}
