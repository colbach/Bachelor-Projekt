package model.pathfinder;

import model.Inlet;
import model.Node;
import model.Outlet;

public class NodePath {

    private NodePathElement first, last;

    private int size() {
        if (first == null) {
            return 0;
        } else {
            NodePathElement elem = first;
            int i = 0;
            while (elem != null) {
                elem = elem.getNext();
                i++;
            }
            return i;
        }
    }

    private NodePathElement getElement(int i) {
        NodePathElement elem = first;
        while (i >= 0) {
            elem = elem.getNext();
            i--;
        }
        return elem;
    }

    private Object getValue(int i) {
        NodePathElement elem = getElement(i);
        return elem.getValue();
    }

    private void addToFront(Object value) {
        if (first == null) {
            if (value instanceof Inlet) {
                first = new NodePathInletElement((Inlet) value);
                last = first;
            } else if (value instanceof Outlet) {
                first = new NodePathOutletElement((Outlet) value);
                last = first;
            } else if (value instanceof Node) {
                first = new NodePathNodeElement((Node) value);
                last = first;
            } else {
                throw new IllegalArgumentException("Falscher Typ");
            }
        } else {
            if (value instanceof Inlet) {
                if (first instanceof NodePathNodeElement) {
                    NodePathInletElement elem = new NodePathInletElement((Inlet) value);
                    elem.setNext(first);
                    first = elem;
                } else {
                    throw new IllegalArgumentException("Falscher Typ");
                }
            } else if (value instanceof Outlet) {
                if (first instanceof NodePathInletElement) {
                    NodePathOutletElement elem = new NodePathOutletElement((Outlet) value);
                    elem.setNext(first);
                    first = elem;
                } else {
                    throw new IllegalArgumentException("Falscher Typ");
                }
            } else if (value instanceof Node) {
                if (first instanceof NodePathOutletElement) {
                    NodePathNodeElement elem = new NodePathNodeElement((Node) value);
                    elem.setNext(first);
                    first = elem;
                } else {
                    throw new IllegalArgumentException("Falscher Typ");
                }
            }
        }
    }

    private void addToBack(Object value) {
        if (first == null) {
            if (value instanceof Inlet) {
                first = new NodePathInletElement((Inlet) value);
                last = first;
            } else if (value instanceof Outlet) {
                first = new NodePathOutletElement((Outlet) value);
                last = first;
            } else if (value instanceof Node) {
                first = new NodePathNodeElement((Node) value);
                last = first;
            } else {
                throw new IllegalArgumentException("Falscher Typ");
            }
        } else {
            if (value instanceof Inlet) {
                if (last instanceof NodePathOutletElement) {
                    NodePathInletElement elem = new NodePathInletElement((Inlet) value);
                    last.setNext(elem);
                    last = elem;
                } else {
                    throw new IllegalArgumentException("Falscher Typ");
                }
            } else if (value instanceof Outlet) {
                if (last instanceof NodePathNodeElement) {
                    NodePathOutletElement elem = new NodePathOutletElement((Outlet) value);
                    last.setNext(elem);
                    last = elem;
                } else {
                    throw new IllegalArgumentException("Falscher Typ");
                }
            } else if (value instanceof Node) {
                if (last instanceof NodePathInletElement) {
                    NodePathNodeElement elem = new NodePathNodeElement((Node) value);
                    last.setNext(elem);
                    last = elem;
                } else {
                    throw new IllegalArgumentException("Falscher Typ");
                }
            }
        }
    }

    private Object[] toArray() {
        Object[] array = new Object[size()];
        NodePathElement elem = first;
        int i = 0;
        while (elem != null && i < array.length) {
            array[i] = elem.getValue();
            elem = elem.getNext();
            i++;
        }
        return array;
    }

    private void print() {
        NodePathElement elem = first;
        while (elem != null) {
            System.out.println("   " + elem.getValue());
            elem = elem.getNext();
        }
    }

    public NodePath deepCopy() {
        NodePath copy = new NodePath();
        if (first instanceof NodePathInletElement) {
            copy.first = new NodePathInletElement(((NodePathInletElement) first).getInlet());
        } else if (first instanceof NodePathNodeElement) {
            copy.first = new NodePathNodeElement(((NodePathNodeElement) first).getNode());
        } else if (first instanceof NodePathOutletElement) {
            copy.first = new NodePathOutletElement(((NodePathOutletElement) first).getOutlet());
        }
        NodePathElement elem = first;
        NodePathElement copyelem = copy.first;
        while (elem != null) {
            elem = elem.getNext();
            if (elem != null) {
                if (elem instanceof NodePathInletElement) {
                    copyelem.setNext(new NodePathInletElement(((NodePathInletElement) first).getInlet()));
                } else if (elem instanceof NodePathNodeElement) {
                    copyelem.setNext(new NodePathNodeElement(((NodePathNodeElement) first).getNode()));
                } else if (elem instanceof NodePathOutletElement) {
                    copyelem.setNext(new NodePathOutletElement(((NodePathOutletElement) first).getOutlet()));
                }
                copyelem = copyelem.getNext();
            }
        }
        copy.last = elem;
        return copy;
    }
}
