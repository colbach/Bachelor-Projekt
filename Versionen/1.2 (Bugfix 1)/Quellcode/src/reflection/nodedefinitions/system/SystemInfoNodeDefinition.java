package reflection.nodedefinitions.system;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.io.File;

public class SystemInfoNodeDefinition implements NodeDefinition {

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
    public boolean isInletEngaged(int index) {

        return false;
    }

    @Override
    public int getOutletCount() {
        return 8;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return Long.class;
            case 4:
                return Long.class;
            case 5:
                return Long.class;
            case 6:
                return Long.class;
            case 7:
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Betriebsystem";
            case 1:
                return "Benutzername";
            case 2:
                return "Arbeits-Pfad";
            case 3:
                return "Freier Speicher [B]";
            case 4:
                return "Speicher insgesamt [B]";
            case 5:
                return "Freier Festplattenspeicher [B]";
            case 6:
                return "Festplattenspeicher insgesamt [B]";
            case 7:
                return "Anzahl Prozessoren";
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
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "System Informationen";
    }

    @Override
    public String getDescription() {
        return "Gibt Systeminformationen weiter" + TAG_PREAMBLE + " [Basics] system informationen Betriebsystem Benutzername Arbeits-Pfad Arbeitspfad Freier Speicher Speicher insgesamt Freier Speicher Freier Festplattenspeicher Festplattenspeicher insgesamt Anzahl Prozessoren";
    }

    @Override
    public String getUniqueName() {
        return "buildin.SystemInfo";
    }

    @Override
    public String getIconName() {
        return "System_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        String betriebsystem = System.getProperty("os.name") + " " + System.getProperty("os.version");
        String benutzername = System.getProperty("user.name");
        String arbeitspfad = System.getProperty("user.dir");
        Long freierspeicherb = Runtime.getRuntime().freeMemory();
        Long speicherinsgesamtb = Runtime.getRuntime().totalMemory();
        File file = new File(".");
        Long freierfestplattenspeicherb = file.getFreeSpace();
        Long festplattenspeicherinsgesamtb = file.getTotalSpace();
        Integer anzahlprozessoren = Runtime.getRuntime().availableProcessors();

        io.out(0, betriebsystem);
        io.out(1, benutzername);
        io.out(2, arbeitspfad);
        io.out(3, freierspeicherb);
        io.out(4, speicherinsgesamtb);
        io.out(5, freierfestplattenspeicherb);
        io.out(6, festplattenspeicherinsgesamtb);
        io.out(7, anzahlprozessoren);
    }

}
