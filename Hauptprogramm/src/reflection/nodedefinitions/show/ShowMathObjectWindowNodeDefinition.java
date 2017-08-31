package reflection.nodedefinitions.show;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import reflection.customdatatypes.SmartIdentifier;
import reflection.customdatatypes.math.MathObject;
import reflection.customdatatypes.math.NumberMathObject;

public class ShowMathObjectWindowNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return MathObject.class;
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
                return "Wert";
            case 1:
                return "Id";
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
        return false;
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
        return "Mathematisches Objekt anzeigen";
    }

    @Override
    public String getDescription() {
        return "Zeigt mathematisches Objekt in Fenster an." + TAG_PREAMBLE + "[UI] [Math] show";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ShowMathObjectWindow";
    }

    @Override
    public String getIconName() {
        return "Show-Math-Object_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        Object m = io.in0(0);
        if (m instanceof Number) {
            m = new NumberMathObject((Number) m);
        }

        SmartIdentifier smartIdentifier = (SmartIdentifier) io.in0(1);

        if (m != null) {
            api.displayContentInWindow(m, smartIdentifier);
        } else {
            api.printErr("Wert ist null. Es wird nichts angezeigt.");
        }

        io.out(0, smartIdentifier);

    }

}
