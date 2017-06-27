package commandline.functions.analyse;

import java.util.Arrays;
import model.Inlet;
import model.Node;
import model.Outlet;
import model.Project;
import reflection.ContextCreator;
import reflection.NodeDefinition;

public class ProjectRepair {

    public static void printChange(String problem, String solution, boolean repair) {
        if (repair) {
            System.out.println("Fund: " + problem + "\n -> " + solution);
        } else {
            System.out.println("Fund: " + problem + "\n (Dieses Problem kann moeglicherweise durch eine Reperatur (repair) geloest werden)");
        }
    }

    public static final boolean CHECK_FOR_INVALID_RELATIVE_NODES = true;
    public static final boolean CHECK_FOR_INVALID_INLETS = true;

    public static void repairProject(Project project, boolean repair) throws Exception {

        // Nach unerlaubten Relative Nodes suchen...
        if (CHECK_FOR_INVALID_RELATIVE_NODES) {
            for (Node node : project.getNodes()) {
                if (node.isRelativeNode()) {
                    try {
                        node.getConnectedInletFromRelativeNode();
                    } catch (Exception e) {
                        printChange("Relative Node " + node.toString() + " nicht an erstem Ausgang verbunden!", "Node " + node.toString() + " entfernen.", repair);
                        if (repair) {
                            project.removeNode(node);
                        }
                    }
                }
            }
        }

        // Nach nicht bidirektional verbundenen Nodes suchen...
        if (CHECK_FOR_INVALID_INLETS) {
            for (Node node : project.getNodes()) {
                for (Inlet inlet : node.getInlets()) {
                    if (inlet.isConnected()) {
                        for (Outlet oultet : inlet.getConnectedLets()) {
                            if (!Arrays.asList(oultet.getConnectedLets()).contains(inlet)) {
                                printChange("Inlet " + inlet.toString() + " von Node " + node.toString() + " ist nicht bidirektional verbunden", "Inlet " + inlet.toString() + " entfernen.", repair);
                                if (repair) {
                                    inlet.unconnectOutlet(oultet);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Nach nicht angemeldeten Lets suchen...
        if (CHECK_FOR_INVALID_INLETS) {
            for (Node node : project.getNodes()) {

                NodeDefinition definition = node.getDefinition();

                if (node.getOutlets().length < definition.getOutletCount() + 1) {
                    for (int i = node.getOutlets().length; i < definition.getOutletCount() + 1; i++) {
                        printChange("Outlet " + node.getOutletName(i) + " von Node " + node.toString() + " ist nicht vorhanden", "Outlet " + node.getOutletName(i) + " anlegen.", repair);
                        if (repair) {
                            node.addOutlet(new Outlet(node, i));
                        }
                    }
                }
                int wantedInletCount = definition.getInletCount();
                if(!(definition instanceof ContextCreator)) {
                    wantedInletCount += 1;
                }
                if (node.getInlets().length < wantedInletCount) {
                    for (int i = node.getInlets().length; i < wantedInletCount; i++) {
                        printChange("Inlet " + node.getInletName(i) + " von Node " + node.toString() + " ist nicht vorhanden", "Inlet " + node.getInletName(i) + " anlegen.", repair);
                        if (repair) {
                            node.addInlet(new Inlet(node, i));
                        }
                    }
                }
            }
        }

        System.out.println("Abgeschlossen");
    }

}
