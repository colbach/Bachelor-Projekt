package model;

import model.type.Type;
import model.type.WrongTypException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import logging.AdditionalErr;
import model.type.CompatibilityTest;

public class Inlet extends InOutlet {

    /**
     * Liste verbundener Outlets. Wenn Typ von Inlet kein Array-Typ ist, ist
     * size von connectedOutlets 0 oder 1.
     */
    private ArrayList<Outlet> connectedOutlets = new ArrayList<>();

    public Inlet(long id) {
        super(id);
    }

    public Inlet(Node node, int inletIndex) {
        super(node, inletIndex);
    }

    public boolean canConnectToOutlet(Outlet outlet) {

        if (CompatibilityTest.canTypeConnectToType(outlet.getType(), getType())) { // Wenn outlet auf diesen Type connecten kann

            if (!connectedOutlets.contains(outlet)) { // Wenn nicht bereits dieses Outlet verbunden ist
                return true;

            } else { // Outlet bereits verbunden
                System.err.println("Outlet bereits verbunden");
                return false;
            }
        }
        return false;
    }

    private synchronized void setOutlet(int index, Outlet outlet) {
        if (connectedOutlets.size() == index) {
            connectedOutlets.add(outlet);

        } else if (connectedOutlets.size() < index) {
            System.err.println("Index darf nicht groesser als connectedOutlets.size() sein! Benutze connectedOutlets.add(Outlet) anstelle von set.");
            connectedOutlets.add(outlet);

        } else {
            Outlet oldConnectedOutlet = connectedOutlets.set(index, outlet);
            oldConnectedOutlet.unconnectInlet(this);
        }

        Node node = getNode();
        if (node != null) {
            node.getDependingProject().somethingChanged();
        }
    }

    public boolean isOptional() {
        
        Node node = getNode();
        int letIndex = getLetIndex();

        if (node == null) {
            System.err.println("node von Inlet ist null");
            return true;

        } else if (letIndex == -1) {
            System.err.println("letIndex ist -1");
            return true;

        } else {
            if (node.getDefinition().getInletCount() <= letIndex) {
                return true;
            } else {
                return !node.getDefinition().isInletEngaged(letIndex);
            }
        }
    }

    public synchronized void connectOutlet(Outlet outlet, int index) throws WrongTypException {
        if (!getType().isArray() && canConnectToOutlet(outlet)) { // Fall kein Array

            if (getIndexForConnectedOutletInList(outlet) != -1) { // Check ob outlet vieleicht schon verbunden...
                System.err.println("Ausgang bereits verbunden. Ausgang wird nicht noch einmal verbunden.");
            } else { // index ignorieren und Ausgang auf Index 0 setzen / ueberschreiben...
                setOutlet(0, outlet);
                if (index > 0) {
                    System.err.println("Achtung Inlet unterstuetzt keine Arrays. Index wird auf 0 gesetzt.");
                }
            }

        } else if (canConnectToOutlet(outlet)) {
            setOutlet(index, outlet);

        } else {
            throw new WrongTypException();
        }
    }

    public synchronized void unconnectOutlet(Outlet outlet) {
        connectedOutlets.remove(outlet);
        Node node = getNode();
        if (node != null) {
            node.getDependingProject().somethingChanged();
        }
    }

    /*public synchronized void unconnectOutlet(int index) {
        if (connectedOutlets.size() > index) {
            connectedOutlets.remove(index);
        } else {
            System.err.println("index " + index + " existiert nicht!");
        }
    }*/
    public synchronized void unconnectOutletBilateral(int index) {
        if (connectedOutlets.size() > index) {
            Outlet removedOutlet = connectedOutlets.remove(index);
            removedOutlet.unconnectInlet(this);
        } else {
            System.err.println("index " + index + " existiert nicht!");
        }
        Node node = getNode();
        if (node != null) {
            node.getDependingProject().somethingChanged();
        }
    }

    public synchronized void unconnectAllOutletBilateral() {
        for (int i = getConnectedLetsCount() - 1; i >= 0; i--) {
            unconnectOutletBilateral(i);
        }
        Node node = getNode();
        if (node != null) {
            node.getDependingProject().somethingChanged();
        }
    }

    public synchronized int getIndexForConnectedOutletInList(InOutlet let) {
        if (let instanceof Outlet) {
            return getIndexForConnectedOutletInList((Outlet) let);
        } else {
            System.err.println("let muss ein Outlet sein damit dieser in Liste gefunden werden kann.");
            return -1;
        }
    }

    /**
     * Gibt Index von fuer verbundenes Outlet zuruek oder -1 falls dieses Outlet
     * nicht in connectedOutlets vorhanden.
     */
    public synchronized int getIndexForConnectedOutletInList(Outlet outlet) {
        for (int i = 0; i < connectedOutlets.size(); i++) {
            if (connectedOutlets.get(i) == outlet) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public synchronized boolean isConnected() {
        return connectedOutlets.size() > 0;
    }

    @Override
    public synchronized boolean isConnectedTo(InOutlet let) {
        if (let instanceof Inlet) {
            return false;
        }
        for (Outlet connectedOutlet : connectedOutlets) {
            if (connectedOutlet == let) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized Outlet getConnectedLet(int index) {
        return connectedOutlets.get(index);
    }

    @Override
    public synchronized Outlet[] getConnectedLets() {
        return connectedOutlets.toArray(new Outlet[0]);
    }

    @Override
    public synchronized int getConnectedLetsCount() {
        return connectedOutlets.size();
    }

    @Override
    public synchronized boolean canConnectToMultipleLets() {
        return getType().isArray(); // Kann nur zu mehreren Outletsverbinden wenn Typ ein Array-Typ ist.
    }

    public void setConnectedOutlets(ArrayList<Outlet> connectedOutlets) {
        this.connectedOutlets = connectedOutlets;
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
        String inletName = getNode().getInletName(getLetIndex());
        if (inletName == null) {
            System.err.println("Node " + getNode().getName() + " gibt bei getInletName(" + getLetIndex() + ") null zurueck.");
            return "?";
        } else {
            return inletName;
        }
    }

    @Override
    public Type getType() {
        return getNode().getInletType(getLetIndex());
    }

}
