package model.resourceloading;

import java.util.ArrayList;
import java.util.HashSet;
import reflection.NodeDefinition;

/**
 * Diese Klasse representiert eine Ansammlung von Nodes welche in einem Verzeichniss
 * gesammelt sind.
 */
public class NodeDefinitionsLibrary {

    /**
     * NodeDefinitionen welche ueber Resource zur Verfuegung gestellt werden.
     */
    private final ArrayList<NodeDefinition> nodeDefinitions = new ArrayList<NodeDefinition>();

    /**
     * Erstellt NodeDefinitionsResource aufgrund eines Wurzelverzeichnisses.
     */
    public NodeDefinitionsLibrary(String path) {
                
        try {
            HashSet<String> uniqueNames = new HashSet<>();
            String[] classnames = FileGrabber.getListOfClassnamesInDirectory(path);
            for (String classname : classnames) { // fuer jede Klasse in Verzeichniss
                try {
                    NodeDefinition nodeDefinition = InstanceLoader.loadNodeDefinition(path, classname);
                    boolean r = uniqueNames.add(nodeDefinition.getUniqueName());
                    if(!r) {
                        System.err.println(nodeDefinition.getUniqueName() + " ist bereits in NodeDefinitionLibrary geladen!");
                    }
                    nodeDefinitions.add(nodeDefinition);
                } catch (Exception e) {
                    System.err.println(classname + " kann nicht geladen werden oder erfuellt nicht das Interface NodeDefinition! (" + e.getMessage() + ")");
                }
            }
        } catch (Exception e) {
            System.err.println("NodeDefinitionsLibrary kann nicht geladen werden.");
        }
        
    }
    
    

    /**
     * Gibt Anzahl NodeDefinitionen an.
     */
    public int size() {
        return nodeDefinitions.size();
    }

    /**
     * Gibt NodeDefinition mit dem index i zurueck.
     */
    public NodeDefinition get(int i) {
        return nodeDefinitions.get(i);
    }
    
    /**
     * Gibt NodeDefinitions zurueck.
     */
    public NodeDefinition[] getAll() {
        return nodeDefinitions.toArray(new NodeDefinition[0]);
    }
    
    /**
     * Sucht nach einer NodeDefinition welche auf UniqueNameVersion passt.
     * Falls keine passende gefunden wurde gibt die Methode null zuruek.
     */
    public NodeDefinition get(String uniqueNameVersion) {
        for(NodeDefinition nodeDefinition : nodeDefinitions) {
            if(UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion(nodeDefinition).equalsIgnoreCase(uniqueNameVersion)) {
                return nodeDefinition;
            }
        }
        System.err.println(uniqueNameVersion + " nicht gefunden. Return null.");
        return null;
    }
    
}