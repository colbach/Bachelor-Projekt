package view.main.rightclickmenue;

import exceptions.IllegalUserDialogInputException;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import javax.swing.WindowConstants;
import model.Node;
import model.Project;
import view.main.MainWindow;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.Inlet;
import model.Outlet;
import model.type.Type;
import model.directinput.DirectInputNodeDefinition;
import model.directinput.DirectInputSupport;
import model.runproject.Breakpoints;
import settings.GeneralSettings;
import utils.TextHandler;
import static view.Constants.DEFAULT_FONT_COLOR;
import static view.Constants.INFO_IN_OUT_CAPABILITIES_FONT;
import static view.Constants.INFO_LABEL_FONT;
import view.assets.ImageAsset;
import view.dialogs.DirectInputDialogs;
import view.dialogs.ErrorDialog;
import view.main.runreport.RunReportWindow;
import view.nodecollection.NodeCollectionWindow;

public class OnNodeRightClickMenue extends RightClickMenue {

    public OnNodeRightClickMenue(int x, int y, Node node, Project project, MainWindow mainWindow) {
        super(null, x, y, mainWindow.getWidth(), mainWindow.getHeight(), mainWindow);

        ArrayList<RightClickMenueItem> is = new ArrayList<>();
        is.add(new RightClickMenueItem("Löschen") {
            @Override
            public void clicked() {
                project.removeNode(node);
                mainWindow.removeRightClickMenue();
            }
        });
        if (node.getInletCount() > 0) {
            is.add(new RightClickMenueItem("Direkter Wert für Eingang setzen") {
                @Override
                public void clicked() {
                    ArrayList<RightClickMenueItem> ii = new ArrayList<>();
                    ArrayList<Boolean> ibc = new ArrayList<>();
                    ArrayList<Boolean> ibs = new ArrayList<>();
                    ArrayList<Boolean> ib = new ArrayList<>();
                    for (Inlet inlet : node.getInlets()) {
                        RightClickMenueItem rcmi = new RightClickMenueItem(inlet.getName() + " (" + inlet.getType().toString() + ")", DirectInputSupport.isTypeSupported(inlet.getType())) {
                            @Override
                            public void clicked() {

                                // DirectInputNodeDefinition erstellen...
                                Type type = inlet.getType();
                                if (!DirectInputSupport.isDirectInputForTypeAvailable(type)) { // Falls kein Direkter Input verfuegbar ist
                                    type = (Type) JOptionPane.showInputDialog(
                                            mainWindow,
                                            "Bitte Typ auswählen welcher mit dem Eingang verbunden werden soll",
                                            "Typ auswählen",
                                            JOptionPane.PLAIN_MESSAGE,
                                            null,
                                            DirectInputSupport.compartibleDirectInputTypeForType(type),
                                            null);
                                    if (type == null) { // Wenn Benutzer abgebrochen hat
                                        return;
                                    }
                                }
                                Type contentTypeForDirectInputNodeDefinition = type;
                                DirectInputNodeDefinition dind = new DirectInputNodeDefinition() {
                                    @Override
                                    public Type getContentType() {
                                        return contentTypeForDirectInputNodeDefinition;
                                    }
                                };

                                // neuen Wert via User-Input einholen...
                                Object[] userInput = null;
                                try {
                                    userInput = DirectInputDialogs.dialogForUserSettableContent(dind.getContentType(), new Object[]{}, mainWindow);
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
                                    Node directInputNode = new Node(dind, inlet);
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
        if (project.getStartNode() != node) {
            is.add(new RightClickMenueItem("Als Start-Element festlegen") {
                @Override
                public void clicked() {
                    project.setStartNode(node);
                    mainWindow.removeRightClickMenue();
                }
            });
        }
        if (!Breakpoints.getInstance().is(node)) {
            is.add(new RightClickMenueItem("Haltemarke setzen") {
                @Override
                public void clicked() {
                    Breakpoints.getInstance().add(node);
                    mainWindow.removeRightClickMenue();
                }
            });
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
        } else {
            is.add(new RightClickMenueItem("Haltemarke entfernen") {
                @Override
                public void clicked() {
                    Breakpoints.getInstance().remove(node);
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
                if (node.getDefinition().getDescription() != null) {
                    String[] infoLines = TextHandler.splitStringToLines(node.getDefinition().getDescription(), LETTERS_PER_LINE);
                    for (String line : infoLines) {
                        sb.append(line).append("\n");
                    }
                }

                // In-Moeglichkeiten in Zeilen gliedern...
                final String IN_TITLE = "Eingänge:";
                String inString = IN_TITLE;
                {
                    boolean first = true;
                    for (int i = 0; i < node.getInletCount(); i++) {
                        Type type = new Type(node.getClassForInlet(i), node.isInletForArray(i));
                        String inletDescription = node.getDefinition().getNameForInlet(i) + " (" + type.toString() + ")";
                        if (first) {
                            inString += " " + inletDescription;
                        } else {
                            inString += ", " + inletDescription;
                        }
                        first = false;
                    }
                }
                String[] inLines = TextHandler.splitStringToLines(inString, LETTERS_PER_LINE);

                // Out-Moeglichkeiten in Zeilen gliedern...
                final String OUT_TITLE = "Ausgänge:";
                String outString = OUT_TITLE;
                {
                    boolean first = true;
                    for (int i = 0; i < node.getOutletCount(); i++) {
                        Type type = new Type(node.getClassForOutlet(i), node.isOutletForArray(i));
                        String inletDescription = node.getDefinition().getNameForOutlet(i) + " (" + type.toString() + ")";
                        if (first) {
                            outString += " " + inletDescription;
                        } else {
                            outString += ", " + inletDescription;
                        }
                        first = false;
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
