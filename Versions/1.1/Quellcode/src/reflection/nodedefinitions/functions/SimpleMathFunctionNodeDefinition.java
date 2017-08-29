package reflection.nodedefinitions.functions;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import reflection.nodedefinitionsupport.functions.FunctionNotAdoptableException;
import reflection.customdatatypes.Function;

public class SimpleMathFunctionNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        return Number.class;
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "k";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        return false;
    }

    @Override
    public boolean isInletEngaged(int i) {
        return false;
    }

    @Override
    public int getOutletCount() {
        return 12;
    }

    @Override
    public Class getClassForOutlet(int index) {
        return Function.class;
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "f = $1 * k";
            case 1:
                return "f = $1 / k";
            case 2:
                return "f = $1 + k";
            case 3:
                return "f = $1 - k";
            case 4:
                return "f = $1^k";
            case 5:
                return "f = $1^(1/k)";
            case 6:
                return "f = $1 * $2";
            case 7:
                return "f = $1 / $2";
            case 8:
                return "f = $1 + $2";
            case 9:
                return "f = $1 - $2";
            case 10:
                return "f = $1^$2";
            case 11:
                return "f = k";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        return false;
    }

    @Override
    public String getName() {
        return "Math-Funktion erstellen";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Funktionen] function Funktion verbinden erstellen neu neue Mathe * / - + ^ Rechner calculator addieren subtrahieren dividieren Math rechnen Taschenrechner mal plus minus geteilt divide times";
    }

    @Override
    public String getUniqueName() {
        return "buildin.SimpleMathFunction";
    }

    @Override
    public String getIconName() {
        return "Simple-Math-Function_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        final Number k = (Number) io.in0(0);

        if (io.outConnected(0)) {
            io.out(0, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    if (k == null) {
                        return parameter;
                    } else {
                        return new Object[]{
                            Function.parameterToNumber(parameter).doubleValue() * k.doubleValue()
                        };
                    }
                }
            });
        }

        if (io.outConnected(1)) {
            io.out(1, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    if (k == null) {
                        return parameter;
                    } else {
                        return new Object[]{
                            Function.parameterToNumber(parameter).doubleValue() / k.doubleValue()
                        };
                    }
                }
            });
        }

        if (io.outConnected(2)) {
            io.out(2, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    if (k == null) {
                        return parameter;
                    } else {
                        return new Object[]{
                            Function.parameterToNumber(parameter).doubleValue() + k.doubleValue()
                        };
                    }
                }
            });
        }

        if (io.outConnected(3)) {
            io.out(3, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    if (k == null) {
                        return parameter;
                    } else {
                        return new Object[]{
                            Function.parameterToNumber(parameter).doubleValue() - k.doubleValue()
                        };
                    }
                }
            });
        }

        if (io.outConnected(4)) {
            io.out(4, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    if (k == null) {
                        return new Object[]{
                            Math.pow(Function.parameterToNumber(parameter).doubleValue(), 2)
                        };
                    } else {
                        return new Object[]{
                            Math.pow(Function.parameterToNumber(parameter).doubleValue(), k.doubleValue())
                        };
                    }
                }
            });
        }

        if (io.outConnected(5)) {
            io.out(5, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    if (k == null) {
                        return new Object[]{
                            Math.pow(Function.parameterToNumber(parameter).doubleValue(), 1 / 2)
                        };
                    } else {
                        return new Object[]{
                            Math.pow(Function.parameterToNumber(parameter).doubleValue(), 1 / k.doubleValue())
                        };
                    }
                }
            });
        }

        if (io.outConnected(6)) {
            io.out(6, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    if (!(parameter[0] instanceof Number) || !(parameter[1] instanceof Number)) {
                        throw new FunctionNotAdoptableException("$1 und $2 muessen vom Typ Number sein.");
                    } else {
                        return new Object[]{
                            ((Number) parameter[0]).doubleValue() * ((Number) parameter[1]).doubleValue()
                        };
                    }
                }
            });
        }

        if (io.outConnected(7)) {
            io.out(7, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    if (!(parameter[0] instanceof Number) || !(parameter[1] instanceof Number)) {
                        throw new FunctionNotAdoptableException("$1 und $2 muessen vom Typ Number sein.");
                    } else {
                        return new Object[]{
                            ((Number) parameter[0]).doubleValue() / ((Number) parameter[1]).doubleValue()
                        };
                    }
                }
            });
        }

        if (io.outConnected(8)) {
            io.out(8, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    if (!(parameter[0] instanceof Number) || !(parameter[1] instanceof Number)) {
                        throw new FunctionNotAdoptableException("$1 und $2 muessen vom Typ Number sein.");
                    } else {
                        return new Object[]{
                            ((Number) parameter[0]).doubleValue() + ((Number) parameter[1]).doubleValue()
                        };
                    }
                }
            });
        }

        if (io.outConnected(9)) {
            io.out(9, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    if (!(parameter[0] instanceof Number) || !(parameter[1] instanceof Number)) {
                        throw new FunctionNotAdoptableException("$1 und $2 muessen vom Typ Number sein.");
                    } else {
                        return new Object[]{
                            ((Number) parameter[0]).doubleValue() - ((Number) parameter[1]).doubleValue()
                        };
                    }
                }
            });
        }

        if (io.outConnected(10)) {
            io.out(10, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    if (!(parameter[0] instanceof Number) || !(parameter[1] instanceof Number)) {
                        throw new FunctionNotAdoptableException("$1 und $2 muessen vom Typ Number sein.");
                    } else {
                        return new Object[]{
                            (Math.pow(((Number) parameter[0]).doubleValue(), ((Number) parameter[1]).doubleValue()))
                        };
                    }
                }
            });
        }

        if (io.outConnected(11)) {
            io.out(11, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    if (k == null) {
                        return new Object[]{
                            0
                        };
                    } else {
                        return new Object[]{
                            k
                        };
                    }
                }
            });
        }
    }

}
