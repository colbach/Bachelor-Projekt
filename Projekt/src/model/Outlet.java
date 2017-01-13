package model;

import model.type.Type;
import exceptions.WrongTypException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import model.type.CompatibilityTest;

public class Outlet extends InOutlet {

    private ArrayList<Inlet> connectedInlets = new ArrayList<>();
    
    private static final AtomicLong instanceCounter = new AtomicLong(0);
    private long identifier = 0; // Wird fuer Debugging verwendet

    public Outlet(Type typ, String name, Node node, int outletIndex) {
        super(typ, name, node, outletIndex);
        this.identifier = instanceCounter.incrementAndGet();
    }

    public boolean canConnectToInlet(Inlet inlet) {
        return CompatibilityTest.canTypeConnectToType(getType(), inlet.getType()) && !connectedInlets.contains(inlet);
    }

    private synchronized void setInlet(int index, Inlet inlet) {
        if (connectedInlets.size() == index) {
            connectedInlets.add(inlet);
        } else if (connectedInlets.size() < index) {
            System.err.println("Index darf nicht groesser als connectedInlets.size() sein! Benutze connectedInlets.add(Inlet) anstelle von set.");
            connectedInlets.add(inlet);
        } else {
            Inlet oldConnectedInlet = connectedInlets.set(index, inlet);
            oldConnectedInlet.unconnectOutlet(this);
        }
    }

    protected synchronized void unconnectInlet(Inlet inlet) {
        connectedInlets.remove(inlet);
    }

    /*public synchronized void unconnectInlet(int index) {
        if (connectedInlets.size() > index) {
            connectedInlets.remove(index);
        } else {
            System.err.println("index " + index + " existiert nicht!");
        }
    }*/

    public synchronized void unconnectInletBilateral(int index) {
        if (connectedInlets.size() > index) {
            Inlet removedInlet = connectedInlets.remove(index);
            removedInlet.unconnectOutlet(this);
        } else {
            System.err.println("index " + index + " existiert nicht!");
        }
    }
    
    public synchronized void unconnectAllInletBilateral() {
        for(int i=getConnectedLetsCount()-1; i>=0; i--) {
            unconnectInletBilateral(i);
        }
    }

    public synchronized void connectInlet(Inlet inlet, int index) throws WrongTypException {
        if (canConnectToInlet(inlet)) {
            setInlet(index, inlet);
        } else {
            throw new WrongTypException();
        }
    }

    public int getIndexForConnectedInletInList(Inlet inlet) {
        for (int i = 0; i < connectedInlets.size(); i++) {
            if (connectedInlets.get(i) == inlet) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isConnected() {
        return connectedInlets.size() > 0;
    }

    @Override
    public synchronized boolean isConnectedTo(InOutlet let) {
        if (let instanceof Outlet) {
            return false;
        }
        for (Inlet connectedInlet : connectedInlets) {
            if (connectedInlet == let) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Inlet[] getConnectedLets() {
        return connectedInlets.toArray(new Inlet[0]);
    }

    @Override
    public int getConnectedLetsCount() {
        return connectedInlets.size();
    }

    @Override
    public boolean canConnectToMultipleLets() {
        return true; // Kann immer zu mehreren Inlets Connecten.
    }

}
