package commandline.functions;

import commandline.Aliases;
import commandline.CommandLine;
import commandline.CommandLineFooterHeaderFormat;
import commandline.CommandLineFunction;
import java.util.Map;
import java.util.TreeMap;
import model.resourceloading.NodeDefinitionDescription;
import model.resourceloading.nodedefinitionslibrary.BuildInNodeDefinitionsLibrary;
import model.type.Type;
import reflection.additionalnodedefinitioninterfaces.VariableVisibleInletCount;
import reflection.common.ContextCreator;
import reflection.common.NodeDefinition;
import reflection.nodedefinitions.specialnodes.SpecialNodeDefinition;
import static utils.text.CharacterRepeateHelper.*;

public class GenerateNodeDefinitionLibraryMarkdownTableCommandLineFunction implements CommandLineFunction {

    @Override
    public void execute(String[] param, CommandLine commandLine) {
        
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

        System.out.println("# Anhang *Vollständige Liste der Implementierten Elemente*");
        System.out.println("");

        System.out.println("## Einfache Elemente");
        System.out.println("");
        generate(normalNodes);
        System.out.println("");

        System.out.println("## Spezielle Elemente");
        System.out.println("");
        generate(specialNodes);
        System.out.println("");

        System.out.println("## Kontext Erzeugende Elemente");
        System.out.println("");
        generate(contextCreatorNodes);
        System.out.println("");

    }
    
    public static void generate(Map<String, NodeDefinition> nodes) {
        System.out.println("");
        System.out.println("Icon | Name | Beschreibung");
        System.out.println(":--- | :---: | :---");
        for (String key : nodes.keySet()) {
            String s = "";

            NodeDefinition node = nodes.get(key);
            String beschreibung = NodeDefinitionDescription.getDescriptionWithoutTags(node);
            
            beschreibung += "\n";

            final String IN_TITLE = "**Eingänge:**";
            String inString = IN_TITLE;
            if (node instanceof VariableVisibleInletCount) {
                Type type = new Type(node.getClassForInlet(0), node.isInletForArray(0));
                inString += " Dynamische Liste von Elementen des Types " + type.toString();
            } else {
                boolean first = true;
                int inletCount = node.getInletCount();
                for (int i = 0; i < inletCount; i++) {
                    Type type = new Type(node.getClassForInlet(i), node.isInletForArray(i));
                    String inletDescription = node.getNameForInlet(i) + " (" + type.toString() + ")";
                    if(!node.isInletEngaged(i)) {
                        inletDescription += " [Opt.]";
                    }
                    if (first) {
                        inString += " " + inletDescription;
                    } else {
                        inString += ", " + inletDescription;
                    }
                    first = false;
                }
                if (inletCount == 0) {
                    inString += " /";
                }
            }
            beschreibung += "\n" + inString;

            final String OUT_TITLE = "**Ausgänge:**";
            String outString = OUT_TITLE;
            {
                boolean first = true;
                int outletCount = node.getOutletCount();
                for (int i = 0; i < outletCount; i++) {
                    Type type = new Type(node.getClassForOutlet(i), node.isOutletForArray(i));
                    String inletDescription = node.getNameForOutlet(i) + " (" + type.toString() + ")";
                    if (first) {
                        outString += " " + inletDescription;
                    } else {
                        outString += ", " + inletDescription;
                    }
                    first = false;
                }
                if (outletCount == 0) {
                    outString += " /";
                }
            }
            beschreibung += "\n" + outString;

            s = "![](../Hauptprogramm/assets/" + node.getIconName() + ") | " + node.getName() + " | " + beschreibung.replaceAll("\n", "<br />");

            System.out.println(s);
        }
        System.out.println("");
    }

    @Override
    public String getDescription() {
        return "Gibt Dokumentation von NodeDefinitonLibrary im Markdownformat aus.";
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }

    @Override
    public String getUsage() {
        return getName();
    }

    @Override
    public String getName() {
        return "nodesdocmarkdown";
    }

    @Override
    public String getAliases() {
        return "nodesdocumentmarkdown ndm";
    }
}
