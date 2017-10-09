package reflection.nodedefinitions.functions;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import reflection.nodedefinitionsupport.functions.FunctionCallToken;

public class FunctionReturnNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Object.class;
            case 1:
                return FunctionCallToken.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Rückgabe";
            case 1:
                return "Token";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            case 1:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
        if (i == 1) {
            return true;
        }
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
        return "Funktions-Rückgabe";
    }

    @Override
    public String getDescription() {
        return "Gibt Daten als Rückgabe von Funktion zurück." + TAG_PREAMBLE + " [Funktionen] [Events] funktions aufruf neu definieren id identifier call return rückgabe rueckgabe ende";
    }

    @Override
    public String getUniqueName() {
        return "buildin.customfunction.FunctionReturn";
    }

    @Override
    public String getIconName() {
        return "Function-Return_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] rckgabe = io.in(0, new Object[0]);
        FunctionCallToken token = (FunctionCallToken) io.in0(1, null);

        if (token != null) {
            token.deliever(rckgabe);
        } else {
            api.printErr("token ist nicht gesetzt.");
        }
    }

}
