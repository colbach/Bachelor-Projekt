package buildin;

import reflection.NodeDefinition;

public class CombineText implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        return String.class;
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        return "Text";
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        return true;
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int outletIndex) {
        return String.class;
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        return "Text";
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return false;
    }

    @Override
    public String getName() {
        return "Text anhängen";
    }

    @Override
    public String getDescription() {
        return "diese Klasse dient dazu mehrere Strings hintereinander zu hängen";
    }

    @Override
    public String getUniqueName() {
        return "buildin.CombineText";
    }

    @Override
    public String getIconName() {
        return "Aa_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io) {
        String[] strings = (String[]) io.in(0, new String[0]);
        StringBuilder sb = new StringBuilder();
        for(String string : strings) {
            sb.append(string);
        }
        io.out(0, sb.toString());
    }
    
}
