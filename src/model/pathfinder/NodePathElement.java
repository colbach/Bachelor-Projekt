package model.pathfinder;

public interface NodePathElement {
    
    public NodePathElement getNext();

    public void setNext(NodePathElement next);
    
    public Object getValue();
}
