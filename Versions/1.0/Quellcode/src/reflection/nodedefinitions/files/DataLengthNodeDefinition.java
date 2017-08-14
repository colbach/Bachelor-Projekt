package reflection.nodedefinitions.files;

import reflection.nodedefinitions.arrays.*;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;
import reflection.customdatatypes.rawdata.*;

public class DataLengthNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return RawData.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Array";
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
    public boolean isInletEngaged(int inletIndex) {
        return true;
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Länge";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Daten-Länge";
    }

    @Override
    public String getDescription() {
        return "Gibt Länge von Daten weiter." + TAG_PREAMBLE + " [Dateien] Länge length lenth lentgh";
    }

    @Override
    public String getUniqueName() {
        return "buildin.DataLength";
    }

    @Override
    public String getIconName() {
        return "Data-Length_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        RawData raw = (RawData) io.in0(0, new RawData());

        io.out(0, raw.size());
    }

}
