package projectrunner.executor.foreachs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import logging.AdditionalLogger;
import model.Node;
import model.check.NodeCheckWarning;
import model.pathfinder.FindTest;
import model.pathfinder.PathDirection;
import model.pathfinder.PathFinder;
import model.pathfinder.TestResult;
import reflection.nodedefinitions.specialnodes.fors.ForEachNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ReduceNodeDefinition;
import utils.structures.tuples.Pair;

public class ForEachAndReduceRelations {

    private final HashSet<Pair</*ForEach*/Node, /*Reduce*/ Node>> relations;

    public ForEachAndReduceRelations(Node[] nodes) {
        this.relations = new HashSet<>();

        for (Node node : nodes) {
            if (node.getDefinition() instanceof ReduceNodeDefinition) {
                Node forEachNode = PathFinder.findNodeRecursive(node, new FindTest() {
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
                if (forEachNode == null) {
                    throw new RuntimeException("Lose Verknuepfung von ForEach und Reduce");
                } else {
                    relations.add(new Pair<>(forEachNode, node));
                }
            }
        }
        AdditionalLogger.out.println("ForEachAndReduceRelations: " + count() + " Relationen gefunden");
        
    }

    public Node[] getReduceNodesForForEachNode(Node forEachNode) {
        ArrayList<Node> reduceNodes = new ArrayList<>();
        for (Pair<Node, Node> relation : relations) {
            if (relation.l == forEachNode) {
                reduceNodes.add(relation.r);
            }
        }
        return reduceNodes.toArray(new Node[0]);
    }

    public Node getForEachForReduceNode(Node reduceNode) {
        for (Pair<Node, Node> relation : relations) {
            if (relation.r == reduceNode) {
                return relation.l;
            }
        }
        return null;
    }

    public int count() {
        return relations.size();
    }
}
