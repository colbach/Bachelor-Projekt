package view.dialogs;

import exceptions.IllegalUserDialogInputException;
import java.awt.Color;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.type.Type;

public class DirectInputDialogs {

    private static String objectArrayToString(Object[] content) {
        if (content == null) {
            return "";
        } else {
            String contentString = "";
            if (content != null) {
                boolean first = true;
                for (Object c : content) {
                    if (!first) {
                        contentString += ", ";
                    }
                    contentString += c.toString();
                    first = false;
                }
            }
            return contentString;
        }
    }

    public static Object[] dialogForUserSettableContent(Type type, Object[] content, JFrame frame) throws IllegalUserDialogInputException {
        if (type.getTypeClass() == String.class) {
            if (!type.isArray()) { // Fall: Kein Array
                String in = (String) JOptionPane.showInputDialog(frame,
                        "String eingeben\n(z.B. Abc)",
                        "Eingabe (String)",
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

        } else if (type.getTypeClass() == Color.class && !type.isArray()) {
            Color color = Color.WHITE;
            if (content != null && content.length > 0) {
                color = (Color) content[0];
            }
            Color in = JColorChooser.showDialog(null, "Eingabe (Color)", color);
            if (in == null) { // Abbruch
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

        } else {
            throw new RuntimeException("Nicht unterstuetzter Typ");
        }
    }

}
