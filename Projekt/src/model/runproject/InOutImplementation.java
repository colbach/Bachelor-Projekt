package model.runproject;

import java.util.HashMap;
import java.util.Map;
import model.Inlet;
import reflection.NodeDefinition;

public class InOutImplementation implements NodeDefinition.InOut {

    private final HashMap<Integer, Object[]> inletIndexData;
    private final HashMap<Integer, Object[]> outletIndexData;
    private boolean canceled = false;

    public InOutImplementation(HashMap<Integer, Object[]> inletIndexData) {
        this.inletIndexData = inletIndexData;
        this.outletIndexData = new HashMap<>();
    }

    @Override
    public Object[] in(int i) {
        
        /*System.out.println("======");
        for (Map.Entry<Integer, Object[]> entry : inletIndexData.entrySet()) {
            Integer key = entry.getKey();
            Object[] value = entry.getValue();
            System.out.println("key:"+key);
            for(Object v : value) {
                System.out.println("      " + v.toString());
            }
        }
        System.out.println("------");*/
        
        if (inletIndexData.containsKey(i)) {
            return inletIndexData.get(i);
        } else {
            return null;
        }
    }

    @Override
    public Object in0(int i) {
        Object[] data = in(i);
        if (data == null) {
            return null;
        } if (data.length > 0) {
            return in(i)[0];
        } else {
            return null;
        }
    }

    @Override
    public void out(int i, Object output) {
        out(i, new Object[]{output});
    }

    @Override
    public void out(int i, Object[] output) {
        outletIndexData.put(i, output);
    }

    protected void cancel() {
        canceled = true;
    }

    @Override
    public boolean canceled() {
        return canceled;
    }

    public HashMap<Integer, Object[]> getOutletIndexData() {
        return outletIndexData;
    }

    @Override
    public boolean outConnected(int i) {
        System.err.println("inConnected(int) Nicht implementiert!");
        return true;
    }

    @Override
    public Object[] in(int i, Object[] def) {
        Object[] os = in(i);
        if(os == null)
            return def;
        else
            return os;
    }

    @Override
    public Object in0(int i, Object def) {
        Object o = in0(i);
        if(o == null)
            return def;
        else
            return o;
    }
}
