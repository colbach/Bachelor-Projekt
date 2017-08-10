package reflection.nodedefinitions.math;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class RoundNumberNodeDefinition implements NodeDefinition {

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
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Wert";
            case 1:
                return "Kommastellen";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
        return false;
    }

    @Override
    public boolean isInletForArray(int index) {
        return false;
    }

    @Override
    public int getOutletCount() {
        return 3;
    }

    @Override
    public Class getClassForOutlet(int index) {
        return Double.class;
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Gerundet";
            case 1:
                return "Aufgerundet";
            case 2:
                return "Abgerundet";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        return false;
    }

    @Override
    public String getName() {
        return "Runden";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Math]";
    }

    @Override
    public String getUniqueName() {
        return "buildin.round";
    }

    @Override
    public String getIconName() {
        return "Round-Number_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Double n = ((Number) io.in0(0, new Double(0))).doubleValue();
        Integer k = ((Number) io.in0(1, new Integer(0))).intValue();

        double factor = Math.pow(10, k);

        double round = Math.round(n * factor) / factor;
        double roundDown = ((int) (n * factor)) / factor;
        double roundUp;
        if (round == n) {
            roundUp = n;
        } else {
            roundUp = roundDown + 1;
        }

        io.out(0, round);
        io.out(1, roundUp);
        io.out(2, roundDown);
    }

}
