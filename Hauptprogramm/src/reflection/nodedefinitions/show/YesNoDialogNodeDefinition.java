package reflection.nodedefinitions.show;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import javax.swing.JOptionPane;

public class YesNoDialogNodeDefinition implements NodeDefinition {

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
                return "Titel";
            case 1:
                return "Mitteilung";
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
        if (index == 1) {
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
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Auswahl";
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
        return "Ja/Nein Dialog";
    }

    @Override
    public String getDescription() {
        return "Öffnet Ja/Nein-Dialog." + TAG_PREAMBLE + " [UI] input dialog";
    }

    @Override
    public String getUniqueName() {
        return "buildin.YesNoDialog";
    }

    @Override
    public String getIconName() {
        return "Yes-No-Dialog_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        String titel = (String) io.in0(0, "Meldung");
        String mitteilung = (String) io.in0(1, "");

        int n = JOptionPane.showConfirmDialog(
                null,
                mitteilung,
                titel,
                JOptionPane.YES_NO_OPTION);

        io.out(0, (Boolean) (n == 0));

    }

}
