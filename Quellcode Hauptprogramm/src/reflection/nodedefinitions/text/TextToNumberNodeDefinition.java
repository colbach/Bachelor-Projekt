package reflection.nodedefinitions.text;

import java.nio.charset.Charset;
import reflection.*;
import reflection.customdatatypes.rawdata.RawData;

public class TextToNumberNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Text";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            case 1:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int index) {
        if (index == 0) {
            return true;
        } else {
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
                return Number.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Zahl";
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
        return "Text zu Zahl";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Text] Text zu Number Zahl Wert";
    }

    @Override
    public String getUniqueName() {
        return "buildin.TextToZahl";
    }

    @Override
    public String getIconName() {
        return "Text-To-Number_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        String text = (String) io.in0(0, "0");

        io.out(0, new Double(text));
    }

}
