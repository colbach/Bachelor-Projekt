package model.pathfinder;

import model.Node;
import model.Inlet;

public class NodePathInletElement implements NodePathElement {

    private Inlet inlet;
    private NodePathNodeElement next;

    public NodePathInletElement(Inlet inlet) {
        this.inlet = inlet;
    }

    public Inlet getInlet() {
        return inlet;
    }

    public void setInlet(Inlet inlet) {
        this.inlet = inlet;
    }

    @Override
    public NodePathNodeElement getNext() {
        return next;
    }

    @Override
    public void setNext(NodePathElement next) {
        if (next instanceof NodePathNodeElement) {
            this.next = (NodePathNodeElement) next;
        } else {
            System.err.println("next muss NodePathNodeElement sein! nicht setzen.");
        }
    }

    @Override
    public Object getValue() {
        return inlet;
    }

}
