package reflection.nodedefinitions.basic;

import reflection.*;
import reflection.customdatatypes.SmartIdentifier;

public class NextNumberNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Integer.class;
            case 1:
                return SmartIdentifier.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Start i";
            case 1:
                return "ID";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            case 1:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int index) {
        return false;
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
                return "i";
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
        return "Counter hochzählen";
    }

    @Override
    public String getDescription() {
        return "Gibt Counter weiter und zählt diesen um 1 hoch." + TAG_PREAMBLE + " i counter up countup";
    }

    @Override
    public String getUniqueName() {
        return "buildin.NextNumber";
    }

    @Override
    public String getIconName() {
        return "Next-Number-Function_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        Number starti = io.inN(0, 0);
        SmartIdentifier id = (SmartIdentifier) io.in0(1, null);
        
        String key;
        if (id != null) {
            key = "buildin.NextNumber:" + id.getIdentifierName();
        } else {
            key = "buildin.NextNumber";
        }
        api.putSharedObjectIfNotAlreadyExists(key, starti.intValue());
        Integer i = ((Number)api.getSharedObject(key)).intValue();
        i += 1;
        api.putSharedObject(key, i);
        
        io.out(0, i);
    }

}
