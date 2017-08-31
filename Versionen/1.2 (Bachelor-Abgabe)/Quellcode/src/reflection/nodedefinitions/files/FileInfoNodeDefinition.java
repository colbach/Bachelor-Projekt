package reflection.nodedefinitions.files;

import java.io.File;
import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;

public class FileInfoNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return File.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Datei";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int index) {

        return true;
    }

    @Override
    public int getOutletCount() {
        return 5;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Long.class;
            case 1:
                return String.class;
            case 2:
                return Boolean.class;
            case 3:
                return String.class;
            case 4:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Grösse (in Byte)";
            case 1:
                return "Dateiname";
            case 2:
                return "Existiert";
            case 3:
                return "Endung";
            case 4:
                return "Verzeichniss";
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
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Datei-Info";
    }

    @Override
    public String getDescription() {
        return "Gibt Datei-Informationen weiter." + TAG_PREAMBLE + " [Dateien] Datei File Info Grösse in Byte Dateiname Endung Verzeichniss Existiert";
    }

    @Override
    public String getUniqueName() {
        return "buildin.FileInfo";
    }

    @Override
    public String getIconName() {
        return "File-Info_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        File datei = (File) io.in0(0, null);

        Long grsseinbyte = null;
        String dateiname = null;
        Boolean existiert = null;
        String endung = null;
        Boolean verzeichniss = null;

        existiert = datei.exists();
        if (existiert) {
            grsseinbyte = datei.length();
            verzeichniss = datei.isDirectory();
        } else {
            grsseinbyte = 0L;
            verzeichniss = datei.isDirectory();
        }
        dateiname = datei.getName();
        if (dateiname.contentEquals(".")) {
            endung = dateiname.substring(dateiname.lastIndexOf(".") + 1);
        } else {
            endung = "";
        }

        io.out(0, grsseinbyte);
        io.out(1, dateiname);
        io.out(2, existiert);
        io.out(3, endung);
        io.out(4, verzeichniss);
    }

}
