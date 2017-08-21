package model.resourceloading.nodedefinitionslibrary;

import reflection.common.NodeDefinition;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import logging.*;
import model.Node;
import model.resourceloading.*;
import main.startuptasks.ProgressIndicator;
import utils.ArrayHelper;
import static utils.files.ClassGrabber.*;
import utils.structures.*;

/**
 * Diese Klasse representiert eine Ansammlung von Nodes welche in einem
 * Verzeichniss gesammelt sind.
 */
public class NodeDefinitionsLibrary {

    private static String classNamesToString(String[] classes) {
        if (classes.length == 0) {
            return "0 Klassen";
        } else if (classes.length == 1) {
            return classes[0];
        } else {
            return classes.length + " Klassen (" + classNamesAppendToString(classes) + ")";
        }
    }

    private static String classNamesAppendToString(String[] classes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < classes.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            if (i == 3 && classes.length > 7) {
                sb.append("...");
                break;
            }
            sb.append(classes[i]);
        }
        return sb.toString();
    }

    /**
     * Vorhandene Kategorien.
     */
    private final TreeSet<String> categorys = new TreeSet<>();

    private final TreeMap<String, NodeDefinition> map = new TreeMap<>();

    /**
     * NodeDefinitionen welche ueber Resource zur Verfuegung gestellt werden.
     */
    private final ArrayList<NodeDefinition> nodeDefinitions = new ArrayList<NodeDefinition>();

    /**
     * Erstellt NodeDefinitionsResource aufgrund eines Wurzelverzeichnisses.
     */
    public NodeDefinitionsLibrary(final String path, String classPrefix) {
        ArrayList<String> notLoaded = new ArrayList<>();
        try {
            ClassLoader classLoader = InstanceLoader.createClassLoader(path);
            HashSet<String> uniqueNames = new HashSet<>();
            AdditionalLogger.out.println("Suche in " + path + " nach Klassen.");
            String[] classnames;
            try {
                classnames = getListOfClassnamesInDirectory(path);
            } catch (IllegalArgumentException e) {
                AdditionalLogger.out.println("Klassenpfad existiert nicht (/noch nicht).");
                classnames = new String[0];
            }
            AdditionalLogger.out.println(classnames.length + " Klassen gefunden.");
            
            for (String classname : classnames) { // fuer jede Klasse in Verzeichniss
                if (classname.startsWith(classPrefix)) {
                    try {
                        
                        NodeDefinition nodeDefinition = InstanceLoader.loadNodeDefinition(classLoader, classname);
                        
                        boolean r = uniqueNames.add(nodeDefinition.getUniqueName());
                        if (!r) {
                            AdditionalLogger.err.println(nodeDefinition.getUniqueName() + " ist bereits in NodeDefinitionLibrary geladen!");
                        }
                        nodeDefinitions.add(nodeDefinition);
                        String description = nodeDefinition.getDescription();

                        // Kategorien bestimmen...
                        int nextOpen = 0, nextClose = 0;
                        while (nextOpen != -1) {
                            nextOpen = description.indexOf("[", nextOpen + 1);
                            if (nextOpen != -1) {
                                nextClose = description.indexOf("]", nextOpen + 1); // ab da wo [ geoeffnet wurde
                                categorys.add(description.substring(nextOpen + 1, nextClose));
                            }
                        }

                    } catch (Exception e) {
                        notLoaded.add(classname);
                        //AdditionalLogger.err.println(classname + " kann nicht geladen werden oder erfuellt nicht das Interface NodeDefinition! (" + e.getMessage() + ")");
                    }
                }
            }
            if (notLoaded.size() > 0) {
                AdditionalLogger.out.println(classNamesToString(notLoaded.toArray(new String[0])) + " wurde/wurden nicht in NodeDefinitionsLibrary geladen da diese nicht die Kriterien erfuellen oder es zu einem Fehler kam.");
            }

            // Daten sortieren...
            Collections.sort(nodeDefinitions, new Comparator<NodeDefinition>() {
                @Override
                public int compare(NodeDefinition n1, NodeDefinition n2) {
                    return n1.getName().compareTo(n2.getName());
                }
            });

            // Map befuellen...
            for (NodeDefinition nodeDefinition : nodeDefinitions) {
                String uniqueName = UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion(nodeDefinition);
                //AdditionalLogger.out.println(uniqueName + " zu NodeDefinitionsLibrary hinzugefuegen geladen (" + nodeDefinition.getName() + ").");
                if (map.put(uniqueName, nodeDefinition) != null) {
                    //AdditionalLogger.err.println(uniqueName + " wurde doppelt hinzugefuegt (" + nodeDefinition.getName() + ")!");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Fehler bei laden von NodeDefinitionsLibrary! (" + e.getClass().getName() + ", " + e.getMessage() + ")");
        }

    }

    protected final void addNodeDefinition(NodeDefinition definition) {
        String uniqueName = UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion(definition);
        if (!map.containsKey(uniqueName)) {
            nodeDefinitions.add(definition);
            map.put(uniqueName, definition);
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
     * Sucht nach einer NodeDefinition welche auf UniqueNameVersion passt. Falls
     * keine passende gefunden wurde gibt die Methode null zuruek.
     */
    public NodeDefinition get(String uniqueNameVersion) {
        /*for (NodeDefinition nodeDefinition : nodeDefinitions) {
            if (UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion(nodeDefinition).equalsIgnoreCase(uniqueNameVersion)) {
                return nodeDefinition;
            }
        }*/
        return map.get(uniqueNameVersion);
        //System.err.println(uniqueNameVersion + " nicht gefunden. Return null."); // Moeglicherweise gewolltes Verhalten
        //return null;
    }

    public TreeSet<String> getCategorys() {
        return categorys;
    }
}
