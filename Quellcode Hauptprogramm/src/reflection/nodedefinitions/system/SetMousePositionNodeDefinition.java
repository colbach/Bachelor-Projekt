package reflection.nodedefinitions.system;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

public class SetMousePositionNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Integer.class;
            case 1:
                return Integer.class;
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
                return "x";
            case 1:
                return "y";
            case 2:
                return "Relativ";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int index) {
        if (index == 0 || index == 1) {
            return true;
        }
        return false;
    }

    @Override
    public int getOutletCount() {
        return 2;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Integer.class;
            case 1:
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Neu x";
            case 1:
                return "Neu y";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            case 1:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Mauspostion setzen";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + "";
    }

    @Override
    public String getUniqueName() {
        return "buildin.SetMousePosition";
    }

    @Override
    public String getIconName() {
        return "Set-Mouse-Position_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws AWTException {

        Integer x = (Integer) io.in0(0, 0);
        Integer y = (Integer) io.in0(1, 0);
        Boolean relativ = (Boolean) io.in0(2, (Boolean) false);

        Robot robot = new Robot();

        if (relativ) {
            Point location = MouseInfo.getPointerInfo().getLocation();
            x += (int) location.getX();
            y += (int) location.getY();
        }
        
        robot.mouseMove(x, y);
        
        io.out(0, x);
        io.out(1, y);
    }

}
