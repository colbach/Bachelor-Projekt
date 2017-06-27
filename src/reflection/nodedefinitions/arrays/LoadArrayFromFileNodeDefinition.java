package reflection.nodedefinitions.arrays;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class LoadArrayFromFileNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return File.class;
            default:
                return String.class;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Datei";
            case 1:
                return "Seperator-Symbol";
            case 2:
                return "Escape-Symbol";
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
    public int getOutletCount() {
        return 1;
    }

    @Override
    public boolean isInletEngaged(int inletIndex) {
        if (inletIndex == 0) {
            return true;
        }
        return false;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Werte";
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
        return "CSV laden";
    }

    @Override
    public String getDescription() {
        return "Lädt Array aus CSV-Datei." + TAG_PREAMBLE + " [Arrays] [Dateien] Text Werte Array Persistent CSV";
    }

    @Override
    public String getUniqueName() {
        return "buildin.LoadArrayFromCSVFile";
    }

    @Override
    public String getIconName() {
        return "Load-Array-From-CSV_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {
        File file = (File) io.in0(0, new File[0]);
        String seperator = (String) io.in0(1, ";");
        String escape = (String) io.in0(2, "\"");
        io.terminatedTest();

        String fileContent = api.loadStringFromFile(file).replace(escape + seperator, seperator).replace(escape + escape, escape);
        String[] entrys = fileContent.split(Pattern.quote(seperator));

        io.out(0, entrys);
    }

}
