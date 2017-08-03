package reflection.composednodedefinition;

import reflection.NodeDefinition;

public class DefaultDataForInlet {
    
    private final Object[] data;
    private final NodeDefinition nodeDefinition;
    private final int inletIndex;

    public DefaultDataForInlet(Object[] data, NodeDefinition nodeDefinition, int inletIndex) {
        this.data = data;
        this.nodeDefinition = nodeDefinition;
        this.inletIndex = inletIndex;
    }

    public Object[] getData() {
        return data;
    }

    public NodeDefinition getNodeDefinition() {
        return nodeDefinition;
    }

    public int getInletIndex() {
        return inletIndex;
    }
    
}
