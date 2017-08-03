package reflection.nodedefinitions.arrays;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class CSVToArrayNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return String.class;
            default:
                return String.class;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "CSV";
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
                return false;
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
        return "CSV zu Array";
    }

    @Override
    public String getDescription() {
        return "Teilt Strings aus CSV in Array auf." + TAG_PREAMBLE + " [Arrays] Text Werte Array CSV";
    }

    @Override
    public String getUniqueName() {
        return "buildin.CSVToArray";
    }

    @Override
    public String getIconName() {
        return "CSV-To-Aray_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {
        String csv = (String) io.in0(0, "");
        String seperator = (String) io.in0(1, ";");
        String escape = (String) io.in0(2, "\"");
        io.terminatedTest();

        String fileContent = csv.replace(escape + seperator, seperator).replace(escape + escape, escape);
        String[] entrys = fileContent.split(Pattern.quote(seperator));

        io.out(0, entrys);
    }

}
