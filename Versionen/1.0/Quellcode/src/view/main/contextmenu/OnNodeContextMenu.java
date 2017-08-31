package view.main.contextmenu;

import generalexceptions.IllegalUserDialogInputException;
import java.io.IOException;
import model.Node;
import model.Project;
import view.main.MainWindow;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.Inlet;
import model.Outlet;
import model.type.Type;
import model.directinput.DirectInputNodeDefinition;
import model.directinput.DirectInputSupport;
import model.resourceloading.NodeDefinitionDescription;
import model.resourceloading.nodedefinitionslibrary.BuildInNodeDefinitionsLibrary;
import model.runproject.debugger.Breakpoints;
import reflection.additionalnodedefinitioninterfaces.VariableVisibleInletCount;
import reflection.nodedefinitions.specialnodes.firstvalues.AValueNodeDefinition;
import utils.text.TextHandler;
import view.assets.ImageAsset;
import view.dialogs.DirectInputDialogs;
import view.dialogs.ErrorDialog;

public class OnNodeContextMenu extends ContextMenu {

    public OnNodeContextMenu(int x, int y, Node node, Project project, MainWindow mainWindow) {
        super(null, x, y, mainWindow.getWidth(), mainWindow.getHeight(), mainWindow);

        ArrayList<RightClickMenueItem> is = new ArrayList<>();
        is.add(new RightClickMenueItem("Löschen") {
            @Override
            public void clicked() {
                project.removeNode(node);
                mainWindow.removeRightClickMenue();
            }
        });
        if (node.getVisibleInletCount() > 0 && !(node.getDefinition() instanceof AValueNodeDefinition)) { // AValueNodeDefinition koennen keine DirectInputs haben weil diese nur ueber ihre Ausgaenge angestossen werden koennen
            is.add(new RightClickMenueItem("Direkter Wert für Eingang setzen") {
                @Override
                public void clicked() {
                    ArrayList<RightClickMenueItem> ii = new ArrayList<>();
                    ArrayList<Boolean> ibc = new ArrayList<>();
                    ArrayList<Boolean> ibs = new ArrayList<>();
                    ArrayList<Boolean> ib = new ArrayList<>();
                    for (Inlet inlet : node.getVisibleInlets()) {
                        RightClickMenueItem rcmi = new RightClickMenueItem(
                                inlet.toString(),
                                DirectInputSupport.isTypeSupported(inlet.getType()) || DirectInputSupport.compartibleDirectInputTypeForType(inlet.getType()).length > 0
                        ) {
                            @Override
                            public void clicked() {

                                // DirectInputNodeDefinition erstellen...
                                Type type = inlet.getType();
                                if (!DirectInputSupport.isDirectInputForTypeAvailable(type)) { // Falls kein Direkter Input verfuegbar ist
                                    Type[] compartibleDirectInputTypeForType = DirectInputSupport.compartibleDirectInputTypeForType(type);
                                    type = (Type) JOptionPane.showInputDialog(
                                            mainWindow,
                                            "Bitte Typ auswählen welcher mit dem Eingang verbunden werden soll",
                                            "Typ auswählen",
                                            JOptionPane.PLAIN_MESSAGE,
                                            null,
                                            compartibleDirectInputTypeForType,
                                            null);
                                    if (type == null) { // Wenn Benutzer abgebrochen hat
                                        return;
                                    }
                                }
                                Type contentTypeForDirectInputNodeDefinition = type;
                                DirectInputNodeDefinition dind = DirectInputNodeDefinition.createDirectInputNodeDefinitionByType(contentTypeForDirectInputNodeDefinition);

                                // neuen Wert via User-Input einholen...
                                Object[] userInput = null;
                                try {
                                    userInput = DirectInputDialogs.dialogForUserSettableContent(node, dind.getContentType(), new Object[]{}, mainWindow, project);
                                } catch (IllegalUserDialogInputException illegalUserDialogInputException) {
                                    ErrorDialog.showErrorDialog(illegalUserDialogInputException, mainWindow);
                                }

                                if (userInput != null) { // Falls Benutzer nicht abgebrochen hat

                                    // UserSettableContent setzen...
                                    dind.setUserSettableContent(userInput);

                                    // Bisherige Relative Nodes zu diesem Inlet entfernen...
                                    for (Outlet potentialRelativeNodeOutlet : inlet.getConnectedLets()) {
                                        Node potentialRelativeNode = potentialRelativeNodeOutlet.getNode();
                                        if (potentialRelativeNode.isRelativeNode()) {
                                            mainWindow.getProject().removeNode(potentialRelativeNode);
                                        }
                                    }

                                    // directInputNode erstellen und zu Projekt hinzufuegen...
                                    Node directInputNode = new Node(dind, inlet, project);
                                    mainWindow.getProject().addNode(directInputNode);
                                }
                                mainWindow.removeRightClickMenue();
                            }
                        };
                        ii.add(rcmi);
                        ibc.add(inlet.isConnected());
                        ibs.add(!rcmi.isClickable());
                    }
                    items = ii.toArray(new RightClickMenueItem[0]);
                    cicles = ibc.toArray(new Boolean[0]);
                    stroked = ibs.toArray(new Boolean[0]);
                }
            });
        }
        /*if (project.getStartNode() != node) {
            is.add(new RightClickMenueItem("Als Start-Element festlegen") {
                @Override
                public void clicked() {
                    project.setStartNode(node);
                    mainWindow.removeRightClickMenue();
                }
            });
        }*/
        if (!Breakpoints.getInstance().is(node) && !node.isRunContextCreator()) {
            is.add(new RightClickMenueItem("Haltemarke setzen") {
                @Override
                public void clicked() {
                    Breakpoints.getInstance().add(node);
                    mainWindow.removeRightClickMenue();
                }
            });
            /*
            if (GeneralSettings.getInstance().getBoolean(GeneralSettings.DEVELOPER_ADVANCED_TESTING_KEY, false)) {
                is.add(new RightClickMenueItem("Als Start-Element festlegen und Haltemarke setzen") {
                    @Override
                    public void clicked() {
                        project.setStartNode(node);
                        mainWindow.removeRightClickMenue();
                        Breakpoints.getInstance().add(node);
                        mainWindow.removeRightClickMenue();
                    }
                });
            }
             */
        } else if (Breakpoints.getInstance().is(node) && !node.isRunContextCreator()) {
            is.add(new RightClickMenueItem("Haltemarke entfernen") {
                @Override
                public void clicked() {
                    Breakpoints.getInstance().remove(node);
                    mainWindow.removeRightClickMenue();
                }
            });
        }
        if (!node.isDirectInputNode()) {
            is.add(new RightClickMenueItem("Duplizieren") {
                @Override
                public void clicked() {
                    BuildInNodeDefinitionsLibrary bindl = BuildInNodeDefinitionsLibrary.getInstance();
                    mainWindow.getProject().addNode(bindl.get(node.getUniqueNameVersion()), node.getUICenterX() + 10, node.getUICenterY() + 10);
                    mainWindow.removeRightClickMenue();
                }
            });
        }
        is.add(new RightClickMenueItem("Info") {
            @Override
            public void clicked() {

                int LETTERS_PER_LINE = 100;

                // Infotext in Zeilen gliedern...
                StringBuilder sb = new StringBuilder();
                String description = NodeDefinitionDescription.getDescriptionWithoutTags(node.getDefinition());
                String[] infoLines = TextHandler.splitStringToLines(description, LETTERS_PER_LINE);
                for (String line : infoLines) {
                    sb.append(line).append("\n");
                }

                // In-Moeglichkeiten in Zeilen gliedern...
                final String IN_TITLE = "Eingänge:";
                String inString = IN_TITLE;
                if (node.getDefinition() instanceof VariableVisibleInletCount) {
                    Type type = new Type(node.getClassForInlet(0), node.isInletForArray(0));
                    inString += " Dynamische Liste von Elementen des Types " + type.toString();
                } else {
                    boolean first = true;
                    int inletCount = node.getDefinitionInletCount();
                    for (int i = 0; i < inletCount; i++) {
                        Type type = new Type(node.getClassForInlet(i), node.isInletForArray(i));
                        String inletDescription = node.getDefinition().getNameForInlet(i) + " (" + type.toString() + ")";
                        if (!node.getDefinition().isInletEngaged(i)) {
                            inletDescription += " [Opt.]";
                        }
                        if (first) {
                            inString += " " + inletDescription;
                        } else {
                            inString += ", " + inletDescription;
                        }
                        first = false;
                    }
                    if (inletCount == 0) {
                        inString += " /";
                    }
                }
                String[] inLines = TextHandler.splitStringToLines(inString, LETTERS_PER_LINE);

                // Out-Moeglichkeiten in Zeilen gliedern...
                final String OUT_TITLE = "Ausgänge:";
                String outString = OUT_TITLE;
                {
                    boolean first = true;
                    int outletCount = node.getDefinitionOutletCount();
                    for (int i = 0; i < outletCount; i++) {
                        Type type = new Type(node.getClassForOutlet(i), node.isOutletForArray(i));
                        String inletDescription = node.getDefinition().getNameForOutlet(i) + " (" + type.toString() + ")";
                        if (first) {
                            outString += " " + inletDescription;
                        } else {
                            outString += ", " + inletDescription;
                        }
                        first = false;
                    }
                    if (outletCount == 0) {
                        outString += " /";
                    }
                }
                String[] outLines = TextHandler.splitStringToLines(outString, LETTERS_PER_LINE);

                for (String line : inLines) {
                    sb.append("\n").append(line);
                }
                sb.append("\n");
                for (String line : outLines) {
                    sb.append("\n").append(line);
                }

                // Dialog anzeigen...
                try {
                    ImageIcon icon = new ImageIcon(ImageAsset.getImageAssetForName(node.getDefinition().getIconName()).getImage());
                    JOptionPane.showMessageDialog(
                            mainWindow,
                            sb,
                            node.getName(),
                            JOptionPane.PLAIN_MESSAGE,
                            icon
                    );
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(
                            mainWindow,
                            sb,
                            node.getName(),
                            JOptionPane.PLAIN_MESSAGE
                    );
                }

                mainWindow.removeRightClickMenue();
            }
        });

        this.items = is.toArray(new RightClickMenueItem[0]);
    }

}
