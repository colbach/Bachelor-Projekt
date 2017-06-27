package reflection.nodedefinitions.arrays;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class ArrayContainsNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Object.class;
            case 1:
                return Object.class;
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
                return "Wert";
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
    public boolean isInletEngaged(int inletIndex) {
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
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Enthält";
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
        return "Enthällt";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Arrays] Arrays Test ? Enthalten";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ArrayContains";
    }

    @Override
    public String getIconName() {
        return "Array-Contains_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] array = io.in(0, new Object[0]);
        Object wert = (Object) io.in0(1);
        if (wert == null) {
            io.out(0, false);
        }

        for (int i = 0; i < array.length; i++) {
            if (i % 100 == 0) {
                io.terminatedTest();
            }
            if (array[i].equals(wert)) {
                io.out(0, true);
                return;
            }
        }
        io.terminatedTest();
        io.out(0, false);
    }

}
