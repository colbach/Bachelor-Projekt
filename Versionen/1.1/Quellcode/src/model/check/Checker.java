package model.check;

import java.io.IOException;
import reflection.nodedefinitions.specialnodes.ifs.IfForwardNodeDefinition;
import reflection.nodedefinitions.specialnodes.ifs.IfBackNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ForEachNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ReduceNodeDefinition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;
import model.pathfinder.*;
import reflection.additionalnodedefinitioninterfaces.CompatibilityTestable;
import reflection.customdatatypes.SourceCode;
import reflection.nodedefinitions.functions.*;
import settings.GeneralSettings;
import utils.codecompilation.CodeCompiler;
import utils.codecompilation.CompilationError;

public class Checker {

    private static String nodesToString(Set<Node> nodes, boolean userReadable) {
        return nodesToString(nodes.toArray(new Node[0]), userReadable);
    }

    private static String nodesToString(Node[] nodes, boolean userReadable) {
        if (nodes.length == 0) {
            if (userReadable) {
                return "0 Elemente";
            } else {
                return "0 Nodes";
            }
        } else if (nodes.length == 1) {
            return nodes[0].getName();
        } else {
            if (userReadable) {
                return nodes.length + " Elemente (" + nodesAppendToString(nodes) + ")";
            } else {
                return nodes.length + " Nodes (" + nodesAppendToString(nodes) + ")";
            }
        }
    }

    private static String nodesAppendToString(Node[] nodes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nodes.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            if (i == 5 && nodes.length > 7) {
                sb.append("...");
                break;
            }
            sb.append(nodes[i].getName());
        }
        return sb.toString();
    }

    public static CheckResult checkProject(Project project) {

        CheckResult result = new CheckResult();

        Node[] allNodes = project.getNodes();
        ArrayList<NodeGroup> nodeGroups = new ArrayList<>();
        HashSet<Node> reachableNodes = new HashSet<>();
        for (Node node : allNodes) {
            if (node.isRunContextCreator()) {
                NodeGroup nodeGroup = new NodeGroup(node);
                nodeGroups.add(nodeGroup);
                reachableNodes.addAll(nodeGroup.getNodes());
            }
        }

        // Komptibilitaetstests durchfuehren...
        for (Node node : allNodes) {
            if (node instanceof CompatibilityTestable) {
                String compatibilityTestableResult = ((CompatibilityTestable) node).testForCompatibility();
                if (compatibilityTestableResult != null) {
                    result.add(new CompatibilityCheckWarning(compatibilityTestableResult, compatibilityTestableResult, node));
                }
            }
        }

        // Nach nicht erreichbaren Nodes suchen...
        if (settings.GeneralSettings.getInstance().getBoolean(GeneralSettings.CHECK_FOR_UNREACHABLE_NODES_KEY, true)) {
            HashSet<Node> notReachableNodes = new HashSet<>();
            for (Node node : allNodes) {
                if (!reachableNodes.contains(node)) {
                    notReachableNodes.add(node);
                }
            }
            if (!notReachableNodes.isEmpty()) {
                result.add(new NodeCheckWarning(
                        nodesToString(notReachableNodes, false) + " können keinem keinem ContextCreator zugeordnet werden.",
                        nodesToString(notReachableNodes, true) + " sind/ist nicht erreichbar.",
                        true,
                        null
                ));
            }
        }

        // Nach mehreren ContextCreator-Nodes in einer Gruppe suchen...
        for (NodeGroup group : nodeGroups) {
            ArrayList<Node> contextCreators = new ArrayList<>();
            for (Node node : group.getNodes()) {
                if (node.isRunContextCreator()) {
                    contextCreators.add(node);
                }
            }
            if (contextCreators.size() > 1) {
                result.add(new NodeCheckWarning(
                        contextCreators.size() + " ContextCreators in einer Gruppe.",
                        "Mehrere Kontexterzeugende Elemente (" + contextCreators.size() + " Stück) in einer Gruppe.",
                        false,
                        null
                ));
            }
        }

        // Alle ContextCreators suchen...
        HashSet<Node> contextCreators = new HashSet<>();
        for (Node node : reachableNodes) {
            if (node.isRunContextCreator()) {
                contextCreators.add(node);
            }
        }

        // Kontrolle ob nicht geladene NodeDefinitionen enthalten sind...
        for (Node node : reachableNodes) {
            if (node.getDefinition().getName().equals("?")) {
                result.add(new NodeCheckWarning(
                        "Die Elementen-Definition " + node.getUniqueNameVersion() + " wurde nicht gefunden",
                        "Die Definition für ein Element (" + node.getUniqueNameVersion() + ") wurde nicht geladen da dieses nicht gefunden wurde.",
                        false,
                        node
                ));
            }
        }

        // Kontrolle ob ueberhaupt Nodes vorhanden sind...
        if (reachableNodes.isEmpty()) {
            result.add(new SimpleCheckWarning(
                    "nodes.size() ist 0.",
                    "Projekt ist leer. Ein Leeres Projekt kann nicht ausgeführt werden.",
                    false));
        }

        // Kontrolle ob ContextCreator in Projekt vorhanden ist...
        if (contextCreators.isEmpty()) {
            result.add(new SimpleCheckWarning(
                    "Kein ContextCreator gefunden.",
                    "Kein Kontexterzeugendes Element im Projekt vorhanden.",
                    false));
        }

        // Kontrolle ob Function-Start-Nodes Function-Return-Nodes haben
        for (Node node : reachableNodes) {
            if (node.getDefinition() instanceof FunctionStartNodeDefinition) {
                boolean pathExists = PathFinder.pathExists(node, new FindTest() {
                    @Override
                    public TestResult test(Node node1) {
                        if (node1.getDefinition() instanceof FunctionReturnNodeDefinition) {
                            return TestResult.FOUND;
                        } else {
                            return TestResult.CONTINUE;
                        }
                    }
                }, PathDirection.FORWARD);
                if (!pathExists) {
                    result.add(new NodeCheckWarning(
                            "FunctionReturnNode fuer FunctionStartNode nicht gefunden.",
                            "Funktion-Start-Element muss folgendes Funktion-Rurückgabe-Element besitzen.",
                            false,
                            node
                    ));
                }
            }
        }

        // Kontrolle ob Function-Return-Nodes haben Function-Start-Nodes...
        for (Node node : reachableNodes) {
            if (node.getDefinition() instanceof FunctionReturnNodeDefinition) {
                boolean pathExists = PathFinder.pathExists(node, new FindTest() {
                    @Override
                    public TestResult test(Node node1) {
                        if (node1.getDefinition() instanceof FunctionStartNodeDefinition) {
                            return TestResult.FOUND;
                        } else {
                            return TestResult.CONTINUE;
                        }
                    }
                }, PathDirection.BACKWARD);
                if (!pathExists) {
                    result.add(new NodeCheckWarning(
                            "FunctionStartNode fuer FunctionReturnNode nicht gefunden.",
                            "Funktion-Rückgabe-Element muss vorhergehendes Funktion-Start-Element besitzen.",
                            true,
                            node
                    ));
                }
            }
        }

        // Kontrolle ob es alleinstehende contextCreators gibt...
        for (Node contextCreator : contextCreators) {
            boolean outletFound = false;
            outletloop:
            for (Outlet outlet : contextCreator.getOutlets()) {
                if (outlet.getConnectedLetsCount() > 0) {
                    outletFound = true;
                    break outletloop;
                }
            }
            if (!outletFound) {
                result.add(new NodeCheckWarning(
                        "Der ContextCreator " + contextCreator.getName() + " hat keine verbundenen Outlets.",
                        "Kontexterzeugendes Element " + contextCreator.getName() + " ist alleinstehend und erfüllt damit keinen Zweck.",
                        true,
                        contextCreator
                ));
            }
        }

        // Kontrolle ob bei IfForwardNode Inlet 1 besetzt ist...
        for (Node node : reachableNodes) {
            if (node.getDefinition() instanceof IfForwardNodeDefinition) {
                if (!node.getInlet(1).isConnected()) {
                    result.add(new NodeCheckWarning(
                            "IfForwardNodeDefinition ist nicht an Inlet 1 verbunden.",
                            "Wenn-Vor-Element ist nicht mit einem Wahrheitswert verbunden.",
                            false,
                            node
                    ));
                }
            }
        }

        // Kontrolle ob bei IfForwardNode Inlet 1 besetzt ist...
        for (Node node : reachableNodes) {
            if (node.getDefinition() instanceof IfBackNodeDefinition) {
                if (!node.getInlet(2).isConnected()) {
                    result.add(new NodeCheckWarning(
                            "IfBackNodeDefinition ist nicht an Inlet 2 verbunden.",
                            "Wenn-Zurück-Element ist nicht mit einem Wahrheitswert verbunden.",
                            false,
                            node
                    ));
                }
            }
        }

        // Kontrolle ob Reduce-Nodes bezug zu For-Each-Nodes haben
        for (Node node : reachableNodes) {
            if (node.getDefinition() instanceof ReduceNodeDefinition) {
                boolean pathExists = PathFinder.pathExists(node, new FindTest() {
                    int depths = 0;

                    @Override
                    public TestResult test(Node node1) {
                        if (node1.isRunContextCreator()) {
                            return TestResult.ABORD;
                        } else if (node1.getDefinition() instanceof ForEachNodeDefinition) {
                            if (depths == 0) {
                                return TestResult.FOUND;
                            } else {
                                depths--;
                                return TestResult.CONTINUE;
                            }
                        } else {
                            return TestResult.CONTINUE;
                        }
                    }
                }, PathDirection.BACKWARD);
                if (!pathExists) {
                    result.add(new NodeCheckWarning(
                            "Pfadsuche vor ReduceNode hat keine ForEachNode gefunden.",
                            "Sammeln-Sturktur-Element muss vorhergehendes For-Each-Sturktur-Element haben.",
                            false,
                            node
                    ));
                }
            }
        }

        // Kontrolle ob alle Nicht-Optionalen Inlets verbunden sind...
        for (Node node : reachableNodes) {
            for (Inlet inlet : node.getInlets()) {
                if (!inlet.isOptional() && !inlet.isConnected()) {
                    result.add(new NodeCheckWarning(
                            inlet.getName() + " von " + node.getName() + " ist nicht verbunden.",
                            "Der Eingang " + inlet.getName() + " von " + node.getName() + " muss verbunden sein. (Nicht optionaler Eingang)",
                            false,
                            node
                    ));
                }
            }
        }

        forEveryNode:
        for (Node node : reachableNodes) {
            if (node.hasUserSettableContent()) {
                Object[] userSettableContents = node.getUserSettableContent();
                for (Object userSettableContent : userSettableContents) {
                    if (userSettableContent instanceof SourceCode) {
                        SourceCode sourceCode = (SourceCode) userSettableContent;
                        try {
                            CodeCompiler.getInstance().compileAndLoadClass(sourceCode);
                        } catch (IOException | CompilationError ex) {
                            if (CodeCompiler.isJDKInstalled()) {
                                result.add(new NodeCheckWarning(
                                        "Unkompilierbarter Javacode (" + ex + ")",
                                        "Unkompilierbarter Javacode.",
                                        false,
                                        node
                                ));
                            } else {
                                result.add(new NodeCheckWarning(
                                        "Java Development Kit nicht gefunden.",
                                        "Java Development Kit nicht gefunden. Dieser ist notwendig um Javacode zur Laufzeit zu kompilieren.",
                                        false,
                                        node
                                ));
                                break forEveryNode;
                            }
                        }
                    }
                }
            }
        }

        return result;
    }
}
