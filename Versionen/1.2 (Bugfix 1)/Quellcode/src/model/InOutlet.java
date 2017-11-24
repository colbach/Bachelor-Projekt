package model;

import java.util.concurrent.atomic.AtomicLong;
import model.type.Type;
import reflection.additionalnodedefinitioninterfaces.VariableVisibleInletCount;

public abstract class InOutlet {

    /**
     * Einzigartiger Bezeichner. Diese wird fuer die Serialisierung von Nodes
     * benoetigt. Der Wert 0 ist zur internen Kennzeichnung reserviert und wird
     * nicht verwendet.
     */
    private final long identifier;

    /**
     * Dient zur Bestimmung des naechsten identifier. identifierUpCounter ist
     * immer gleich wie der hoeste identifier im System.
     */
    public static final AtomicLong identifierUpCounter = new AtomicLong(0);

    private Node node;
    private int letIndex = -1;

    public InOutlet(long id) {

        // identifierUpCounter anpassen...
        Long iuc;
        Long newiuc = -1L;
        do {
            iuc = identifierUpCounter.get();
            newiuc = iuc;
            if (newiuc < id) {
                newiuc = id;
            }
        } while (!identifierUpCounter.compareAndSet(iuc, newiuc));

        // identifier setzen...
        identifier = id;

        if (node != null) {
            node.getDependingProject().somethingChanged();
        }
    }

    public InOutlet(Node node, int letIndex) {
        this.node = node;
        this.letIndex = letIndex;
        this.identifier = identifierUpCounter.incrementAndGet();
    }

    public abstract Type getType();

    public abstract String getName();

    public Node getNode() {
        return node;
    }

    public int getLetIndex() {
        return letIndex;
    }

    public abstract boolean isConnected();

    public abstract InOutlet getConnectedLet(int index);

    public abstract InOutlet[] getConnectedLets();

    public abstract int getConnectedLetsCount();

    public synchronized void unconnectLetBilateral(int index) {
        if (this instanceof Inlet) {
            ((Inlet) this).unconnectOutletBilateral(index);
        } else if (this instanceof Outlet) {
            ((Outlet) this).unconnectInletBilateral(index);
        }
        node.getDependingProject().somethingChanged();
    }

    public abstract boolean isConnectedTo(InOutlet let);

    @Override
    public String toString() {
        String toString = getName();
        if (this instanceof Inlet && ((Inlet) this).isOptional()) {
            if (node.getDefinition() instanceof VariableVisibleInletCount && this instanceof Inlet) {
                toString += " [Dyn.]";
            } else {
                toString += " [Opt.]";
            }
        }
        if (!(getType() instanceof VoidType)) {
            toString += " (" + getType().toString() + ")";
        }
        return toString;
    }

    public abstract boolean canConnectToMultipleLets();

    public long getIdentifier() {
        return identifier;
    }

    public void setNode(Node node) {
        this.node = node;
        node.getDependingProject().somethingChanged();
    }

    public void setLetIndex(int letIndex) {
        this.letIndex = letIndex;
        node.getDependingProject().somethingChanged();
    }
}
