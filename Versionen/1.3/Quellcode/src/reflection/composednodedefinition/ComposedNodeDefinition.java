package reflection.composednodedefinition;

import java.util.ArrayList;
import java.util.HashMap;
import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;
import reflection.common.TerminatedException;
import reflection.customdatatypes.math.MathObject;
import reflection.customdatatypes.math.NumberMathObject;
import utils.structures.tuples.Pair;

public abstract class ComposedNodeDefinition implements NodeDefinition {

    private final ArrayList<NodeDefinition> composedNodeDefinitons;

    private final ArrayList<InletLink> externalInlets;
    private final ArrayList<OutletLink> externalOutlets;
    private final ArrayList<BidirectionalLink> internalConnections;
    private final ArrayList<DefaultDataForInlet> defaultDataForInlets;

    private final String name;
    private final String iconName;
    private final int version;

    private String uniqueName = null;
    private String description = null;

    public static String COMPOSED_TAG = "[Kombiniert]";

    public ComposedNodeDefinition(String name) {
        this(name, null, 1);
    }

    public ComposedNodeDefinition(String name, int version) {
        this(name, null, version);
    }

    public ComposedNodeDefinition(String name, String iconName) {
        this(name, iconName, 1);
    }

    public ComposedNodeDefinition(String name, String iconName, int version) {
        this.name = name;
        this.iconName = iconName;
        this.version = version;
        externalInlets = new ArrayList<>();
        externalOutlets = new ArrayList<>();
        composedNodeDefinitons = new ArrayList<>();
        internalConnections = new ArrayList<>();
        defaultDataForInlets = new ArrayList<>();
    }

    public final void addNodeDefinitoon(NodeDefinition nodeDefinition) {
        composedNodeDefinitons.add(nodeDefinition);
    }

    public final void addExternalInlet(InletLink inlet) {
        externalInlets.add(inlet);
    }

    public final void addExternalOutlet(OutletLink outlet) {
        externalOutlets.add(outlet);
    }

    public final void addInternalConnections(BidirectionalLink connection) {
        internalConnections.add(connection);
    }

    public final void addDefaultDataForInlet(DefaultDataForInlet defaultDataForInlet) {
        defaultDataForInlets.add(defaultDataForInlet);
    }

    @Override
    public int getInletCount() {
        return externalInlets.size();
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        InletLink get = externalInlets.get(inletIndex);
        return get.getNodeDefinition().getClassForInlet(get.getInletIndex());
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        InletLink get = externalInlets.get(inletIndex);
        return get.getNodeDefinition().getNameForInlet(get.getInletIndex());
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        InletLink get = externalInlets.get(inletIndex);
        return get.getNodeDefinition().isInletForArray(get.getInletIndex());
    }

    @Override
    public boolean isInletEngaged(int inletIndex) {
        InletLink get = externalInlets.get(inletIndex);
        return get.getNodeDefinition().isInletEngaged(get.getInletIndex());
    }

    @Override
    public int getOutletCount() {
        return externalOutlets.size();
    }

    @Override
    public Class getClassForOutlet(int outletIndex) {
        OutletLink get = externalOutlets.get(outletIndex);
        return get.getNodefDefinition().getClassForOutlet(get.getOultetIndex());
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        OutletLink get = externalOutlets.get(outletIndex);
        return get.getNodefDefinition().getNameForOutlet(get.getOultetIndex());
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        OutletLink get = externalOutlets.get(outletIndex);
        return get.getNodefDefinition().isInletForArray(get.getOultetIndex());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {

        if (description == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Schaltet folgende Elemente hintereinander:\n");
            boolean first = true;
            for (NodeDefinition composed : composedNodeDefinitons) {
                if (first) {
                    first = false;
                } else {
                    sb.append(" -> ");
                }
                sb.append(composed.getName());
            }
            sb.append(NodeDefinition.TAG_PREAMBLE);
            sb.append(" ");
            sb.append(COMPOSED_TAG);
            for (NodeDefinition composed : composedNodeDefinitons) {
                sb.append(" ");
                String composedDescription = composed.getDescription();
                int indexOf = composedDescription.indexOf(NodeDefinition.TAG_PREAMBLE);
                if (indexOf != -1) {
                    sb.append(composedDescription.substring(indexOf + NodeDefinition.TAG_PREAMBLE.length()));
                }
            }
            sb.append("Zusammengesetzt composed");
            description = sb.toString();
        }
        return description;
    }

    @Override
    public String getUniqueName() {
        if (uniqueName == null) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (NodeDefinition composed : composedNodeDefinitons) {
                if (first) {
                    first = false;
                } else {
                    sb.append("+");
                }
                sb.append(composed.getUniqueName());
            }
            uniqueName = sb.toString();
        }
        return uniqueName;
    }

    @Override
    public String getIconName() {
        if (iconName == null) {
            switch (composedNodeDefinitons.size()) {
                case 0:
                    return "ui/Clear.png";
                case 1:
                    return "Composed_Node_Definitions_1x_30px.png";
                case 2:
                    return "Composed_Node_Definitions_2x_30px.png";
                case 3:
                    return "Composed_Node_Definitions_3x_30px.png";
                default:
                    return "Composed_Node_Definitions_3+x_30px.png";
            }

        } else {
            return iconName;
        }
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public void run(InOut io, API api) throws Exception {
        HashMap<Pair<NodeDefinition, Integer>, Object[]> storage = new HashMap<>();

        for (DefaultDataForInlet defaultDataForInlet : defaultDataForInlets) {
            storage.put(new Pair<>(defaultDataForInlet.getNodeDefinition(), defaultDataForInlet.getInletIndex()), defaultDataForInlet.getData());
        }

        for (int i = 0; i < externalInlets.size(); i++) {
            InletLink externalInlet = externalInlets.get(i);
            Object[] in = io.in(i);
            if (in != null) {
                storage.put(new Pair<>(externalInlet.getNodeDefinition(), externalInlet.getInletIndex()), in);
            }
        }

        for (final NodeDefinition composed : composedNodeDefinitons) {

            io.terminatedTest();

            composed.run(new InOut() {
                @Override
                public Object[] in(int i) throws TerminatedException {
                    io.terminatedTest();
                    Object[] get = storage.get(new Pair<>(composed, i));
                    if (get == null) {
                        return null;
                    } else {
                        return get;
                    }
                }

                @Override
                public Object in0(int i) throws TerminatedException {
                    io.terminatedTest();
                    Object[] in = in(i);
                    if (in == null || in.length == 0) {
                        return null;
                    } else {
                        return in[0];
                    }
                }

                @Override
                public Object[] in(int i, Object[] def) throws TerminatedException {
                    io.terminatedTest();
                    Object[] in = in(i);
                    if (in == null) {
                        return def;
                    } else {
                        return in;
                    }
                }

                @Override
                public Object in0(int i, Object def) throws TerminatedException {
                    io.terminatedTest();
                    Object in0 = in0(i);
                    if (in0 == null) {
                        return def;
                    } else {
                        return in0;
                    }
                }

                @Override
                public void out(int i, Object ausgabe) throws TerminatedException {
                    io.terminatedTest();
                    out(i, new Object[]{ausgabe});
                }

                @Override
                public void out(int inI, Object[] ausgabe) throws TerminatedException {
                    io.terminatedTest();
                    for (BidirectionalLink internalConnection : internalConnections) {
                        if (internalConnection.getFromNodeDefinition() == composed && internalConnection.getFromOutletIndex() == inI) {
                            storage.put(new Pair<>(internalConnection.getToNodeDefinition(), internalConnection.getToInletIndex()), ausgabe);
                        }
                    }
                    io.terminatedTest();
                    for (int outI = 0; outI < externalOutlets.size(); outI++) {
                        OutletLink externalOutlet = externalOutlets.get(outI);
                        if (externalOutlet.getNodefDefinition() == composed && externalOutlet.getOultetIndex() == inI) {
                            io.out(outI, ausgabe);
                        }
                    }
                    io.terminatedTest();
                }

                @Override
                public boolean outConnected(int i) throws TerminatedException {
                    io.terminatedTest();
                    for (OutletLink externalOutlet : externalOutlets) {
                        if (externalOutlet.getNodefDefinition() == composed && externalOutlet.getOultetIndex() == i) {
                            return true;
                        }
                    }
                    io.terminatedTest();
                    for (BidirectionalLink internalConnection : internalConnections) {
                        if (internalConnection.getFromNodeDefinition() == composed && internalConnection.getToInletIndex() == i) {
                            return true;
                        }
                    }
                    io.terminatedTest();
                    return false;
                }

                @Override
                public long getContextIdentifier() throws TerminatedException {
                    io.terminatedTest();
                    return io.getContextIdentifier();
                }

                @Override
                public boolean isTerminated() {
                    return io.isTerminated();
                }

                @Override
                public void terminatedTest() throws TerminatedException {
                    io.terminatedTest();
                }

                @Override
                public Number inN(int i, Number def) throws TerminatedException {
                    
                    Object in0 = in0(i);
                    if(in0 == null)
                        return def;
                    if(in0 instanceof NumberMathObject) {
                        in0 = ((NumberMathObject) in0).getWrappedNumber();
                    }
                    return (Number) in0;
                }

                @Override
                public MathObject inM(int i, MathObject def) throws TerminatedException {
                    
                    Object in0 = in0(i);
                    if(in0 == null)
                        return def;
                    if(in0 instanceof Number) {
                        in0 = new NumberMathObject((Number) in0);
                    }
                    return (NumberMathObject) in0;
                }

                @Override
                public Boolean inB(int i, Boolean def) throws TerminatedException {
                    Object in0 = in0(i);
                    if(in0 == null)
                        return def;
                    return (Boolean) in0;
                }
            }, api);
        }
        io.terminatedTest();
    }

}
