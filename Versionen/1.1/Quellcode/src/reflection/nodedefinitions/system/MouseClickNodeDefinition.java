package reflection.nodedefinitions.system;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MouseClickNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 8;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Boolean.class;
            case 1:
                return Boolean.class;
            case 2:
                return Boolean.class;
            case 3:
                return Long.class;
            case 4:
                return Long.class;
            case 5:
                return Long.class;
            case 6:
                return Boolean.class;
            case 7:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Links";
            case 1:
                return "Mitte";
            case 2:
                return "Rechts";
            case 3:
                return "Clickdauer in ms";
            case 4:
                return "x";
            case 5:
                return "y";
            case 6:
                return "Drücken";
            case 7:
                return "Loslassen";
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

        return false;
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
        return "Mouse Click";
    }

    @Override
    public String getDescription() {
        return "Simumliert Mausklick an Position (x, y)." + TAG_PREAMBLE + " [UI] ";
    }

    @Override
    public String getUniqueName() {
        return "buildin.MouseClick";
    }

    @Override
    public String getIconName() {
        return "Mouse-Click_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws AWTException, InterruptedException {

        Boolean links = (Boolean) io.in0(0, Boolean.TRUE);
        Boolean mitte = (Boolean) io.in0(1, Boolean.FALSE);
        Boolean rechts = (Boolean) io.in0(2, Boolean.FALSE);
        Long dauer = (Long) io.in0(3, 500L);
        Integer x = (Integer) io.in0(4, null);
        Integer y = (Integer) io.in0(5, null);
        Boolean press = (Boolean) io.in0(6, Boolean.TRUE);
        Boolean release = (Boolean) io.in0(7, Boolean.TRUE);

        Robot robot = new Robot();

        io.terminatedTest();
        if (x != null || y != null) {
            if (x == null || y == null) {
                Point location = MouseInfo.getPointerInfo().getLocation();
                if (x == null) {
                    x = (int) location.getX();
                }
                if (y == null) {
                    y = (int) location.getY();
                }
            }
            io.terminatedTest();
            robot.mouseMove(x, y);
        }
        if (links) {
            if (press) {
                robot.mousePress(InputEvent.BUTTON1_MASK);
            }
            Thread.sleep(dauer);
            if (release) {
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            }
        }
        if (mitte) {
            if (press) {
                robot.mousePress(InputEvent.BUTTON2_MASK);
            }
            Thread.sleep(dauer);
            if (release) {
                robot.mouseRelease(InputEvent.BUTTON2_MASK);
            }
        }
        if (rechts) {
            if (press) {
                robot.mousePress(InputEvent.BUTTON3_MASK);
            }
            Thread.sleep(dauer);
            if (release) {
                robot.mouseRelease(InputEvent.BUTTON3_MASK);
            }
        }
        io.terminatedTest();

    }

}
