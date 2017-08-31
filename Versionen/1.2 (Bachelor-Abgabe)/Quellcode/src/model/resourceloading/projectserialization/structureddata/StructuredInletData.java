package model.resourceloading.projectserialization.structureddata;

import model.Node;
import model.type.Type;

public class StructuredInletData {
    
    public long id;
    
    public long[] connectedOutlets;
    
    public int letIndex;

    public StructuredInletData(long id, long[] connectedOutlets) {
        this.id = id;
        this.connectedOutlets = connectedOutlets;
        this.letIndex = letIndex;
    }

    public StructuredInletData() {
    }
    
}
