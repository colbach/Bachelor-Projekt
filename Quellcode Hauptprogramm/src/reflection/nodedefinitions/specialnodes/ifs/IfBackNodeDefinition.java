package reflection.nodedefinitions.specialnodes.ifs;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;
import view.assets.ImageAsset;

public class IfBackNodeDefinition implements IfNodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        switch (inletIndex) {
            case 0:
                return Object.class;
            case 1:
                return Object.class;
            case 2:
                return Boolean.class;
            default:
                throw new IllegalArgumentException("inletIndex muss kleiner 3 sein.");
        }
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        switch (inletIndex) {
            case 0:
                return "Falls Wahr";
            case 1:
                return "Falls Unwahr";
            case 2:
                return "Wahrheitswert";
            default:
                throw new IllegalArgumentException("inletIndex muss kleiner 3 sein.");
        }
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        switch (inletIndex) {
            case 0:
                return true;
            case 1:
                return true;
            case 2:
                return false;
            default:
                throw new IllegalArgumentException("inletIndex muss kleiner 3 sein.");
        }
    }

    @Override
    public boolean isInletEngaged(int inletIndex) {
        if (inletIndex == 2) {
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
        return Object.class;
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        return "Objekt";
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return true;
    }

    @Override
    public String getName() {
        return "Wenn-Zurück";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Basics]";
    }

    @Override
    public String getUniqueName() {
        return "special.IfBack";
    }

    @Override
    public String getIconName() {
        return "ui/If-Back_30px.png";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void run(InOut io, API api) {
        throw new RuntimeException("run darf nicht auf Objekte der Klasse IfBackNodeDefinition aufgerufen werden");
    }

}
