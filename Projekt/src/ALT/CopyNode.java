package ALT;

import model.GeneralType;
import model.Node;
import model.type.Type;
import model.resourceloading.UniqueNameVersionGenerator;
import reflection.NodeDefinition;

public class CopyNode extends Node {

    private Type valueType = new GeneralType();
    
    private static String UNIQUE_NAME = "BuildIn.CopyNode";
    private static int VERSION = 1;
    public static String UNIQUE_NAME_VERSION = UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion(UNIQUE_NAME, VERSION);
    
    public CopyNode(int uiCenterX, int uiCenterY) {
        super(null, uiCenterX, uiCenterY);
        
        definition = new NodeDefinition() {
            @Override
            public int getInletCount() {
                return 1;
            }

            @Override
            public Class getClassForInlet(int inletIndex) {
                return valueType.getTypeClass();
            }

            @Override
            public boolean isInletForArray(int inletIndex) {
                return valueType.isArray();
            }

            @Override
            public int getOutletCount() {
                return 2;
            }

            @Override
            public Class getClassForOutlet(int outletIndex) {
                return valueType.getTypeClass();
            }

            @Override
            public boolean isOutletForArray(int outletIndex) {
                return valueType.isArray();
            }

            @Override
            public String getName() {
                return "Kopie";
            }

            @Override
            public void run(NodeDefinition.InOut io) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getNameForInlet(int inletIndex) {
                switch(inletIndex) {
                    case 0: return "Daten";
                }
                throw new IllegalArgumentException();
            }

            @Override
            public String getNameForOutlet(int outletIndex) {
                switch(outletIndex) {
                    case 0: return "Daten";
                    case 1: return "Kopie";
                }
                throw new IllegalArgumentException();
            }

            @Override
            public int getVersion() {
                return VERSION;
            }

            @Override
            public String getUniqueName() {
                return UNIQUE_NAME;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public String getIconName() {
                return null;
            }
        };
    }
}
