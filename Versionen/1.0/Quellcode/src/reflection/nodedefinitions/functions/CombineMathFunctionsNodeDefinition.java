package reflection.nodedefinitions.functions;

import reflection.customdatatypes.Function;
import reflection.*;

public class CombineMathFunctionsNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        if (index == 1) {
            return Number.class;
        } else {
            return Function.class;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "k";
            case 1:
                return "f1";
            case 2:
                return "f2";
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
        if (i == 1) {
            return true;
        }
        return false;
    }

    @Override
    public int getOutletCount() {
        return 11;
    }

    @Override
    public Class getClassForOutlet(int index) {
        return Function.class;
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "f = f1 * k";
            case 1:
                return "f = f1 / k";
            case 2:
                return "f = f1 + k";
            case 3:
                return "f = f1 - k";
            case 4:
                return "f = f1^k";
            case 5:
                return "f = f1^(1/k)";
            case 6:
                return "f = f1 * f2";
            case 7:
                return "f = f1 / f2";
            case 8:
                return "f = f1 + f2";
            case 9:
                return "f = f1 - f2";
            case 10:
                return "f = f1^f2";
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
        return "Math-Funktionen kombinieren";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Funktionen] function Funktion verbinden erstellen neu neue Mathe * / - + ^";
    }

    @Override
    public String getUniqueName() {
        return "buildin.CombineFunctions";
    }

    @Override
    public String getIconName() {
        return "Combine-Function_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        final Number k = (Number) io.in0(0);
        final Function f1 = (Function) io.in0(1, new Function() {
            @Override
            public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                return parameter;
            }
        });
        final Function f2 = (Function) io.in0(2, new Function() {
            @Override
            public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                return parameter;
            }
        });

        if (io.outConnected(0)) {
            io.out(0, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    if (k == null) {
                        return parameter;
                    } else {
                        return new Object[]{
                            Function.parameterToNumber(f1.evaluate(parameter)).doubleValue() * k.doubleValue()
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
                            Function.parameterToNumber(f1.evaluate(parameter)).doubleValue() / k.doubleValue()
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
                            Function.parameterToNumber(f1.evaluate(parameter)).doubleValue() + k.doubleValue()
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
                            Function.parameterToNumber(f1.evaluate(parameter)).doubleValue() - k.doubleValue()
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
                            Math.pow(Function.parameterToNumber(f1.evaluate(parameter)).doubleValue(), 2)
                        };
                    } else {
                        return new Object[]{
                            Math.pow(Function.parameterToNumber(f1.evaluate(parameter)).doubleValue(), k.doubleValue())
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
                            Math.pow(Function.parameterToNumber(f1.evaluate(parameter)).doubleValue(), 1 / 2)
                        };
                    } else {
                        return new Object[]{
                            Math.pow(Function.parameterToNumber(f1.evaluate(parameter)).doubleValue(), 1 / k.doubleValue())
                        };
                    }
                }
            });
        }

        if (io.outConnected(6)) {
            io.out(6, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    return new Object[]{
                        Function.parameterToNumber(f1.evaluate(parameter)).doubleValue() * Function.parameterToNumber(f2.evaluate(parameter)).doubleValue()
                    };
                }
            });
        }

        if (io.outConnected(7)) {
            io.out(7, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    return new Object[]{
                        Function.parameterToNumber(f1.evaluate(parameter)).doubleValue() / Function.parameterToNumber(f2.evaluate(parameter)).doubleValue()
                    };
                }
            });
        }

        if (io.outConnected(8)) {
            io.out(8, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    return new Object[]{
                        Function.parameterToNumber(f1.evaluate(parameter)).doubleValue() + Function.parameterToNumber(f2.evaluate(parameter)).doubleValue()
                    };
                }
            });
        }

        if (io.outConnected(9)) {
            io.out(9, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    return new Object[]{
                        Function.parameterToNumber(f1.evaluate(parameter)).doubleValue() - Function.parameterToNumber(f2.evaluate(parameter)).doubleValue()
                    };
                }
            });
        }

        if (io.outConnected(10)) {
            io.out(10, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
                    return new Object[]{
                        Math.pow(Function.parameterToNumber(f1.evaluate(parameter)).doubleValue(), Function.parameterToNumber(f2.evaluate(parameter)).doubleValue())
                    };
                }
            });
        }
    }

}
