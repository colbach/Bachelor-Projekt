package model.resourceloading.projectserialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.InOutlet;
import model.Inlet;
import model.Node;
import model.Outlet;
import model.Project;
import model.resourceloading.nodedefinitionslibrary.BuildInNodeDefinitionsLibrary;
import model.resourceloading.nodedefinitionslibrary.NodeDefinitionsLibrary;
import model.resourceloading.projectserialization.pools.LetPool;
import model.resourceloading.projectserialization.pools.NodePool;
import model.resourceloading.projectserialization.pools.WrongLetKindException;
import model.resourceloading.projectserialization.structureddata.StructuredInletData;
import model.resourceloading.projectserialization.structureddata.StructuredNodeData;
import model.resourceloading.projectserialization.structureddata.StructuredOutletData;
import model.resourceloading.projectserialization.structureddata.StructuredProjectData;
import model.runproject.debugger.Breakpoints;
import reflection.NodeDefinition;
import reflection.SmartIdentifier;
import model.SmartIdentifierContextImplementation;
import reflection.API;
import reflection.InOut;
import utils.ObjectSerializer;

public class ProjectStructureBuilder {

    public static String buildJsonProjectStructure(Project project) {

        StructuredProjectData structuredProjectData = new StructuredProjectData(project);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            //jsonInString = mapper.writeValueAsString(structuredProjectData);
            String indentedJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(structuredProjectData);
            jsonString = indentedJsonString;

        } catch (JsonProcessingException ex) {
            Logger.getLogger(ProjectStructureBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jsonString;

        /* // Manuelle erzeugung der json
        
            StringBuilder sb = new StringBuilder();

            Node[] nodes = project.getNodes();
            HashSet<Inlet> inlets = new HashSet<>();
            HashSet<Outlet> outlets = new HashSet<>();

            sb.append("{\n");

            sb.append(" \"nodes\": [");
            boolean firstNode = true;
            for (Node node : nodes) {
                // Alle Lets zu inlets und outlets hinzufuegen...
                inlets.addAll(Arrays.asList(node.getInlets()));
                outlets.addAll(Arrays.asList(node.getOutlets()));

                if (!firstNode) {
                    sb.append(", {\n");
                } else {
                    sb.append("\n  {\n");
                }
                firstNode = false;
                sb.append("   \"id\": ").append(node.getIdentifier()).append(",\n");
                sb.append("   \"inlets\": [");
                {
                    boolean first = true;
                    for (Inlet inlet : node.getInlets()) {
                        if (!first) {
                            sb.append(", ");
                        }
                        first = false;
                        sb.append(String.valueOf(inlet.getIdentifier()));
                    }
                }
                sb.append("],\n");
                sb.append("   \"outlets\": [");
                {
                    boolean first = true;
                    for (Outlet outlet : node.getOutlets()) {
                        if (!first) {
                            sb.append(", ");
                        }
                        first = false;
                        sb.append(String.valueOf(outlet.getIdentifier()));
                    }
                }
                sb.append("],\n");
                sb.append("   \"definitionUniqueNameVersion\": \"").append(node.getUniqueNameVersion()).append("\",\n");
                sb.append("   \"uiCenterX\": ").append(node.getUICenterX()).append(",\n");
                sb.append("   \"uiCenterY\": ").append(node.getUICenterY()).append("\n");
                sb.append("  }");
            }
            sb.append("\n ],\n");

            sb.append(" \"inlets\": [");
            boolean firstInlet = true;
            for (Inlet inlet : inlets) {

                if (!firstInlet) {
                    sb.append(", {\n");
                } else {
                    sb.append("\n  {\n");
                }
                firstInlet = false;
                sb.append("   \"id\": ").append(inlet.getIdentifier()).append(",\n");
                sb.append("   \"connectedOutlets\": [");
                {
                    boolean first = true;
                    for (InOutlet let : inlet.getConnectedLets()) {
                        if (!first) {
                            sb.append(", ");
                        }
                        first = false;
                        sb.append(String.valueOf(let.getIdentifier()));
                    }
                }
                sb.append("]\n");
                sb.append("  }");

            }
            sb.append("\n ],\n");

            sb.append(" \"outlets\": [");
            boolean firstOutlet = true;
            for (Outlet outlet : outlets) {

                if (!firstOutlet) {
                    sb.append(", {\n");
                } else {
                    sb.append("\n  {\n");
                }
                firstOutlet = false;
                sb.append("   \"id\": ").append(outlet.getIdentifier()).append(",\n");
                sb.append("   \"connectedInlets\": [");
                {
                    boolean first = true;
                    for (InOutlet let : outlet.getConnectedLets()) {
                        if (!first) {
                            sb.append(", ");
                        }
                        first = false;
                        sb.append(String.valueOf(let.getIdentifier()));
                    }
                }
                sb.append("]\n");
                sb.append("  }");

            }
            sb.append("\n ]\n");

            sb.append("}");

            return sb.toString();
         */
    }

    public static Project buildProjectStructureFromJson(String json) throws CorruptedProjectException {

        Project project = new Project(new Node[0]);
        NodeDefinitionsLibrary library = project.getProjectNodeDefinitionsLibrary();
        BuildInNodeDefinitionsLibrary buildInNodeDefinitionsLibrary = BuildInNodeDefinitionsLibrary.getInstance();

        LetPool letPool = new LetPool();
        NodePool nodePool = new NodePool(project);

        ObjectMapper mapper = new ObjectMapper();
        StructuredProjectData structuredProjectData = null;
        try {
            try {
                structuredProjectData = mapper.readValue(json, StructuredProjectData.class);
            } catch (IOException iOException) {
                throw new CorruptedProjectException("Fehlerhafte Json-Struktur");
            }

            // Inlets...
            for (StructuredInletData structuredInletData : structuredProjectData.inlets) {
                try {
                    Inlet inlet = letPool.getOrCreateInlet(structuredInletData.id);
                    ArrayList<Outlet> connectedOutlets = new ArrayList<>();
                    for (long connectedOutletId : structuredInletData.connectedOutlets) {
                        Outlet connectedOutlet = letPool.getOrCreateOutlet(connectedOutletId);
                        connectedOutlets.add(connectedOutlet);
                    }
                    inlet.setConnectedOutlets(connectedOutlets);
                } catch (WrongLetKindException ex) {
                    ex.printStackTrace();
                }
            }

            // Outlets...
            for (StructuredOutletData structuredOutletData : structuredProjectData.outlets) {
                try {
                    Outlet outlet = letPool.getOrCreateOutlet(structuredOutletData.id);
                    ArrayList<Inlet> connectedInlets = new ArrayList<>();
                    for (long connectedInletId : structuredOutletData.connectedInlets) {
                        Inlet connectedInlet = letPool.getOrCreateInlet(connectedInletId);
                        connectedInlets.add(connectedInlet);
                    }
                    outlet.setConnectedInlets(connectedInlets);
                } catch (WrongLetKindException ex) {
                    ex.printStackTrace();
                    throw new CorruptedProjectException("Fehlerhafte Bezeichnung in Json-Struktur");
                }
            }

            // Nodes...
            for (StructuredNodeData structuredNodeData : structuredProjectData.nodes) {

                NodeDefinition definition = null;
                if (library != null) {
                    definition = library.get(structuredNodeData.definitionUniqueNameVersion);
                }
                if (definition == null) {
                    definition = buildInNodeDefinitionsLibrary.get(structuredNodeData.definitionUniqueNameVersion);
                }
                if (definition == null) { // nicht mit vorherrigem if verbiden!
                    System.err.println(structuredNodeData.definitionUniqueNameVersion + " nicht gefunden obwohl diese fuer die Deserialisierung benoetigt wird!");
                    definition = new NodeDefinition() {
                        @Override
                        public int getInletCount() {
                            return 0;
                        }

                        @Override
                        public Class getClassForInlet(int inletIndex) {
                            return Object.class;
                        }

                        @Override
                        public String getNameForInlet(int inletIndex) {
                            return "?";
                        }

                        @Override
                        public boolean isInletForArray(int inletIndex) {
                            return true;
                        }

                        @Override
                        public int getOutletCount() {
                            return 0;
                        }

                        @Override
                        public Class getClassForOutlet(int outletIndex) {
                            return Object.class;
                        }

                        @Override
                        public String getNameForOutlet(int outletIndex) {
                            return "?";
                        }

                        @Override
                        public boolean isOutletForArray(int outletIndex) {
                            return true;
                        }

                        @Override
                        public String getName() {
                            return "?";
                        }

                        @Override
                        public String getDescription() {
                            return "?";
                        }

                        @Override
                        public String getUniqueName() {
                            int c = structuredNodeData.definitionUniqueNameVersion.lastIndexOf("[");
                            if(c != -1) {
                                return structuredNodeData.definitionUniqueNameVersion.substring(0, c);
                            } else {
                                System.err.println(structuredNodeData.definitionUniqueNameVersion + " ist keine gueltige UniqueNameVersion da diese kein [ enthaelt.");
                                return structuredNodeData.definitionUniqueNameVersion;
                            }
                        }

                        @Override
                        public String getIconName() {
                            return "ui/clear.png";
                        }

                        @Override
                        public int getVersion() {
                            int c = structuredNodeData.definitionUniqueNameVersion.lastIndexOf("[");
                            int d = structuredNodeData.definitionUniqueNameVersion.lastIndexOf("]");
                            if(c != -1 && d != -1) {
                                String versionString = structuredNodeData.definitionUniqueNameVersion.substring(c+1, d);
                                int version;
                                try {
                                    version = Integer.valueOf(versionString);
                                } catch (NumberFormatException numberFormatException) {
                                    System.err.println(structuredNodeData.definitionUniqueNameVersion + " ist keine gueltige UniqueNameVersion da \"" + versionString + "\" keine gueltige Versionsnummer ist.");
                                    version = 1;
                                }
                                return version;
                            } else {
                                System.err.println(structuredNodeData.definitionUniqueNameVersion + " ist keine gueltige UniqueNameVersion da diese kein [ oder ] enthaelt.");
                                return 1;
                            }
                        }

                        @Override
                        public void run(InOut io, API api) throws Exception {
                            api.printErr(structuredNodeData.definitionUniqueNameVersion + " konnte nicht richtig geladen werden.");
                            api.cancelExecution("Fehlende NodeDefinition", true);
                        }

                        @Override
                        public boolean isInletEngaged(int inletIndex) {
                            return false;
                        }
                    };
                }

                Node node = nodePool.getOrCreateNode(structuredNodeData.id);
                node.setUiCenterX(structuredNodeData.uiCenterX);
                node.setUiCenterY(structuredNodeData.uiCenterY);
                node.setDefinition(definition);
                ArrayList<Inlet> inlets = new ArrayList<>();
                for (int i = 0; i < structuredNodeData.inlets.length; i++) {
                    try {
                        Inlet inlet = letPool.getOrCreateInlet(structuredNodeData.inlets[i]);
                        inlet.setNode(node);
                        inlet.setLetIndex(i);
                        inlets.add(inlet);
                    } catch (WrongLetKindException ex) {
                        ex.printStackTrace();
                        throw new CorruptedProjectException("Fehlerhafte Bezeichnung in Json-Struktur");
                    }
                }
                node.setInlets(inlets);
                ArrayList<Outlet> outlets = new ArrayList<>();
                for (int i = 0; i < structuredNodeData.outlets.length; i++) {
                    try {
                        Outlet outlet = letPool.getOrCreateOutlet(structuredNodeData.outlets[i]);
                        outlet.setNode(node);
                        outlet.setLetIndex(i);
                        outlets.add(outlet);
                    } catch (WrongLetKindException ex) {
                        ex.printStackTrace();
                        throw new CorruptedProjectException("Fehlerhafte Bezeichnung in Json-Struktur");
                    }
                }
                node.setOutlets(outlets);

                if (node.isDirectInputNode()) {
                    String[] base64a = structuredNodeData.settabledata;
                    Object[] objects = ObjectSerializer.deserializeBase64ArrayToObjects(base64a);
                    if(objects.length > 0 && objects[0] instanceof SmartIdentifier) {
                        SmartIdentifierContextImplementation smartIdentifierContext = project.getSmartIdentifierContext();
                        for(Object object : objects) {
                            if(object instanceof SmartIdentifier) {
                                SmartIdentifier smartIdentifier = (SmartIdentifier) object;
                                smartIdentifierContext.put(smartIdentifier);
                            }
                        }
                    }
                    node.setUserSettableContent(objects);
                }

                project.addNode(node);
            }

        } catch (Exception ex) {
            if (ex instanceof CorruptedProjectException) {
                throw ex;
            } else {
                ex.printStackTrace();
                throw new CorruptedProjectException("Unbekannter Fehler in Json-Struktur");
            }
        }

        return project;
    }

}
