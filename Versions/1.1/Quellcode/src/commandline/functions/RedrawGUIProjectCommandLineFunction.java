package commandline.functions;

import main.componenthub.ComponentHub;
import commandline.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Node;
import model.Project;
import static utils.text.CharacterRepeateHelper.*;
import view.main.MainWindow;

public class RedrawGUIProjectCommandLineFunction implements CommandLineFunction {

    @Override
    public void execute(String[] param, CommandLine commandLine) {

        switch (param.length) {
            case 0:
                MainWindow gui = ComponentHub.getInstance().getGUI();
                if (gui != null) {
                    gui.repaint();
                } else {
                    System.err.println("GUI nicht verfuegbar.");
                }

                break;

            default:
                System.err.println("Zu viele Argumente.");
                break;
        }
    }

    @Override
    public String getDescription() {
        return "Loest Redraw der GUI aus.";
    }

    @Override
    public String getLongDescription() {
        return getDescription() + " (Nur Hauptfenster)";
    }

    @Override
    public String getUsage() {
        return getName();
    }

    @Override
    public String getName() {
        return "redraw";
    }

    @Override
    public String getAliases() {
        return "redrawui redrawgui";
    }
}
