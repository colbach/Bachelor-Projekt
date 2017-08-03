package reflection.nodedefinitions;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class _TestElementC implements NodeDefinition {


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
        return "Testbaustein C";
    }

    @Override
    public String getDescription() {
        return "Ich bin müde, ich schlafe ein wenig.";
    }

    @Override
    public String getUniqueName() {
        return "BuildIn.TestC";
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
        
        try {
            Thread.sleep((int) Math.random() * 2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        io.out(4, "Fertig");
    }
    
}
