package buildin;

import reflection.NodeDefinition;

public class PrintLog implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        return Object.class;
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        return "Data";
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        return true;
    }

    @Override
    public int getOutletCount() {
        return 0;
    }

    @Override
    public Class getClassForOutlet(int outletIndex) {
        return null;
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        return null;
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return false;
    }

    @Override
    public String getName() {
        return "Print to Log";
    }

    @Override
    public String getDescription() {
        return "Loggt Objekt (bzw. Objekte) zeilenweise in die Konsole.\nAusgaben können über den Toolbaricon Log eingesehen werden.";
    }

    @Override
    public String getUniqueName() {
        return "buildin.PrintLog";
    }

    @Override
    public String getIconName() {
        return "Monitor-3_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io) {
        for (Object o : io.in(0, new Object[0])) {
            if (o == null) {
                System.out.println("null");
            } else {
                System.out.println(o.toString());
            }
        }
    }

}
