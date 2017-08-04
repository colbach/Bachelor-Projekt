package view.dialogs;

import generalexceptions.IllegalUserDialogInputException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import logging.*;
import model.*;
import model.type.*;
import reflection.customdatatypes.*;
import model.*;
import model.type.*;
import reflection.additionalnodedefinitioninterfaces.SourceCodeTemplate;
import reflection.additionalnodedefinitioninterfaces.SourceCodeTestable;
import reflection.customdatatypes.*;
import reflection.customdatatypes.math.ArrayBasedVector;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.math.OneDimensionalArrayBasedMatrix;
import reflection.customdatatypes.math.Vector;
import utils.ArrayHelper;
import utils.format.TimeFormat;
import view.dialogs.codeinput.*;
import view.dialogs.matrixinput.MatrixInputDialog;
import view.dialogs.vectorinput.VectorInputDialog;

public class DirectInputDialogs {

    private static String objectArrayToString(Object[] content) {
        if (content == null) {
            return "";
        } else {
            String contentString = "";
            boolean first = true;
            for (Object c : content) {
                if (!first) {
                    contentString += ", ";
                }
                if (c == null) {
                    contentString += "null";
                } else {
                    contentString += c.toString();
                }
                first = false;
            }
            return contentString;
        }
    }

    public static Object[] dialogForUserSettableContent(Node node, Type type, Object[] content, JFrame frame, Project project) throws IllegalUserDialogInputException {
        TypeNameTranslation typeNameTranslation = TypeNameTranslation.getGermanInstance();

        if (type.getTypeClass() == Matrix.class && !type.isArray()) {
            Matrix template;
            if (content != null && content.length > 0) {
                template = ((Matrix) content[0]);
            } else {
                template = new OneDimensionalArrayBasedMatrix(new Number[]{0, 0, 1, 0, 1, 0, 0, 0, 1}, 3);
            }
            
            Matrix in = MatrixInputDialog.showMatrixInputDialog(template, frame);
                
            if (in == null) { // Abbruch
                AdditionalLogger.err.println("Benutzer hat Matrix-Input-Dialog abgebrochen");
                return null;
            }

            return new Object[]{in};

        } else if (type.getTypeClass() == Vector.class && !type.isArray()) {
            Vector template;
            if (content != null && content.length > 0) {
                template = ((Vector) content[0]);
            } else {
                template = new ArrayBasedVector(new Number[]{0, 0, 0});
            }
            
            Vector in = VectorInputDialog.showVectorInputDialog(template, frame);
                
            if (in == null) { // Abbruch
                AdditionalLogger.err.println("Benutzer hat Vector-Input-Dialog abgebrochen");
                return null;
            }

            return new Object[]{in};

        } else if (type.getTypeClass() == SourceCode.class && !type.isArray()) {

            String template = "public class NewClass {\n\n// ...\n}";

            if (node != null && node.getDefinition() instanceof SourceCodeTemplate) {
                template = ((SourceCodeTemplate) node.getDefinition()).getTemplate();
            }

            if (content != null && content.length > 0) {
                template = ((SourceCode) content[0]).getJavaCode();
            }

            final String finalTemplate = template;

            SourceCode in = CodeInputDialog.showSourceCodeInputDialog(new CodeInputInstructions() {

                @Override
                public String getTitle() {
                    return "Eingabe (Quellcode)";
                }

                @Override
                public String getTemplate() {
                    return finalTemplate;
                }

                @Override
                public CodeInputTestResult performTest(Object testInstance) {
                    if (node != null && node.getDefinition() instanceof SourceCodeTestable) {
                        SourceCodeTestable test = (SourceCodeTestable) node.getDefinition();
                        CodeInputTestResult codeInputTestResult;
                        try {
                            long time = System.currentTimeMillis();
                            test.performTest(testInstance);
                            codeInputTestResult = new CodeInputTestResult("Test durchgeführt in " + TimeFormat.format(System.currentTimeMillis() - time), true);
                        } catch (Exception ex) {
                            codeInputTestResult = new CodeInputTestResult(ArrayHelper.arrayToMultiLineString(ex.getStackTrace()), false);
                        }
                    }
                    return null;
                }

                @Override
                public boolean isTestable() {
                    return node != null && node.getDefinition() instanceof SourceCodeTestable;
                }
            }, frame);

            if (in == null) { // Abbruch
                AdditionalLogger.err.println("Benutzer hat Code-Input-Dialog abgebrochen");
                return null;
            }

            return new Object[]{in};

        } else if (type.getTypeClass() == String.class) {
            if (!type.isArray()) { // Fall: Kein Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        typeNameTranslation.get(String.class) + " eingeben\n(z.B. Abc)",
                        "Eingabe (" + typeNameTranslation.get(String.class) + ")",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                return new Object[]{in};
            } else { // Fall: Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "String kommagetrennt eingeben\n(z.B. Mr. White, Mr. Orange, Mr. Blonde, Mr. Pink, Mr. Brown, Mr. Blue)",
                        "Eingabe (String...)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                String[] ins = in.split(",");
                Object[] os = new Object[ins.length];
                for (int i = 0; i < ins.length; i++) {
                    os[i] = ins[i].trim();
                }
                return os;
            }

        } else if (type.getTypeClass() == Boolean.class) {
            if (!type.isArray()) { // Fall: Kein Array
                String[] possibilities = {"True", "False"};
                String in = (String) JOptionPane.showInputDialog(
                        frame,
                        "Wählen sie zwischen folgender Auswahl:",
                        "Eingabe (Boolean)",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        possibilities,
                        objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                if (in.equalsIgnoreCase(possibilities[0])) {
                    return new Object[]{((Boolean) true)};
                } else if (in.equalsIgnoreCase(possibilities[1])) {
                    return new Object[]{((Boolean) false)};
                } else {
                    throw new RuntimeException(in + " kann nicht als Boolean interpretiert werden");
                }
            } else { // Fall: Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "Boolean kommagetrennt eingeben\n(z.B. true, true, false)",
                        "Eingabe (Boolean...)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                String[] ins = in.split(",");
                Object[] os = new Object[ins.length];
                for (int i = 0; i < ins.length; i++) {
                    if (ins[i].trim().equalsIgnoreCase("true") || ins[i].trim().equalsIgnoreCase("t") || ins[i].trim().equalsIgnoreCase("1")) {
                        os[i] = new Boolean(true);
                    } else if (ins[i].trim().equalsIgnoreCase("false") || ins[i].trim().equalsIgnoreCase("f") || ins[i].trim().equalsIgnoreCase("0")) {
                        os[i] = new Boolean(false);
                    } else {
                        String userMessage = ins[i].trim() + " kann nicht als Boolean interpretiert werden";
                        throw new IllegalUserDialogInputException("Fehlerhafte Benutzereingabe: " + userMessage, userMessage);
                    }
                }
                return os;
            }

        } else if (type.getTypeClass() == File.class && !type.isArray()) {

            final JFileChooser chooser;
            if (content != null && content.length > 0 && content[0] instanceof File) {
                chooser = new JFileChooser((File) content[0]);
            } else {
                chooser = new JFileChooser(".");
            }
            chooser.setDialogTitle("Datei/Verzeichniss auswählen");
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int rueckgabeWert = chooser.showDialog(frame, "Auswählen");

            if (rueckgabeWert == JFileChooser.APPROVE_OPTION) { // Fall: Benutzer hat nicht abgebrochen

                File file = chooser.getSelectedFile();
                AdditionalLogger.out.println(file.getAbsolutePath() + " ausgewaehlt");
                return new Object[]{file};

            } else { // Fall: Benutzer hat abgebrochen
                AdditionalLogger.err.println("Benutzer hat Dateiauswahldialog abgebrochen");
                return null; // Abbruch
            }

        } else if (type.getTypeClass() == File.class && type.isArray()) {

            final JFileChooser chooser;
            if (content != null && content.length > 0 && content[0] instanceof File) {
                chooser = new JFileChooser((File) content[0]);
            } else {
                chooser = new JFileChooser(".");
            }
            chooser.setDialogTitle("Dateien/Verzeichnisse auswählen");
            chooser.setMultiSelectionEnabled(true);
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int rueckgabeWert = chooser.showDialog(frame, "Auswählen");

            if (rueckgabeWert == JFileChooser.APPROVE_OPTION) { // Fall: Benutzer hat nicht abgebrochen

                File[] files = chooser.getSelectedFiles();
                AdditionalLogger.out.println(files.length + " Dateien ausgewaehlt");
                for (File file : files) {
                    AdditionalLogger.out.println(file.getAbsolutePath() + " ausgewaehlt");
                }
                return files;

            } else { // Fall: Benutzer hat abgebrochen
                AdditionalLogger.err.println("Benutzer hat Dateiauswahldialog abgebrochen");
                return null; // Abbruch
            }

        } else if (type.getTypeClass() == Color.class && !type.isArray()) {
            Color color = Color.WHITE;
            if (content != null && content.length > 0) {
                color = (Color) content[0];
            }
            Color in = JColorChooser.showDialog(null, "Eingabe (Color)", color);
            if (in == null) { // Abbruch
                AdditionalLogger.err.println("Benutzer hat Farbauswahldialog abgebrochen");
                return null;
            }
            return new Object[]{in};

        } else if (type.getTypeClass() == Number.class) {
            if (!type.isArray()) { // Fall: Kein Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "Wert eingeben\n(z.B. 3.14159)",
                        "Eingabe (Number)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                try {
                    return new Object[]{new Double(in.trim())};
                } catch (NumberFormatException numberFormatException) {
                    String userMessage = in.trim() + " kann nicht als Double interpretiert werden";
                    throw new IllegalUserDialogInputException("Fehlerhafte Benutzereingabe: " + userMessage, userMessage);
                }
            } else { // Fall: Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "Werte kommagetrennt eingeben\n(z.B. 42, 3.14, 420)",
                        "Eingabe (Number...)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                String[] ins = in.split(",");
                Object[] os = new Object[ins.length];
                for (int i = 0; i < ins.length; i++) {
                    try {
                        os[i] = new Double(ins[i].trim());
                    } catch (NumberFormatException numberFormatException) {
                        String userMessage = in.trim() + " kann nicht als Double interpretiert werden";
                        throw new IllegalUserDialogInputException("Fehlerhafte Benutzereingabe: " + userMessage, userMessage);
                    }
                }
                return os;
            }

        } else if (type.getTypeClass() == Integer.class) {
            if (!type.isArray()) { // Fall: Kein Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "Integer eingeben\n(z.B. 54292)",
                        "Eingabe (Integer)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                try {
                    return new Object[]{new Integer(in.trim())};
                } catch (NumberFormatException numberFormatException) {
                    String userMessage = in.trim() + " kann nicht als Integer interpretiert werden";
                    throw new IllegalUserDialogInputException("Fehlerhafte Benutzereingabe: " + userMessage, userMessage);
                }
            } else { // Fall: Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "Integer kommagetrennt eingeben\n(z.B. -8, 5, -3, 2, -1, 1, 0)",
                        "Eingabe (Integer...)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                String[] ins = in.split(",");
                Object[] os = new Object[ins.length];
                for (int i = 0; i < ins.length; i++) {
                    try {
                        os[i] = new Integer(ins[i].trim());
                    } catch (NumberFormatException numberFormatException) {
                        String userMessage = in.trim() + " kann nicht als Integer interpretiert werden";
                        throw new IllegalUserDialogInputException("Fehlerhafte Benutzereingabe: " + userMessage, userMessage);
                    }
                }
                return os;
            }

        } else if (type.getTypeClass() == Long.class) {
            if (!type.isArray()) { // Fall: Kein Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "Long eingeben\n(z.B. 5762963)",
                        "Eingabe (Long)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                try {
                    return new Object[]{new Long(in.trim())};
                } catch (NumberFormatException numberFormatException) {
                    String userMessage = in.trim() + " kann nicht als Long interpretiert werden";
                    throw new IllegalUserDialogInputException("Fehlerhafte Benutzereingabe: " + userMessage, userMessage);
                }
            } else { // Fall: Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "Long kommagetrennt eingeben\n(z.B. 0, -98927, 98929, -98939)",
                        "Eingabe (Long...)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                String[] ins = in.split(",");
                Object[] os = new Object[ins.length];
                for (int i = 0; i < ins.length; i++) {
                    try {
                        os[i] = new Long(ins[i].trim());
                    } catch (NumberFormatException numberFormatException) {
                        String userMessage = in.trim() + " kann nicht als Long interpretiert werden";
                        throw new IllegalUserDialogInputException("Fehlerhafte Benutzereingabe: " + userMessage, userMessage);
                    }
                }
                return os;
            }

        } else if (type.getTypeClass() == Double.class) {
            if (!type.isArray()) { // Fall: Kein Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "Double eingeben\n(z.B. 2.718281)",
                        "Eingabe (Double)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                try {
                    return new Object[]{new Double(in.trim())};
                } catch (NumberFormatException numberFormatException) {
                    String userMessage = in.trim() + " kann nicht als Double interpretiert werden";
                    throw new IllegalUserDialogInputException("Fehlerhafte Benutzereingabe: " + userMessage, userMessage);
                }
            } else { // Fall: Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "Double kommagetrennt eingeben\n(z.B. -100, 3.99, 1, 50)",
                        "Eingabe (Double...)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                String[] ins = in.split(",");
                Object[] os = new Object[ins.length];
                for (int i = 0; i < ins.length; i++) {
                    try {
                        os[i] = new Double(ins[i].trim());
                    } catch (NumberFormatException numberFormatException) {
                        String userMessage = in.trim() + " kann nicht als Double interpretiert werden";
                        throw new IllegalUserDialogInputException("Fehlerhafte Benutzereingabe: " + userMessage, userMessage);
                    }
                }
                return os;
            }

        } else if (type.getTypeClass() == Float.class) {
            if (!type.isArray()) { // Fall: Kein Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "Float eingeben\n(z.B. 1.001)",
                        "Eingabe (Float)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                try {
                    return new Object[]{new Float(in.trim())};
                } catch (NumberFormatException numberFormatException) {
                    String userMessage = in.trim() + " kann nicht als Float interpretiert werden";
                    throw new IllegalUserDialogInputException("Fehlerhafte Benutzereingabe: " + userMessage, userMessage);
                }
            } else { // Fall: Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "Float kommagetrennt eingeben\n(z.B. 33, 0.99, 5.0, -7)",
                        "Eingabe (Float...)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                String[] ins = in.split(",");
                Object[] os = new Object[ins.length];
                for (int i = 0; i < ins.length; i++) {
                    try {
                        os[i] = new Float(ins[i].trim());
                    } catch (NumberFormatException numberFormatException) {
                        String userMessage = in.trim() + " kann nicht als Float interpretiert werden";
                        throw new IllegalUserDialogInputException("Fehlerhafte Benutzereingabe: " + userMessage, userMessage);
                    }
                }
                return os;
            }

        } else if (type.getTypeClass() == Byte.class) {
            if (!type.isArray()) { // Fall: Kein Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "Byte eingeben\n(z.B. 30)",
                        "Eingabe (Byte)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                try {
                    return new Object[]{new Byte(in.trim())};
                } catch (NumberFormatException numberFormatException) {
                    String userMessage = in.trim() + " kann nicht als Byte interpretiert werden";
                    throw new IllegalUserDialogInputException("Fehlerhafte Benutzereingabe: " + userMessage, userMessage);
                }
            } else { // Fall: Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "Byte kommagetrennt eingeben\n(z.B. 16, -32, 64)",
                        "Eingabe (Byte...)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                String[] ins = in.split(",");
                Object[] os = new Object[ins.length];
                for (int i = 0; i < ins.length; i++) {
                    try {
                        os[i] = new Byte(ins[i].trim());
                    } catch (NumberFormatException numberFormatException) {
                        String userMessage = in.trim() + " kann nicht als Byte interpretiert werden";
                        throw new IllegalUserDialogInputException("Fehlerhafte Benutzereingabe: " + userMessage, userMessage);
                    }
                }
                return os;
            }

        } else if (type.getTypeClass() == Short.class) {
            if (!type.isArray()) { // Fall: Kein Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "Short eingeben\n(z.B. 300)",
                        "Eingabe (Short)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                try {
                    return new Object[]{new Short(in.trim())};
                } catch (NumberFormatException numberFormatException) {
                    String userMessage = in.trim() + " kann nicht als Short interpretiert werden";
                    throw new IllegalUserDialogInputException("Fehlerhafte Benutzereingabe: " + userMessage, userMessage);
                }
            } else { // Fall: Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "Short kommagetrennt eingeben\n(z.B. 77, 69, -1)",
                        "Eingabe (Short...)",
                        JOptionPane.PLAIN_MESSAGE, null, null, objectArrayToString(content));
                if (in == null) { // Abbruch
                    return null;
                }
                String[] ins = in.split(",");
                Object[] os = new Object[ins.length];
                for (int i = 0; i < ins.length; i++) {
                    try {
                        os[i] = new Short(ins[i].trim());
                    } catch (NumberFormatException numberFormatException) {
                        String userMessage = in.trim() + " kann nicht als Short interpretiert werden";
                        throw new IllegalUserDialogInputException("Fehlerhafte Benutzereingabe: " + userMessage, userMessage);
                    }
                }
                return os;
            }

        } else if (type.getTypeClass() == SmartIdentifier.class && !type.isArray()) {

            SmartIdentifierContextImplementation smartIdentifierContext = project.getSmartIdentifierContext();
            SmartIdentifier[] smartIdentifiers = smartIdentifierContext.getSmartIdentifiers().toArray(new SmartIdentifier[0]);
            String[] options = new String[smartIdentifiers.length + 1];
            for (int i = 0; i < smartIdentifiers.length; i++) {
                options[i] = smartIdentifiers[i].toString();
            }
            final String NEW = "Neu";
            options[options.length - 1] = NEW;

            String inID = (String) JOptionPane.showInputDialog(
                    frame,
                    "Wählen sie zwischen folgender Auswahl:",
                    "Auswahl (SmartIdentifier)",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    objectArrayToString(content));

            if (inID != null && inID.equals(NEW)) {
                String inNewID = (String) JOptionPane.showInputDialog(frame,
                        "SmartIdentifier eingeben",
                        "Eingabe (neuer SmartIdentifier)",
                        JOptionPane.PLAIN_MESSAGE, null, null, "");
                if (inNewID == null) { // Abbruch
                    System.out.println("*** 1");
                    return null;
                }
                if (smartIdentifierContext.doesSmartIdentifierExist(inNewID)) {

                    Object[] useUsenot = {"Verwenden", "Abbrechen"};
                    int n = JOptionPane.showOptionDialog(frame,
                            "Identifier existiert bereits. Trotzdem verwenden?",
                            "Identifier bereits registiert",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            useUsenot,
                            useUsenot[1]);
                    if (n == 1) {
                        return null;
                    } else {
                        return new Object[]{smartIdentifierContext.getOrCreate(inNewID)};
                    }

                } else {
                    return new Object[]{smartIdentifierContext.getOrCreate(inNewID)};
                }
            } else if (inID != null) {
                return new Object[]{smartIdentifierContext.getOrCreate(inID)};

            } else { // Abbruch
                return null;
            }

        } else {
            throw new RuntimeException("Nicht unterstuetzter Typ");
        }
    }

}
