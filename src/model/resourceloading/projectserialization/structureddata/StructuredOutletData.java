package model.resourceloading.projectserialization.structureddata;

public class StructuredOutletData {
    
    public long id;
    
    public long[] connectedInlets;

    public StructuredOutletData(long id, long[] connectedInlets) {
        this.id = id;
        this.connectedInlets = connectedInlets;
    }

    public StructuredOutletData() {
    }
    
}
