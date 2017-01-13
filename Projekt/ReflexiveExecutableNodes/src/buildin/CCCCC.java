package buildin;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import reflection.NodeDefinition;

public class CCCCC implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 10;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        if(inletIndex == 3)
            return Date.class;
        if(inletIndex == 4)
            return Color.class;
        if(inletIndex == 5)
            return ArrayList.class;
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
        return "Testbaustein C";
    }

    @Override
    public String getDescription() {
        return "Hier soll irgentwas stehen";
    }

    @Override
    public String getUniqueName() {
        return "BuildIn.TestC";
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
