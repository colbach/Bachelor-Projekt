package model.runproject.debugger;

import java.util.HashSet;
import model.Node;

public class Breakpoints {
    
    private static Breakpoints instance = null;

    public synchronized static Breakpoints getInstance() { // Die verwendung eines Singleton stellt hier kein problem dar da Unabhaengig von Projekt Nodes zu identifizieren sind.
        if(instance == null) {
            instance = new Breakpoints();
        }
        return instance;
    }
    
    private final HashSet<Node> breakpoints;

    private Breakpoints() {
        breakpoints = new HashSet<Node>();
    }
    
    public synchronized boolean add(Node e) {
        return breakpoints.add(e);
    }

    public synchronized boolean remove(Object o) {
        return breakpoints.remove(o);
    }
    
    public synchronized boolean is(Node node) {
        return breakpoints.contains(node);
    }
    
    public synchronized void clear() {
        breakpoints.clear();
    }
}
