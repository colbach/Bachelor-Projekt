package view.nodecollection;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.JPanel;
import jdk.nashorn.internal.objects.Global;
import logging.AdditionalLogger;
import logging.AdditionalOut;
import logging.AdvancedLogger;
import model.resourceloading.nodedefinitionslibrary.BuildInNodeDefinitionsLibrary;
import model.resourceloading.NodeDefinitionDescription;
import model.resourceloading.nodedefinitionslibrary.NodeDefinitionsLibrary;
import reflection.NodeDefinition;
import reflection.additionalnodedefinitioninterfaces.Experimental;
import reflection.composednodedefinition.ComposedNodeDefinition;
import settings.GeneralSettings;
import static settings.GeneralSettings.*;
import utils.measurements.Area;
import static view.Constants.*;
import view.main.NodeDrafter;
import view.sharedcomponents.scrollbar.ScrollbarDrafter;
import view.sharedcomponents.scrollbar.Direction;
import view.sharedcomponents.scrollbar.Scrollbar;

public class NodeCollectionOverviewPanel extends JPanel {

    private final Scrollbar scrollbar;
    private NodeDefinition[] allDefinitions;
    private TreeMap<String, NodeDefinition> actualShownDefinitions;
    private String searchString = "";
    private TreeSet<String> categorys;
    private NodeDefinition highlightedDefinition = null;
    private long clickTime = 0;
    private NodeCollectionWindow nodeCollectionWindow = null; // muss gesetzt werden!
    private String category = "";
    private boolean placeButtonPressed = false;

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
            if (!GeneralSettings.getInstance().getBoolean(EXPERIMENTAL_NODE_DEFINITIONS_KEY, EXPERIMENTAL_NODE_DEFINITIONS_VALUE)) {
                ArrayList<NodeDefinition> allNonExperimentalDefinitions = new ArrayList<>();
                for (NodeDefinition definition : allDefinitions) {
                    if (!(definition instanceof Experimental)) {
                        allNonExperimentalDefinitions.add(definition);
                    }
                }
                allDefinitions = allNonExperimentalDefinitions.toArray(new NodeDefinition[0]);
            }
            if (!GeneralSettings.getInstance().getBoolean(COMPOSED_NODE_DEFINITIONS_KEY, COMPOSED_NODE_DEFINITIONS_VALUE)) {
                ArrayList<NodeDefinition> allNonComposedDefinitions = new ArrayList<>();
                for (NodeDefinition definition : allDefinitions) {
                    if (!(definition instanceof ComposedNodeDefinition)) {
                        allNonComposedDefinitions.add(definition);
                    }
                }
                allDefinitions = allNonComposedDefinitions.toArray(new NodeDefinition[0]);
            }

            categorys = buildInNodeDefinitionsLibrary.getCategorys();
            actualShownDefinitions = new TreeMap<>();
            for (NodeDefinition definition : allDefinitions) {
                actualShownDefinitions.put(definition.getName(), definition);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNodeDefinitionsLibrary(NodeDefinitionsLibrary ndl) {
        NodeDefinition[] definitionsToAdd = ndl.getAll();
        NodeDefinition[] allDefinitionsNewArray = new NodeDefinition[allDefinitions.length + definitionsToAdd.length];
        System.arraycopy(definitionsToAdd, 0, allDefinitionsNewArray, 0, definitionsToAdd.length);
        System.arraycopy(allDefinitions, 0, allDefinitionsNewArray, definitionsToAdd.length, allDefinitions.length);
        allDefinitions = allDefinitionsNewArray;
        categorys.addAll(ndl.getCategorys());
        actualShownDefinitions = new TreeMap<>();
        for (NodeDefinition definition : allDefinitions) {
            actualShownDefinitions.put(definition.getName(), definition);
        }
    }

    public void setWindow(NodeCollectionWindow nodeCollectionWindow) {
        this.nodeCollectionWindow = nodeCollectionWindow;
    }

    public void searchDefinitions(String searchString) {
        boolean exact = false;
        if (searchString.startsWith("!")) {
            exact = true;
            searchString = searchString.substring(1);
            searchString = searchString.trim();
        }

        AdditionalLogger.out.println("Suche nach: " + searchString + " (" + searchString.length() + " Symbole)");
        this.searchString = searchString;
        actualShownDefinitions = new TreeMap<>();

        // Genaue Suche (Genau den Suchstring)...
        for (NodeDefinition definition : allDefinitions) {
            String totalStringWithAllInformation = NodeDefinitionDescription.getTotalStringWithAllInformation(definition).toLowerCase();
            if (totalStringWithAllInformation.indexOf(category.toLowerCase()) != -1) { // Falls Kategorie richtig
                if (definition.getName() != null && totalStringWithAllInformation.contains(searchString.toLowerCase())) {
                    actualShownDefinitions.put(definition.getName(), definition);
                }
            }
        }
        AdditionalLogger.out.println(actualShownDefinitions.size() + " genaue Definitionen gefunden.");

        // Ungenaue Suche (Vorkommen von Worten)...
        if (!exact) {
            String[] words = searchString.toLowerCase().split(" ");
            for (NodeDefinition definition : allDefinitions) {
                String totalStringWithAllInformation = NodeDefinitionDescription.getTotalStringWithAllInformation(definition).toLowerCase();
                if (totalStringWithAllInformation.indexOf(category.toLowerCase()) != -1) { // Falls Kategorie richtig
                    ForEveryWord:
                    for (String word : words) {
                        if (definition.getName() != null && totalStringWithAllInformation.contains(word)) {
                            actualShownDefinitions.put(definition.getName(), definition);
                            break ForEveryWord;
                        }
                    }
                }
            }
        }

        AdditionalLogger.out.println(actualShownDefinitions.size() + " weitere Definitionen gefunden.");
    }

    public void updateScrollbarArea() {
        scrollbar.setArea(calcScrollbarArea());
    }

    public Area calcScrollbarArea() {
        if (highlightedDefinition == null) { // Fall nichts gehighlightet
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
            NodeDefinition[] actualShownDefinitionsArray = actualShownDefinitions.values().toArray(new NodeDefinition[0]);
            int neededHeight = NodeCollectionDrafter.calcNeededHeightForNodesPreview(g, width, actualShownDefinitionsArray);
            scrollbar.setRepresentedHeightOrWidth(neededHeight);

            // NodePreview zeichnen...
            NodeCollectionDrafter.drawNodesPreview(g, 0, 0 - scrollbar.getOffsetForContent(), width, highlightedDefinition == null ? getHeight() : getHeight() - INFO_SECTION_HEIGHT, actualShownDefinitionsArray, highlightedDefinition);

            // Scrollbar zeichnen...
            ScrollbarDrafter.drawScrollbar(g, scrollbar);

            // Nodeinfo zeichnen (Falls nötig)...
            if (highlightedDefinition != null) {
                NodeCollectionDrafter.drawInfoSection(g, 0, getHeight() - INFO_SECTION_HEIGHT, getWidth(), INFO_SECTION_HEIGHT, highlightedDefinition);
                g.setColor(DEFAULT_LINE_COLOR);
                g.setStroke(ONE_PX_LINE_STROKE);
                g.drawLine(0, getHeight() - INFO_SECTION_HEIGHT, getWidth(), getHeight() - INFO_SECTION_HEIGHT);

                // Plazierbutton zeichnen...
                if (placeButtonPressed) {
                    g.setColor(CATEGORY_NOT_ACTIVE_COLOR);
                } else {
                    g.setColor(CATEGORY_ACTIVE_COLOR);
                }
                g.setFont(CATEGORY_LABEL_FONT);
                g.drawString("Platzieren", getWidth() - 70, getHeight() - 10);
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

        if (highlightedDefinition != null && y > getHeight() - INFO_SECTION_HEIGHT) { // Fall: Click auf Info-Section
            if (x > getWidth() - 100 && y > getHeight() - 30) {
                placeButtonPressed = true;
            }

        } else { // Fall: Click auf Nodes
            if (savedGraphics != null) {

                NodeDefinition matchedDefinition = NodeCollectionDrafter.calcNodesForMousePosition((Graphics2D) savedGraphics, 0, 0 - scrollbar.getOffsetForContent(), getWidth() - SCROLLBAR_WIDTH, actualShownDefinitions.values().toArray(new NodeDefinition[0]), x, y);

                if (matchedDefinition != null && matchedDefinition == highlightedDefinition && System.currentTimeMillis() - clickTime < 500) {
                    AdditionalLogger.out.println("Node anlegen: " + highlightedDefinition.getName());
                    if (nodeCollectionWindow != null) {
                        nodeCollectionWindow.addNodeToProject(matchedDefinition);
                        if (GeneralSettings.getInstance().getBoolean(VIEW_NODECOLLECTION_AUTODISPOSE_OND_OUBLECLICK_KEY, true)) {
                            nodeCollectionWindow.dispose();
                        }
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
        }
        return false;
    }

    // Diese Methode wird von einem anonymen MouseAndKeyboardListener in NodeCollectionWindow aufgerufen (Grund ist dass NodeCollectionOverviewPanel nicht von 2 Klassen erben kann)
    public boolean mouseReleased(int x, int y, int pressedX, int pressedY) {
        placeButtonPressed = false;
        if (highlightedDefinition != null && y > getHeight() - INFO_SECTION_HEIGHT) { // Fall: Click auf Info-Section
            if (x > getWidth() - 100 && y > getHeight() - 30) {
                nodeCollectionWindow.addNodeToProject(highlightedDefinition);
            }

        }
        return false;
    }

    public TreeSet<String> getCategorys() {
        return categorys;
    }

    public String[] getCategorysAssArray() {
        if (categorys == null) {
            return null;
        } else {
            return categorys.toArray(new String[0]);
        }
    }

    public void setCategory(String category) {
        this.category = category;
        searchDefinitions(searchString);
    }

    public int getCount() {
        if (actualShownDefinitions == null) {
            return 0;
        } else {
            return actualShownDefinitions.size();
        }
    }
}
