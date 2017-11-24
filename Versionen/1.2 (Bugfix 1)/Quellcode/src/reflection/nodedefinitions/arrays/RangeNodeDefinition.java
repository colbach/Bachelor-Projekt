package reflection.nodedefinitions.arrays;

import java.util.ArrayList;
import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;

public class RangeNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 4;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Integer.class;
            case 1:
                return Integer.class;
            case 2:
                return Integer.class;
            case 3:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Start";
            case 1:
                return "Ende";
            case 2:
                return "Schritgrösse";
            case 3:
                return "Ende einschliessen";
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
            case 2:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
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
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Reihe";
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
        return "Reihe";
    }

    @Override
    public String getDescription() {
        return "Gibt eine Reihe von Zahlen weiter." + TAG_PREAMBLE + " [Arrays] Range";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Range";
    }

    @Override
    public String getIconName() {
        return "Range_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Integer start = (Integer) io.in0(0, 0);
        Integer ende = (Integer) io.in0(1, start + 1);
        Integer schritgrsse = (Integer) io.in0(2, 1);
        Boolean endeEin = (Boolean) io.in0(3, true);

        ArrayList<Integer> reihe = new ArrayList<>();
        for (int i = start; endeEin ? i <= ende : i < ende; i += schritgrsse) {
            reihe.add(i);
        }

        io.out(0, reihe.toArray());
    }

}
