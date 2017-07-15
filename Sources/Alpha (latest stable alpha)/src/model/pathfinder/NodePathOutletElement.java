package model.pathfinder;

import model.Node;
import model.Outlet;

public class NodePathOutletElement implements NodePathElement {

    private Outlet outlet;
    private NodePathNodeElement next;

    public NodePathOutletElement(Outlet outlet) {
        this.outlet = outlet;
    }

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    @Override
    public NodePathNodeElement getNext() {
        return next;
    }

    @Override
    public void setNext(NodePathElement next) {
        if (next instanceof NodePathInletElement) {
            this.next = (NodePathNodeElement) next;
        } else {
            System.err.println("next muss NodePathNodeElement sein! nicht setzen.");
        }
    }

    @Override
    public Object getValue() {
        return outlet;
    }

}
