package reflection.nodedefinitions.specialnodes.firstvalues;

import reflection.nodedefinitions.specialnodes.SpecialNodeDefinition;
import reflection.API;
import reflection.InOut;
import reflection.NoTriggerInlet;
import reflection.NodeDefinition;
import static reflection.NodeDefinition.TAG_PREAMBLE;
import reflection.VariableVisibleInletCount;
import view.assets.ImageAsset;

public abstract class FirstValueNodeDefinition implements SpecialNodeDefinition, NoTriggerInlet, VariableVisibleInletCount {
    
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
        return String.valueOf((char)('A' + inletIndex));
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
    public boolean isInletEngaged(int inletIndex) {
        return false;
    }

    @Override
    public Class getClassForOutlet(int outletIndex) {
        return Object.class;
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        return "Wert";
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return true;
    }

    @Override
    public String getIconName() {
        return ImageAsset.CLEAR;
    }

    @Override
    public int getVersion() {
        return 0;
    }
    
    @Override
    public void run(InOut io, API api) {
        throw new RuntimeException("run darf nicht auf Objekte der Klasse FirstValueNodeDefinition aufgerufen werden");
    }
    
}
