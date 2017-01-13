package buildin;

import reflection.NodeDefinition;

public class TextLength implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
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
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            default:
                return false;
        }
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Länge";
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
        return "Text-Länge";
    }

    @Override
    public String getDescription() {
        return "Gibt Textlänge weiter." + TAG_PREAMBLE + " Länge String length lenght Anzahl Text";
    }

    @Override
    public String getUniqueName() {
        return "buildin.TextLength";
    }

    @Override
    public String getIconName() {
        return "lAal_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io) {

        String text = (String) io.in0(0, "");

        Integer laenge = text.length();

        io.out(0, laenge);
    }

}
