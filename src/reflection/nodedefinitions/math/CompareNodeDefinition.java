package reflection.nodedefinitions.math;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class CompareNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        return Number.class;
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        if (inletIndex == 0) {
            return "A";
        } else if (inletIndex == 1) {
            return "B";
        }
        return null;
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        return false;
    }

    @Override
    public boolean isInletEngaged(int i) {
        return true;
    }

    @Override
    public int getOutletCount() {
        return 5;
    }

    @Override
    public Class getClassForOutlet(int outletIndex) {
        return Boolean.class;
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        switch (outletIndex) {
            case 0:
                return "A < B";
            case 1:
                return "A > B";
            case 2:
                return "A =< B";
            case 3:
                return "A => B";
            case 4:
                return "A == B";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return false;
    }

    @Override
    public String getName() {
        return "Vergleichen";
    }

    @Override
    public String getDescription() {
        return "Vergleichen von vergleichbaren Werten." + TAG_PREAMBLE + " [Math] Vergleich compareto == >= <= < > grösser kleiner gleich";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Compare";
    }

    @Override
    public String getIconName() {
        return "Compare_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {
        Number a = (Number) io.in0(0, new Double(0));
        Number b = (Number) io.in0(1, new Double(0));
        if (a instanceof Double || b instanceof Double || a instanceof Float || b instanceof Float) {
            io.out(0, b.doubleValue() > a.doubleValue());
            io.out(1, b.doubleValue() < a.doubleValue());
            io.out(2, b.doubleValue() >= a.doubleValue());
            io.out(3, b.doubleValue() <= a.doubleValue());
            io.out(4, b.doubleValue() == a.doubleValue());
        } else {
            io.out(0, b.longValue() > a.longValue());
            io.out(1, b.longValue() < a.longValue());
            io.out(2, b.longValue() >= a.longValue());
            io.out(3, b.longValue() <= a.longValue());
            io.out(4, b.longValue() == a.longValue());
        }
    }

}
