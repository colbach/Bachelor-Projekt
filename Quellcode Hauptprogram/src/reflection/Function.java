package reflection;

import java.awt.Color;

public abstract class Function {

    public boolean evaluateBoolean(Object[] parameter) throws FunctionNotAdoptableException {
        Object[] result = evaluate(parameter);
        if (result.length == 1) {
            if (result[0] instanceof Boolean) {
                return (Boolean) result[0];
            }
        }
        throw new FunctionNotAdoptableException("Keine boolesche Rueckgabe");
    }

    public boolean evaluateBoolean(Object parameter) throws FunctionNotAdoptableException {
        return evaluateBoolean(new Object[]{parameter});
    }

    public String evaluateString(Object[] parameter) throws FunctionNotAdoptableException {
        Object[] result = evaluate(parameter);
        if (result.length == 1) {
            if (result[0] instanceof String) {
                return (String) result[0];
            }
        }
        throw new FunctionNotAdoptableException("Kein String als Rueckgabe");
    }

    public String evaluateString(Object parameter) throws FunctionNotAdoptableException {
        return evaluateString(new Object[]{parameter});
    }

    public Number evaluateNumber(Object[] parameter) throws FunctionNotAdoptableException {
        Object[] result = evaluate(parameter);
        if (result.length == 1) {
            if (result[0] instanceof Number) {
                return (Number) result[0];
            }
        }
        throw new FunctionNotAdoptableException("Keine numerische Rueckgabe");
    }

    public Number evaluateNumber(Object parameter) throws FunctionNotAdoptableException {
        return evaluateNumber(new Object[]{parameter});
    }

    public Object evaluateObject(Object[] parameter) throws FunctionNotAdoptableException {
        Object[] result = evaluate(parameter);
        if (result.length == 1) {
            return (Object) result[0];
        }
        throw new FunctionNotAdoptableException("Keine numerische Rueckgabe");
    }

    public Object evaluateObject(Object parameter) throws FunctionNotAdoptableException {
        return evaluateObject(new Object[]{parameter});
    }

    public static Boolean parameterToBoolean(Object[] parameter) throws FunctionNotAdoptableException {
        if (parameter.length == 1) {
            if (parameter[0] instanceof Boolean) {
                return (Boolean) parameter[0];
            }
        }
        throw new FunctionNotAdoptableException("Funktion erwartet boolesche Eingabe");
    }

    public static String parameterToString(Object[] parameter) throws FunctionNotAdoptableException {
        if (parameter.length == 1) {
            if (parameter[0] instanceof String) {
                return (String) parameter[0];
            }
        }
        throw new FunctionNotAdoptableException("Funktion erwartet String als Eingabe");
    }

    public static Number parameterToNumber(Object[] parameter) throws FunctionNotAdoptableException {
        if (parameter.length == 1) {
            if (parameter[0] instanceof Number) {
                return (Number) parameter[0];
            }
        }
        throw new FunctionNotAdoptableException("Funktion erwartet numerische Eingabe");
    }

    public static Color parameterToColor(Object[] parameter) throws FunctionNotAdoptableException {
        if (parameter.length == 1) {
            if (parameter[0] instanceof Color) {
                return (Color) parameter[0];
            }
        }
        throw new FunctionNotAdoptableException("Funktion erwartet Farbe als Eingabe");
    }

    public static Object parameterToObject(Object[] parameter) throws FunctionNotAdoptableException {
        if (parameter.length == 1) {
            return parameter[0];
        }
        throw new FunctionNotAdoptableException("Funktion erwartet numerische Eingabe");
    }

    public abstract Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException;

}
