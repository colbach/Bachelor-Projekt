package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import model.Node;
import model.resourceloading.NodeDefinitionsLibrary;
import reflection.NodeDefinition;
import settings.ProjectSettings;
import utils.measurements.Area;

/**
 * Representiert ein vom Benutzer angelegtes Projekt.
 */
public class Project {

    public static final String FOLDER_FOR_PROJECT_NODES_CLASSES = "classes";
    public static final String FOLDER_FOR_PROJECT_STRUCTURE = "model";
    public static final String FILE_FOR_PROJECT_SETTINGS = "properties.txt";

    private String projectPath;
    private ArrayList<Node> nodes;
    private Node startNode = null;
    private HashSet<Node> breakPoint;
    private NodeDefinitionsLibrary projectNodeDefinitionsLibrary;
    
    private final ProjectSettings projectSettings;

    /**
     * Erzeugt Instanz eines Projektes unter angabe aller Knoten und des
     * Projekt-Wurzel-Pfades.
     */
    public Project(Node[] nodes, String projectPath) {
        this.projectPath = projectPath;
        this.nodes = new ArrayList<>(Arrays.asList(nodes));
        this.projectSettings = new ProjectSettings(FILE_FOR_PROJECT_SETTINGS);
        this.breakPoint = new HashSet<>();
        initProjectNodeDefinitionsLibrary();
    }

    /**
     * Erzeugt Instanz eines Projektes unter angabe aller Knoten.
     * Projekt-Wurzel-Pfades muss nachtraeglich gesetzt werden damit das Projekt
     * abgespeichert werden kann.
     */
    public Project(Node[] nodes) {
        this(nodes, null);
    }

    /**
     * Erzeugt Instanz eines leeren Projektes. Projekt-Wurzel-Pfades muss
     * nachtraeglich gesetzt werden damit das Projekt abgespeichert werden kann.
     */
    public Project() {
        this(new Node[0]);
    }

    /**
     * Fuegt Node zum Projekt hinzu.
     */
    public synchronized void addNode(Node node) {
        if (node != null) {
            nodes.add(node);
        } else {
            System.err.println("node ist null und kann nicht hinzugefuegt werden!");
        }
    }
    
    public synchronized boolean removeNode(Node node) {
        
        if (node != null) { // Falls node nicht null
            
            // Nodes entfernen welche relativ zu diesem Node stehen...
            for(Inlet connectedInlet : node.getConnectedInlets()) {
                for(Outlet connectedInletconnectedOutlet : connectedInlet.getConnectedLets()) {
                    Node potentialRelativeNode = connectedInletconnectedOutlet.getNode();
                    if(potentialRelativeNode.isRelativeNode()) { // Falls es sich um ein relativen Node handelt
                        removeNode(potentialRelativeNode);
                    }
                }
            }
            
            // Verbindungen trennen...
            node.capConnections();
            
            // Node aus Projekt loeschen...
            boolean r = this.nodes.remove(node);
            if(node == this.startNode) {
                this.startNode = null;
            }
            return r;
        } else {
            System.err.println("node ist null und kann nicht geloecht werden!");
            return false;
        }
    }

    /**
     * Fuegt Node via NodeDefinition zum Projekt hinzu.
     */
    public synchronized void addNode(NodeDefinition definition, int x, int y) {
        if (definition != null) {
            nodes.add(new Node(definition, x, y));
        } else {
            System.err.println("definition ist null und kann nicht hinzugefuegt werden!");
        }
    }
    
    public synchronized void setNodeToTop(Node node) {
        if (node != null) {
            nodes.remove(node);
            nodes.add(node);
        } else {
            System.err.println("node ist null und kann damit auch nicht nach oben geschoben werden!");
        }
    }

    public synchronized Node[] getNodes() {
        return nodes.toArray(new Node[0]);
    }
    
    public synchronized int getNodeCount() {
        return nodes.size();
    }
    
    public synchronized int getConnectionsCount() {
        int count = 0;
        for(Node node : nodes) {
            for(Outlet outlet : node.getOutlets()) {
                count += outlet.getConnectedLetsCount();
            }
        }
        return count;
    }

    private synchronized void initProjectNodeDefinitionsLibrary() {
        projectNodeDefinitionsLibrary = new NodeDefinitionsLibrary(projectPath + "/" + FOLDER_FOR_PROJECT_NODES_CLASSES);
    }

    public synchronized NodeDefinitionsLibrary getProjectNodeDefinitionsLibrary() {
        return projectNodeDefinitionsLibrary;
    }

    public synchronized String getProjectPath() {
        return projectPath;
    }

    public synchronized void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
        if (projectPath != null) {
            initProjectNodeDefinitionsLibrary(); // neuinitialisieren der projectNodeDefinitionsLibrary 
        }
    }

    /**
     * Gibt Flaeche zuruek auf welcher sich Nodes befinden. Beruecksichtigt wird
     * immer nur der Mittelpunkt von Nodes.
     */
    public synchronized Area getNeededProjectArea() {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        if (nodes.size() == 0) { // Falls es keine Nodes gibt
            return new Area(0, 0, 0, 0);
        } else {
            for (Node node : nodes) {
                int x = node.getUICenterX();
                int y = node.getUICenterY();
                if (x > maxX) {
                    maxX = x;
                }
                if (x < minX) {
                    minX = x;
                }
                if (y > maxY) {
                    maxY = y;
                }
                if (y < minY) {
                    minY = y;
                }
            }
            int width = maxX - minX;
            int height = maxY - minY;
            return new Area(minX, minY, width, height);
        }
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public ProjectSettings getProjectSettings() {
        return projectSettings;
    }
    
}
