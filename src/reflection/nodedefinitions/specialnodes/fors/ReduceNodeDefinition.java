package reflection.nodedefinitions.specialnodes.fors;

import reflection.API;
import reflection.InOut;
import reflection.NoTriggerInlet;
import reflection.NodeDefinition;
import view.assets.ImageAsset;

public class ReduceNodeDefinition implements ForNodeDefinition, NoTriggerInlet {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        return Object.class;
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        return "Element";
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        return true;
    }

    @Override
    public boolean isInletEngaged(int inletIndex) {
        return true;
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int outletIndex) {
        return Object.class;
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        return "Elemente";
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return true;
    }

    @Override
    public String getName() {
        return "Zusammenführen";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Basics] reduce for sammeln combine kombinieren";
    }

    @Override
    public String getUniqueName() {
        return "special.Reduce";
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
        throw new RuntimeException("run darf nicht auf Objekte der Klasse ForEachNodeDefinition aufgerufen werden");
    }
    
}
