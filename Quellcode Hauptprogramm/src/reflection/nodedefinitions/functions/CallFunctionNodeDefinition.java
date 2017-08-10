package reflection.nodedefinitions.functions;

import reflection.customdatatypes.Function;
import reflection.*;
import reflection.nodedefinitionsupport.functions.CustomFunction;

public class CallFunctionNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Function.class;
            case 1:
                return Object.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Funktion";
            case 1:
                return "Parameter";
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
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
        if (i == 0) {
            return true;
        }
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
                return Object.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Resultat";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Aufruf Funktion";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Funktionen] function Funktion aufrufen call evaluate";
    }

    @Override
    public String getUniqueName() {
        return "buildin.CallFunction";
    }

    @Override
    public String getIconName() {
        return "Call-Function_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws FunctionNotAdoptableException, Exception {

        Function function = (Function) io.in0(0);

        if (function == null) {
            throw new Exception("Function muss gesetzt sein!");
        }
        Object[] parameter = io.in(1, new Object[0]);

        Object[] result = function.evaluate(parameter);

        io.out(0, result);
    }

}
