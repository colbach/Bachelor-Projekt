package model.runproject.inputdata;

import java.util.*;
import model.*;
import utils.structures.*;

public class CollectRequest {
    
    private HashSet<Tuple<Inlet, Integer>> inletsForCollect;

    public CollectRequest() {
        this.inletsForCollect = new HashSet<>();
    }
    
    public CollectRequest(Tuple<Inlet, Integer>[] inletsForCollect) {
        this();
        this.inletsForCollect.addAll(Arrays.asList(inletsForCollect));
    }
    
    public void add(Inlet inlet, int index) {
        inletsForCollect.add(new Tuple<Inlet, Integer>(inlet, index));
    }
    
    public void add(CollectRequest otherRequest) {
        for(Tuple<Inlet, Integer> inletForCollect : otherRequest.inletsForCollect) {
            inletsForCollect.add(inletForCollect);
        }
    }
    
    public boolean contains(Inlet inlet, int index) {
        return inletsForCollect.contains(new Tuple<Inlet, Integer>(inlet, index));
    }

    public HashSet<Tuple<Inlet, Integer>> getInletsWithIndexsForCollect() {
        return inletsForCollect;
    }

    @Override
    public String toString() {
        String ins = "";
        boolean first = true;
        for(Tuple<Inlet, Integer> inlet : inletsForCollect) {
            if(!first) {
                ins+= ", ";
            }
            ins += inlet.l.getName() + ":" + inlet.r + "(" + inlet.l.getNode().getName() + ")";
            first = false;
        }
        
        return "CollectRequest { inlets: " + ins + " }";
    }
    
}
