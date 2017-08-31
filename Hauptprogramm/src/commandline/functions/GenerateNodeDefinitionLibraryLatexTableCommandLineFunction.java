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

public class GenerateNodeDefinitionLibraryLatexTableCommandLineFunction implements CommandLineFunction {

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

        System.out.println("\\chapter{Vollständige Liste der Implementierten Elemente}\\label{anhang}");
        System.out.println("");

        System.out.println("\\section{Einfache Elemente}");
        System.out.println("");
        generate(normalNodes);
        System.out.println("");

        System.out.println("\\section{Spezielle Elemente}");
        System.out.println("");
        generate(specialNodes);
        System.out.println("");

        System.out.println("\\section{Kontext Erzeugende Elemente}");
        System.out.println("");
        generate(contextCreatorNodes);
        System.out.println("");

    }

    public static void generate(Map<String, NodeDefinition> nodes) {
        System.out.println("");
        printT();
        for (String key : nodes.keySet()) {
            String s = "";

            NodeDefinition node = nodes.get(key);
            String beschreibung = NodeDefinitionDescription.getDescriptionWithoutTags(node);

            beschreibung += "\n";

            final String IN_TITLE = "\\textbf{Eingänge:}";
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
                    if (!node.isInletEngaged(i)) {
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

            final String OUT_TITLE = "\\textbf{Ausgänge:}";
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
            
            print(node.getIconName(), node.getName().replace("\\", "\\textbackslash{}").replace("^", "\\textasciicircum{}").replace("&", "\\&"), beschreibung.replace("\\", "\\textbackslash{}").replace("&", "\\&").replaceAll("\n", " (LINEBREAK) ").replace("^", "\\textasciicircum{}"));
        }
        printB();
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
        return "nodesdoclatex";
    }

    @Override
    public String getAliases() {
        return "nodesdocumentlatex ndl";
    }

    public static void print(String icon, String name, String description) {
        System.out.println("\\cline{1-3}\n"
                + "\\endhead\n"
                + "\\begin{minipage}[c]{0.15\\columnwidth}\\centering\\strut\n"
                + "	\\includegraphics{Grafiken/assets50x50/" + icon + "}\\strut\n"
                + "\\end{minipage}\n"
                + "&\n"
                + "\\begin{minipage}[b]{0.2\\columnwidth}\\centering\\strut\n"
                + "	" + name + "\\strut\n"
                + "\\end{minipage}\n"
                + "&\n"
                + "\\begin{minipage}[b]{0.6\\columnwidth}\\centering\\strut\n"
                + "	" + description + ".\\strut\n"
                + "\\end{minipage}\\tabularnewline");
    }

    public static void printT() {
        System.out.println("\\begin{longtable}[]{|c|c|c|}\n"
                + "\\cline{1-3}\n"
                + "\\begin{minipage}[b]{0.15\\columnwidth}\\centering\\strut\n"
                + "	Icon\\strut\n"
                + "\\end{minipage}\n"
                + "&\n"
                + "\\begin{minipage}[b]{0.2\\columnwidth}\\centering\\strut\n"
                + "	Name\\strut\n"
                + "\\end{minipage}\n"
                + "&\n"
                + "\\begin{minipage}[b]{0.6\\columnwidth}\\centering\\strut\n"
                + "	Beschreibung\\strut\n"
                + "\\end{minipage}\\tabularnewline");
    }

    public static void printB() {
        System.out.println("\\cline{1-3}\n"
                + "\\end{longtable}");
    }

}
