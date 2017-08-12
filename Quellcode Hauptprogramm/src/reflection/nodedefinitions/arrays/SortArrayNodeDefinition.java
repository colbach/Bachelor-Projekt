package reflection.nodedefinitions.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class SortArrayNodeDefinition implements NodeDefinition {

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
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Array";
            case 1:
                return "Aufsteigend";
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
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
        if (i == 0) {
            return true;
        }
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
                return "Sortiert";
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
        return "Sortieren";
    }

    @Override
    public String getDescription() {
        return "Sortiert Werte-Array aufsteigend oder absteigend und gibt Array weiter." + TAG_PREAMBLE + " [Arrays] ";
    }

    @Override
    public String getUniqueName() {
        return "buildin.SortArray";
    }

    @Override
    public String getIconName() {
        return "Sort-Array_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] array = io.in(0, new Number[0]);
        Boolean aufsteigend = (Boolean) io.in0(1, true);

        ArrayList<Number> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            list.add((Number) array[i]);
        }
        Collections.sort(list, new Comparator<Number>() {
            @Override
            public int compare(Number o1, Number o2) {
                if (o1.doubleValue() > o2.doubleValue()) {
                    if (aufsteigend) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else if (o1.doubleValue() < o2.doubleValue()) {
                    if (aufsteigend) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    return 0;
                }
            }
        });

        io.out(0, list.toArray());
    }

}
