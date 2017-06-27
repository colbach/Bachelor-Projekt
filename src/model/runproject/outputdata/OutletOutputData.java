package model.runproject.outputdata;

import java.util.HashMap;
import java.util.Set;
import model.Outlet;

public class OutletOutputData {
    
    private final HashMap<Outlet, Object[]> outletData;

    public OutletOutputData() {
        this.outletData = new HashMap<>();
    }
    
    public OutletOutputData(Outlet outlet, Object[] data) {
        this();
        put(outlet, data);
    }

    /**
     * Kopier-Konstruktor.
     */
    public OutletOutputData(OutletOutputData other) {
        this.outletData = (HashMap<Outlet, Object[]>) other.outletData.clone();
    }
    
    public synchronized void put(Outlet outlet, Object[] data) {
        outletData.put(outlet, data);
    }
    
    public synchronized Object[] get(Outlet outlet) {
        return outletData.get(outlet);
    }

    public synchronized int size() {
        return outletData.size();
    }
    
    public synchronized Set<Outlet> getOutlets() {
        return outletData.keySet();
    }

    @Override
    public synchronized String toString() {
        String keys = "";
        boolean first = true;
        for(Outlet outlet : outletData.keySet()) {
            if(!first) {
                keys += ", ";
            }
            keys += outlet.getName() + "(" + outlet.getNode().getName() + ")";
            first = false;
        }
        
        return "OutletOutputData { keys: " + keys + " }";
    }
    
    public synchronized void clear() {
        outletData.clear();
    }
}
