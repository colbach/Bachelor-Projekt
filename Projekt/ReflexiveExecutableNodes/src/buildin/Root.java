package buildin;

import reflection.NodeDefinition;

public class Root implements NodeDefinition {

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
                return "x";
            case 1:
                return "n";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            case 1:
                return false;
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
                return "Resultat";
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
        return "Wurzel";
    }

    @Override
    public String getDescription() {
        return "Berechnet die n-te Wurzel von x. " + TAG_PREAMBLE + " sqrt n^1/n Quadratwurzel";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Root";
    }

    @Override
    public String getIconName() {
        return "Sqrt_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io) {

        Number x = (Number) io.in0(0, 1);
        Number n = (Number) io.in0(1, 2);

        Double resultat = Math.pow(x.doubleValue(), 1 / n.doubleValue());

        io.out(0, resultat);
    }

}
