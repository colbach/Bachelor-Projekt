package reflection.composednodedefinition;

import reflection.common.NodeDefinition;

public class BidirectionalLink {

    private final NodeDefinition fromNodeDefinition;
    private final int fromOutletIndex;
    private final NodeDefinition toNodeDefinition;
    private final int toInletIndex;

    public BidirectionalLink(NodeDefinition fromNodeDefinition, int fromOutletIndex, NodeDefinition toNodeDefinition, int toInletIndex) {
        this.fromNodeDefinition = fromNodeDefinition;
        this.fromOutletIndex = fromOutletIndex;
        this.toNodeDefinition = toNodeDefinition;
        this.toInletIndex = toInletIndex;
    }

    public NodeDefinition getFromNodeDefinition() {
        return fromNodeDefinition;
    }

    public int getFromOutletIndex() {
        return fromOutletIndex;
    }

    public NodeDefinition getToNodeDefinition() {
        return toNodeDefinition;
    }

    public int getToInletIndex() {
        return toInletIndex;
    }

}
