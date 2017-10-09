package reflection.nodedefinitions.show;

import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;
import reflection.customdatatypes.SmartIdentifier;

public class DisposeWindowNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return SmartIdentifier.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Id";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return true;
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
        return 0;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Fenster verbergen";
    }

    @Override
    public String getDescription() {
        return "Verbirgt offene Fenster." + TAG_PREAMBLE + " [UI] schliessen close Fenster Window dispose schliesen verbergen x";
    }

    @Override
    public String getUniqueName() {
        return "buildin.show.DisposeWindow";
    }

    @Override
    public String getIconName() {
        return "Close-Show-Window_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] ids = io.in(0, new Object[0]);

        for (Object id : ids) {
            api.disposeWindow((SmartIdentifier) id);
        }

    }

}
