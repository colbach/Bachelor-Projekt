package reflection.nodedefinitions.basic;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class CopyNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Object.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Data";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
        return true;
    }

    @Override
    public int getOutletCount() {
        return 2;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Object.class;
            case 1:
                return Object.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Data";
            case 1:
                return "Kopie";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
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
    public String getName() {
        return "Kopie";
    }

    @Override
    public String getDescription() {
        return "Koppiert ein beliebige Objekt. Hierfür wird clone() auf das Object aufgerufen. " + TAG_PREAMBLE + " [Basics] Kopieren Copy-Paste clone cp Dublizieren vervielfältigen";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Copy";
    }

    @Override
    public String getIconName() {
        return "Duplicate_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] data = io.in(0, new Object[0]);
        Object[] copy = new Object[data.length];
        for (int i = 0; i < data.length; i++) {
            copy[i] = data.clone();
        }

        io.out(0, data);
        io.out(1, copy);
    }

}
