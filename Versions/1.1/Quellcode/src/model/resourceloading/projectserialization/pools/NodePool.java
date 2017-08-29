package model.resourceloading.projectserialization.pools;

import java.util.HashMap;
import model.InOutlet;
import model.Node;
import model.Project;

public class NodePool {

    private final HashMap<Long, Node> nodes;
    private final Project desinationProject;

    public NodePool(Project desinationProject) {
        this.nodes = new HashMap<>();
        this.desinationProject = desinationProject;
    }

    public Node getOrCreateNode(Long id) {
        
        Node got = nodes.get(id);
        if (got == null) { // erzeuge Node...
            Node newNode = new Node(id, desinationProject);
            nodes.put(id, newNode);
            return newNode;

        } else { // Zurueck geben...
            return got;
        }
    }

}
