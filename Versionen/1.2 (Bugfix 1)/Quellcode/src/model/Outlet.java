package model;

import model.type.Type;
import model.type.WrongTypException;
import java.util.ArrayList;
import model.type.CompatibilityTest;

public class Outlet extends InOutlet {

    private ArrayList<Inlet> connectedInlets = new ArrayList<>();
    
    public Outlet(long id) {
        super(id);
    }
    
    public Outlet(Node node, int outletIndex) {
        super(node, outletIndex);
    }

    public boolean canConnectToInlet(Inlet inlet) {
        boolean canTypeConnectToType = CompatibilityTest.canTypeConnectToType(getType(), inlet.getType());
        boolean notContainsInlet = !connectedInlets.contains(inlet);
        return canTypeConnectToType && notContainsInlet;
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
        Node node = getNode();
        if (node != null) {
            node.getDependingProject().somethingChanged();
        }
    }

    protected synchronized void unconnectInlet(Inlet inlet) {
        connectedInlets.remove(inlet);
        Node node = getNode();
        if (node != null) {
            node.getDependingProject().somethingChanged();
        }
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
        Node node = getNode();
        if (node != null) {
            node.getDependingProject().somethingChanged();
        }
    }
    
    public synchronized void unconnectAllInletBilateral() {
        for(int i=getConnectedLetsCount()-1; i>=0; i--) {
            unconnectInletBilateral(i);
        }
        Node node = getNode();
        if (node != null) {
            node.getDependingProject().somethingChanged();
        }
    }

    public synchronized void connectInlet(Inlet inlet, int index) throws WrongTypException {
        if (canConnectToInlet(inlet)) {
            setInlet(index, inlet);
        } else {
            throw new WrongTypException();
        }
        Node node = getNode();
        if (node != null) {
            node.getDependingProject().somethingChanged();
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
    public Inlet getConnectedLet(int index) {
        if(index >= connectedInlets.size() || index < 0) {
            return null;
        }
        return connectedInlets.get(index);
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

    public void setConnectedInlets(ArrayList<Inlet> connectedInlets) {
        this.connectedInlets = connectedInlets;
        Node node = getNode();
        if (node != null) {
            node.getDependingProject().somethingChanged();
        }
    }
    
    /**
     * Gibt Namen zurueck. (nie null)
     */
    
    @Override
    public String getName() {
        String outletName = getNode().getOutletName(getLetIndex());
        if(outletName == null) {
            //System.err.println("Node " + getNode().getName() + " gibt bei getOutletName(" + getLetIndex() + ") null zurueck.");
            return "";
        } else {
            return outletName;
        }
    }

    @Override
    public Type getType() {
        return getNode().getOutletType(getLetIndex());
    }
    
}
