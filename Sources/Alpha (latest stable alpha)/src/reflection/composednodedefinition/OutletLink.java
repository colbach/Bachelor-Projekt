package reflection.composednodedefinition;

import reflection.NodeDefinition;

public class OutletLink {

    private final String name;
    private final NodeDefinition nodefDefinition;
    private final int oultetIndex;

    public OutletLink(NodeDefinition nodefDefinition, int oultetIndex) {
        this(nodefDefinition.getNameForOutlet(oultetIndex), nodefDefinition, oultetIndex);
    }

    public OutletLink(String name, NodeDefinition nodefDefinition, int oultetIndex) {
        this.name = name;
        this.nodefDefinition = nodefDefinition;
        this.oultetIndex = oultetIndex;
    }

    public String getName() {
        return name;
    }

    public NodeDefinition getNodefDefinition() {
        return nodefDefinition;
    }

    public int getOultetIndex() {
        return oultetIndex;
    }

}
