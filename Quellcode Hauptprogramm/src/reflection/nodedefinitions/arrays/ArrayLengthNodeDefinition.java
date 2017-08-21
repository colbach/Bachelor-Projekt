package reflection.nodedefinitions.arrays;

import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;

public class ArrayLengthNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
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
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Länge";
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
        return "Array-Länge";
    }

    @Override
    public String getDescription() {
        return "Gibt Länge von Array weiter." + TAG_PREAMBLE + " [Arrays] Länge length lenth lentgh";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ArrayLength";
    }

    @Override
    public String getIconName() {
        return "Array-Length_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] array = io.in(0, new Object[0]);

        io.out(0, array.length);
    }

}
