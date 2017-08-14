package reflection.nodedefinitions.logic;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class NOTNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
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
                return "!A";
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
        return "NOT";
    }

    @Override
    public String getDescription() {
        return "Logisches NOT." + TAG_PREAMBLE + " [Logik] nicht";
    }

    @Override
    public String getUniqueName() {
        return "buildin.NOT";
    }

    @Override
    public String getIconName() {
        return "NOT_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] a = io.in(0, new Boolean[0]);

        for (int i = 0; i < a.length; i++) {
            if (i % 1000 == 0) {
                io.terminatedTest();
            }
            a[i] = !(boolean) (Boolean) a[i];
        }

        io.out(0, a);
    }

}
