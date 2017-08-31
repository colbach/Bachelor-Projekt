package reflection.nodedefinitions.show;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;
import reflection.customdatatypes.SmartIdentifier;

public class ShowImageWindowNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return BufferedImage.class;
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
                return "Bild";
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
        return "Bild anzeigen";
    }

    @Override
    public String getDescription() {
        return "Zeigt Bitmap in Fenster an." + TAG_PREAMBLE + " [UI] [Grafik] show zeigen Bitmap Photo Fenster";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ShowImageWindow";
    }

    @Override
    public String getIconName() {
        return "Show-Image_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        BufferedImage bild = (BufferedImage) io.in0(0);
        SmartIdentifier smartIdentifier = (SmartIdentifier) io.in0(1);

        if (bild != null) {
            api.displayContentInWindow(bild, smartIdentifier);
        } else {
            api.printErr("Bild ist null. Es wird nichts angezeigt.");
        }

        io.out(0, smartIdentifier);

    }

}
