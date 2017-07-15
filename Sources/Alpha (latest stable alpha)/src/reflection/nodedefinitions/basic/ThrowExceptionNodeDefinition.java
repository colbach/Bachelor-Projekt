package reflection.nodedefinitions.basic;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class ThrowExceptionNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Boolean.class;
            case 1:
                return String.class;
            case 2:
                return Object.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Auslösen";
            case 1:
                return "Nachicht";
            case 2:
                return "Data";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            case 1:
                return false;
            default:
                return true;
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
                return "Data";
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
        return "Ausnahme";
    }

    @Override
    public String getDescription() {
        return "Löst eine Ausnahme aus falls Auslösen true ist. Falls Auslösen false ist wird der Eingang Data an den Ausgang Data übergeben." + TAG_PREAMBLE + " [Basics] Exception Fehler Ausnahme Debugging Assert";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ThrowException";
    }

    @Override
    public String getIconName() {
        return "Exception_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Boolean auslsen = (Boolean) io.in0(0, true);
        String nachicht = (String) io.in0(1, "Ausnahme");
        Object[] data = io.in(2, new Object[0]);

        if (auslsen) {
            throw new RuntimeException(nachicht);
        }

        io.out(0, data);
    }

}
