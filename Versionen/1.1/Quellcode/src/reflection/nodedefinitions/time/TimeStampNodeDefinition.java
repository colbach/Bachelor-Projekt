package reflection.nodedefinitions.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;

public class TimeStampNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 0;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
        return false;
    }

    @Override
    public int getOutletCount() {
        return 9;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Date.class;
            case 1:
                return Integer.class;
            case 2:
                return Integer.class;
            case 3:
                return Integer.class;
            case 4:
                return Integer.class;
            case 5:
                return Integer.class;
            case 6:
                return Integer.class;
            case 7:
                return Long.class;
            case 8:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Datum";
            case 1:
                return "Jahr";
            case 2:
                return "Monat";
            case 3:
                return "Tag";
            case 4:
                return "Stunde";
            case 5:
                return "Minute";
            case 6:
                return "Sekunde";
            case 7:
                return "Millisekunden seit 1970";
            case 8:
                return "Als Text";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:
                return false;
            case 3:
                return false;
            case 4:
                return false;
            case 5:
                return false;
            case 6:
                return false;
            case 7:
                return false;
            case 8:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Zeitstempel";
    }

    @Override
    public String getDescription() {
        return "Gibt das aktuelle Datum weiter." + TAG_PREAMBLE + " [Zeit]";
    }

    @Override
    public String getUniqueName() {
        return "buildin.TimeStamp";
    }

    @Override
    public String getIconName() {
        return "Time-Stamp_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Date date = new Date();
        io.out(0, date);

        Calendar cal = Calendar.getInstance();
        io.out(1, cal.get(Calendar.YEAR));
        io.out(2, cal.get(Calendar.MONTH));
        io.out(3, cal.get(Calendar.DATE));
        io.out(4, cal.get(Calendar.HOUR_OF_DAY));
        io.out(5, cal.get(Calendar.MINUTE));
        io.out(6, cal.get(Calendar.SECOND));
        io.out(7, System.currentTimeMillis());
        io.out(8, new SimpleDateFormat("dd-MM-yy HH:mm:ss").format(date));
    }

}
