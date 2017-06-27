package reflection.nodedefinitions.basic.print;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class PrintObjectInfoNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        return Object.class;
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        return "Data";
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        return true;
    }
    
    @Override
    public boolean isInletEngaged(int i) {
        return true;
    }

    @Override
    public int getOutletCount() {
        return 0;
    }

    @Override
    public Class getClassForOutlet(int outletIndex) {
        return null;
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        return null;
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return false;
    }

    @Override
    public String getName() {
        return "Print Object-Info";
    }

    @Override
    public String getDescription() {
        return "Loggt Informationen zu Object-Array in die Log-Ausgabe." + TAG_PREAMBLE + " [Basics] Ausgabe ausgeben debugging Info Objekt Analyse Debug Konsole Drucken toString Tabelle";
    }

    @Override
    public String getUniqueName() {
        return "buildin.PrintObjectInfo";
    }

    @Override
    public String getIconName() {
        return "Print-Object-Info_30px 2.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {
        Object[] objects = io.in(0, new Object[0]);
        api.additionalPrintOut(objects.length + " Objecke ausgeben");

        // Nummerierung...
        String[] is = new String[objects.length];
        String isTitle = "Nr ";
        int mis = isTitle.length();
        for (int i = 0; i < objects.length; i++) {
            is[i] = String.valueOf(i) + " ";
            mis = Math.max(mis, is[i].length());
        }

        // Klassen...
        String[] cs = new String[objects.length];
        String csTitle = " Klasse ";
        int mcs = csTitle.length();
        for (int i = 0; i < objects.length; i++) {
            Object o = objects[i];
            if (o != null) {
                cs[i] = " " + String.valueOf(classToString(o.getClass())) + " ";
            } else {
                cs[i] = " null ";
            }
            mcs = Math.max(mcs, cs[i].length());
        }

        // toString...
        String[] ts = new String[objects.length];
        String tsTitle = " toString()";
        int mts = tsTitle.length();
        for (int i = 0; i < objects.length; i++) {
            Object o = objects[i];
            if (o != null) {
                if (o.getClass().isArray()) { // Fall: ist Array (enthalten im Array -> Mehrdimensionales Array)
                    Object[] oo = (Object[]) o;
                    ts[i] = " [";
                    boolean first = true;
                    for (Object ooo : oo) {
                        if (!first) {
                            ts[i] += ", ";
                        }
                        ts[i] += ooo.toString();
                        first = false;
                    }
                    ts[i] += "]";
                } else { // Fall: Kein Array
                    ts[i] = " " + o.toString();
                }
            } else {
                ts[i] = "null";
            }
            mts = Math.max(mts, ts[i].length());
        }

        // Ausgaben...
        for (int i = -2; i < objects.length; i++) {
            String line;
            switch (i) {
                case -2:
                    // Fall: Titelzeile
                    line = isTitle + spaces(mis - isTitle.length()) + "|" + csTitle + spaces(mcs - csTitle.length()) + "|" + tsTitle + spaces(mts - tsTitle.length());
                    break;
                case -1:
                    // Fall: Trennlinie
                    line = repeat("-", mis) + "+" + repeat("-", mcs) + "+" + repeat("-", mts);
                    break;
                default:
                    // Fall: Zeilen
                    line = spaces(mis - is[i].length()) + is[i] + "|" + cs[i] + spaces(mcs - cs[i].length()) + "|" + ts[i] + spaces(mts - ts[i].length());
                    break;
            }
            api.printOut(line);
        }

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
    
    /**
     * Gibt count Leerzeichen zuruek.
     */
    public String spaces(int count) {
        return repeat(" ", count);
    }
    
    /**
     * Wiederholt String count mal.
     */
    public String repeat(String s, int count) {
        StringBuilder sb = new StringBuilder(count);
        while(count > 0) {
            sb.append(s);
            count--;
        }
        return sb.toString();
    }

}
