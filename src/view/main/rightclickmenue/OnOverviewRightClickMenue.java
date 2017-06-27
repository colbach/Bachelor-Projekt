package view.main.rightclickmenue;

import model.type.WrongTypException;
import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.WindowConstants;
import model.Node;
import model.Project;
import model.directinput.DirectInputNodeDefinition;
import model.resourceloading.nodedefinitionslibrary.BuildInNodeDefinitionsLibrary;
import model.resourceloading.nodedefinitionslibrary.UniqueNameVersionGenerator;
import model.runproject.debugger.Breakpoints;
import reflection.nodedefinitions.specialnodes.firstvalues.AValueNodeDefinition;
import reflection.nodedefinitions.specialnodes.firstvalues.FirstValueNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ForEachNodeDefinition;
import reflection.nodedefinitions.specialnodes.ifs.IfBackNodeDefinition;
import reflection.nodedefinitions.specialnodes.ifs.IfForwardNodeDefinition;
import reflection.nodedefinitions.specialnodes.fors.ReduceNodeDefinition;
import model.type.Type;
import reflection.NodeDefinition;
import settings.GeneralSettings;
import view.main.MainWindow;
import view.main.runreport.RunReportWindow;
import view.nodecollection.NodeCollectionWindow;

public class OnOverviewRightClickMenue extends RightClickMenue {
    
    public OnOverviewRightClickMenue(int x, int y, int targetX, int targetY, Project project, MainWindow mainWindow) {
        super(null, x, y, mainWindow.getWidth(), mainWindow.getHeight(), mainWindow);

        ArrayList<RightClickMenueItem> is = new ArrayList<>();
        NodeDefinition lastCreatingNodeDefiniton = Node.getLastCreatingNodeDefiniton();
        if (lastCreatingNodeDefiniton != null) {
            is.add(new RightClickMenueItem("\"" + lastCreatingNodeDefiniton.getName() + " Element\" erstellen (zuletzt erstellt)") {
                @Override
                public void clicked() {
                    mainWindow.getProject().addNode(lastCreatingNodeDefiniton, targetX, targetY);
                    mainWindow.removeRightClickMenue();
                }
            });
        }
        is.add(new RightClickMenueItem("\"Wenn Vor Verzweigung\" erstellen") {
            @Override
            public void clicked() {
                mainWindow.getProject().addNode(new IfForwardNodeDefinition(), targetX, targetY);
                mainWindow.removeRightClickMenue();
            }
        });
        is.add(new RightClickMenueItem("\"Wenn Zurück Verzweigung\" erstellen") {
            @Override
            public void clicked() {
                mainWindow.getProject().addNode(new IfBackNodeDefinition(), targetX, targetY);
                mainWindow.removeRightClickMenue();
            }
        });
        is.add(new RightClickMenueItem("\"Für Alle Struktur Element\" erstellen") {
            @Override
            public void clicked() {
                mainWindow.getProject().addNode(new ForEachNodeDefinition(), targetX, targetY);
                mainWindow.removeRightClickMenue();
            }
        });
        is.add(new RightClickMenueItem("\"Zusammenführen Struktur Element\" erstellen") {
            @Override
            public void clicked() {
                mainWindow.getProject().addNode(new ReduceNodeDefinition(), targetX, targetY);
                mainWindow.removeRightClickMenue();
            }
        });
        is.add(new RightClickMenueItem("\"Ein Wert Struktur Element\" erstellen") {
            @Override
            public void clicked() {
                mainWindow.getProject().addNode(new AValueNodeDefinition(), targetX, targetY);
                mainWindow.removeRightClickMenue();
            }
        });
        is.add(new RightClickMenueItem("\"Brücken Element\" erstellen") {
            @Override
            public void clicked() {
                BuildInNodeDefinitionsLibrary bindl = BuildInNodeDefinitionsLibrary.getInstance();
                mainWindow.getProject().addNode(bindl.get(UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion("buildin.Bridge", 1)), targetX, targetY);
                mainWindow.removeRightClickMenue();
            }
        });
        is.add(new RightClickMenueItem("\"Einfaches Start Element\" erstellen") {
            @Override
            public void clicked() {
                BuildInNodeDefinitionsLibrary bindl = BuildInNodeDefinitionsLibrary.getInstance();
                mainWindow.getProject().addNode(bindl.get(UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion("buildin.Start1x", 1)), targetX, targetY);
                mainWindow.removeRightClickMenue();
            }
        });
        is.add(new RightClickMenueItem("\"Print to Log Element\" erstellen") {
            @Override
            public void clicked() {
                BuildInNodeDefinitionsLibrary bindl = BuildInNodeDefinitionsLibrary.getInstance();
                mainWindow.getProject().addNode(bindl.get(UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion("buildin.PrintLog", 1)), targetX, targetY);
                mainWindow.removeRightClickMenue();
            }
        });
        is.add(new RightClickMenueItem("Anderes Element erstellen") {
            @Override
            public void clicked() {
                if (mainWindow != null) {
                    mainWindow.openNodeCollectionOverviewWindow();
                }
                mainWindow.removeRightClickMenue();
            }
        });

        if (GeneralSettings.getInstance().getBoolean(GeneralSettings.DEVELOPER_ADVANCED_TESTING_KEY, false)) {
            is.add(new RightClickMenueItem("Testaufbau 1 einfügen") {
                @Override
                public void clicked() {
                    BuildInNodeDefinitionsLibrary bindl = BuildInNodeDefinitionsLibrary.getInstance();

                    Node node0 = new Node(bindl.get(UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion("buildin.PrintLog", 1)), targetX + 120, targetY, project);
                    Node node1 = new Node(bindl.get(UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion("buildin.Add", 1)), targetX, targetY - 60, project);
                    Node node2 = new Node(bindl.get(UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion("buildin.Multiply", 1)), targetX, targetY + 60, project);
                    DirectInputNodeDefinition d3 = new DirectInputNodeDefinition() {
                        @Override
                        public Type getContentType() {
                            return new Type(Integer.class, false);
                        }
                    };
                    d3.setUserSettableContent(new Object[]{12});
                    Node node3 = new Node(d3, node1.getInlet(0), project);
                    DirectInputNodeDefinition d4 = new DirectInputNodeDefinition() {
                        @Override
                        public Type getContentType() {
                            return new Type(Integer.class, false);
                        }
                    };
                    d4.setUserSettableContent(new Object[]{34});
                    Node node4 = new Node(d4, node1.getInlet(1), project);
                    DirectInputNodeDefinition d5 = new DirectInputNodeDefinition() {
                        @Override
                        public Type getContentType() {
                            return new Type(Integer.class, false);
                        }
                    };
                    d5.setUserSettableContent(new Object[]{56});
                    Node node5 = new Node(d5, node2.getInlet(0), project);
                    DirectInputNodeDefinition d6 = new DirectInputNodeDefinition() {
                        @Override
                        public Type getContentType() {
                            return new Type(Integer.class, false);
                        }
                    };
                    d6.setUserSettableContent(new Object[]{78});
                    Node node6 = new Node(d6, node2.getInlet(1), project);
                    try {
                        node0.getInlet(0).connectOutlet(node1.getOutlet(0), 0);
                        node1.getOutlet(0).connectInlet(node0.getInlet(0), 0);
                    } catch (WrongTypException ex) {
                        System.err.println("Kann nicht verbunden werden (WrongTypException)");
                    }
                    try {
                        node0.getInlet(0).connectOutlet(node2.getOutlet(0), 1);
                        node2.getOutlet(0).connectInlet(node0.getInlet(0), 0);
                    } catch (WrongTypException ex) {
                        System.err.println("Kann nicht verbunden werden (WrongTypException)");
                    }

                    mainWindow.getProject().addNode(node0);
                    mainWindow.getProject().addNode(node1);
                    mainWindow.getProject().addNode(node2);
                    mainWindow.getProject().addNode(node3);
                    mainWindow.getProject().addNode(node4);
                    mainWindow.getProject().addNode(node5);
                    mainWindow.getProject().addNode(node6);
                    mainWindow.removeRightClickMenue();
                }
            });
            is.add(new RightClickMenueItem("Testaufbau 2 einfügen") {
                @Override
                public void clicked() {
                    BuildInNodeDefinitionsLibrary bindl = BuildInNodeDefinitionsLibrary.getInstance();

                    Node node0 = new Node(bindl.get(UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion("buildin.ShowImageWindow", 1)), targetX + 100, targetY, project);
                    Node node1 = new Node(bindl.get(UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion("buildin.LoadImageViaDialog", 1)), targetX - 100, targetY, project);
                    try {
                        node1.getOutlet(0).connectInlet(node0.getInlet(0), 0);
                        node0.getInlet(0).connectOutlet(node1.getOutlet(0), 0);
                    } catch (WrongTypException wrongTypException) {
                    }

                    mainWindow.getProject().addNode(node0);
                    mainWindow.getProject().addNode(node1);
                    mainWindow.removeRightClickMenue();
                }
            });
        }

        this.items = is.toArray(new RightClickMenueItem[0]);
    }

}
