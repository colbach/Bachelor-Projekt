package reflection.nodedefinitions.functions;

import reflection.*;
import reflection.nodedefinitionsupport.functions.CustomFunction;
import reflection.nodedefinitionsupport.functions.CustomFunctionManager;
import reflection.nodedefinitionsupport.functions.FunctionCallToken;
import utils.structures.Tuple;

public class FunctionStartNodeDefinition implements NodeDefinition, SupportingContextCreator {

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
    public boolean isInletEngaged(int i) {
        return true;
    }

    @Override
    public int getOutletCount() {
        return 2;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Object.class;
            case 1:
                return FunctionCallToken.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Parameter";
            case 1:
                return "Token";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            case 1:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Funktions-Start";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Funktionen] [Events] funktions aufruf neu definieren id identifier call start";
    }

    @Override
    public String getUniqueName() {
        return "buildin.FunctionStart";
    }

    @Override
    public String getIconName() {
        return "Function-Start_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut uio, API api) throws Exception {
        ContextCreatorInOut io = (ContextCreatorInOut) uio;

        SmartIdentifier id = (SmartIdentifier) io.in0(0, null);
        if (id == null) {
            throw new Exception("ID muss gesetzt sein!");
        }

        CustomFunctionManager customFunctionManager = (CustomFunctionManager) api.putSharedObjectIfNotAlreadyExists(CustomFunctionManager.SHARED_OBJECT_KEY, new CustomFunctionManager());
        CustomFunction function = customFunctionManager.createOrGetCustomFunction(id, io);

        while (true) {
            Tuple<FunctionCallToken, Object[]> token = function.start();
            io.out(0, token.r);
            io.out(1, token.l);
            io.startNewContext();
        }
    }

}
