package model.resourceloading.nodedefinitionslibrary;

import static main.MainClass.BUILDIN_NODE_CLASSES_CLASSNAME_PREFIX;
import model.directinput.DirectInputNodeDefinition;
import reflection.common.NodeDefinition;
import reflection.nodedefinitions.specialnodes.firstvalues.AValueNodeDefinition;
import reflection.nodedefinitions.specialnodes.firstvalues.FastestValueNodeDefinition;
import reflection.nodedefinitions.specialnodes.firstvalues.FirstValueNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ForEachNodeDefinition;
import reflection.nodedefinitions.specialnodes.ifs.IfBackNodeDefinition;
import reflection.nodedefinitions.specialnodes.ifs.IfForwardNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ReduceNodeDefinition;
import static main.MainClass.pathForBuildinNodeClasses;

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
        super(pathForBuildinNodeClasses, BUILDIN_NODE_CLASSES_CLASSNAME_PREFIX);
//        addNodeDefinition(new IfBackNodeDefinition());
//        addNodeDefinition(new IfForwardNodeDefinition());
//        addNodeDefinition(new ForEachNodeDefinition());
//        addNodeDefinition(new ReduceNodeDefinition());
//        addNodeDefinition(new AValueNodeDefinition());
//        addNodeDefinition(new FastestValueNodeDefinition());
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

    @Override
    public NodeDefinition get(String uniqueNameVersion) {
        if(uniqueNameVersion.startsWith(DirectInputNodeDefinition.UNIQUE_NAME_PREFIX)) { // Spezialfall: DirectInputNodeDefinition
            return DirectInputNodeDefinition.createDirectInputNodeDefinitionByUniqueName(uniqueNameVersion);
        }
        return super.get(uniqueNameVersion);
    }
}
