package model.runproject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import log.AdditionalErr;
import log.AdditionalOut;
import log.Logging;
import model.*;
import model.type.ExtendedCompatibility;
import utils.structures.Tuple;

public final class ExecutionHub {

    private final Debugger debugger;
    protected final HashMap<Tuple<Inlet, Integer>, Object[]> arrivedInletData;
    protected final HashMap<Node, Thread> executorThreads;
    private boolean canceled = false;
    private static AtomicLong changeCounterForUI = new AtomicLong(0);

    public ExecutionHub(Node startNode, boolean debug) {
        arrivedInletData = new HashMap<>();
        executorThreads = new HashMap<>();
        debugger = new Debugger(this, debug);
        startExecutorIfNotRunning(startNode);
    }
    
    protected void incrementChangeCounterForUI() {
        changeCounterForUI.incrementAndGet();
    }

    public Debugger getDebugger() {
        return debugger;
    }

    private void putArrivedInletData(Outlet fromOutlet, Tuple<Inlet, Integer> toInlet, Object[] data) {
        synchronized (arrivedInletData) {
            // Ueberpruefen ob daten konvertiert werden muessen...
            if (ExtendedCompatibility.typeNeedsExtendedCompatibilityToConnectToType(fromOutlet.getType(), toInlet.l.getType())) {
                // Konvertieren...
                data = ExtendedCompatibility.convertObjectToType(data, fromOutlet.getType(), toInlet.l.getType());
            }

            // Daten ablegen...
            arrivedInletData.put(toInlet, data);
            arrivedInletData.notifyAll();
            startExecutorIfNotRunning(toInlet.l.getNode());
        }
        incrementChangeCounterForUI();
    }

    protected void startExecutorIfNotRunning(Node node) {
        synchronized (executorThreads) {
            if (!executorThreads.containsKey(node)) {
                Executor executor = new Executor(node, this);
                Thread executorThread = new Thread(executor);
                executorThread.start();
                executorThreads.put(node, executorThread);
            }
        }
        incrementChangeCounterForUI();
    }

    protected boolean isInletDataArived(Tuple<Inlet, Integer> inlet) {
        synchronized (arrivedInletData) {
            if (arrivedInletData.containsKey(inlet)) {
                return true;
            } else {
                startExecutorIfNotRunning(inlet.l.getConnectedLets()[inlet.r].getNode());
                return false;
            }
        }
    }

    private boolean isInletDataArrived(Tuple<Inlet, Integer>[] inlets) {
        for (Tuple<Inlet, Integer> inlet : inlets) { // Falls ein Inlet noch nicht angekommen ist...
            if (!isInletDataArived(inlet)) {
                return false;
            }
        }
        // Falls alle angekommen sind...
        return true;
    }

    private Object[] getArrivedInletData(Tuple<Inlet, Integer> inlet) {
        synchronized (arrivedInletData) {
            if (arrivedInletData.containsKey(inlet)) {
                return arrivedInletData.get(inlet);
            } else {
                startExecutorIfNotRunning(inlet.l.getNode());
                return null;
            }
        }
    }

    private Object[] removeArrivedInletData(Tuple<Inlet, Integer> inlet) {
        synchronized (arrivedInletData) {
            if (arrivedInletData.containsKey(inlet)) {
                Object[] r = arrivedInletData.remove(inlet);
                incrementChangeCounterForUI();
                return r;
            } else {
                startExecutorIfNotRunning(inlet.l.getNode());
                incrementChangeCounterForUI();
                return null;
            }
        }
        
    }

    protected HashMap<Inlet, Object[]> collect(Tuple<Inlet, Integer>[] inlets, Executor executor) {
        synchronized (arrivedInletData) {
            debugger.threadWait(executor);
            while (!isInletDataArrived(inlets)) {
                try {
                    arrivedInletData.wait();
                } catch (InterruptedException ex) {
                    AdditionalErr.println("InterruptedException. return null.");
                    return null;
                }
            }
            debugger.threadFinishedWaiting(executor);
            HashMap<Inlet, Object[]> map = new HashMap<>();
            for (Tuple<Inlet, Integer> inlet : inlets) {
                if (map.containsKey(inlet.l)) { // Wenn Inlet bereits in map enthalten ist
                    // Haenge Daten an Value an...
                    Object[] data = map.get(inlet.l);
                    Object[] appendData = removeArrivedInletData(inlet);
                    Object[] newData = new Object[data.length + appendData.length];
                    System.arraycopy(data, 0, newData, 0, data.length);
                    System.arraycopy(appendData, 0, newData, data.length, appendData.length);
                    map.put(inlet.l, newData);
                } else {
                    // (Inlet, Data) ablegen...
                    map.put(inlet.l, removeArrivedInletData(inlet));
                }
            }
            return map;
        }
    }

    protected void passOutletData(Executor executor, HashMap<Outlet, Object[]> outletData) {

        // Executor aus executorThreads entfernen...
        executorThreads.remove(executor.getExecutingNode());

        // ArrivedInletData ablegen...
        for (Map.Entry<Outlet, Object[]> entry : outletData.entrySet()) {
            Outlet outlet = entry.getKey();
            Object[] data = entry.getValue();

            Inlet[] inlets = outlet.getConnectedLets(); // Verbundene Inlets (Kopien)
            for (Inlet inlet : inlets) { // Fuer jedes verbundene Inlet

                // Inlet-Index ermitteln...
                Outlet[] connectedOutlet = inlet.getConnectedLets();
                boolean delivered = false;
                for (int i = 0; i < inlet.getConnectedLetsCount(); i++) {
                    if (connectedOutlet[i] == outlet) {
                        putArrivedInletData(entry.getKey(), new Tuple<>(inlet, (Integer) i), data);
                        delivered = true;
                    }
                }
                if (!delivered) {
                    System.err.println("InletData " + inlet.getName() + " konnte nicht abgelegt werden!");
                }
            }
        }

        debugger.threadFinished(executor);
        incrementChangeCounterForUI();

    }

    /**
     * Bricht Ausfuehrung von Projekt ab falls dieses nicht bereits abgebrochen
     * wurde.
     *
     * @return true falls Projekt noch nicht gestoppt wurde, false falls doch.
     */
    public boolean cancel() {
        if (!canceled) {
            for (Thread executorThread : executorThreads.values()) {
                executorThread.interrupt();
                canceled = true;
            }
            incrementChangeCounterForUI();
            return true;
        } else {
            return false;
        }
    }

    public boolean isCanceled() {
        return canceled;
    }

    public boolean isFinished() {
        return executorThreads.size() == 0;
    }

    /**
     * Gibt an ob UIs sich neu zeichnen sollen.
     *
     * @param lastChangeCounter long welcher beim letzten Aufruf dieser Methode
     * zuruekgegeben wurde oder -1 falls erster Aufruf.
     * @return changeCounter (Dieser muss gemerkt werden und beim naechten
     * aufruf dieser Methode mitgegeben werde) oder -1 falls es keine Aenderung
     * gab.
     */
    public long needsRedrawOfUI(long lastChangeCounter) { // nicht syncronized
        long changeCounter = changeCounterForUI.get();
        if (lastChangeCounter < changeCounter) {
            return changeCounter; // redraw noetig
        } else {
            return -1; // Kein redraw noetig
        }
    }
    
}
