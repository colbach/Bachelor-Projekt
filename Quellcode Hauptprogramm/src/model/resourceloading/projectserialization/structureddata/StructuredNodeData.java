package model.resourceloading.projectserialization.structureddata;

import java.util.HashMap;
import model.Inlet;
import model.Outlet;

public class StructuredNodeData {
    
    public long id;
    
    public long[] inlets;
    
    public long[] outlets;
    
    public String definitionUniqueNameVersion;
    
    public int uiCenterX;
    
    public int uiCenterY;
    
    public String[] settabledata;

    public StructuredNodeData(long id, long[] inlets, long[] outlets, String definitionUniqueNameVersion, int uiCenterX, int uiCenterY) {
        this.id = id;
        this.inlets = inlets;
        this.outlets = outlets;
        this.definitionUniqueNameVersion = definitionUniqueNameVersion;
        this.uiCenterX = uiCenterX;
        this.uiCenterY = uiCenterY;
    }

    public StructuredNodeData() {
    }
    
}
