package reflection.nodedefinitions.text;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class SplitTextNodeDefinition implements NodeDefinition {

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
                return "Regex";
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
                return false;
        }
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
        return 2;
    }

    @Override
    public Class getClassForOutlet(int index) {
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
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Getrennter Text";
            case 1:
                return "Count";
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
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Text trennen";
    }

    @Override
    public String getDescription() {
        return "Trennt Text anhand von Regex in Array." + TAG_PREAMBLE + " [Text] [Arrays]";
    }

    @Override
    public String getUniqueName() {
        return "buildin.SplitText";
    }

    @Override
    public String getIconName() {
        return "Split-Text.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        String text = (String) io.in0(0, "");
        String regex = (String) io.in0(1, "");

        String[] splitted = text.split(regex);

        io.out(0, splitted);
        io.out(1, splitted.length);
    }

}
