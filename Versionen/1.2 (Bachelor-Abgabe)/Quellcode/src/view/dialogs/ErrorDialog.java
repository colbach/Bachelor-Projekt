package view.dialogs;

import generalexceptions.IllegalUserActionException;
import java.awt.Component;
import javax.swing.JOptionPane;

public class ErrorDialog {

    public static void showErrorDialog(IllegalUserActionException iuae, Component frame) {

        System.err.println(iuae.getMessage());
        JOptionPane.showMessageDialog(frame,
                iuae.getUserMessage() + " (Siehe Log fuer genauere Informationen)",
                "Fehler",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void showErrorDialog(Exception iuae, Component frame) {
        
        String className = iuae.getClass().getName();
        int punktPosition = className.lastIndexOf(".");
        if (punktPosition != -1) {
            className = className.substring(punktPosition + 1);
        }
        String message = iuae.getMessage();
        if (message == null) {
            message = "nicht weiter genau spezifizierte " + className;
        }
        System.err.println(iuae.getMessage());
        JOptionPane.showMessageDialog(frame,
                message + " (Siehe Log fuer genauere Informationen)",
                "Fehler (" + className + ")",
                JOptionPane.ERROR_MESSAGE);
    }

}
