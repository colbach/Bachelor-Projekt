package reflection.nodedefinitions.specialnodes.ifs;

import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;
import view.assets.ImageAsset;

public class IfForwardNodeDefinition implements IfNodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        switch (inletIndex) {
            case 0:
                return Object.class;
            case 1:
                return Boolean.class;
            default:
                throw new IllegalArgumentException("inletIndex muss kleiner 2 sein.");
        }
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        switch (inletIndex) {
            case 0:
                return "Objekt";
            case 1:
                return "Wahrheitswert";
            default:
                throw new IllegalArgumentException("inletIndex muss kleiner 2 sein.");
        }
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        switch (inletIndex) {
            case 0:
                return true;
            case 1:
                return false;
            default:
                throw new IllegalArgumentException("inletIndex muss kleiner 2 sein.");
        }
    }

    @Override
    public int getOutletCount() {
        return 2;
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
        if(outletIndex == 0) {
            return "Falls Wahr";
        } else if(outletIndex == 1) {
            return "Falls Unwahr";
        } else {
            throw new IllegalArgumentException("inletIndex muss kleiner 2 sein.");
        }
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return true;
    }

    @Override
    public String getName() {
        return "Wenn-Vor";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Basics]";
    }

    @Override
    public String getUniqueName() {
        return "special.IfForward";
    }

    @Override
    public String getIconName() {
        return "ui/If-Forward_30px.png";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void run(InOut io, API api) {
        if((Boolean) io.in0(1, false)) {
            io.out(0, io.in(0, null));
        } else {
            io.out(1, io.in(0, null));
        }
    }
    
}
