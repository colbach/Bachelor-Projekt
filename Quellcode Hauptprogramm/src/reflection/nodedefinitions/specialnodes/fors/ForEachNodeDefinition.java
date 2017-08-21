package reflection.nodedefinitions.specialnodes.fors;

import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;
import view.assets.ImageAsset;

public class ForEachNodeDefinition implements ForNodeDefinition {

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
        return "Elemente";
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
        return true;
    }

    @Override
    public Class getClassForOutlet(int outletIndex) {
        return Object.class;
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        return "Element";
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return false;
    }

    @Override
    public String getName() {
        return "Für-Alle";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Basics] foreach for-each für jeden für-jeden für-alle alle";
    }

    @Override
    public String getUniqueName() {
        return "special.ForEach";
    }

    @Override
    public String getIconName() {
        return "ui/For-Each_30px.png";
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
