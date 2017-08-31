package reflection.nodedefinitions.text;

import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;

public class StartsWithNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Text";
            case 1:
                return "Prefix";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        return false;
    }

    @Override
    public boolean isInletEngaged(int i) {
        return true;
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
                return "Ist Prefix";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        return false;
    }

    @Override
    public String getName() {
        return "Beginnt mit";
    }

    @Override
    public String getDescription() {
        return "Gibt weiter ob Text mit Prefix beginnt." + TAG_PREAMBLE + " [Text] startswith";
    }

    @Override
    public String getUniqueName() {
        return "buildin.StartsWith";
    }

    @Override
    public String getIconName() {
        return "Starts-With_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        String text = io.in0(0, "").toString();
        String prefix = io.in0(1, "").toString();

        io.out(0, text.startsWith(prefix));
    }

}
