package reflection.nodedefinitions.text;

import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;

public class SubTextNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 4;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return String.class;
            case 1:
                return Integer.class;
            case 2:
                return Integer.class;
            case 3:
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
                return "Start";
            case 2:
                return "Länge";
            case 3:
                return "Ende";
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
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Teiltext";
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
        return "Teil von Text";
    }

    @Override
    public String getDescription() {
        return "Gibt Teil von Text weiter" + TAG_PREAMBLE + " [Text] substring";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Substring";
    }

    @Override
    public String getIconName() {
        return "Substring_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        String text = io.in0(0, "").toString();
        Integer s = io.inN(1, -1).intValue();
        Integer l = io.inN(2, -1).intValue();
        Integer e = io.inN(3, -1).intValue();
        
        if(s < 0) {
            if(e >= 0 && l >= 0) {
                s = e - l;
            } else {
                s = 0;
            }
        }
        
        if(e < 0) {
            if(s >= 0 && l >= 0) {
                e = s + l;
            } else {
                e = text.length();
            }
        }
        
        text = text.substring(s, e);

        io.out(0, text);
    }

}
