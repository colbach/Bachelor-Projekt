package reflection.nodedefinitions.text;

import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;

public class CombineTextNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        return String.class;
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        if (inletIndex == 0) {
            return "Text";
        } else {
            return "Separator";
        }
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        if (inletIndex == 0) {
            return true;
        } else {
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
        return "Texte kombinieren";
    }

    @Override
    public String getDescription() {
        return "Dient dazu mehrere Texte zu kombinieren." + TAG_PREAMBLE + " [Text] [Arrays] combine kombinieren add append";
    }

    @Override
    public String getUniqueName() {
        return "buildin.CombineText";
    }

    @Override
    public String getIconName() {
        return "Combine-Text_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {
        Object[] strings = io.in(0, new String[0]);
        String separator = (String) io.in0(1, "");
        StringBuilder sb = new StringBuilder();

        io.terminatedTest();
        boolean first = true;
        for (Object string : strings) {
            if (!first) {
                sb.append(separator);
            }
            sb.append(string.toString());
            first = false;
        }
        io.terminatedTest();
        io.out(0, sb.toString());
    }

}
