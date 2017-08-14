package view.onrun;

/**
 * Diese Exception wird beim Versuch geworfen einen nicht unterstützten Typ
 * darzustellen.
 */
public class UnsupportedShowTypeException extends Exception {

    public UnsupportedShowTypeException(Class c) {
        super(classToString(c) + " kann nicht grafisch dargestellt werden.");
    }

    /**
     * Gibt Klasse als schoen lesbaren kurzen String zurueck (und nicht wie
     * Java-Ueblich).
     */
    public static String classToString(Class c) {

        boolean array = c.isArray();
        String name;
        if (array) { // fall: Handelt sich um ein Array
            name = c.getComponentType().getName();
        } else {
            name = c.getName();
        }
        int punktPosition = name.lastIndexOf(".");
        if (punktPosition != -1) {
            name = name.substring(punktPosition + 1);
        }
        if (array) {
            name += "[]";
        }
        return name;
    }

}
