package reflection.nodedefinitions.math;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class RandomNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Number.class;
            case 1:
                return Number.class;
            case 2:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Min";
            case 1:
                return "Max";
            case 2:
                return "Nur Ganzzahlen";
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
                return Number.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Zufallswert";
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
        return "Zufallsgenerator";
    }

    @Override
    public String getDescription() {
        return "Generiert zufällige Werte." + TAG_PREAMBLE + " [Math] random würfeln";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Random";
    }

    @Override
    public String getIconName() {
        return "Dice-2_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Number min = (Number) io.in0(0, 0);
        Number max = (Number) io.in0(1, 1);

        Boolean nurGanzzahlen = (Boolean) io.in0(2, false);
        if (min.doubleValue() > max.doubleValue()) {
            Number tmp = min;
            min = max;
            max = min;
        }

        Number zufallswert = null;
        if (nurGanzzahlen != null && nurGanzzahlen) {
            Integer n = max.intValue() - min.intValue();
            zufallswert = (Integer) (int) (min.intValue() + (Math.random() * (n + 1)));
        } else {
            Double n = max.doubleValue() - min.doubleValue();
            zufallswert = (Double) (min.doubleValue() + (Math.random() * n));
        }

        io.out(0, zufallswert);
    }

}
