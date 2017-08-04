package reflection.nodedefinitions;

import java.io.File;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class _TestElementB implements NodeDefinition {


    @Override
    public boolean isInletEngaged(int inletIndex) {
        return false;
    }
    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        return File.class;
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
        return File.class;
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
        return "Testbaustein B";
    }

    @Override
    public String getDescription() {
        return "Ich löse einen gemeinen Fehler aus." + TAG_PREAMBLE + "Fehler Exception";
    }

    @Override
    public String getUniqueName() {
        return "BuildIn.TestB";
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

        File f = (File) io.in0(0, new File("/"));
        
        System.out.println("File: " + f);
        
        io.out(0, new File("/"));
        
        //throw new RuntimeException("Ich bin ein gemeiner Fehler!");
    }

}
