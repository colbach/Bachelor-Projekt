package model;

import componenthub.ComponentHub;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import logging.AdditionalLogger;
import model.Node;
import model.resourceloading.nodedefinitionslibrary.BuildInNodeDefinitionsLibrary;
import model.resourceloading.nodedefinitionslibrary.NodeDefinitionsLibrary;
import model.resourceloading.nodedefinitionslibrary.UniqueNameVersionGenerator;
import model.resourceloading.projectserialization.ProjectStructureBuilder;
import model.projectversioning.ProjectVersioner;
import model.resourceloading.projectserialization.CorruptedProjectException;
import org.apache.commons.io.FileUtils;
import reflection.NodeDefinition;
import settings.ProjectSettings;
import utils.measurements.Area;

/**
 * Representiert ein vom Benutzer angelegtes Projekt.
 */
public class Project {

    public static final String FOLDER_NAME_FOR_PROJECT_NODES_CLASSES = "nodedefinitions/";
    public static final String FOLDER_NAME_FOR_VERSIONING = "versions/";
    public static final String FILE_NAME_FOR_PROJECT_SETTINGS = "properties.txt";
    public static final String FILE_NAME_FOR_NODE_DEFINITIONS_INFO_TEXT_FILE = "info.txt";
    public static final String NODE_DEFINITIONS_INFO_TEXT_FILE_CONTENT = "In diesem Ordner können kompilierte class-Dateien abgelegt werden.\n"
            + "Klassen welche das Interface NodeDefinition implementieren können als Elemente verwendet werden.";
    public static final String FILE_NAME_FOR_PROJECT_STRUCTURE = "structure.json";

    private File fileForProjectSettings;
    private File fileForProjectStructure;
    private File folderForVersioning;
    private File folderForNodedefinitions;
    private File fileForNodedefinitionsInfoTextFile;

    private String projectPath; // Mit anschliessedem "/" (wird ueber Konstruktor sicher gestellt)
    private File projectFolder;

    private ArrayList<Node> nodes;
    private HashSet<Node> breakPoint;
    private NodeDefinitionsLibrary projectNodeDefinitionsLibrary;

    private final ProjectSettings projectSettings;

    private boolean somethingChanged;

    private final SmartIdentifierContextImplementation smartIdentifierContext;

    /**
     * Erzeugt Instanz eines Projektes unter angabe aller Knoten und des
     * Projekt-Wurzel-Pfades.
     */
    public Project(Node[] nodes, String projectPath) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));
        this.projectSettings = new ProjectSettings(projectPath);
        this.breakPoint = new HashSet<>();
        this.smartIdentifierContext = new SmartIdentifierContextImplementation();
        if (projectPath != null) {
            this.setProjectLocation(new File(projectPath));
        }
        this.somethingChanged = false;
    }

    /**
     * Erzeugt Instanz eines Projektes unter angabe aller Knoten.
     * Projekt-Wurzel-Pfades muss nachtraeglich gesetzt werden damit das Projekt
     * abgespeichert werden kann.
     */
    public Project(Node[] nodes) {
        this(nodes, null);
    }

    /**
     * Erzeugt Instanz eines leeren Projektes. Projekt-Wurzel-Pfades muss
     * nachtraeglich gesetzt werden damit das Projekt abgespeichert werden kann.
     */
    public Project() {
        this(new Node[0]);
        BuildInNodeDefinitionsLibrary bindl = BuildInNodeDefinitionsLibrary.getInstance();
        NodeDefinition startNodeDefinition = bindl.get(UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion("buildin.Start1x", 1));
        if (startNodeDefinition != null) {
            nodes.add(new Node(startNodeDefinition, 100, 100, this));
        } else {
            System.err.println("Start-Node-Definition nicht gefunden");
        }
    }

    /**
     * Fuegt Node zum Projekt hinzu.
     */
    public synchronized void addNode(Node node) {
        if (node != null) {
            nodes.add(node);
            somethingChanged();
        } else {
            System.err.println("node ist null und kann nicht hinzugefuegt werden!");
        }
    }

    public synchronized boolean removeNode(Node node) {

        if (node != null) { // Falls node nicht null

            // Nodes entfernen welche relativ zu diesem Node stehen...
            for (Inlet connectedInlet : node.getConnectedInlets()) {
                for (Outlet connectedInletconnectedOutlet : connectedInlet.getConnectedLets()) {
                    Node potentialRelativeNode = connectedInletconnectedOutlet.getNode();
                    if (potentialRelativeNode.isRelativeNode()) { // Falls es sich um ein relativen Node handelt
                        removeNode(potentialRelativeNode);
                    }
                }
            }

            // Verbindungen trennen...
            node.capConnections();

            // Node aus Projekt loeschen...
            boolean r = this.nodes.remove(node);
            somethingChanged();

            return r;
        } else {
            System.err.println("node ist null und kann nicht geloecht werden!");
            return false;
        }
    }

    /**
     * Fuegt Node via NodeDefinition zum Projekt hinzu.
     */
    public synchronized void addNode(NodeDefinition definition, int x, int y) {
        if (definition != null) {
            nodes.add(new Node(definition, x, y, this));
            somethingChanged();
        } else {
            System.err.println("definition ist null und kann nicht hinzugefuegt werden!");
        }
    }

    public synchronized void setNodeToTop(Node node) {
        if (node != null) {
            nodes.remove(node);
            nodes.add(node);
            somethingChanged();
        } else {
            System.err.println("node ist null und kann damit auch nicht nach oben geschoben werden!");
        }
    }

    public synchronized Node[] getNodes() {
        return nodes.toArray(new Node[0]);
    }

    public synchronized int getNodeCount() {
        return nodes.size();
    }

    public synchronized int getConnectionsCount() {
        int count = 0;
        for (Node node : nodes) {
            for (Outlet outlet : node.getOutlets()) {
                count += outlet.getConnectedLetsCount();
            }
        }
        return count;
    }

    public synchronized NodeDefinitionsLibrary getProjectNodeDefinitionsLibrary() {
        return projectNodeDefinitionsLibrary;
    }

    public synchronized void setProjectLocation(File projectFolder) {
        if (projectFolder == null) {
            this.fileForProjectSettings = null;
            this.fileForProjectStructure = null;
            this.projectFolder = null;
            this.projectNodeDefinitionsLibrary = null;
            this.projectSettings.setPath(null);
            this.folderForVersioning = null;
        } else {
            String newProjectPath;
            try {
                newProjectPath = projectFolder.getCanonicalPath();
            } catch (IOException ex) {
                System.err.println("projectFolder kann nicht in CanonicalPath umgewandelt werden! Verwende AbsolutePath.");
                newProjectPath = projectFolder.getAbsolutePath();
            }
            if (!newProjectPath.endsWith(File.separator)) {
                newProjectPath += File.separator;
            }
            if (this.projectPath != null && this.projectPath.equals(newProjectPath)) {
                AdditionalLogger.err.println("projectPath ist der Gleiche und muss somit nicht neu gesetzt werden.");
                return;
            }
            this.projectPath = newProjectPath;
            this.fileForProjectSettings = new File(newProjectPath + FILE_NAME_FOR_PROJECT_SETTINGS);
            this.fileForProjectStructure = new File(newProjectPath + FILE_NAME_FOR_PROJECT_STRUCTURE);
            this.folderForVersioning = new File(newProjectPath + FOLDER_NAME_FOR_VERSIONING);
            this.folderForNodedefinitions = new File(newProjectPath + FOLDER_NAME_FOR_PROJECT_NODES_CLASSES);
            this.fileForNodedefinitionsInfoTextFile = new File(newProjectPath + FOLDER_NAME_FOR_PROJECT_NODES_CLASSES + File.separator + FILE_NAME_FOR_NODE_DEFINITIONS_INFO_TEXT_FILE);
            this.projectNodeDefinitionsLibrary = new NodeDefinitionsLibrary(folderForNodedefinitions.getAbsolutePath(), "");
            this.projectFolder = new File(newProjectPath);
            this.projectSettings.setPath(fileForProjectSettings.getPath());
        }
    }

    public File getFileForProjectSettings() {
        return fileForProjectSettings;
    }

    public File getFileForProjectStructure() {
        return fileForProjectStructure;
    }

    public File getProjectLocation() {
        return projectFolder;
    }

    public File getFolderForVersioning() {
        return folderForVersioning;
    }

    public boolean isProjectLocationSet() {
        return projectFolder != null;
    }

    /**
     * Gibt Flaeche zuruek auf welcher sich Nodes befinden. Beruecksichtigt wird
     * immer nur der Mittelpunkt von Nodes.
     */
    public synchronized Area getNeededProjectArea() {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        if (nodes.size() == 0) { // Falls es keine Nodes gibt
            return new Area(0, 0, 0, 0);
        } else {
            for (Node node : nodes) {
                int x = node.getUICenterX();
                int y = node.getUICenterY();
                if (x > maxX) {
                    maxX = x;
                }
                if (x < minX) {
                    minX = x;
                }
                if (y > maxY) {
                    maxY = y;
                }
                if (y < minY) {
                    minY = y;
                }
            }
            int width = maxX - minX;
            int height = maxY - minY;
            return new Area(minX, minY, width, height);
        }
    }

    public ProjectSettings getProjectSettings() {
        return projectSettings;
    }

    public int getProjectVersion() {
        return projectSettings.getInt(ProjectSettings.PROJECT_VERSION_KEY, 0);
    }

    @Override
    public String toString() {
        return ProjectStructureBuilder.buildJsonProjectStructure(this);
    }

    public synchronized void saveProject() throws NoProjectLocationSetException, IOException {

        if (!isProjectLocationSet()) {
            throw new NoProjectLocationSetException();

        } else {

            if (!projectFolder.exists()) {
                projectFolder.mkdirs();
            }

            // Version sichern falls erwuenscht...
            if (projectSettings.getBoolean(ProjectSettings.VERSIONING_ENABLED_KEY, ProjectSettings.VERSIONING_ENABLED_DEFAULT_VALUE) && projectSettings.getInt(ProjectSettings.PROJECT_VERSION_KEY, 0) != 0) {
                ProjectVersioner.saveProjectVersion(this);
            }

            // Json von Projekt-Strucktur erstellen...
            String json = ProjectStructureBuilder.buildJsonProjectStructure(this);

            // Werte in projectSettings anpassen...
            projectSettings.set(ProjectSettings.PROJECT_VERSION_KEY, projectSettings.getInt(ProjectSettings.PROJECT_VERSION_KEY, 0) + 1);
            projectSettings.set(ProjectSettings.CREATION_PROGRAM_VERSION_NAME_KEY, projectSettings.getString(ProjectSettings.CREATION_PROGRAM_VERSION_NAME_KEY, ProjectSettings.PROGRAM_VERSION_NAME_INITIAL_VALUE));
            projectSettings.set(ProjectSettings.SAVE_PROGRAM_VERSION_NAMEE_KEY, ProjectSettings.PROGRAM_VERSION_NAME_INITIAL_VALUE);
            projectSettings.set(ProjectSettings.SAVE_DATE_KEY, ProjectSettings.DATE_FORMAT.format(new Date()));

            // projectSettings schreiben...
            projectSettings.writeSettingsToSettingsFile(fileForProjectSettings.getAbsolutePath());

            // Json schreiben...
            FileUtils.writeStringToFile(fileForProjectStructure, json, StandardCharsets.UTF_8);

            // nodedefinitions Ordner anlegen
            if (!folderForNodedefinitions.exists()) {
                folderForNodedefinitions.mkdir();
            }
            FileUtils.writeStringToFile(fileForNodedefinitionsInfoTextFile, NODE_DEFINITIONS_INFO_TEXT_FILE_CONTENT, StandardCharsets.UTF_8);

            // somethingChanged zurücksetzen...
            somethingChanged = false;
        }
    }

    public boolean didSomethingChanged() {
        return somethingChanged;
    }

    public void somethingChanged() {
        somethingChanged = true;
    }

    /**
     * Factory-Methode um Projekte zu laden.
     */
    public static Project loadProject(File projectFolder) throws IOException, CorruptedProjectException {
        String projectPath = projectFolder.getCanonicalPath();
        if (projectPath.endsWith(File.separator)) {
            projectPath += File.separator;
        }
        File fileForProjectStructure = new File(projectPath + File.separator + FILE_NAME_FOR_PROJECT_STRUCTURE);

        String json = FileUtils.readFileToString(fileForProjectStructure, StandardCharsets.UTF_8);
        Project project = ProjectStructureBuilder.buildProjectStructureFromJson(json);
        project.setProjectLocation(projectFolder);
        project.somethingChanged = false;

        return project;
    }

    public String getTitle() {
        File location = getProjectLocation();
        if (location != null) {
            return location.getName();
        } else {
            return "Unbenannt";
        }
    }

    public Node getNodeById(int id) {
        for (Node node : nodes) {
            if (node.getIdentifier() == id) {
                return node;
            }
        }
        //System.err.println("Keine Node mit der ID " + id + " gefunden. return null");
        return null;
    }

    public SmartIdentifierContextImplementation getSmartIdentifierContext() {
        return smartIdentifierContext;
    }
}
