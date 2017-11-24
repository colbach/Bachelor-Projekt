package reflection.nodedefinitions.text;

import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;

public class ToStringNodeDefinition implements NodeDefinition {

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
                return "Objekt";
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
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Text";
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
        return "Zu Text";
    }

    @Override
    public String getDescription() {
        return "Wandelt Objekt in Text um." + TAG_PREAMBLE + " [Text] [Umwandeln] toString() zu String Text als";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ToString";
    }

    @Override
    public String getIconName() {
        return "To-String_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] objekts = io.in(0, new Object[0]);

        StringBuilder sb = new StringBuilder();

        for (Object objekt : objekts) {
            if (objekt != null) {
                sb.append(objekt.toString());
            } else {
                sb.append("null");
            }
        }

        io.out(0, sb.toString());
    }

}
