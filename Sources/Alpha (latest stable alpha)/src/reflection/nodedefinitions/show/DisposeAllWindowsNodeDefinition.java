package reflection.nodedefinitions.show;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;
import reflection.SmartIdentifier;

public class DisposeAllWindowsNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Alle Kontexte";
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
    public boolean isInletEngaged(int i) {
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
        return "Alle Fenster verbergen";
    }

    @Override
    public String getDescription() {
        return "Verbirgt alle offenen Fenster. Wenn <Alle Kontexte> gesetzt ist werden auch Fenster anderer Ausführungskontexte geschlossen." + TAG_PREAMBLE + " [UI] schliessen close Fenster Window dispose schliesen verbergen x alle";
    }

    @Override
    public String getUniqueName() {
        return "buildin.show.DisposeAllWindows";
    }

    @Override
    public String getIconName() {
        return "Close-All-Windows_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Boolean allekontexte = (Boolean) io.in0(0, false);

        api.disposeAllWindows(true);
    }

}
