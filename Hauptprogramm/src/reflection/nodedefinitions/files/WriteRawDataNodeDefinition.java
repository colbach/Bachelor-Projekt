package reflection.nodedefinitions.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;
import reflection.customdatatypes.rawdata.RawData;
import reflection.customdatatypes.rawdata.RawDataFromFile;

public class WriteRawDataNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return File.class;
            case 1:
                return RawData.class;
            case 2:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Zieldatei";
            case 1:
                return "Daten";
            case 2:
                return "Überschreiben";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            case 1:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int index) {
        switch (index) {
            case 0:
                return true;
            case 1:
                return true;
            default:
                return false;
        }
    }

    @Override
    public int getOutletCount() {
        return 0;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Daten in Datei schreiben";
    }

    @Override
    public String getDescription() {
        return "Schreibt Rohdaten in Datei" + TAG_PREAMBLE + " [Dateien] raw file Datei write schreiben save speichern festplatte disk";
    }

    @Override
    public String getUniqueName() {
        return "buildin.WriteRawData";
    }

    @Override
    public String getIconName() {
        return "Write-Data_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        Object[] files = io.in(0, new File[0]);
        Object[] datas = io.in(1, new RawData[0]);
        Boolean overwrite = (Boolean) io.in0(2, false);

        ArrayList<RawDataFromFile> daten = new ArrayList<>();
        for(int i=0; i<Math.min(files.length, datas.length); i++) {
            File file = (File) files[i];
            if(!overwrite && file.exists())
                throw new Exception("Datei existiert bereits!");
            ((RawData) datas[i]).writeToFile(file);
        }
    }

}
