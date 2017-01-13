package view.nodecollection;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JPanel;
import log.AdditionalOut;
import log.Logging;
import model.resourceloading.BuildInNodeDefinitionsLibrary;
import model.resourceloading.NodeDefinitionDescription;
import reflection.NodeDefinition;
import utils.measurements.Area;
import static view.Constants.*;
import view.main.NodeDrafter;
import view.sharedcomponents.scrollbar.ScrollbarDrafter;
import view.sharedcomponents.scrollbar.Direction;
import view.sharedcomponents.scrollbar.Scrollbar;

public class NodeCollectionOverviewPanel extends JPanel {

    private final Scrollbar scrollbar;
    private NodeDefinition[] allDefinitions;
    private ArrayList<NodeDefinition> actualShownDefinitions;
    private NodeDefinition highlightedDefinition = null;
    private long clickTime = 0;
    NodeCollectionWindow nodeCollectionWindow = null; // muss gesetzt werden!

    /**
     * Graphics-Pointer fuer MouseEvents. Soll nicht zum zeichnen benutzt
     * werden!
     */
    private Graphics savedGraphics = null;

    public NodeCollectionOverviewPanel() {
        scrollbar = new Scrollbar(1000, 0f, Direction.VERTICAL, calcScrollbarArea());
        try {
            BuildInNodeDefinitionsLibrary buildInNodeDefinitionsLibrary = BuildInNodeDefinitionsLibrary.getInstance();
            allDefinitions = buildInNodeDefinitionsLibrary.getAll();
            actualShownDefinitions = new ArrayList<>(Arrays.asList(allDefinitions));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWindow(NodeCollectionWindow nodeCollectionWindow) {
        this.nodeCollectionWindow = nodeCollectionWindow;
    }
    
    public void searchDefinitions(String searchString) {
        AdditionalOut.println("Suche nach: " + searchString + " (" + searchString.length() + " Symbole)");
        actualShownDefinitions = new ArrayList<>();
        ArrayList<NodeDefinition> allDefinitionsCopy = new ArrayList<>(Arrays.asList(allDefinitions));

        // Genaue Suche (Genau den Suchstring)...
        for (NodeDefinition definition : allDefinitionsCopy) {
            if (definition.getName() != null && NodeDefinitionDescription.getTotalStringWithAllInformation(definition).toLowerCase().contains(searchString.toLowerCase())) {
                actualShownDefinitions.add(definition);
            }
        }
        allDefinitionsCopy.removeAll(allDefinitionsCopy);
        AdditionalOut.println(actualShownDefinitions.size() + " genaue Definitionen gefunden.");

        // Ungenaue Suche (Vorkommen von Worten)...
        String[] words = searchString.toLowerCase().split(" ");
        for (NodeDefinition definition : allDefinitionsCopy) {
            ForEveryWord:
            for (String word : words) {
                if (definition.getName() != null && NodeDefinitionDescription.getTotalStringWithAllInformation(definition).toLowerCase().contains(word)) {
                    actualShownDefinitions.add(definition);
                    break ForEveryWord;
                }
            }
        }
        allDefinitionsCopy.removeAll(allDefinitionsCopy);

        AdditionalOut.println(actualShownDefinitions.size() + " weitere Definitionen gefunden.");
    }

    public void updateScrollbarArea() {
        scrollbar.setArea(calcScrollbarArea());
    }

    public Area calcScrollbarArea() {
        if(highlightedDefinition == null) { // Fall nichts gehighlightet
            return new Area(getWidth() - SCROLLBAR_WIDTH - 2, 2, SCROLLBAR_WIDTH, getHeight() - 4);
        } else { // Fall gehighlightet
            return new Area(getWidth() - SCROLLBAR_WIDTH - 2, 2, SCROLLBAR_WIDTH, getHeight() - 4 - INFO_SECTION_HEIGHT);
        }
    }

    public Scrollbar getScrollbar() {
        return scrollbar;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        savedGraphics = graphics;

        try {
            // Graphics ind Graphics2D casten...
            Graphics2D g = (Graphics2D) graphics;

            // Breite berechnen...
            int width = getWidth() - SCROLLBAR_WIDTH;

            // Benoetigte Hoehe berechnen...
            NodeDefinition[] actualShownDefinitionsArray = actualShownDefinitions.toArray(new NodeDefinition[0]);
            int neededHeight = NodeCollectionDrafter.calcNeededHeightForNodesPreview(g, width, actualShownDefinitionsArray);
            scrollbar.setRepresentedHeightOrWidth(neededHeight);

            // NodePreview zeichnen...
            NodeCollectionDrafter.drawNodesPreview(g, 0, 0 - scrollbar.getOffsetForContent(), width, highlightedDefinition == null ? getHeight() : getHeight()-INFO_SECTION_HEIGHT, actualShownDefinitionsArray, highlightedDefinition);

            // Scrollbar zeichnen...
            ScrollbarDrafter.drawScrollbar(g, scrollbar);

            // Nodeinfo zeichnen (Falls nötig)...
            if (highlightedDefinition != null) {
                NodeCollectionDrafter.drawInfoSection(g, 0, getHeight() - INFO_SECTION_HEIGHT, getWidth(), INFO_SECTION_HEIGHT, highlightedDefinition);
                g.setColor(DEFAULT_LINE_COLOR);
                g.setStroke(ONE_PX_LINE_STROKE);
                g.drawLine(0, getHeight() - INFO_SECTION_HEIGHT, getWidth(), getHeight() - INFO_SECTION_HEIGHT);
            }

            // Trennlinie zeichnen
            g.setColor(DEFAULT_LINE_COLOR);
            g.setStroke(ONE_PX_LINE_STROKE);
            g.drawLine(0, 0, getWidth(), 0);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Diese Methode wird von einem anonymen MouseAndKeyboardListener in NodeCollectionWindow aufgerufen (Grund ist dass NodeCollectionOverviewPanel nicht von 2 Klassen erben kann)
    public boolean mousePressed(int x, int y) {
        if (savedGraphics != null) {
            
            NodeDefinition matchedDefinition = NodeCollectionDrafter.calcNodesForMousePosition((Graphics2D) savedGraphics, 0, 0 - scrollbar.getOffsetForContent(), getWidth() - SCROLLBAR_WIDTH, actualShownDefinitions.toArray(new NodeDefinition[0]), x, y);
            
            if(matchedDefinition != null && matchedDefinition == highlightedDefinition && System.currentTimeMillis() - clickTime < 500) {
                AdditionalOut.println("Node anlegen: " + highlightedDefinition.getName());
                if(nodeCollectionWindow != null) {
                    nodeCollectionWindow.addNodeToProject(matchedDefinition);
                } else {
                    System.err.println("nodeCollectionWindow ist null!");
                    System.err.println("Node kann nicht angelegt werden.");
                }
            } else {
                clickTime = System.currentTimeMillis();
            }
            highlightedDefinition = matchedDefinition;
            updateScrollbarArea();
        } else {
            System.err.println("savedGraphics ist null!");
        }
        return false;
    }

    // Diese Methode wird von einem anonymen MouseAndKeyboardListener in NodeCollectionWindow aufgerufen (Grund ist dass NodeCollectionOverviewPanel nicht von 2 Klassen erben kann)
    public boolean mouseReleased(int x, int y, int pressedX, int pressedY) {
        return false;
    }
}
