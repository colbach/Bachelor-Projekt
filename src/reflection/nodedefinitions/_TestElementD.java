package reflection.nodedefinitions;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class _TestElementD implements NodeDefinition {


    @Override
    public boolean isInletEngaged(int inletIndex) {
        return false;
    }
    @Override
    public int getInletCount() {
        return 10;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        if(inletIndex == 3)
            return Date.class;
        if(inletIndex == 4)
            return Color.class;
        if(inletIndex == 5)
            return ArrayList.class;
        return String.class;
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        return "In " + inletIndex;
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        return false;
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int outletIndex) {
        return String.class;
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        return "Out " + outletIndex;
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return false;
    }

    @Override
    public String getName() {
        return "Testbaustein D";
    }

    @Override
    public String getDescription() {
        return "Ich bin gemein und gebe nichts an meine Ausgänge weiter.";
    }

    @Override
    public String getUniqueName() {
        return "BuildIn.TestD";
    }

    @Override
    public String getIconName() {
        return null;
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {
        
    }
    
}
