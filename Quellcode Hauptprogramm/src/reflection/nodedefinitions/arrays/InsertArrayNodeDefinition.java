package reflection.nodedefinitions.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;

public class InsertArrayNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 5;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Object.class;
            case 1:
                return Object.class;
            case 2:
                return Object.class;
            case 3:
                return Object.class;
            case 4:
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
                return "Davor";
            case 2:
                return "Danach";
            case 3:
                return "an Position i";
            case 4:
                return "i";
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
            case 2:
                return true;
            case 3:
                return true;
            case 4:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int inletIndex) {
        if (inletIndex == 0) {
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
        return "In Array einfügen";
    }

    @Override
    public String getDescription() {
        return "Fügt Elemente zu Array hinzu und gibt Array weiter." + TAG_PREAMBLE + " [Arrays] Array einfügen anfügen i Position Listen Feld";
    }

    @Override
    public String getUniqueName() {
        return "buildin.InsertArray";
    }

    @Override
    public String getIconName() {
        return "Insert-Array_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] array = io.in(0, new Object[0]);
        Object[] davor = io.in(1, new Object[0]);
        Object[] dahinter = io.in(2, new Object[0]);
        Object[] anpositioni = io.in(3, new Object[0]);
        Integer position = (Integer) io.in0(4, 0);

        List<Object> resultat = Arrays.asList(array);
        
        if(davor.length > 0) {
            resultat.addAll(0, Arrays.asList(davor));
        }
        if(anpositioni.length > 0) {
            resultat.addAll(position, Arrays.asList(anpositioni));
        }
        if(dahinter.length > 0) {
            resultat.addAll(Arrays.asList(dahinter));
        }

        io.out(0, resultat.toArray());
    }

}
