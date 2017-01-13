package view.dialogs;

import exceptions.IllegalUserActionException;
import java.awt.Component;
import javax.swing.JOptionPane;

public class ErrorDialog {
    
    public static void showErrorDialog(IllegalUserActionException iuae, Component frame) {
        
        JOptionPane.showMessageDialog(frame,
            iuae.getUserMessage(),
            "Fehler",
            JOptionPane.ERROR_MESSAGE);
    }
    
}
