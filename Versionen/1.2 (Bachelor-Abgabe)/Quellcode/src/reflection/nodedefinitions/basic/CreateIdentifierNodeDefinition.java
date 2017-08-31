package reflection.nodedefinitions.basic;

import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;
import reflection.customdatatypes.SmartIdentifier;
import reflection.common.SmartIdentifierContext;

public class CreateIdentifierNodeDefinition implements NodeDefinition {

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
                return "Identifier Name";
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
        return true;
    }

    @Override
    public int getOutletCount() {
        return 3;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return SmartIdentifier.class;
            case 1:
                return Boolean.class;
            case 2:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Identifier";
            case 1:
                return "Neu erzeugt";
            case 2:
                return "Identifier Name";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "neuer Identifier";
    }

    @Override
    public String getDescription() {
        return "Erstellt SmartIdentifier. Wenn kein Name angegeben wird, wird ein neuer erstellt." + TAG_PREAMBLE + " [Basics] ID ";
    }

    @Override
    public String getUniqueName() {
        return "buildin.basic.smartidentifiers.CreateIdentifier";
    }

    @Override
    public String getIconName() {
        return "Id_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object in0 = io.in0(0);
        String identifiername;
        Boolean neuerzeugt;
        SmartIdentifier smartIdentifier;
        SmartIdentifierContext smartIdentifierContext = api.getSmartIdentifierContext();

        if (in0 == null) {
            neuerzeugt = true;
            smartIdentifier = api.getSmartIdentifierContext().createNew();
        } else {
            identifiername = (String) in0;
            synchronized (SmartIdentifier.class) {
                if (smartIdentifierContext.doesSmartIdentifierExist(identifiername)) {
                    neuerzeugt = false;
                } else {
                    neuerzeugt = true;
                }
                smartIdentifier = smartIdentifierContext.getOrCreate(identifiername);
            }
        }

        io.out(0, smartIdentifier);
        io.out(1, neuerzeugt);
        io.out(2, smartIdentifier.getIdentifierName());

    }

}
