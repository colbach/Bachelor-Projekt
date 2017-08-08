package reflection.nodedefinitions.arrays;

import java.awt.image.BufferedImage;
import java.io.File;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class ArrayToCSVFileNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
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
            default: // 1 2
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
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        return String.class;
    }

    @Override
    public String getNameForOutlet(int index) {
        return "CSV";
    }

    @Override
    public boolean isOutletForArray(int index) {
        return false;
    }

    @Override
    public String getName() {
        return "Array zu CSV";
    }

    @Override
    public String getDescription() {
        return "Gibt Array als CSV weiter. Standart-Seperator ist ; und das Standart-Escape-Symbol ist \"." + TAG_PREAMBLE + " [Arrays] Text Werte Array CSV";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ArrayToCSV";
    }

    @Override
    public String getIconName() {
        return "Array-To-CSV_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        Object[] values = io.in(0, new String[0]);
        String seperator = (String) io.in0(1, ";");
        String escape = (String) io.in0(2, "\"");
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
        io.out(0, sb.toString());

    }

}
