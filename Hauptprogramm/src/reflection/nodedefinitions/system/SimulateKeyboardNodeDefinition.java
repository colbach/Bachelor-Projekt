package reflection.nodedefinitions.system;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import reflection.additionalnodedefinitioninterfaces.Experimental;

public class SimulateKeyboardNodeDefinition implements NodeDefinition, Experimental {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return String.class;
            case 1:
                return Boolean.class;
            case 2:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Eingabe";
            case 1:
                return "Spezielle Ausdrücke";
            case 2:
                return "Mit Enter beenden";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        return false;
    }

    @Override
    public boolean isInletEngaged(int index) {
        if (index == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getOutletCount() {
        return 0;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Tastatureingabe simulieren";
    }

    @Override
    public String getDescription() {
        return "Simuliert Eingabe über Tastatur. Um das Drücken der Entertaste zu simulieren muss \"Spezielle Ausdrücke\" aktiviert sein und die Eingabe \\n enthalten." + TAG_PREAMBLE + "";
    }

    @Override
    public String getUniqueName() {
        return "buildin.SimulateKeyboard";
    }

    @Override
    public String getIconName() {
        return "Keyboard_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        String string = (String) io.in0(0, "");
        Boolean special = (Boolean) io.in0(1, Boolean.TRUE);
        Boolean exitWithEnter = (Boolean) io.in0(2, Boolean.FALSE);

        Robot robot = new Robot();

        int stringLength = string.length();

        boolean escaped = false;
        for (int j = 0; j < stringLength; j++) {

            char letter = string.charAt(j);
            int keycode = KeyEvent.getExtendedKeyCodeForChar(letter);

            if (exitWithEnter) {
                if (escaped) {
                    switch (letter) {
                        case 'n':
                            keycode = KeyEvent.VK_ENTER;
                    }
                }
                if (letter == '\\') {
                    escaped = true;
                } else {
                    escaped = false;
                }
            }

            robot.keyPress(keycode);
            robot.keyRelease(keycode);
        }

        if (exitWithEnter) {
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        }

        try {
            Thread.sleep(520);
        } catch (Exception exec) {
            exec.printStackTrace();
        }
    }

    @Override
    public String getExperimentalNote() {
        return "Dieses Element scheint noch Probleme auf bestimmten Betriebsystemen zu haben.\n"
                + "Bekannte Probleme:"
                + "Auf macOS Sierra kann in seltenen Situationen ein Problem auftreten dass das aktuelle Program seinen Fokus verliert.";
    }

}
