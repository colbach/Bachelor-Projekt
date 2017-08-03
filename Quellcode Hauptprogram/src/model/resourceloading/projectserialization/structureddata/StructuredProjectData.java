package model.resourceloading.projectserialization.structureddata;

import java.util.ArrayList;
import java.util.Date;
import model.Inlet;
import model.Node;
import model.Outlet;
import model.Project;
import utils.ObjectSerializer;

public class StructuredProjectData {
    
    public StructuredNodeData[] nodes;
    
    public StructuredInletData[] inlets;
    
    public StructuredOutletData[] outlets;
    
    public StructuredProjectData(Project project) {
        Node[] nodes = project.getNodes();

        ArrayList<StructuredNodeData> structuredNodeDatas = new ArrayList<>();
        ArrayList<StructuredInletData> structuredInletDatas = new ArrayList<>();
        ArrayList<StructuredOutletData> structuredOutletDatas = new ArrayList<>();

        for (Node node : nodes) {

            Inlet[] inlets = node.getInlets();
            Outlet[] outlets = node.getOutlets();
            long[] inletIds = new long[inlets.length];
            long[] outletIds = new long[outlets.length];

            for (int i1 = 0; i1 < inlets.length; i1++) {
                Inlet inlet = inlets[i1];
                long id = inlet.getIdentifier();
                inletIds[i1] = id;
                Outlet[] connectedOutlets = inlet.getConnectedLets();
                long[] connectedOutletIds = new long[connectedOutlets.length];
                for (int i2 = 0; i2 < connectedOutlets.length; i2++) {
                    connectedOutletIds[i2] = connectedOutlets[i2].getIdentifier();
                }
                StructuredInletData structuredInletData = new StructuredInletData(id, connectedOutletIds);
                structuredInletDatas.add(structuredInletData);
            }

            for (int i1 = 0; i1 < outlets.length; i1++) {
                Outlet outlet = outlets[i1];
                long id = outlet.getIdentifier();
                outletIds[i1] = id;
                Inlet[] connectedInlets = outlet.getConnectedLets();
                long[] connectedInletIds = new long[connectedInlets.length];
                for (int i2 = 0; i2 < connectedInlets.length; i2++) {
                    connectedInletIds[i2] = connectedInlets[i2].getIdentifier();
                }
                StructuredOutletData structuredOutletData = new StructuredOutletData(id, connectedInletIds);
                structuredOutletDatas.add(structuredOutletData);
            }

            StructuredNodeData structuredNodeData = new StructuredNodeData(node.getIdentifier(), inletIds, outletIds, node.getUniqueNameVersion(), node.getUICenterX() /*+ (int)(Math.random()*20)*/, node.getUICenterY() /*+ (int)(Math.random()*20)*/);
            
            if(node.isDirectInputNode()) {
                Object[] directInput = node.getUserSettableContent();
                String[] base64a = ObjectSerializer.serializeObjectsToBase64Array(directInput);
                structuredNodeData.settabledata = base64a;
            }
            
            structuredNodeDatas.add(structuredNodeData);
        }

        this.nodes = structuredNodeDatas.toArray(new StructuredNodeData[0]);
        this.inlets = structuredInletDatas.toArray(new StructuredInletData[0]);
        this.outlets = structuredOutletDatas.toArray(new StructuredOutletData[0]);
    }

    public StructuredProjectData() {
    }
    
}
