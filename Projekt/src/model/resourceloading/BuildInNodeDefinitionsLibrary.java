package model.resourceloading;

import static main.MainClass.PATH_FOR_BUILDIN_NODES_CLASSES;

/**
 * NodeDefinitionsLibrary welche alle eingebauten (mitgelieferten)
 NodeDefinitions enthaelt.
 */
public class BuildInNodeDefinitionsLibrary extends NodeDefinitionsLibrary {
    
    /**
     * Singleton der eigenen Klasse, es darf nur eine Instanz geben.
     */
    private static BuildInNodeDefinitionsLibrary buildInNodeDefinitionsLibrary;
    
    /**
     * Privater Konstruktor damit keine weiteren Objekte erzeugt werden koennen.
     */
    private BuildInNodeDefinitionsLibrary() {
        super(PATH_FOR_BUILDIN_NODES_CLASSES);
    }
    
    /**
     * Gibt einzige Instanz der Klasse BuildInNodeDefinitionsLibrary zuruek.
     */
    public static synchronized BuildInNodeDefinitionsLibrary getInstance() {
        if(buildInNodeDefinitionsLibrary == null) {
            buildInNodeDefinitionsLibrary = new BuildInNodeDefinitionsLibrary();
        }
        return buildInNodeDefinitionsLibrary;
    }
    
}
