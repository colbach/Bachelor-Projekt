package reflection.nodedefinitions.logic;

import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;

public class ANDNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Boolean.class;
            case 1:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "A";
            case 1:
                return "B";
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
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
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
                return "A & B";
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
        return "AND";
    }

    @Override
    public String getDescription() {
        return "Logisches AND. A und B muss die gleiche Länge haben." + TAG_PREAMBLE + " [Logik] und";
    }

    @Override
    public String getUniqueName() {
        return "buildin.AND";
    }

    @Override
    public String getIconName() {
        return "AND_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] a = io.in(0, new Boolean[0]);
        Object[] b = io.in(1, new Boolean[0]);

        if (a.length != b.length) {
            throw new IllegalArgumentException("A und B muss gleiche Länge haben.");
        }

        for (int i = 0; i < a.length; i++) {
            if (i % 1000 == 0) {
                io.terminatedTest();
            }
            a[i] = ((boolean) (Boolean) a[i]) & ((boolean) (Boolean) b[i]);
        }

        io.out(0, a);
    }

}
