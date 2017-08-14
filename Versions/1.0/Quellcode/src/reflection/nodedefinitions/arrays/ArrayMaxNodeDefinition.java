package reflection.nodedefinitions.arrays;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class ArrayMaxNodeDefinition implements NodeDefinition {

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
                return "Array";
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
                return Number.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Maximum";
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
        return "Maximum";
    }

    @Override
    public String getDescription() {
        return "Gibt Maximum aus Array weiter." + TAG_PREAMBLE + " [Arrays] max";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ArrayMax";
    }

    @Override
    public String getIconName() {
        return "Array-Max_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] array = io.in(0, new Number[0]);

        Number maximum = Double.MIN_VALUE;
        for (Object o : array) {
            if (((Number) o).doubleValue() > maximum.doubleValue()) {
                maximum = (Number) o;
            }
        }

        io.out(0, maximum);
    }

}
