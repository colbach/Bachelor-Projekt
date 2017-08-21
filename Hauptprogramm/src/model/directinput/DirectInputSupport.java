package model.directinput;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import model.type.Type;
import model.settablecontent.DefinitionWithUserSettableContent;
import model.type.CompatibilityTest;
import reflection.customdatatypes.SourceCode;
import reflection.customdatatypes.SmartIdentifier;
import reflection.customdatatypes.math.MathObject;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.math.NumberMathObject;
import reflection.customdatatypes.math.Vector;

public class DirectInputSupport {

    private static final Type[] SUPPORTED_TYPES = new Type[]{
        new Type(String.class, false),
        new Type(String.class, true),
        new Type(Long.class, false),
        new Type(Long.class, true),
        new Type(Integer.class, false),
        new Type(Integer.class, true),
        new Type(Byte.class, false),
        new Type(Byte.class, true),
        new Type(Short.class, false),
        new Type(Short.class, true),
        new Type(Double.class, false),
        new Type(Double.class, true),
        new Type(Float.class, false),
        new Type(Float.class, true),
        new Type(Boolean.class, false),
        new Type(Boolean.class, true),
        new Type(Color.class, false),
        new Type(Number.class, false),
        new Type(Number.class, true),
        new Type(Matrix.class, false),
        new Type(Vector.class, false),
        new Type(File.class, false),
        new Type(File.class, true),
        new Type(SmartIdentifier.class, false),
        new Type(SourceCode.class, false)
    };

    public static boolean isTypeSupported(Type type) {
        if (type.getTypeClass() == Void.class) {
            return false;
        }
        if (isDirectInputForTypeAvailable(type)) { // Wenn direker Input existiert...
            return true;
        }
        for (Type supportedType : SUPPORTED_TYPES) { // Suche nach Typ welcher das Interface unterstuetzt oder konvertiert werden kann...
            if (supportedType.isArray() == type.isArray()) {
                if (CompatibilityTest.canTypeConnectToType(supportedType, type)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Type[] compartibleDirectInputTypeForType(Type type) {
        HashSet<Type> compartibleDirectInputType = new HashSet<>();
        for (Type supportedType : SUPPORTED_TYPES) { // Suche nach Typ welcher das Interface unterstuetzt oder konvertiert werden kann...
            if (supportedType.isArray() == type.isArray() || type.isArray()) {
                if (CompatibilityTest.canTypeConnectToType(supportedType, type)) {
                    compartibleDirectInputType.add(supportedType);
                }
            }
        }
        if (type.getTypeClass() == MathObject.class || type.getTypeClass() == NumberMathObject.class) {
            compartibleDirectInputType.add(new Type(Long.class, type.isArray()));
            compartibleDirectInputType.add(new Type(Integer.class, type.isArray()));
            compartibleDirectInputType.add(new Type(Byte.class, type.isArray()));
            compartibleDirectInputType.add(new Type(Short.class, type.isArray()));
            compartibleDirectInputType.add(new Type(Double.class, type.isArray()));
            compartibleDirectInputType.add(new Type(Float.class, type.isArray()));
        }
        return compartibleDirectInputType.toArray(new Type[0]);
    }

    public static boolean isDirectInputForTypeAvailable(Type type) {
//        if(type.getTypeClass() == MathObject.class || type.getTypeClass() == NumberMathObject.class) {
//            return true;
//        }
        for (Type supportedType : SUPPORTED_TYPES) {
            if (supportedType.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public String convertToString(Object[] content, Type type) {
        if (isTypeSupported(type)) {
            if (type.isArray()) {
                StringBuffer sb = new StringBuffer();
                boolean first = true;
                for (Object object : content) {
                    if (!first) {
                        sb.append(",");
                    }
                    sb.append(convertToString(new Object[]{object}, new Type(type.getTypeClass(), false)));
                    first = false;
                }
                return sb.toString();
            } else {
                return String.valueOf(content[0]).replaceAll(",", "//comma//");
            }
        } else {
            System.err.println("Typ wird nicht unterstuetzt");
        }
        return null;
    }

}
