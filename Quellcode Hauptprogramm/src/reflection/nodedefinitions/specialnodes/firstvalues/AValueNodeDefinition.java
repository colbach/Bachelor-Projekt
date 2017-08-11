package reflection.nodedefinitions.specialnodes.firstvalues;

import reflection.API;
import reflection.InOut;

public class AValueNodeDefinition extends FirstValueNodeDefinition {

    @Override
    public String getIconName() {
        return "ui/A-Value_30px.png";
    }
    
    @Override
    public String getName() {
        return "Ein Wert";
    }

    @Override
    public String getDescription() {
        return "Dieses Element unterscheidet sich dahingehend von \"Schnellster Wert\" (und auch allen anderen Elementen) dass die Eingänge NICHT angestossen werden."
                + TAG_PREAMBLE + " [Basics] erster ein wert oder first value if zusammenführen zusammenfuehren";
    }

    @Override
    public String getUniqueName() {
        return "special.AValue";
    }
    
}
