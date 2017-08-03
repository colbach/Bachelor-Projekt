package reflection.nodedefinitions.text;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;
import reflection.VariableVisibleInletCount;

public class AppendTextNodeDefinition implements NodeDefinition, VariableVisibleInletCount {

    @Override
    public int getInletCount() {
        return 'Z' - 'A' + 1;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        return Object.class;
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        return String.valueOf((char) ('A' + inletIndex));
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        return false;
    }

    @Override
    public boolean isInletEngaged(int i) {
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
        return "Dieses Element dient dazu Texte aneinander zu hängen. " + TAG_PREAMBLE + " [Text] apend append zusammenhängen add append aneinander";
    }

    @Override
    public String getUniqueName() {
        return "buildin.AppendText";
    }

    @Override
    public String getIconName() {
        return "Append-Text_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getInletCount(); i++) {
            sb.append(io.in0(i, ""));
        }
        io.out(0, sb.toString());
    }

}
