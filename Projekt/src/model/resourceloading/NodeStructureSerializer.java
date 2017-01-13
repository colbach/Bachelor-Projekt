package model.resourceloading;

import exceptions.WrongTypException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import model.Inlet;
import model.Node;
import model.Outlet;
import ALT.AttributeNode;
import ALT.CopyNode;
import ALT.DirectInputNode;
import ALT.IfBackNode;
import ALT.IfForwardNode;
import reflection.NodeDefinition;
import utils.structures.Quadrupel;

/**
 * Diese Klasse dient der Serialisierung und deserialisierung von Node-Strukturen
 * sowie dessen Speicherung und Laden vom Dateisystem.
 * @author christiancolbach
 */
public class NodeStructureSerializer {

    /**
     * Trennsymbol fuer Nodes.
     */
    private static final char NODE_SEPARATOR = '\n';

    /**
     * Trennsymbol fuer Felder.
     */
    private static final char FIELD_SEPARATOR = ':';

    /**
     * Trennsymbol fuer Lets.
     */
    private static final char LET_SEPARATOR = ',';

    /**
     * Verwendetes Encoding zur Speicherung von Strings.
     */
    private static final Charset ENCODING = Charset.forName("UTF-8");

    /**
     * Wandelt Node in String um welcher nachher wieder zurueckgewandelt werden
     * kann.
     */
    /*private static String encodeNodeToString(Node node) {

        synchronized (node) {
            StringBuilder buffer = new StringBuilder();

            // ID (Ganzzahl)
            buffer.append(String.valueOf(node.getIdentifier()));
            buffer.append(FIELD_SEPARATOR);

            // UniqueName + Version (Zeichenkette)
            buffer.append(UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion(node.getDefinition()));
            buffer.append(FIELD_SEPARATOR);

            // x (Ganzzahl)
            buffer.append(String.valueOf(node.getUICenterX()));
            buffer.append(FIELD_SEPARATOR);

            // y (Ganzzahl)
            buffer.append(String.valueOf(node.getUICenterY()));
            buffer.append(FIELD_SEPARATOR);

            // outlets (Ganzzahl-Ganzzahl-Ganzahl/Ganzzahl-Ganzzahl-Ganzahl/..., Ganzzahl-Ganzzahl-Ganzahl/..., ...)
            {
                ArrayList<Outlet> outlets = node.getOutlets();
                for (int i = 0; i < outlets.size(); i++) {
                    if (i != 0) {
                        buffer.append(LET_SEPARATOR);
                    }
                    boolean first = true;
                    for(Inlet inlet : outlets.get(i).getConnectedLets()) {
                        if(!first) { // Wenn nicht erster
                            buffer.append("/");
                        }
                        buffer.append(String.valueOf(inlet.getNode().getIdentifier())).append("-").append(inlet.getLetIndex()).append("-").append(String.valueOf(inlet.getIndexForConnectedOutletInList(outlets.get(i))));
                        first = false;
                    }
                }
            }

            return buffer.toString();
        }
    }*/

    /**
     * Wandelt Nodes in String um welcher nachher wieder zurueckgewandelt werden
     * kann.
     */
    /*private static String encodeNodeStructureToString(Node[] nodes) {

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < nodes.length; i++) {
            if (i != 0) {
                buffer.append(NODE_SEPARATOR);
            }
            buffer.append(encodeNodeToString(nodes[i]));
        }
        return buffer.toString();
    }*/

    ////////////////// MUSS ANGEPASST WERDEN DAMIT NEUE STRUKTUR ERKANNT WERDEN KANN 
    /**
     * Wandelt String wieder in Nodes zuruek.
     */
    /*private static Node[] decodeNodeStructureFromString(String nodesString, NodeDefinitionsLibrary... nodeDefinitionsResources) {

        HashMap<Long, Node> unconnectedNodes = new HashMap<>(); // [ID, Node]
        ArrayList<Quadrupel<Node, Integer, Long, Integer>> outgoingConnections = new ArrayList<>(); // (Node, Outletindex, Verbundene ID, Verbundenes Inletindex)

        // In Zeilen aufteilen...
        String[] nodeStrings = nodesString.split(String.valueOf(NODE_SEPARATOR));

        for (String nodeString : nodeStrings) { // Fuer jede Zeile

            // In Felder aufteilen...
            String[] fields = nodeString.split(String.valueOf(FIELD_SEPARATOR));

            // Erste 4 Felder analysieren...
            Long identifier = Long.parseLong(fields[0]);
            String uniqueNameVersion = fields[1];
            int x = Integer.valueOf(fields[2]);
            int y = Integer.valueOf(fields[3]);

            // Nach passender NodeDefinition suchen...
            Node node = null;
            if (uniqueNameVersion.equalsIgnoreCase(AttributeNode.UNIQUE_NAME_VERSION)) {
                node = new AttributeNode(x, y);
            } else if (uniqueNameVersion.equalsIgnoreCase(CopyNode.UNIQUE_NAME_VERSION)) {
                node = new CopyNode(x, y);
            } else if (uniqueNameVersion.equalsIgnoreCase(DirectInputNode.UNIQUE_NAME_VERSION)) {
                node = new DirectInputNode(x, y);
            } else if (uniqueNameVersion.equalsIgnoreCase(IfBackNode.UNIQUE_NAME_VERSION)) {
                node = new IfBackNode(x, y);
            } else if (uniqueNameVersion.equalsIgnoreCase(IfForwardNode.UNIQUE_NAME_VERSION)) {
                node = new IfForwardNode(x, y);
            } else {
                for (NodeDefinitionsLibrary nodeDefinitionsResource : nodeDefinitionsResources) {
                    NodeDefinition nodeDefinition = nodeDefinitionsResource.get(uniqueNameVersion);
                    if (nodeDefinition != null) {
                        node = new Node(nodeDefinition, x, y);
                        break;
                    }
                }
            }
            // kein else
            if (node == null) {
                System.err.println("Keine passende NodeDefinition zu " + uniqueNameVersion + " gefunden!");
                throw new RuntimeException("Unbekannte NodeDefinition \"" + uniqueNameVersion + "\"");
            }

            // Node ablegen...
            unconnectedNodes.put(identifier, node);

            // Outles ermitteln...
            {
                String[] outletStrings = fields[4].split(String.valueOf(LET_SEPARATOR));
                if (outletStrings.length != node.getOutletCount()) {
                    System.err.println("Zeile mit ID " + identifier + " enthaelt " + outletStrings.length + " Outlet IDs. Die Anzahl von Outlets der NodeDefinition " + node.getDefinition().getOutletCount());
                }
                for (Integer i = 0; i < outletStrings.length; i++) {
                    int indexOfMinus = outletStrings[i].indexOf("-");
                    String substring1 = outletStrings[i].substring(0, indexOfMinus);
                    String substring2 = outletStrings[i].substring(indexOfMinus + 1);
                    outgoingConnections.add(new Quadrupel(node, i, Long.valueOf(substring1), Integer.valueOf(substring2)));
                }
            }
        }

        // Nodes miteinander verbinden...
        for (Quadrupel<Node, Integer, Long, Integer> outgoingConnection : outgoingConnections) { // Fuer jede ausgehende Verbindung
            Node from = outgoingConnection.a;
            int fromOutletIndex = outgoingConnection.b;
            long toID = outgoingConnection.c;
            int toInletIndex = outgoingConnection.d;

            Node to = unconnectedNodes.get(toID); // Suche nach Node mit ID

            try { // Versuche zu verbinden
                from.getOutlet(fromOutletIndex).connectInlet(to.getInlet(toInletIndex));
            } catch (WrongTypException ex) {
                System.err.println("Nodes koennen nicht verbunden werden (WrongTypException)");
            }
        }

        return unconnectedNodes.values().toArray(new Node[0]);
    }*/

    /**
     * Wandelt Node-Struktur in Bytearray um und schreibt diese in
     * ByteArrayOutputStream.
     */
    /*private static void writeNodeStructure(ByteArrayOutputStream stream, Node[] nodes) throws IOException {

        // Node-Struktur in String umwandeln...
        String nodeStructureAsString = encodeNodeStructureToString(nodes);

        // String in UTF-8-Array umwandeln und schreiben...
        stream.write(nodeStructureAsString.getBytes(ENCODING));
    }*/

    /**
     * Liesst Node-Struktur aus Datei, wandelt diese um und gibt diese zrueck.
     */
    /*public static Node[] readNodeStructureFromFile(File src, NodeDefinitionsLibrary... nodeDefinitionsResources) throws IOException {

        // Bytes von Festplatte laden und in String umwandeln...
        byte[] bytes = Files.readAllBytes(src.toPath());
        String string = new String(bytes, ENCODING);

        // Dekodieren und zurueckgeben
        return decodeNodeStructureFromString(string, nodeDefinitionsResources);
    }*/

    /**
     * Wandelt Node-Struktur in Bytearray um und schreibt diese in Datei.
     */
    /*public static void writeNodeStructureToFile(File dest, Node[] nodes) throws IOException {

        System.out.println("Schreibe Node-Struktur in: " + dest.getAbsolutePath());

        FileOutputStream fos = null;
        ByteArrayOutputStream baos = null;

        // Versuche Node-Struktur zu schreiben...
        try {

            // Oeffne Streams...
            fos = new FileOutputStream(dest);
            baos = new ByteArrayOutputStream();

            // Umwandeln & Schreiben...
            writeNodeStructure(baos, nodes);
            baos.writeTo(fos);

        } catch (IOException ioe) { // Fehler
            System.err.println("Node-Struktur konnte nicht geschrieben werden! (" + ioe.getMessage() + ")");
            ioe.printStackTrace();

        } finally {

            // Schliesse Streams...
            if (fos != null) {
                fos.close();
            }
            // kein else
            if (baos != null) {
                baos.close();
            }
        }
    }*/

}
