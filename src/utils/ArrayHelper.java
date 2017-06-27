package utils;

public class ArrayHelper {

    public static Object[] concat(Object[] a, Object[] b) {
        int al = a.length;
        int bl = b.length;
        Object[] c = new Object[al + bl];
        System.arraycopy(a, 0, c, 0, al);
        System.arraycopy(b, 0, c, al, bl);
        return c;
    }
}
