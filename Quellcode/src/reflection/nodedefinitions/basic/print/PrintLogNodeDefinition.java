package reflection.nodedefinitions.basic.print;

import java.text.SimpleDateFormat;
import java.util.Date;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class PrintLogNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 4;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        if (inletIndex == 0) {
            return Object.class;
        } else {
            return Boolean.class;
        }
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        if (inletIndex == 0) {
            return "Data";
        } else if (inletIndex == 1) {
            return "Fehlerausabe";
        } else if (inletIndex == 2) {
            return "Nummerieren";
        } else if (inletIndex == 3) {
            return "Zeitstempel";
        } else {
            return null;
        }
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        if (inletIndex == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
        if (i == 0) {
            return true;
        }
        return false;
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
        return "Print to Log";
    }

    @Override
    public String getDescription() {
        return "Loggt Objekt (bzw. Objekte) zeilenweise in die Konsole.\nAusgaben können über den Toolbaricon Log eingesehen werden. " + TAG_PREAMBLE + " [Basics] Ausgabe ausgeben ausgegeben log system terminal output debugging System.out.println Konsole Drucken";
    }

    @Override
    public String getUniqueName() {
        return "buildin.PrintLog";
    }

    @Override
    public String getIconName() {
        return "Print-Log_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {
        Object[] objects = io.in(0, new Object[0]);
        api.additionalPrintOut(objects.length + " Objecke ausgeben");
        Boolean num = (Boolean) io.in0(2, false);
        Boolean time = (Boolean) io.in0(3, false);

        int i = 0;
        if (!((Boolean) io.in0(1, false))) {
            for (Object o : objects) {
                io.terminatedTest();
                if (o == null) {
                    if (num) {
                        api.printOut((time ? api.getTimeStamp() + " " : "") + "[" + i + "] null");
                    } else {
                        api.printOut((time ? api.getTimeStamp() + " " : "") + "null");
                    }
                } else {
                    if (num) {
                        api.printOut((time ? api.getTimeStamp() + " " : "") + "[" + i + "] " + o.toString());
                    } else {
                        api.printOut((time ? api.getTimeStamp() + " " : "") + o.toString());
                    }
                }
                i++;
            }
        } else {
            for (Object o : objects) {
                io.terminatedTest();
                if (o == null) {
                    if (num) {
                        api.printErr("[" + i + "] null");
                    } else {
                        api.printErr("null");
                    }
                } else {
                    if (num) {
                        api.printErr("[" + i + "] " + o.toString());
                    } else {
                        api.printErr(o.toString());
                    }
                }
                i++;
            }
        }

    }

}
