package documentationgenerator;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import model.resourceloading.NodeDefinitionDescription;
import model.resourceloading.nodedefinitionslibrary.BuildInNodeDefinitionsLibrary;
import reflection.ContextCreator;
import reflection.NodeDefinition;
import reflection.nodedefinitions.specialnodes.SpecialNodeDefinition;

public class DocumentationGenerator {

    public static void main(String[] args) {

        BuildInNodeDefinitionsLibrary nodeDefinitionsLibrary = BuildInNodeDefinitionsLibrary.getInstance();

        TreeMap<String, NodeDefinition> specialNodes = new TreeMap<>();
        TreeMap<String, NodeDefinition> contextCreatorNodes = new TreeMap<>();
        TreeMap<String, NodeDefinition> normalNodes = new TreeMap<>();

        for (int i = 0; i < nodeDefinitionsLibrary.size(); i++) {

            NodeDefinition get = nodeDefinitionsLibrary.get(i);

            if (get instanceof SpecialNodeDefinition) {
                NodeDefinition old = specialNodes.put(get.getName(), get);
                if (old != null) {
                    System.err.println("Achtung: NodeDefinition mit Namen \"" + get.getName() + "\" bereits gefunden! (" + get + ")");
                }
            } else if (get instanceof ContextCreator) {
                NodeDefinition old = contextCreatorNodes.put(get.getName(), get);
                if (old != null) {
                    System.err.println("Achtung: NodeDefinition mit Namen \"" + get.getName() + "\" bereits gefunden! (" + get + ")");
                }
            } else {
                NodeDefinition old = normalNodes.put(get.getName(), get);
                if (old != null) {
                    System.err.println("Achtung: NodeDefinition mit Namen \"" + get.getName() + "\" bereits gefunden! (" + get + ")");
                }
            }

        }

        System.out.println("Einfache Elemente:");
        generate(normalNodes);

        System.out.println("Spezielle Elemente:");
        generate(specialNodes);

        System.out.println("Kontext Erzeugende Elemente:");
        generate(contextCreatorNodes);

    }
    
    public static void generate(Map<String, NodeDefinition> nodes) {
        for (String key : nodes.keySet()) {
            System.out.println("   " + key);
            NodeDefinition node = nodes.get(key);
            String beschreibung = NodeDefinitionDescription.getDescriptionWithoutTags(node);
            System.out.println("      " + beschreibung);
        }
    }

}
