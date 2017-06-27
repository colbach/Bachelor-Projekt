package reflection.nodedefinitions.arrays;

import java.util.ArrayList;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class SubArrayNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Object.class;
            case 1:
                return Integer.class;
            case 2:
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Array";
            case 1:
                return "Start";
            case 2:
                return "Länge";
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
            case 2:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
        if (i == 0) {
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
                return Object.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Subarray";
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
        return "Sub-Array";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Arrays] ";
    }

    @Override
    public String getUniqueName() {
        return "buildin.SubArray";
    }

    @Override
    public String getIconName() {
        return "Sub-Array_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] array = io.in(0, new Object[0]);
        Integer start = (Integer) io.in0(1, 0);
        Integer lnge = (Integer) io.in0(2, 1);

        ArrayList<Object> subarray = new ArrayList<>();
        for (int i = 0; i < lnge; i++) {
            subarray.add(array[start + i]);
        }

        io.out(0, subarray.toArray());
    }

}
