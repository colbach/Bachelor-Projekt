package buildin;

import reflection.NodeDefinition;

public class Pi implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 0;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            default:
                return false;
        }
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Double.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Pi";
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
        return "Pi";
    }

    @Override
    public String getDescription() {
        return "Gibt die Konstante Pi weiter" + TAG_PREAMBLE + " 3.14159265359 Konstante Constant";
    }

    @Override
    public String getUniqueName() {
        return "buildin.pi";
    }

    @Override
    public String getIconName() {
        return "Pi_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io) {

        Double pi = Math.PI;

        io.out(0, pi);
    }

}
