package model.resourceloading.nodedefinitionslibrary;

import reflection.common.NodeDefinition;

/**
 * Diese Klasse dient zum Generieren von UniqueNameVersion-Strings.
 * Ein UniqueNameVersion-String ist ein String welcher eine NodeDefinition
 * inklusive ihrer Version eindeutig bezeichnet.
 */
public class UniqueNameVersionGenerator {
    
    /**
     * Erzeugt UniqueNameVersion-String welcher NodeDefinition inklusive Version eindeutig bezeichnet.
     */
    public static String nodeDefinitionToUniqueNameVersion(String uniqueName, int version) {
        return uniqueName.replace('[', '_')
                .replace(']', '_')
                .replace('\n', '_')
                .replace(',', '_')
                .replace(' ', '_')
                .replace(']', '_')
                + "["
                + version
                + "]";
    }
    
    /**
     * Erzeugt UniqueNameVersion-String welcher NodeDefinition inklusive Version eindeutig bezeichnet.
     */
    public static String nodeDefinitionToUniqueNameVersion(NodeDefinition nodeDefinition) {
        return nodeDefinitionToUniqueNameVersion(nodeDefinition.getUniqueName(), nodeDefinition.getVersion());
    }
    
}
