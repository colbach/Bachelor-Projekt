package reflection.nodedefinitions;

import java.io.File;
import java.nio.file.Path;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class _TestElementA implements NodeDefinition {


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
        return Path.class;
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
        return Path.class;
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
        return "Testbaustein A";
    }

    @Override
    public String getDescription() {
        return "Ich benutze eine externe Klasse um Hallo auszugeben.";
    }

    @Override
    public String getUniqueName() {
        return "buildin.TestA";
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
        
        _ExterneTestKlasse.hallo();
        
        Path p = (Path) io.in0(0, new File("/").toPath());
        
        System.out.println("Path: " + p);
        
        io.out(0, new File("/").toPath());
    }
    
}
