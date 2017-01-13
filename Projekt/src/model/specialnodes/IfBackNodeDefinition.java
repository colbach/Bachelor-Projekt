package model.specialnodes;

import reflection.NodeDefinition;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class IfBackNodeDefinition implements IfNodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        switch (inletIndex) {
            case 0:
                return Object.class;
            case 1:
                return Object.class;
            case 2:
                return Boolean.class;
            default:
                throw new IllegalArgumentException("inletIndex muss kleiner 3 sein.");
        }
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        switch (inletIndex) {
            case 0:
                return "Falls Wahr";
            case 1:
                return "Falls Unwahr";
            case 2:
                return "Wahrheitswert";
            default:
                throw new IllegalArgumentException("inletIndex muss kleiner 3 sein.");
        }
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        switch (inletIndex) {
            case 0:
                return true;
            case 1:
                return true;
            case 2:
                return false;
            default:
                throw new IllegalArgumentException("inletIndex muss kleiner 3 sein.");
        }
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int outletIndex) {
        return Object.class;
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        return "Objekt";
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return true;
    }

    @Override
    public String getName() {
        return "Wenn-Vor";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUniqueName() {
        return "special.IfBack";
    }

    @Override
    public String getIconName() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void run(InOut io) {
        throw new RuntimeException("runn darf nicht auf Objekte der Klasse IfBackNodeDefinition aufgerufen werden");
    }
    
}
