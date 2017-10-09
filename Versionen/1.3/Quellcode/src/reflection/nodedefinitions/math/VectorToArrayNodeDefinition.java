package reflection.nodedefinitions.math;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.util.ArrayList;
import reflection.customdatatypes.math.Vector;

public class VectorToArrayNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Vector.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Vector";
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
                return Number.class;
            case 1:
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
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Vector zu Array";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Math] [Umwandeln] Array to convert erzeugen Vector";
    }

    @Override
    public String getUniqueName() {
        return "buildin.VectorToArray";
    }

    @Override
    public String getIconName() {
        return "Vector-To-Array_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Vector vector = (Vector) io.in0(0, Vector.NULL_VECTOR);
        
        io.out(0, vector.toArray());
        io.out(1, vector.getRowCount());
    }

}
