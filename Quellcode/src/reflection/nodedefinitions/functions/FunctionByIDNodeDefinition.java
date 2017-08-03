package reflection.nodedefinitions.functions;

import reflection.customdatatypes.SmartIdentifier;
import reflection.*;
import reflection.nodedefinitionsupport.functions.CustomFunctionManager;

public class FunctionByIDNodeDefinition implements NodeDefinition {

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
            default:
                return false;
        }
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public boolean isInletEngaged(int i) {
        return true;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Function.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Funktion";
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
        return "Funktion durch ID";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Funktionen] [Events] funktions aufruf definieren";
    }

    @Override
    public String getUniqueName() {
        return "buildin.FunctionByID";
    }

    @Override
    public String getIconName() {
        return "Function-By-ID_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        SmartIdentifier id = (SmartIdentifier) io.in0(0);
        if (id == null) {
            throw new Exception("ID ist nicht gesetzt!");
        }

        CustomFunctionManager customFunctionManager = (CustomFunctionManager) api.getSharedObjectBlocking(CustomFunctionManager.SHARED_OBJECT_KEY);
        Function funktion = customFunctionManager.getCustomFunctionBlocking(id, io);
        if (funktion == null) {
            throw new Exception("Funktion existiert nicht!");
        }

        io.out(0, funktion);
    }

}
