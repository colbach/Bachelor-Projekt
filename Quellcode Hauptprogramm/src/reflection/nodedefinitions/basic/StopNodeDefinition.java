package reflection.nodedefinitions.basic;

import reflection.common.API;
import reflection.common.InOut;
import reflection.additionalnodedefinitioninterfaces.NoTriggerOutlet;
import reflection.common.NodeDefinition;

public class StopNodeDefinition implements NodeDefinition, NoTriggerOutlet {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case (0):
                return String.class;
            case (1):
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case (0):
                return "Meldung";
            case (1):
                return "Fehler";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
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
        return 0;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Stop";
    }

    @Override
    public String getDescription() {
        return "Stoppt Ausführung" + TAG_PREAMBLE + " [Basics] return finish end beenden abbrechen exit";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Stop";
    }

    @Override
    public String getIconName() {
        return "Stop_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        api.printOut("Stoppe Ausführung");
        api.cancelExecution((String) io.in0(0, "Stop"), (boolean) (Boolean) io.in0(1, false));

    }

}
