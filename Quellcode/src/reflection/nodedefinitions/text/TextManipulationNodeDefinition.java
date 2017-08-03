package reflection.nodedefinitions.text;

import java.util.ArrayList;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class TextManipulationNodeDefinition implements NodeDefinition {
    
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
                return "Text";
            default:
                return null;
        }
    }
    
    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return true;
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
                return String.class;
            case 1:
                return String.class;
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
                return "Uppercase";
            case 1:
                return "Lowercase";
            case 2:
                return "Trim";
            default:
                return null;
        }
    }
    
    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            case 1:
                return true;
            case 2:
                return true;
            default:
                return false;
        }
    }
    
    @Override
    public String getName() {
        return "Text-Manipulation";
    }
    
    @Override
    public String getDescription() {
        return "Bietet verschiedene Textoperationen an." + TAG_PREAMBLE + " [Text]";
    }
    
    @Override
    public String getUniqueName() {
        return "buildin.TextManipulation";
    }
    
    @Override
    public String getIconName() {
        return "String-Manipulation_30px.png";
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public void run(InOut io, API api) {
        
        Object[] texts = io.in(0, new String[0]);
        
        ArrayList<String> uppercase = new ArrayList<>();
        ArrayList<String> lowercase = new ArrayList<>();
        ArrayList<String> trim = new ArrayList<>();
        
        for (Object o : texts) {
            if (io.outConnected(0)) {
                uppercase.add(o.toString().toUpperCase());
            }
            if (io.outConnected(1)) {
                lowercase.add(o.toString().toLowerCase());
            }
            if (io.outConnected(2)) {
                trim.add(o.toString().trim());
            }
        }
        
        io.out(0, uppercase.toArray());
        io.out(1, lowercase.toArray());
        io.out(2, trim.toArray());
    }
    
}
