package model.runproject.inputdata;

import utils.structures.tuples.Pair;
import java.util.*;
import model.*;
import utils.structures.*;

public class CollectRequest {
    
    private HashSet<Pair<Inlet, Integer>> inletsForCollect;

    public CollectRequest() {
        this.inletsForCollect = new HashSet<>();
    }
    
    public CollectRequest(Pair<Inlet, Integer>[] inletsForCollect) {
        this();
        this.inletsForCollect.addAll(Arrays.asList(inletsForCollect));
    }
    
    public void add(Inlet inlet, int index) {
        inletsForCollect.add(new Pair<Inlet, Integer>(inlet, index));
    }
    
    public void add(CollectRequest otherRequest) {
        for(Pair<Inlet, Integer> inletForCollect : otherRequest.inletsForCollect) {
            inletsForCollect.add(inletForCollect);
        }
    }
    
    public boolean contains(Inlet inlet, int index) {
        return inletsForCollect.contains(new Pair<Inlet, Integer>(inlet, index));
    }

    public HashSet<Pair<Inlet, Integer>> getInletsWithIndexsForCollect() {
        return inletsForCollect;
    }

    @Override
    public String toString() {
        String ins = "";
        boolean first = true;
        for(Pair<Inlet, Integer> inlet : inletsForCollect) {
            if(!first) {
                ins+= ", ";
            }
            ins += inlet.l.getName() + ":" + inlet.r + "(" + inlet.l.getNode().getName() + ")";
            first = false;
        }
        
        return "CollectRequest { inlets: " + ins + " }";
    }
    
}
