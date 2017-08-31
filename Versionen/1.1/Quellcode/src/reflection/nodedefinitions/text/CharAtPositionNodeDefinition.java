package reflection.nodedefinitions.text;

import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;

public class CharAtPositionNodeDefinition implements NodeDefinition {

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
                return Integer.class;
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
                return "Positon";
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
                return Character.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Zeichen";
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
        return "Zeichen an Position";
    }

    @Override
    public String getDescription() {
        return "Gibt Zeichen an Position weiter" + TAG_PREAMBLE + " [Text] charat";
    }

    @Override
    public String getUniqueName() {
        return "buildin.CharAt";
    }

    @Override
    public String getIconName() {
        return "Char-At_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        String s = io.in0(0, "").toString();
        int i = io.inN(1, 0).intValue();
        
        io.out(0, (Character) s.charAt(i));
    }

}
