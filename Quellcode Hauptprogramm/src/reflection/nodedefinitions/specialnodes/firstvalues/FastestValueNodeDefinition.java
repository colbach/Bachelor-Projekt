package reflection.nodedefinitions.specialnodes.firstvalues;

import reflection.API;
import reflection.InOut;

public class FastestValueNodeDefinition extends FirstValueNodeDefinition {

    @Override
    public String getIconName() {
        return "ui/Fastest-Value_30px.png";
    }
    
    @Override
    public String getName() {
        return "Schnellster-Wert";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Basics] erster wert oder first value fastest schnellster schnelster";
    }

    @Override
    public String getUniqueName() {
        return "special.FastestValue";
    }
    
}
