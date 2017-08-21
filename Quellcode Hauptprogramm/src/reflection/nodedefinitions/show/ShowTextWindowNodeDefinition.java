package reflection.nodedefinitions.show;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;
import reflection.customdatatypes.SmartIdentifier;

public class ShowTextWindowNodeDefinition implements NodeDefinition {

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
                return SmartIdentifier.class;
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
                return "Id";
            default:
                return null;
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
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return SmartIdentifier.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Id";
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
        return "Text anzeigen";
    }

    @Override
    public String getDescription() {
        return "Zeigt Text in Fenster an. Die Angabe eines Kontextes gibt an ob jedes mal ein neues Fenster geöffnet werden soll oder ob die neue Bitmap eine alte ersetzen soll. Der Kontext \"test\" öffnet ein neues Fenster falls noch keines mit dieser Bezeichnung angelegt ist oder ersetzt dieses falls dieser Kontext bereits geöffnet ist." + TAG_PREAMBLE + " [UI] show zeigen Text String Fenster";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ShowTextWindow";
    }

    @Override
    public String getIconName() {
        return "Show-Text-Object_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        Object text = io.in0(0);
        SmartIdentifier smartIdentifier = (SmartIdentifier) io.in0(1);

        if (text != null) {
            api.displayContentInWindow(text.toString(), smartIdentifier);
        } else {
            api.printErr("Text ist null. Es wird nichts angezeigt.");
        }

        io.out(0, smartIdentifier);

    }

}
