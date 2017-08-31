package reflection.composednodedefinition;

import reflection.common.NodeDefinition;

public class InletLink {

    private final String name;
    private final NodeDefinition nodeDefinition;
    private final int inletIndex;
    
    public InletLink(NodeDefinition nodefDefinition, int inletIndex) {
        this(nodefDefinition.getNameForInlet(inletIndex), nodefDefinition, inletIndex);
    }

    public InletLink(String name, NodeDefinition nodefDefinition, int inletIndex) {
        this.name = name;
        this.nodeDefinition = nodefDefinition;
        this.inletIndex = inletIndex;
    }

    public String getName() {
        return name;
    }

    public NodeDefinition getNodeDefinition() {
        return nodeDefinition;
    }

    public int getInletIndex() {
        return inletIndex;
    }
}
