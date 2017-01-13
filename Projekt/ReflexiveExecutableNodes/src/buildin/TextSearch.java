package buildin;

import reflection.NodeDefinition;

public class TextSearch implements NodeDefinition {

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
    public int getOutletCount() {
        return 2;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Integer.class;
            case 1:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Index";
            case 1:
                return "Enthält";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
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
    public String getName() {
        return "Textsuche";
    }

    @Override
    public String getDescription() {
        return "Sucht nach String anhand von Regex." + TAG_PREAMBLE + " finden suchen search find String Text";
    }

    @Override
    public String getUniqueName() {
        return "buildin.TextSearch";
    }

    @Override
    public String getIconName() {
        return "Search-Text.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io) {

        String text = (String) io.in0(0, "");
        String regex = (String) io.in0(1, "");

        Integer index = text.indexOf(regex);
        Boolean enthaelt = index != -1;

        io.out(0, index);
        io.out(1, enthaelt);
    }

}
