package model.pathfinder;

import model.Node;
import model.Outlet;

public class NodePathNodeElement implements NodePathElement {

    private Node node;
    private NodePathOutletElement next;

    public NodePathNodeElement(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public NodePathOutletElement getNext() {
        return next;
    }

    @Override
    public void setNext(NodePathElement next) {
        if (next instanceof NodePathOutletElement) {
            this.next = (NodePathOutletElement) next;
        } else {
            System.err.println("next muss NodePathOutletElement sein! nicht setzen.");
        }
    }

    @Override
    public Object getValue() {
        return node;
    }

}
