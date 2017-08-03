package model.pathfinder;

import java.util.HashSet;
import model.Inlet;
import model.Node;
import model.Outlet;
import static model.pathfinder.PathFinder.findNodeRecursive;
import utils.structures.Tuple;

public class NodeGroup {
    
    public static HashSet<Node> collectNodes(Node node) {
        HashSet<Node> collectedNodes = new HashSet<>();
        collectNodesRecursive(node, collectedNodes);
        return collectedNodes;
    }
    
    public static void collectNodesRecursive(Node node, HashSet<Node> collectedNodes) {
        if(!collectedNodes.contains(node)) {
            collectedNodes.add(node);
            for(Inlet inlet : node.getInlets()) {
                for(Outlet outlet : inlet.getConnectedLets()) {
                    collectNodesRecursive(outlet.getNode(), collectedNodes);
                }
            }
            for(Outlet outlet : node.getOutlets()) {
                for(Inlet inlet : outlet.getConnectedLets()) {
                    collectNodesRecursive(inlet.getNode(), collectedNodes);
                }
            }
        }
    }
    
    private HashSet<Node> nodes;
    private Node root;

    public NodeGroup(Node root) {
        this.nodes = collectNodes(root);
        this.root = root;
    }

    public HashSet<Node> getNodes() {
        return nodes;
    }

    public Node getRoot() {
        return root;
    }
}
