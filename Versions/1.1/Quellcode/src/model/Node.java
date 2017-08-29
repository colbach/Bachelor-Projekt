package model;

import model.type.Type;
import model.type.WrongTypException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.directinput.DirectInputNodeDefinition;
import model.resourceloading.nodedefinitionslibrary.UniqueNameVersionGenerator;
import model.settablecontent.DefinitionWithUserSettableContent;
import reflection.nodedefinitions.specialnodes.firstvalues.FirstValueNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ReduceNodeDefinition;
import reflection.common.ContextCreator;
import reflection.additionalnodedefinitioninterfaces.NoTriggerInlet;
import reflection.additionalnodedefinitioninterfaces.NoTriggerOutlet;
import reflection.common.NodeDefinition;
import reflection.additionalnodedefinitioninterfaces.VariableVisibleInletCount;
import utils.structures.tuples.Pair;

public class Node {

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

    private final static int UI_CENTER_FOR_RELATIVE_NODES = Integer.MIN_VALUE;
    private int uiCenterX;
    private int uiCenterY;

    private ArrayList<Inlet> inlets = new ArrayList<>();
    private ArrayList<Outlet> outlets = new ArrayList<>();

    protected NodeDefinition definition;

    public static final String TRIGGER_LABEL = "Auslöser";
    public static final String UNKNOWN_LABEL = "Unbekannt";

    private final Project dependingProject;

    private static NodeDefinition lastCreatingNodeDefiniton;

    /**
     * Kontruktor fuer Deserialisierung. Achtung Attribute muessen manuell
     * gesetzt werden!
     */
    public Node(Long id, Project dependingProject) {

        // identifierUpCounter anpassen...
        Long iuc;
        Long newiuc = -1L;
        do {
            iuc = identifierUpCounter.get();
            newiuc = iuc;
            if (iuc < id) {
                newiuc = id;
            }
        } while (!identifierUpCounter.compareAndSet(iuc, newiuc));

        // identifier setzen...
        identifier = id;

        // dependingProject setzen...
        this.dependingProject = dependingProject;
    }

    /**
     * Erstellen einer zugehoerigen Node. (Direkter Input)
     */
    public Node(NodeDefinition definition, Inlet inlet, Project dependingProject) {
        this(definition, UI_CENTER_FOR_RELATIVE_NODES, UI_CENTER_FOR_RELATIVE_NODES, dependingProject);
        
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
    public Node(NodeDefinition definition, int uiCenterX, int uiCenterY, Project dependingProject) {

        // Koordinaten zuweisen...
        this.uiCenterX = uiCenterX;
        this.uiCenterY = uiCenterY;

        // ID bestimmen...
        identifier = identifierUpCounter.incrementAndGet();

        // Definition setzen...
        this.definition = definition;
        if (!(definition instanceof DirectInputNodeDefinition)) {
            setLastCreatingNodeDefiniton(definition);
        }

        // Inlets fuellen...
        for (int i = 0; i < definition.getInletCount(); i++) { // fuer Anzahl Inlets

            // Inlet erstellen und hinzufuegen...
            inlets.add(new Inlet(this, i));
        }

        // Outlets fuellen...
        for (int i = 0; i < definition.getOutletCount(); i++) { // fuer Anzahl Outlets

            // Outlet erstellen und hinzufuegen...
            outlets.add(new Outlet(this, i));
        }

        // Auslöser...
        if (!(definition instanceof ContextCreator) && !(definition instanceof NoTriggerInlet)) {
            inlets.add(new Inlet(this, definition.getInletCount()));
        }
        if (!(definition instanceof NoTriggerOutlet)) {
            outlets.add(new Outlet(this, definition.getOutletCount()));
        }

        // dependingProject setzen...
        this.dependingProject = dependingProject;

    }

    public boolean isRunContextCreator() {
        return definition instanceof ContextCreator;
    }

    public String getInletName(int i) {
        int definitionInletCount = definition.getInletCount();
        if (i == definitionInletCount) {
            return TRIGGER_LABEL;
        } else if (i > definitionInletCount) {
            System.err.println("Inlet-Index (" + i + ", " + definition.getName() + ") ist zu gross! return UNKNOWN_LABEL.");
            return UNKNOWN_LABEL;
        } else {
            return definition.getNameForInlet(i);
        }
    }

    public Type getInletType(int i) {
        int definitionInletCount = definition.getInletCount();
        if (i == definitionInletCount) {
            return VoidType.getInstance();
        } else if (i > definitionInletCount) {
            System.err.println("Inlet-Index (" + i + ", " + definition.getName() + ") ist zu gross! return VoidType.getInstance().");
            return VoidType.getInstance();
        } else {
            return new Type(definition.getClassForInlet(i), definition.isInletForArray(i));
        }
    }

    public String getOutletName(int i) {
        int definitionOutletCount = definition.getOutletCount();
        if (i == definitionOutletCount) {
            return TRIGGER_LABEL;
        } else if (i > definitionOutletCount) {
            System.err.println("Outlet-Index (" + i + ", " + definition.getName() + ") ist zu gross! return UNKNOWN_LABEL");
            return UNKNOWN_LABEL;
        } else {
            return definition.getNameForOutlet(i);
        }
    }

    public Type getOutletType(int i) {
        int definitionOutletCount = definition.getOutletCount();
        if (i == definitionOutletCount) {
            return VoidType.getInstance();
        } else if (i > definitionOutletCount) {
            System.err.println("Outlet-Index (" + i + ", " + definition.getName() + ") ist zu gross! return VoidType.getInstance().");
            return VoidType.getInstance();
        } else {
            return new Type(definition.getClassForOutlet(i), definition.isOutletForArray(i));
        }
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
            dependingProject.somethingChanged();
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
            dependingProject.somethingChanged();
        }
    }

    public Inlet[] getInlets() {
        return inlets.toArray(new Inlet[0]);
    }

    /**
     * Gibt Anzahl real sichtbarer (!=verbundener) Inlets zuruek.
     */
    public int getVisibleInletCount() {
        if (definition instanceof VariableVisibleInletCount) {
            int size = inlets.size();
            while (size > 0 && !inlets.get(--size).isConnected()) {
            }
            return Math.min(inlets.size(), size + 3);
        }
        return getInletCount();
    }

    public Inlet[] getVisibleInlets() {
        if (definition instanceof VariableVisibleInletCount) {
            Inlet[] visibleInlets = new Inlet[getVisibleInletCount()];
            for (int i = 0; i < visibleInlets.length; i++) {
                visibleInlets[i] = inlets.get(i);
            }
            return visibleInlets;
        } else {
            return inlets.toArray(new Inlet[0]);
        }
    }

    public Inlet getInlet(int i) {
        if (inlets.size() > i) {
            return inlets.get(i);
        } else {
            return null;
        }
    }

    public Inlet getInlet(String name) {
        for (int i = inlets.size() - 1; i >= 0; i--) {
            Inlet inlet = inlets.get(i);
            if (inlet.getName().equals(name)) {
                return inlet;
            }
        }
        return null;
    }

    public Outlet getOutlet(String name) {
        for (int i = outlets.size() - 1; i >= 0; i--) {
            Outlet outlet = outlets.get(i);
            if (outlet.getName().equals(name)) {
                return outlet;
            }
        }
        return null;
    }

    /**
     * Gibt Anzahl real existierender (!=verbundener) Inlets zuruek.
     */
    public int getInletCount() {
        return inlets.size();
    }

    public boolean addOutlet(Outlet e) {
        return outlets.add(e);
    }

    public boolean addInlet(Inlet e) {
        return inlets.add(e);
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
     * Gibt Anzahl der laut Definiton existierenden Inlets zuruek. (Im
     * Normalfall sollte dies 1 weniger als die real existierende Inlets-Anzahl
     * sein)
     */
    public int getDefinitionInletCount() {
        return definition.getInletCount();
    }

    public Class getClassForInlet(int inletIndex) {
        Class classForInlet = definition.getClassForInlet(inletIndex);
        if (classForInlet == null) {
            System.err.println("Definition (" + definition.getUniqueName() + ") hat als Class an Inlet (" + inletIndex + ") null zurueck gegeben!");
        }
        return classForInlet;
    }

    public boolean isInletForArray(int inletIndex) {
        return definition.isInletForArray(inletIndex);
    }

    /**
     * Gibt Anzahl real existierender (!=verbundener) Outlets zuruek.
     */
    public int getOutletCount() {
        return outlets.size();
    }

    public Outlet[] getOutlets() {
        return outlets.toArray(new Outlet[0]);
    }

    public Outlet[] getVisibleOutlets() {
//        if (definition instanceof VariableVisibleOutletCount) {
//            Outlet[] visibleOutlets = new Outlet[getVisibleOutletCount()];
//            for (int i = 0; i < visibleOutlets.length; i++) {
//                visibleOutlets[i] = outlets.get(i);
//            }
//            return visibleOutlets;
//        } else {
        return outlets.toArray(new Outlet[0]);
//        }
    }

    public Outlet getOutlet(int i) {
        if (outlets.size() > i) {
            return outlets.get(i);
        } else {
            return null;
        }
    }

    /**
     * Gibt Anzahl real sichtbarer (!=verbundener) Outlets zuruek.
     */
    public int getVisibleOutletCount() {
        return getOutletCount();
    }

    /**
     * Gibt Anzahl der laut Definiton existierenden Outlets zuruek. (Im
     * Normalfall sollte dies 1 weniger als die real existierende Outlet-Anzahl
     * sein)
     */
    public int getDefinitionOutletCount() {
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
    public Pair<Inlet, Integer>[] getConnectedInletsAndIndexs() {
        int size = 0;
        for (Inlet inlet : this.inlets) {
            size += inlet.getConnectedLetsCount();
        }
        Pair<Inlet, Integer>[] connectedInletsAndIndexs = (Pair<Inlet, Integer>[]) new Pair[size];
        int count = 0;
        for (Inlet inlet : this.inlets) {
            for (int i = 0; i < inlet.getConnectedLetsCount(); i++) {
                connectedInletsAndIndexs[count] = new Pair<>(inlet, (Integer) i);
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
        Class classForOutlet = definition.getClassForOutlet(outletIndex);
        if (classForOutlet == null) {
            System.err.println("Definition (" + definition.getUniqueName() + ") hat als Class an Outlet (" + outletIndex + ") null zurueck gegeben!");
        }
        return classForOutlet;
    }

    public boolean isOutletForArray(int outletIndex) {
        return definition.isOutletForArray(outletIndex);
    }

    public String getName() {
        return definition.getName();
    }

    public String getNameAndID() {
        return definition.getName() + " [" + identifier + "]";
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
            dependingProject.somethingChanged();
        } else {
            System.err.println("NodeDefinition ist keine DefinitionWithUserSettableContent.");
        }
    }

    public void setInlets(ArrayList<Inlet> inlets) {
        this.inlets = inlets;
    }

    public void setOutlets(ArrayList<Outlet> outlets) {
        this.outlets = outlets;
    }

    public void setDefinition(NodeDefinition definition) {
        this.definition = definition;
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

    public Project getDependingProject() {
        return dependingProject;
    }

    @Override
    public String toString() {
        if (hasUserSettableContent()) {
            return getName() + " (" + getShortenedUserSettableContent() + ")";
        } else {
            return getName();
        }
    }

    public synchronized static NodeDefinition getLastCreatingNodeDefiniton() {
        return lastCreatingNodeDefiniton;
    }

    public synchronized static void setLastCreatingNodeDefiniton(NodeDefinition lastCreatingNodeDefiniton) {
        Node.lastCreatingNodeDefiniton = lastCreatingNodeDefiniton;
    }

}
