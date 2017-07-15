package reflection.nodedefinitions.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;
import reflection.customdatatypes.rawdata.RawDataFromFile;

public class ReadRawDataNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return File.class;
            case 1:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Quelldatei";
            case 1:
                return "Spät laden";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
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
            default:
                return false;
        }
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return RawDataFromFile.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Daten";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Daten aus Datei lesen";
    }

    @Override
    public String getDescription() {
        return "Liesst Rohdaten aus Datei" + TAG_PREAMBLE + " [Dateien] raw file Datei read laden load lesen festplatte disk";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ReadRawData";
    }

    @Override
    public String getIconName() {
        return "Read-Data_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws IOException {

        Object[] quelldateien = io.in(0, new File[0]);
        Boolean lazy = (Boolean) io.in0(0, false);

        ArrayList<RawDataFromFile> daten = new ArrayList<>();
        for(Object quelldatei : quelldateien) {
            daten.add(new RawDataFromFile((File) quelldatei, lazy));
        }

        io.out(0, daten.toArray());
    }

}
