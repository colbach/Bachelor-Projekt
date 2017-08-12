package reflection.nodedefinitions.basic;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class NullTestNodeDefinition implements NodeDefinition {

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
    public boolean isInletEngaged(int index) {

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
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Ist NULL";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "NULL-Test";
    }

    @Override
    public String getDescription() {
        return "Gibt an Ausgang weiter ob Wert NULL ist. Data ist NULL wenn seine Länge 0 ist oder alle Werte null (leer) sind." + TAG_PREAMBLE + " [Basics] === equals is null";
    }

    @Override
    public String getUniqueName() {
        return "buildin.NullTest";
    }

    @Override
    public String getIconName() {
        return "Is-Null_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] data = io.in(0, null);

        Boolean istnull;
        if(data == null) {
            istnull = true;
        } else if(data.length == 0) {
            istnull = true;
        } else {
            istnull = true;
            for(Object d : data) {
                if(d != null) {
                    istnull = false;
                    break;
                }
            }
        }

        io.out(0, istnull);
    }

}
