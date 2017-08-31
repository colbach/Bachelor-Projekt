package reflection.nodedefinitions.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class RemoveElementsOfArrayNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Object.class;
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
                return "Array";
            case 1:
                return "Indexs";
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
                return true;
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
                return Object.class;
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
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Elemente entfernen";
    }

    @Override
    public String getDescription() {
        return "Entfernt ein oder mehrere Objeckte aus Array." + TAG_PREAMBLE + " [Arrays] ";
    }

    @Override
    public String getUniqueName() {
        return "buildin.RemoveElementsOfArray";
    }

    @Override
    public String getIconName() {
        return "Remove-From-Array_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] array = io.in(0, new Object[0]);
        Object[] indexs = io.in(1, new Boolean[0]);

        ArrayList resultat = new ArrayList(Arrays.asList(array));
        for (int i = 0; i < indexs.length; i++) {
            if (i % 100 == 0) {
                io.terminatedTest();
            }
            resultat.remove((int) ((Integer) indexs[i]) - i);
        }
        io.terminatedTest();
        io.out(0, resultat.toArray());
    }

}
