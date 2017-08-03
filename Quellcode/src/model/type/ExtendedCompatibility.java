package model.type;

import java.io.File;
import java.nio.file.Path;
import reflection.customdatatypes.math.MathObject;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.math.NumberMathObject;
import reflection.customdatatypes.math.Vector;
import utils.ArrayHelper;

/**
 * Diese Klasse dient dazu die Kompatibilitaet von Typen zu erweitern welche
 * sich standartmaessig nicht zu einander casten lassen.
 */
public class ExtendedCompatibility {

    protected static boolean typeCanConnectToTypeViaExtendedCompatibility(Type from, Type to) {
        
        if (to.getTypeClass() == Void.class) { // to ist von Typ Void
            return true;

        } else if (typeIsCompatiblePrimitiveType(from) && typeIsCompatiblePrimitiveType(to)) { // Beides sind Primitive Datentypen
            return true;

        } else if (Number.class.isAssignableFrom(from.getTypeClass()) && typeIsCompatiblePrimitiveType(to)) { // to ist ein Primitiver-Datentyp und from leitet von Number ab
            return true;

        } else if (from.getTypeClass() == Object.class && to.getTypeClass() == String.class) { // Object nach String
            return true;

        } else if (from.getTypeClass() == String.class && to.getTypeClass() == File.class) { // String nach File
            return true;

        } else if (from.getTypeClass() == File.class && to.getTypeClass() == String.class) { // File nach String
            return true;

        } else if (from.getTypeClass() == String.class && to.getTypeClass() == Path.class) {
            return true;

        } else if (from.getTypeClass() == Path.class && to.getTypeClass() == String.class) {
            return true;

        } else if (from.getTypeClass() == File.class && to.getTypeClass() == Path.class) { // File nach Path
            return true;
            
        } else if (from.getTypeClass() == Path.class && to.getTypeClass() == File.class) { // Path nach File
            return true;
            
        } else if (MathObject.class.isAssignableFrom(from.getTypeClass()) && Number.class.isAssignableFrom(to.getTypeClass())) { // MathObject nach Number (funktioniert nicht immer!)
            return true;

        } else if (MathObject.class.isAssignableFrom(to.getTypeClass()) && Number.class.isAssignableFrom(from.getTypeClass())) { // Number nach MathObject 
            return true;

        } else { // andere Faelle
            return false;

        }
    }

    private static boolean typeIsCompatiblePrimitiveType(Type type) {
        Class typeClass = type.getTypeClass();
        if (typeClass == Byte.class || typeClass == Short.class || typeClass == Integer.class || typeClass == Long.class || typeClass == Float.class || typeClass == Double.class || typeClass == Character.class || typeClass == Boolean.class || typeClass == String.class) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean typeNeedsExtendedCompatibilityToConnectToType(Type from, Type to) {
        
        if (!CompatibilityTest.canTypeConnectToType(from, to)) {
            throw new IllegalArgumentException("Typen muessen Kompatibilitaets-Test zu einander bestehen");
        } else {
            boolean result = from.getTypeClass() != to.getTypeClass() && typeCanConnectToTypeViaExtendedCompatibility(from, to);
            return result;
        }
    }

    public static Object[] convertObjectToType(Object[] o, Type from, Type to) {
                
        Object[] r = new Object[o.length];

        Class fromTypeClass = from.getTypeClass();
        Class toTypeClass = to.getTypeClass();

        if (fromTypeClass == NumberMathObject.class) {
            for (int i = 0; i < r.length; i++) {
                MathObject oi = (MathObject) o[i];
                if (oi instanceof NumberMathObject) {
                    r[i] = ((NumberMathObject) oi).getWrappedNumber();
                } else {
                    System.err.println("fromTypeClass ist als NumberMathObject ausgewiesen, erhaltener Typ ist aber: " + r[i].getClass().toString());
                }
            }
            fromTypeClass = Number.class;
            from = new Type(fromTypeClass, from.isArray());
        }

        if (to.getTypeClass() == Void.class) { // to ist von Typ Void
            return new Object[0]; // Nichts weitergeben

        }
        if (Number.class.isAssignableFrom(from.getTypeClass()) && typeIsCompatiblePrimitiveType(to)) { // to ist ein Primitiver-Datentyp und from leitet von Number ab

            if (toTypeClass == Byte.class) {
                for (int i = 0; i < r.length; i++) {
                    r[i] = (Byte) ((Number) o[i]).byteValue();
                }
            } else if (toTypeClass == Short.class) {
                for (int i = 0; i < r.length; i++) {
                    r[i] = (Short) ((Number) o[i]).shortValue();
                }
            } else if (toTypeClass == Integer.class) {
                for (int i = 0; i < r.length; i++) {
                    r[i] = (Integer) ((Number) o[i]).intValue();
                }
            } else if (toTypeClass == Long.class) {
                for (int i = 0; i < r.length; i++) {
                    r[i] = (Long) ((Number) o[i]).longValue();
                }
            } else if (toTypeClass == Double.class) {
                for (int i = 0; i < r.length; i++) {
                    r[i] = (Double) ((Number) o[i]).doubleValue();
                }
            } else if (toTypeClass == Float.class) {
                for (int i = 0; i < r.length; i++) {
                    r[i] = (Float) ((Number) o[i]).floatValue();
                }
            } else if (toTypeClass == Character.class) {
                for (int i = 0; i < r.length; i++) {
                    r[i] = (Character) (char) ((Number) o[i]).longValue();
                }
            } else if (toTypeClass == Boolean.class) {
                for (int i = 0; i < r.length; i++) {
                    r[i] = (Boolean) (boolean) (((Number) o[i]).doubleValue() != 0);
                }
            } else if (toTypeClass == String.class) {
                for (int i = 0; i < r.length; i++) {
                    Number n = (Number) o[i];
                    double d = n.doubleValue();
                    if (d - ((int) d) == 0) {
                        r[i] = String.valueOf(n.longValue());
                    } else {
                        r[i] = String.valueOf(n.doubleValue());
                    }
                }
            }

            return r;

        } else if (typeIsCompatiblePrimitiveType(from) && typeIsCompatiblePrimitiveType(to)) { // Beides sind Primitive Datentypen

            if (from.getTypeClass() == String.class) {
                if (toTypeClass == Byte.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = Byte.valueOf((String) o[i]);
                    }
                } else if (toTypeClass == Short.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = Short.valueOf((String) o[i]);
                    }
                } else if (toTypeClass == Integer.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = Integer.valueOf((String) o[i]);
                    }
                } else if (toTypeClass == Long.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = Long.valueOf((String) o[i]);
                    }
                } else if (toTypeClass == Double.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = Double.valueOf((String) o[i]);
                    }
                } else if (toTypeClass == Float.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = Float.valueOf((String) o[i]);
                    }
                } else if (toTypeClass == Character.class) {
                    int rlen = 0;
                    for (Object e : o) {
                        String s = (String) e;
                        rlen += s.length();
                    }
                    r = new Object[rlen];
                    int ri = 0;
                    for (int i1 = 0; i1 < o.length; i1++) {
                        String s = (String) o[i1];
                        for (int i2 = 0; i2 < s.length(); i2++) {
                            r[ri] = (Character) s.charAt(i2);
                            ri++;
                        }
                    }
                } else if (toTypeClass == Boolean.class) {
                    for (int i = 0; i < r.length; i++) {
                        if (((String) o[i]).equalsIgnoreCase("true") || ((String) o[i]).equalsIgnoreCase("t") || ((String) o[i]).equalsIgnoreCase("wahr") || ((String) o[i]).equalsIgnoreCase("w")) {
                            r[i] = (Boolean) true;
                        } else if (((String) o[i]).equalsIgnoreCase("false") || ((String) o[i]).equalsIgnoreCase("f") || ((String) o[i]).equalsIgnoreCase("unwahr")) {
                            r[i] = (Boolean) false;
                        } else {
                            throw new IllegalArgumentException("String " + ((String) o[i]) + " kann nicht zu Boolean umgewandelt werden");
                        }
                    }
                } else if (toTypeClass == String.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = o[i];
                    }
                }

            } else if (from.getTypeClass() == Boolean.class) {
                if (toTypeClass == Byte.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = (Byte) (byte) (((Boolean) o[i]) ? 1 : 0);
                    }
                } else if (toTypeClass == Short.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = (Short) (short) (((Boolean) o[i]) ? 1 : 0);
                    }
                } else if (toTypeClass == Integer.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = (Integer) (((Boolean) o[i]) ? 1 : 0);
                    }
                } else if (toTypeClass == Long.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = (Long) (((Boolean) o[i]) ? 1l : 0l);
                    }
                } else if (toTypeClass == Double.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = (Double) (((Boolean) o[i]) ? 1d : 0d);
                    }
                } else if (toTypeClass == Float.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = (Float) (((Boolean) o[i]) ? 1f : 0f);
                    }
                } else if (toTypeClass == Character.class) {
                    for (int i = 0; i < r.length; i++) {
                        if ((Boolean) o[i]) {
                            r[i] = (Character) 't';
                        } else {
                            r[i] = (Character) 'f';
                        }
                    }
                } else if (toTypeClass == Boolean.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = o[i];
                    }
                } else if (toTypeClass == String.class) {
                    for (int i = 0; i < r.length; i++) {
                        if ((Boolean) o[i]) {
                            r[i] = "true";
                        } else {
                            r[i] = "false";
                        }
                    }
                }

            } else if (from.getTypeClass() == Character.class) {
                if (toTypeClass == Byte.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = (Byte) (byte) ((Character) o[i]).charValue();
                    }
                } else if (toTypeClass == Short.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = (Short) (short) ((Character) o[i]).charValue();
                    }
                } else if (toTypeClass == Integer.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = (Integer) (int) ((Character) o[i]).charValue();
                    }
                } else if (toTypeClass == Long.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = (Long) (long) ((Character) o[i]).charValue();
                    }
                } else if (toTypeClass == Double.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = (Double) (double) (int) ((Character) o[i]).charValue();
                    }
                } else if (toTypeClass == Float.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = (Float) (float) (int) ((Character) o[i]).charValue();
                    }
                } else if (toTypeClass == Character.class) {
                    for (int i = 0; i < r.length; i++) {
                        r[i] = o[i];
                    }
                } else if (toTypeClass == Boolean.class) {
                    for (int i = 0; i < r.length; i++) {
                        if (((Character) o[i]).charValue() == 't') {
                            r[i] = (Boolean) true;
                        } else if (((Character) o[i]).charValue() == 'f') {
                            r[i] = (Boolean) false;
                        } else {
                            throw new IllegalArgumentException("String " + ((String) o[i]) + " kann nicht zu Boolean umgewandelt werden");
                        }
                    }
                } else if (toTypeClass == String.class) {
                    StringBuilder sb = new StringBuilder();
                    for (Object e : o) {
                        sb.append((Character) e);
                    }
                    r = new String[1];
                    r[0] = sb.toString();
                }

                return r;
            }

        } else if (from.getTypeClass() == String.class && to.getTypeClass() == File.class) { // String nach File
            for (int i = 0; i < r.length; i++) {
                r[i] = new File(o[i].toString());
            }

            return r;

        } else if (from.getTypeClass() == File.class && to.getTypeClass() == String.class) { // File nach String 
            for (int i = 0; i < r.length; i++) {
                r[i] = ((File) o[i]).getAbsolutePath();
            }

            return r;

        } else if (from.getTypeClass() == String.class && to.getTypeClass() == Path.class) { // String nach File
            for (int i = 0; i < r.length; i++) {
                r[i] = new File(o[i].toString()).toPath();
            }

            return r;

        } else if (from.getTypeClass() == Path.class && to.getTypeClass() == String.class) { // File nach String 
            for (int i = 0; i < r.length; i++) {
                r[i] = ((Path) o[i]).toString();
            }

            return r;

        } else if (from.getTypeClass() == File.class && to.getTypeClass() == Path.class) { // File nach Path 
            for (int i = 0; i < r.length; i++) {
                r[i] = ((File) o[i]).toPath();
            }

            return r;

        } else if (from.getTypeClass() == Path.class && to.getTypeClass() == File.class) { // Path nach File
            for (int i = 0; i < r.length; i++) {
                r[i] = ((Path) o[i]).toFile();
            }

            return r;

        } else if (from.getTypeClass() == Object.class && to.getTypeClass() == String.class) { // Object nach String
            for (int i = 0; i < r.length; i++) {
                r[i] = o[i].toString();
            }

            return r;

        } else if (Number.class.isAssignableFrom(from.getTypeClass()) && MathObject.class.isAssignableFrom(to.getTypeClass())) { // Number nach MathObject
            
            for (int i = 0; i < r.length; i++) {
                r[i] = new NumberMathObject((Number) o[i]);
            }

            return r;

        } else if (MathObject.class.isAssignableFrom(from.getTypeClass()) && Number.class.isAssignableFrom(to.getTypeClass())) { // MathObject nach Number (funktioniert nicht immer!)
            for (int i = 0; i < r.length; i++) {
                MathObject oi = (MathObject) o[i];

                if (oi instanceof NumberMathObject) {
                    r[i] = ((NumberMathObject) oi).getWrappedNumber();
                }

                if (oi instanceof Matrix) {
                    Matrix m = (Matrix) oi;
                    if (m.getColumCount() == 1 && m.getRowCount() == 1) {
                        r[i] = m.get(0, 0);
                    } else {
                        System.err.println("Matrix (" + from.getTypeClass() + " -> " + to.getTypeClass() + ") kann nicht konvertiert werden");
                    }
                }

                if (oi instanceof Vector) {
                    Vector v = (Vector) oi;
                    if (v.getRowCount() == 1) {
                        r[i] = v.get(0);
                    } else {
                        System.err.println("Vector (" + from.getTypeClass() + " -> " + to.getTypeClass() + ") kann nicht konvertiert werden");
                    }
                }

            }

            return r;

        } else if(to.getTypeClass() == Object.class) {
            return o;
        }

        // andere Faelle...
        System.err.println("Object[] (" + from.getTypeClass() + " -> " + to.getTypeClass() + ") kann nicht konvertiert werden");
        return o;
    }

}
