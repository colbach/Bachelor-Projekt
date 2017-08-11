package utils;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Set;

public class ArrayHelper {

    public static String arrayToMultiLineString(Object[] objects) {
        StringBuilder sb = new StringBuilder("");
        boolean first = true;
        for (Object object : objects) {
            if (first) {
                first = false;
            } else {
                sb.append("\n");
            }
            sb.append(object);
        }
        return sb.toString();
    }
    
    public static String arrayToString(long[] ls) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (long i : ls) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(i);
        }
        sb.append("]");
        return sb.toString();
    }
    
    public static String arrayToString(int[] is) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (int i : is) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(i);
        }
        sb.append("]");
        return sb.toString();
    }
    
    public static String arrayToString(double[] ds) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (double d : ds) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(d);
        }
        sb.append("]");
        return sb.toString();
    }
    
    public static String arrayToString(float[] fs) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (float d : fs) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(d);
        }
        sb.append("]");
        return sb.toString();
    }

    public static String arrayToString(Object[] objects) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Object object : objects) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(object.toString());
        }
        sb.append("]");
        return sb.toString();
    }
    
    public static void print(Set objects) {
        ArrayHelper.print("", objects.toArray());
    }
    
    public static void print(List objects) {
        ArrayHelper.print("", objects.toArray());
    }

    public static void print(Object[] objects) {
        ArrayHelper.print("", objects);
    }
    
    public static void print(String prefix, Set objects) {
        ArrayHelper.print(prefix, objects.toArray());
    }
    
    public static void print(String prefix, List objects) {
        ArrayHelper.print(prefix, objects.toArray());
    }
    
    public static void print(String prefix, Object[] objects) {
        System.out.println(prefix + arrayToString(objects));
    }

    public static void print(int[] is) {
        ArrayHelper.print("", is);
    }

    public static void print(String prefix, int[] is) {
        System.out.println(prefix + arrayToString(is));
    }

    public static void print(long[] ls) {
        ArrayHelper.print("", ls);
    }

    public static void print(String prefix, long[] ls) {
        System.out.println(prefix + arrayToString(ls));
    }

    public static void print(double[] ds) {
        ArrayHelper.print("", ds);
    }

    public static void print(String prefix, double[] ds) {
        System.out.println(prefix + arrayToString(ds));
    }

    public static void print(float[] fs) {
        ArrayHelper.print("", fs);
    }

    public static void print(String prefix, float[] fs) {
        System.out.println(prefix + arrayToString(fs));
    }

    public static Object[] concatObjectArrays(Object[] a, Object[] b) {
        int al = a.length;
        int bl = b.length;
        Object[] c = new Object[al + bl];
        System.arraycopy(a, 0, c, 0, al);
        System.arraycopy(b, 0, c, al, bl);
        return c;
    }

    public static <T> T[] castToGenericArray(Object[] src, Class clazz) {
        T[] dest = (T[]) Array.newInstance(clazz, src.length);
        System.arraycopy(src, 0, dest, 0, src.length);
        return dest;
    }

    public static Number[] castToNumberArray(Object[] src) {
        return castToGenericArray(src, Number.class);
    }

    public static double[] numberArrayToPrimitiveDoubleArray(Number[] src) {
        double[] dest = new double[src.length];
        for (int i = 0; i < src.length; i++) {
            dest[i] = src[i].doubleValue();
        }
        return dest;
    }

    public static float[] numberArrayToPrimitiveFloatArray(Number[] src) {
        float[] dest = new float[src.length];
        for (int i = 0; i < src.length; i++) {
            dest[i] = src[i].floatValue();
        }
        return dest;
    }
}
