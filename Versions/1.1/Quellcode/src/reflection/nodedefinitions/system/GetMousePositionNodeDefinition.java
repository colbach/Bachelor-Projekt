package reflection.nodedefinitions.system;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.MouseInfo;
import java.awt.Point;

public class GetMousePositionNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 0;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int index) {

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
                return "x";
            case 1:
                return "y";
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
        return "Mausposition";
    }

    @Override
    public String getDescription() {
        return "Gibt die x- und y-Koordinaten der Maus weiter." + TAG_PREAMBLE + " [UI] Maus Mouse get";
    }

    @Override
    public String getUniqueName() {
        return "buildin.GetMousePosition";
    }

    @Override
    public String getIconName() {
        return "Mouse-Position_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {
        
        Point location = MouseInfo.getPointerInfo().getLocation();

        Integer x = (int) location.getX();
        Integer y = (int) location.getY();

        io.out(0, x);
        io.out(1, y);
    }

}
