package reflection.nodedefinitions.functions;

import reflection.*;

public class MapArrayNodeDefinition implements NodeDefinition {

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
                return "Array";
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
    }@Override
    public boolean isInletEngaged(int i) {
        return true;
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
                return "Array";
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
        return "Array abbilden";
    }

    @Override
    public String getDescription() {
        return "Wendet Funktion auf jedes Element aus Array an." + TAG_PREAMBLE + " [Funktionen] [Arrays] map abbilden abbildung auf jedes Element anwenden function";
    }

    @Override
    public String getUniqueName() {
        return "buildin.functions.MapArray";
    }

    @Override
    public String getIconName() {
        return "Map-Array_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws FunctionNotAdoptableException {

        Function function = (Function) io.in0(0, new Function() {
            @Override
            public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                return parameter;
            }
        });
        Object[] array = io.in(1, new Object[0]);
        
        for(int i=0; i<array.length; i++) {
            array[i] = function.evaluateObject(array[i]);
        }
        
        io.out(0, array);
    }

}
