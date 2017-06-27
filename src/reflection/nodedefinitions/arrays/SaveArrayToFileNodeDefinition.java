package reflection.nodedefinitions.arrays;

import java.awt.image.BufferedImage;
import java.io.File;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class SaveArrayToFileNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 5;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return String.class;
            case 1:
                return File.class;
            case 2:
                return Boolean.class;
            case 3:
                return String.class;
            case 4:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Werte";
            case 1:
                return "Datei";
            case 2:
                return "Überschreiben";
            case 3:
                return "Seperator-Symbol";
            case 4:
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
            default: // 1 2 3 4
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
        if (i == 0 || i == 1) {
            return true;
        }
        return false;
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
        return "CSV speichern";
    }

    @Override
    public String getDescription() {
        return "Speichert Array als CSV-Datei. Standart-Seperator ist ; und das Standart-Escape-Symbol ist \"." + TAG_PREAMBLE + " [Arrays] [Dateien] Text Werte Array Persistent CSV";
    }

    @Override
    public String getUniqueName() {
        return "buildin.SaveArrayToCSVFile";
    }

    @Override
    public String getIconName() {
        return "Save-Array-To-CSV_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        Object[] values = io.in(0, new String[0]);
        File file = (File) io.in0(1, new File[0]);
        Boolean overwrite = (Boolean) io.in0(2, true);
        String seperator = (String) io.in0(3, ";");
        String escape = (String) io.in0(4, "\"");
        io.terminatedTest();

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object value : values) {
            io.terminatedTest();
            if (!first) {
                sb.append(seperator);
            }
            String s = value.toString().replace(escape, escape + escape).replace(seperator, escape + seperator);
            sb.append(value.toString());
            first = false;
        }

        io.terminatedTest();
        if (overwrite || !file.exists()) {
            api.saveStringToFile(sb.toString(), file);
        } else {
            api.additionalPrintErr(file.getAbsolutePath() + " existiert bereits. Abbrechen");
        }

    }

}
