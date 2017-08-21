package reflection.nodedefinitions.text;

import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;

public class TextSearchNodeDefinition implements NodeDefinition {

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
        return true;
    }

    @Override
    public int getOutletCount() {
        return 4;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Integer.class;
            default:
                return Boolean.class;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Index";
            case 1:
                return "Enthält";
            case 2:
                return "Beginnt mit";
            case 3:
                return "Endet mit";
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
        return "Textsuche";
    }

    @Override
    public String getDescription() {
        return "Sucht nach String anhand von Regex." + TAG_PREAMBLE + " [Text] finden suchen search find String Text endet mit enthällt beginnt starts with ends";
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
    public void run(InOut io, API api) {

        String text = (String) io.in0(0, "");
        String regex = (String) io.in0(1, "");

        Integer index = text.indexOf(regex);
        Boolean enthaelt = index != -1;

        io.out(0, index);
        io.out(1, enthaelt);
        io.out(2, index == 0);
        io.out(3, text.endsWith(regex));
    }

}
