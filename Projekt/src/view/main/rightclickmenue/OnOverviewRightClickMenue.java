package view.main.rightclickmenue;

import exceptions.WrongTypException;
import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.WindowConstants;
import model.Node;
import model.Project;
import model.directinput.DirectInputNodeDefinition;
import model.resourceloading.BuildInNodeDefinitionsLibrary;
import model.resourceloading.UniqueNameVersionGenerator;
import model.runproject.Breakpoints;
import model.specialnodes.IfBackNodeDefinition;
import model.specialnodes.IfForwardNodeDefinition;
import model.type.Type;
import settings.GeneralSettings;
import view.main.MainWindow;
import view.main.runreport.RunReportWindow;
import view.nodecollection.NodeCollectionWindow;

public class OnOverviewRightClickMenue extends RightClickMenue {

    public OnOverviewRightClickMenue(int x, int y, int targetX, int targetY, Project project, MainWindow mainWindow) {
        super(null, x, y, mainWindow.getWidth(), mainWindow.getHeight(), mainWindow);

        ArrayList<RightClickMenueItem> is = new ArrayList<>();
        is.add(new RightClickMenueItem("Wenn-Vor-Verzweigung erstellen") {
            @Override
            public void clicked() {
                mainWindow.getProject().addNode(new IfForwardNodeDefinition(), targetX, targetY);
                mainWindow.removeRightClickMenue();
            }
        });
        is.add(new RightClickMenueItem("Wenn-Zurück-Verzweigung erstellen") {
            @Override
            public void clicked() {
                mainWindow.getProject().addNode(new IfBackNodeDefinition(), targetX, targetY);
                mainWindow.removeRightClickMenue();
            }
        });
        is.add(new RightClickMenueItem("Element erstellen") {
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
                    
                    
                    Node node0 = new Node(bindl.get(UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion("buildin.PrintLog", 1)), targetX + 120, targetY);
                    Node node1 = new Node(bindl.get(UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion("buildin.Add", 1)), targetX, targetY - 60);
                    Node node2 = new Node(bindl.get(UniqueNameVersionGenerator.nodeDefinitionToUniqueNameVersion("buildin.Multiply", 1)), targetX, targetY + 60);
                    DirectInputNodeDefinition d3 = new DirectInputNodeDefinition() {
                        @Override
                        public Type getContentType() {
                            return new Type(Integer.class, false);
                        }
                    };
                    d3.setUserSettableContent(new Object[] {12});
                    Node node3 = new Node(d3, node1.getInlet(0));
                    DirectInputNodeDefinition d4 = new DirectInputNodeDefinition() {
                        @Override
                        public Type getContentType() {
                            return new Type(Integer.class, false);
                        }
                    };
                    d4.setUserSettableContent(new Object[] {34});
                    Node node4 = new Node(d4, node1.getInlet(1));
                    DirectInputNodeDefinition d5 = new DirectInputNodeDefinition() {
                        @Override
                        public Type getContentType() {
                            return new Type(Integer.class, false);
                        }
                    };
                    d5.setUserSettableContent(new Object[] {56});
                    Node node5 = new Node(d5, node2.getInlet(0));
                    DirectInputNodeDefinition d6 = new DirectInputNodeDefinition() {
                        @Override
                        public Type getContentType() {
                            return new Type(Integer.class, false);
                        }
                    };
                    d6.setUserSettableContent(new Object[] {78});
                    Node node6 = new Node(d6, node2.getInlet(1));
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
        }
        
        this.items = is.toArray(new RightClickMenueItem[0]);
    }

}
