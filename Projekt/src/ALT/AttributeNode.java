package ALT;

import model.GeneralType;
import model.Node;
import model.type.Type;
import model.resourceloading.UniqueNameVersionGenerator;
import reflection.NodeDefinition;

public class AttributeNode extends Node {
    
    private Type startValueType = new GeneralType();
    private String attributeName = "neues Attribut";
    
    private static String UNIQUE_NAME = "BuildIn.AttributNode";
    private static int VERSION = 1;
    public static String UNIQUE_NAME_VERSION = UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion(UNIQUE_NAME, VERSION);

    public AttributeNode(int uiCenterX, int uiCenterY) {
        super(null, uiCenterX, uiCenterY);
        
        definition = new NodeDefinition() {
            @Override
            public int getInletCount() {
                return 2;
            }

            @Override
            public Class getClassForInlet(int inletIndex) {
                return startValueType.getTypeClass();
            }

            @Override
            public boolean isInletForArray(int inletIndex) {
                return startValueType.isArray();
            }

            @Override
            public int getOutletCount() {
                return 1;
            }

            @Override
            public Class getClassForOutlet(int outletIndex) {
                return startValueType.getTypeClass();
            }

            @Override
            public boolean isOutletForArray(int outletIndex) {
                return startValueType.isArray();
            }

            @Override
            public void run(NodeDefinition.InOut io) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getName() {
                return attributeName;
            }

            @Override
            public String getNameForInlet(int inletIndex) {
                switch(inletIndex) {
                    case 0: return "Startwert";
                    case 1: return "Neuer Wert";
                }
                throw new IllegalArgumentException();
            }

            @Override
            public String getNameForOutlet(int outletIndex) {
                switch(outletIndex) {
                    case 0: return "";
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
