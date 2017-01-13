package model;

import model.type.Type;
import exceptions.WrongTypException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.directinput.DirectInputNodeDefinition;
import model.resourceloading.UniqueNameVersionGenerator;
import model.settablecontent.DefinitionWithUserSettableContent;
import reflection.NodeDefinition;
import utils.structures.Tuple;

public class Node {

    /**
     * Einzigartiger Bezeichner. Diese wird fuer die Serialisierung von Nodes
     * benoetigt. Der Wert 0 ist zur internen Kennzeichnung reserviert und wird
     * nicht verwendet.
     */
    private final long identifier;

    /**
     * Counter zaelt Anzahl von Instanzen. Wird fuer identifier benoetigt.
     */
    private static final AtomicLong instanceCounter = new AtomicLong(0);

    private final static int UI_CENTER_FOR_RELATIVE_NODES = Integer.MIN_VALUE;
    private int uiCenterX;
    private int uiCenterY;

    private ArrayList<Inlet> inlets = new ArrayList<>();
    private ArrayList<Outlet> outlets = new ArrayList<>();

    protected NodeDefinition definition;

    /**
     * Erstellen einer zugehoerigen Node. (Direkter Input)
     */
    public Node(NodeDefinition definition, Inlet inlet) {
        this(definition, UI_CENTER_FOR_RELATIVE_NODES, UI_CENTER_FOR_RELATIVE_NODES);
        try {
            Outlet outlet = outlets.get(0);
            inlet.unconnectAllOutletBilateral();
            outlet.connectInlet(inlet, 0);
            inlet.connectOutlet(outlet, 0);
        } catch (WrongTypException ex) {
            throw new RuntimeException("Outlet hat falscher Typ");
        }
    }

    /**
     * Erstellen einer normalen oder spezialen Node.
     */
    public Node(NodeDefinition definition, int uiCenterX, int uiCenterY) {

        // Koordinaten zuweisen...
        this.uiCenterX = uiCenterX;
        this.uiCenterY = uiCenterY;

        // ID bestimmen...
        identifier = instanceCounter.incrementAndGet();

        // Definition setzen...
        this.definition = definition;

        // Inlets fuellen...
        for (int i = 0; i < definition.getInletCount(); i++) { // fuer Anzahl Inlets

            // Inlet erstellen und hinzufuegen...
            inlets.add(new Inlet(new Type(definition.getClassForInlet(i), definition.isInletForArray(i)), definition.getNameForInlet(i), this, i));
        }

        // Outlets fuellen...
        for (int i = 0; i < definition.getOutletCount(); i++) { // fuer Anzahl Outlets

            // Outlet erstellen und hinzufuegen...
            outlets.add(new Outlet(new Type(definition.getClassForOutlet(i), definition.isOutletForArray(i)), definition.getNameForOutlet(i), this, i));
        }

        // Auslöser...
        inlets.add(new Inlet(VoidType.getInstance(), "Auslöser", this, definition.getInletCount()));
        outlets.add(new Outlet(VoidType.getInstance(), "Auslöser", this, definition.getOutletCount()));

    }

    /**
     * Gibt an ob diese Node relativ zu einer anderen Node steht oder eigene
     * Koordinaten hat.
     */
    public boolean isRelativeNode() {
        return uiCenterX == UI_CENTER_FOR_RELATIVE_NODES && uiCenterY == UI_CENTER_FOR_RELATIVE_NODES;
    }

    public Node[] getRelativeNodes() {
        ArrayList<Node> relativeNodes = new ArrayList<>();
        for (Inlet inlet : inlets) {
            for (Outlet outlet : inlet.getConnectedLets()) {
                Node node = outlet.getNode();
                if (node.isRelativeNode()) {
                    relativeNodes.add(node);
                }
            }
        }
        return relativeNodes.toArray(new Node[0]);
    }

    public Inlet getConnectedInletFromRelativeNode() {
        if (isRelativeNode()) {
            if (outlets.size() > 0) {
                if (outlets.get(0).isConnected()) {
                    return outlets.get(0).getConnectedLets()[0];
                } else {
                    throw new RuntimeException("Relative Node muss ersten Ausgang verbunden haben.");
                }
            } else {
                throw new RuntimeException("Relative Node muss mindestends einen Ausgang haben.");
            }
        } else {
            throw new IllegalArgumentException("Node muss relativ zu anderem Node stehen.");
        }
    }

    public Node getRelativeNode() {
        return getConnectedInletFromRelativeNode().getNode();
    }

    /**
     * Gibt eindeutige ID der Node. Diese ist innerhalb der Laufzeit einzigartig
     * und wird von keinem anderen Node verwendet (auch wenn dieser bereits
     * nicht mehr existiert)
     */
    public long getIdentifier() {
        return identifier;
    }

    /**
     * Gibt X-Position in der UI.
     */
    public int getUICenterX() {
        return uiCenterX;
    }

    /**
     * Setzt X-Positon in der UI. Die UI muss neu gezeichnet werden damit
     * Aenderungen aktiv werden. Dies geschieht nicht automatisch.
     */
    public void setUiCenterX(int uiCenterX) {
        if (!isRelativeNode()) {
            this.uiCenterX = uiCenterX;
        }
    }

    /**
     * Gibt X-Position in der UI.
     */
    public int getUICenterY() {
        return uiCenterY;
    }

    /**
     * Setzt Y-Positon in der UI. Die UI muss neu gezeichnet werden damit
     * Aenderungen aktiv werden. Dies geschieht nicht automatisch.
     */
    public void setUiCenterY(int uiCenterY) {
        if (!isRelativeNode()) {
            this.uiCenterY = uiCenterY;
        }
    }

    public Inlet[] getInlets() {
        return inlets.toArray(new Inlet[0]);
    }

    public Inlet getInlet(int i) {
        return inlets.get(i);
    }

    public Outlet[] getOutlets() {
        return outlets.toArray(new Outlet[0]);
    }

    public Outlet getOutlet(int i) {
        return outlets.get(i);
    }
    
    /**
     * Gibt Anzahl real existierender (!=verbundener) Inlets zuruek.
     */
    public int getInletCount() {
        return definition.getInletCount();
    }

    public Class getClassForInlet(int inletIndex) {
        return definition.getClassForInlet(inletIndex);
    }

    public boolean isInletForArray(int inletIndex) {
        return definition.isInletForArray(inletIndex);
    }

    /**
     * Gibt Anzahl real existierender (!=verbundener) Outlets zuruek.
     */
    public int getOutletCount() {
        return definition.getOutletCount();
    }

    /**
     * Gibt Anzahl verbundener Outlets zuruek.
     */
    public int getConnectedOutletCount() {
        int counter = 0;
        for (Outlet outlet : outlets) {
            if (outlet.isConnected()) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Gibt Anzahl verbundener Inlets zuruek.
     */
    public int getConnectedInletCount() {
        int counter = 0;
        for (Inlet inlet : inlets) {
            if (inlet.isConnected()) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Gibt Verbundene verbundener Outlets zuruek.
     */
    public Outlet[] getConnectedOutlets() {
        Outlet[] connectedOutlets = new Outlet[getConnectedOutletCount()];
        int counter = 0;
        for (Outlet outlet : this.outlets) {
            if (outlet.isConnected()) {
                connectedOutlets[counter] = outlet;
                counter++;
            }
        }
        return connectedOutlets;
    }

    /**
     * Gibt Verbundene verbundener Inlets zuruek.
     */
    public Tuple<Inlet, Integer>[] getConnectedInletsAndIndexs() {
        int size = 0;
        for (Inlet inlet : this.inlets) {
            size += inlet.getConnectedLetsCount();
        }
        Tuple<Inlet, Integer>[] connectedInletsAndIndexs = (Tuple<Inlet, Integer>[]) new Tuple[size];
        int count = 0;
        for (Inlet inlet : this.inlets) {
            for (int i = 0; i < inlet.getConnectedLetsCount(); i++) {
                connectedInletsAndIndexs[count] = new Tuple<>(inlet, (Integer) i);
                count++;
            }
        }
        return connectedInletsAndIndexs;
    }

    /**
     * Gibt Verbundene verbundener Inlets zuruek.
     */
    public Inlet[] getConnectedInlets() {
        Inlet[] connectedInlets = new Inlet[getConnectedInletCount()];
        int counter = 0;
        for (Inlet inlet : this.inlets) {
            if (inlet.isConnected()) {
                connectedInlets[counter] = inlet;
                counter++;
            }
        }
        return connectedInlets;
    }

    public Set<Node> getOverOutletsConnectedNodes() {
        HashSet<Node> overOutletsConnectedNodes = new HashSet<>();
        for (Outlet connectedOutlet : getConnectedOutlets()) {
            for (Inlet overOutletsConnectedInlet : connectedOutlet.getConnectedLets()) {
                overOutletsConnectedNodes.add(overOutletsConnectedInlet.getNode());
            }
        }
        return overOutletsConnectedNodes;
    }

    public Class getClassForOutlet(int outletIndex) {
        return definition.getClassForOutlet(outletIndex);
    }

    public boolean isOutletForArray(int outletIndex) {
        return definition.isOutletForArray(outletIndex);
    }

    public void run(NodeDefinition.InOut io) {
        definition.run(io);
    }

    public String getName() {
        return definition.getName();
    }

    public NodeDefinition getDefinition() {
        return definition;
    }

    public String getUniqueNameVersion() {
        if (definition == null) {
            return null;
        } else {
            return UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion(definition);
        }
    }

    public String getIconName() {
        return definition.getIconName();
    }

    public void capConnections() {
        inlets.forEach((inlet) -> {
            inlet.unconnectAllOutletBilateral();
        });
        outlets.forEach((outlet) -> {
            outlet.unconnectAllInletBilateral();
        });
    }

    public boolean hasUserSettableContent() {
        return getDefinition() instanceof DefinitionWithUserSettableContent;
    }

    public Object[] getUserSettableContent() {
        NodeDefinition definition = getDefinition();
        if (definition instanceof DefinitionWithUserSettableContent) {
            DefinitionWithUserSettableContent dwusc = (DefinitionWithUserSettableContent) definition;
            return dwusc.getUserSettableContent();
        } else {
            System.err.println("NodeDefinition ist keine DefinitionWithUserSettableContent.");
            return null;
        }
    }

    public String getShortenedUserSettableContent() {
        NodeDefinition definition = getDefinition();
        if (definition instanceof DefinitionWithUserSettableContent) {
            DefinitionWithUserSettableContent dwusc = (DefinitionWithUserSettableContent) definition;
            return dwusc.getShortenedUserSettableContent();
        } else {
            System.err.println("NodeDefinition ist keine DefinitionWithUserSettableContent.");
            return null;
        }
    }

    public void setUserSettableContent(Object[] content) {
        NodeDefinition definition = getDefinition();
        if (definition instanceof DefinitionWithUserSettableContent) {
            DefinitionWithUserSettableContent dwusc = (DefinitionWithUserSettableContent) definition;
            dwusc.setUserSettableContent(content);
        } else {
            System.err.println("NodeDefinition ist keine DefinitionWithUserSettableContent.");
        }
    }

    public Type getUserSettableContentType() {
        NodeDefinition definition = getDefinition();
        if (definition instanceof DefinitionWithUserSettableContent) {
            DefinitionWithUserSettableContent dwusc = (DefinitionWithUserSettableContent) definition;
            return dwusc.getContentType();
        } else {
            System.err.println("NodeDefinition ist keine DefinitionWithUserSettableContent.");
            return null;
        }
    }

    public boolean isDirectInputNode() {
        return getDefinition() instanceof DirectInputNodeDefinition;
    }

    @Override
    public String toString() {
        if (hasUserSettableContent()) {
            return getName() + "(" + getShortenedUserSettableContent() + ")";
        } else {
            return getName();
        }
    }

}
