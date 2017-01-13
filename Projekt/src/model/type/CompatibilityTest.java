package model.type;

public class CompatibilityTest {

    /**
     * Gibt an ob Typen verbunden werden koennen. Muss auf Type von Outlet
     * ausgefuert werden. Achtung: Reihenfolge ist entscheident da Typen nicht
     * in alle Richtungen verbunden werden koennen.
     */
    public static boolean canTypeConnectToType(Type from, Type to) {

        if(from.getTypeClass() == to.getTypeClass()) { // Der Typ ist gleich
            return true;
            
        } else if(to.getTypeClass().isAssignableFrom(from.getTypeClass())) { // to ist Superklasse von from
            return true;
            
        } else if(from.getTypeClass().isAssignableFrom(to.getTypeClass())) { // from ist Superklasse von to
            return true;
            
        } else if(ExtendedCompatibility.typeCanConnectToTypeViaExtendedCompatibility(from, to)) { // from und to werden über die ExtendedCompatibility-Klasse kompartibel
            return true;
            
        } else {
            return false;
        }

    }

}
