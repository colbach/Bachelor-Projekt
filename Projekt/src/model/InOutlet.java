package model;

import model.type.Type;

public abstract class InOutlet {

    private final Type type;
    private final String name;
    private final Node node;
    private final int letIndex;

    public InOutlet(Type typ, String name, Node node, int letIndex) {
        this.type = typ;
        this.name = name;
        this.node = node;
        this.letIndex = letIndex;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Node getNode() {
        return node;
    }

    public int getLetIndex() {
        return letIndex;
    }

    public abstract boolean isConnected();

    public abstract InOutlet[] getConnectedLets();

    public abstract int getConnectedLetsCount();

    public synchronized void unconnectLetBilateral(int index) {
        if (this instanceof Inlet) {
            ((Inlet) this).unconnectOutletBilateral(index);
        } else if (this instanceof Outlet) {
            ((Outlet) this).unconnectInletBilateral(index);
        }
    }

    public abstract boolean isConnectedTo(InOutlet let);

    @Override
    public String toString() {
        if (type instanceof VoidType) {
            return getName();
        } else {
            return getName() + " (" + getType().toString() + ")";
        }
    }

    public abstract boolean canConnectToMultipleLets();

}
