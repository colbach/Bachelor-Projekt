package view.dialogs;

import java.awt.Component;
import javax.swing.JOptionPane;
import model.check.CheckResult;
import settings.GeneralSettings;
import static settings.GeneralSettings.CHECK_PROJECTS_BEFOR_RUN_KEY;

public class CheckDialog {

    public static CheckDialogResult showCheckDialog(CheckResult checkResult, Component frame) {

        if (!GeneralSettings.getInstance().getBoolean(CHECK_PROJECTS_BEFOR_RUN_KEY, true)) {
            return CheckDialogResult.CONTINUE;

        } else if (checkResult.isEmpty()) {
            return CheckDialogResult.CONTINUE;

        } else {

            String message = "";
            for (int i = 0; i < checkResult.size(); i++) {
                message += "- " + checkResult.get(i).getUserMessage() + "\n";
            }

            if (checkResult.projectIsStillRunnable()) {

                Object[] options = {"Ignorieren und trotzdem fortfahren", "Abbruch"};
                int n = JOptionPane.showOptionDialog(frame,
                        message,
                        "Das Projekt enthällt Warnungen",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        options,
                        options[0]);
                if (n == 0) {
                    return CheckDialogResult.CONTINUE;
                } else {
                    return CheckDialogResult.CANCEL;
                }

            } else {

                Object[] options = {"Ok"};
                JOptionPane.showOptionDialog(frame,
                        message,
                        "Das Projekt kann nicht ausgeführt werden",
                        JOptionPane.OK_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        options,
                        options[0]);
                return CheckDialogResult.CANCEL;
            }

        }
    }
}
