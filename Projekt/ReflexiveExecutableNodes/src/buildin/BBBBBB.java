package buildin;

import reflection.NodeDefinition;

public class BBBBBB implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 10;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        return String.class;
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        return "In " + inletIndex;
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        return false;
    }

    @Override
    public int getOutletCount() {
        return 8;
    }

    @Override
    public Class getClassForOutlet(int outletIndex) {
        return String.class;
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        return "Out " + outletIndex;
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return false;
    }

    @Override
    public String getName() {
        return "Testbaustein B";
    }

    @Override
    public String getDescription() {
        return "Hier soll irgentwas stehen";
    }

    @Override
    public String getUniqueName() {
        return "BuildIn.TestB";
    }

    @Override
    public String getIconName() {
        return null;
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io) {
        Comparable a = (Comparable) io.in0(0);
        Comparable b = (Comparable) io.in0(1);
        int result = a.compareTo(b);
        io.out(0, result > 0);
        io.out(1, result < 0);
        io.out(2, result >= 0);
        io.out(3, result <= 0);
        io.out(4, result == 0);
    }
    
}
