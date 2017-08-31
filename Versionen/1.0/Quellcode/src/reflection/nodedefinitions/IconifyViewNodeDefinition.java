package reflection.nodedefinitions;

import java.awt.Component;
import java.awt.Frame;
import javax.swing.JFrame;
import reflection.*;

public class IconifyViewNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        return Boolean.class;
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Minimiert";
            case 1:
                return "Normal";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
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
        return "Hauptfenster Zustand setzen";
    }

    @Override
    public String getDescription() {
        return "Setzt Zustand von Hauptfenster. Diese Element kann beispielsweise verwendet werden um das Hauptfenster automatisch zu verbergen und wieder aanzuzeigen." + TAG_PREAMBLE + "[UI] minimieren Zustand";
    }

    @Override
    public String getUniqueName() {
        return "buildin.IconifyView";
    }

    @Override
    public String getIconName() {
        return "Iconify-View_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        Component view = api.getView();

        if (view != null || view instanceof JFrame) {

            if ((Boolean) io.in0(0, Boolean.TRUE)) {
                ((JFrame) view).setExtendedState(Frame.ICONIFIED);

            } else if ((Boolean) io.in0(0, Boolean.FALSE)) {
                ((JFrame) view).setExtendedState(Frame.NORMAL);
            }
            
        } else {
            api.printErr("Hauptfenster kann nicht minimiert werden.");
        }

    }

}
