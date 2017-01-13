package model.check;

import java.util.ArrayList;
import model.Node;
import model.Outlet;

public class TypeCheck {
    
    /**
     * gibt Array mit Warnungen zurück.
     */
    /*public static CheckWarning[] checkTypesOfOutlets(Node node) {
        
        ArrayList<CheckWarning> warnings = null;
        
        for(Outlet outlet : node.getOutlets()) {
            if(!outlet.getType().canConnectToType(outlet.getOther().getType())) {
                if(warnings == null) {
                    warnings = new ArrayList<>();
                } // kein else
                warnings.add(new CheckWarning(true, "Bei der Verbindung -["
                                                    + outlet.getName()
                                                    + "]-["
                                                    + outlet.getOther().getName()
                                                    + "]- passen die Typen "
                                                    + outlet.getType()
                                                    + " und "
                                                    + outlet.getOther().getType()
                                                    + "  nicht aufeinander!", node));
            }
        }
        
        if(warnings == null) {
            return null;
        } else {
            return warnings.toArray(new CheckWarning[0]);
        }
    }*/
    
    
    
}
