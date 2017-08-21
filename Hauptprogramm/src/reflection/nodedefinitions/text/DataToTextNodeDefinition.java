package reflection.nodedefinitions.text;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.nio.charset.Charset;
import reflection.customdatatypes.rawdata.RawData;

public class DataToTextNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return RawData.class;
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
                return "Data";
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
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Text";
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
        return "Daten zu Text";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Text] Text von Rohdaten from RawData";
    }

    @Override
    public String getUniqueName() {
        return "buildin.DataToText";
    }

    @Override
    public String getIconName() {
        return "Data-To-Text_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        RawData data = (RawData) io.in0(0, new RawData());
        Charset charset = Charset.forName((String) io.in0(1, "UTF-8"));

        String text = new String(data.getData(), charset);

        io.out(0, text);
    }

}
