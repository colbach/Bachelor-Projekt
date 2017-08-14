package reflection.nodedefinitions.basic;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class BridgeNodeDefinition implements NodeDefinition {

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
                return "Eingang";
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
                return "Ausgang";
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
        return "Brücke";
    }

    @Override
    public String getDescription() {
        return "Leeres Element. Gibt Eingang weiter ohne diesen zu verarbeiten." + TAG_PREAMBLE + " [Basics]";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Bridge";
    }

    @Override
    public String getIconName() {
        return "Right_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        io.out(0, io.in(0, new Object[0]));
    }

}
