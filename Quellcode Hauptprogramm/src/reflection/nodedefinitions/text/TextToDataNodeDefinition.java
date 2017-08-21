package reflection.nodedefinitions.text;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.nio.charset.Charset;
import reflection.customdatatypes.rawdata.RawData;

public class TextToDataNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return String.class;
            case 1:
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
            case 1:
                return "Charset";
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
                return RawData.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Data";
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
        return "Text zu Daten";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Text] Text zu Rohdaten to RawData";
    }

    @Override
    public String getUniqueName() {
        return "buildin.TextToData";
    }

    @Override
    public String getIconName() {
        return "Text-To-Data_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        String text = (String) io.in0(0, "");
        Charset charset = Charset.forName((String) io.in0(1, "UTF-8"));

        RawData data = new RawData(text.getBytes(charset));

        io.out(0, data);
    }

}
