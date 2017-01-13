package model.runproject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import log.AdditionalErr;
import log.AdditionalOut;
import log.Logging;
import model.*;
import model.specialnodes.IfBackNodeDefinition;
import model.specialnodes.IfForwardNodeDefinition;
import model.specialnodes.IfNodeDefinition;
import utils.structures.Tuple;

public class Executor implements Runnable {

    /**
     * Einzigartiger Bezeichner.
     */
    private final long identifier;

    /**
     * Counter zaelt Anzahl von Instanzen. Wird fuer identifier benoetigt.
     */
    private static final AtomicLong instanceCounter = new AtomicLong(0);

    private final Node executingNode;
    private final ExecutionHub hub;
    private InOutImplementation io;
    private final Debugger debugger;

    public Executor(Node executingNode, ExecutionHub hub) {
        this.executingNode = executingNode;
        this.hub = hub;
        this.debugger = hub.getDebugger();
        this.identifier = instanceCounter.incrementAndGet();
        AdditionalOut.println("New Executor: " + toString());
    }

    @Override
    public void run() {
        debugger.threadStarted(this);

        if (executingNode.getDefinition() instanceof IfNodeDefinition) { // If-Ellement
            
            if(executingNode.getDefinition() instanceof IfBackNodeDefinition) {
                
                // Bool ermitteln...
                Tuple<Inlet, Integer>[] inlet2Only = (Tuple<Inlet, Integer>[]) new Tuple[] {new Tuple<Inlet, Integer>(executingNode.getInlet(2), 0)};
                HashMap<Inlet, Object[]> inletData2Only = hub.collect(inlet2Only, this);
                Boolean bool = true;
                for(Object[] os : inletData2Only.values()) {
                    for(Object o : os) {
                        bool = (Boolean) o;
                    }
                }
                
                if(bool) { // Wenn true...
                    Tuple<Inlet, Integer>[] inlets = (Tuple<Inlet, Integer>[]) new Tuple[executingNode.getInlet(0).getConnectedLetsCount()];
                    for(int i=0; i<inlets.length; i++) {
                        inlets[i] = new Tuple<>(executingNode.getInlet(0), i);
                    }
                    HashMap<Inlet, Object[]> inletData0Only = hub.collect(inlets, this);
                    int trueDataLength = 0;
                    for(Object[] vs : inletData0Only.values()) {
                        trueDataLength += vs.length;
                    }
                    Object[] trueData = new Object[trueDataLength];
                    int trueDataI = 0;
                    for(Object[] vs : inletData0Only.values()) {
                        for(Object v : vs) {
                            trueData[trueDataI] = v;
                            trueDataI++;
                        }
                    }
                    HashMap<Outlet, Object[]> outletData = new HashMap<>();
                    outletData.put(this.executingNode.getOutlet(0), trueData);
                    debugger.block();
                    hub.passOutletData(this, outletData);
                    
                } else { // Wenn false...
                    Tuple<Inlet, Integer>[] inlets = (Tuple<Inlet, Integer>[]) new Tuple[executingNode.getInlet(1).getConnectedLetsCount()];
                    for(int i=0; i<inlets.length; i++) {
                        inlets[i] = new Tuple<>(executingNode.getInlet(1), i);
                    }
                    HashMap<Inlet, Object[]> inletData1Only = hub.collect(inlets, this);
                    int falseDataLength = 0;
                    for(Object[] vs : inletData1Only.values()) {
                        falseDataLength += vs.length;
                    }
                    Object[] falseData = new Object[falseDataLength];
                    int trueDataI = 0;
                    for(Object[] vs : inletData1Only.values()) {
                        for(Object v : vs) {
                            falseData[trueDataI] = v;
                            trueDataI++;
                        }
                    }
                    HashMap<Outlet, Object[]> outletData = new HashMap<>();
                    outletData.put(this.executingNode.getOutlet(0), falseData);
                    debugger.block();
                    hub.passOutletData(this, outletData);
                }
                
            } else if(executingNode.getDefinition() instanceof IfForwardNodeDefinition) {
                
                // Bool und Daten sammeln...
                int dataConnectedInletsCount = executingNode.getInlet(0).getConnectedLetsCount();
                Tuple<Inlet, Integer>[] inlet1And0 = (Tuple<Inlet, Integer>[]) new Tuple[dataConnectedInletsCount + 1]; // + 1 fuer bool
                inlet1And0[0] = new Tuple<Inlet, Integer>(executingNode.getInlet(1), 0); // bool
                for(int i=0; i<dataConnectedInletsCount; i++) { // data
                    inlet1And0[1+i] = new Tuple<>(executingNode.getInlet(1), i);
                }
                HashMap<Inlet, Object[]> inletData1And0 = hub.collect(inlet1And0, this);
                
                // bool ermitteln...
                Object[] boolObjectArray = inletData1And0.get(executingNode.getInlet(1));
                boolean bool = false;
                if(boolObjectArray.length >= 1) {
                    if(boolObjectArray.length > 1) {
                        System.err.println("Inlet Wahrheitswert sollte eine laenge von 1 haben.");
                    }
                    bool = (boolean) (Boolean) boolObjectArray[0];
                } else {
                    System.err.println("Wahrheitswert kann nicht ermittelt werden. Fallback: false");
                }
                
                // Daten ermiteln...
                Tuple<Inlet, Integer>[] inlet0Only = (Tuple<Inlet, Integer>[]) new Tuple[executingNode.getInlet(0).getConnectedLetsCount()];
                for(int i=0; i<inlet0Only.length; i++) {
                    inlet0Only[i] = new Tuple<>(executingNode.getInlet(1), i);
                }
                HashMap<Inlet, Object[]> inletData0Only = hub.collect(inlet0Only, this);
                Object[] data = new Object[0];
                for(Object[] d : inletData0Only.values()) {
                    if(data.length == 0) {
                        data = d;
                    } else {
                        System.err.println("Fehler");
                    }
                }
                
                if(bool) { // Wenn true...
                    HashMap<Outlet, Object[]> outletData = new HashMap<>();
                    outletData.put(this.executingNode.getOutlet(0), data);
                    debugger.block();
                    hub.passOutletData(this, outletData);
                    
                } else { // Wenn false...
                    HashMap<Outlet, Object[]> outletData = new HashMap<>();
                    outletData.put(this.executingNode.getOutlet(1), data);
                    debugger.block();
                    hub.passOutletData(this, outletData);
                }
                
                
            } else { // Fehler
                System.err.println(executingNode.getDefinition().getClass() + " unbekannte IfNodeDefinition");
            }
            
        } else { // Kein If-Ellement

            Tuple<Inlet, Integer>[] connectedInlets = executingNode.getConnectedInletsAndIndexs();
            HashMap<Inlet, Object[]> inletData = hub.collect(connectedInlets, this);

            if (inletData == null) { // Interrupted...
                AdditionalErr.println("inletData ist null. Abbruch.");

            } else { // Normal-Fall...

                // InOut-Implementierung befuellen...
                HashMap<Integer, Object[]> inletIndexData = new HashMap<>();
                for (int i = 0; i < executingNode.getInletCount(); i++) {
                    Inlet inlet = executingNode.getInlet(i);
                    if (inletData.containsKey(inlet)) {
                        inletIndexData.put(i, inletData.get(inlet));
                    }
                }
                io = new InOutImplementation(inletIndexData);
                if (hub.isCanceled()) {
                    return;
                }

                // Node ausfueren...
                AdditionalOut.println(toString() + " -> run definition");
                debugger.threadWork(this);
                long nanos = System.nanoTime();
                
                debugger.block();
                executingNode.getDefinition().run(io);
                debugger.block();
                
                debugger.registerRuntimeInNanosForNode(executingNode, System.nanoTime()-nanos); // Zeit registrieren
                if (hub.isCanceled()) {
                    return;
                }
                debugger.threadFinishedWorking(this);

                // Outlet-Data extrahieren...
                HashMap<Outlet, Object[]> outletData = new HashMap<>();
                {
                    HashMap<Integer, Object[]> outletIndexData = io.getOutletIndexData();
                    for (Map.Entry<Integer, Object[]> entry : outletIndexData.entrySet()) {
                        outletData.put(executingNode.getOutlet(entry.getKey()), entry.getValue());
                    }
                }
                if (hub.isCanceled()) {
                    return;
                }

                // Outlet-Data weiterreichen...
                debugger.block();
                hub.passOutletData(this, outletData);

            }
        }

    }

    public Node getExecutingNode() {
        return executingNode;
    }

    public long getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "Executor " + identifier + " [" + executingNode.toString() + "]";
    }
}
