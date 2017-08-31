package reflection.nodedefinitions.math;

import reflection.nodedefinitions.images.*;
import java.awt.image.BufferedImage;
import reflection.nodedefinitions.camera.*;
import reflection.*;
import reflection.customdatatypes.math.Vector;
import reflection.customdatatypes.camera.Camera;
import reflection.customdatatypes.camera.Webcam;

public class VectorSizeNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Vector.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Vector";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return false;
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
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Anzahl Zeilen";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Vektor Zeilenanzahl";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Math] Vector Vectorgrösse Vectorgröße Maße Masse Size Dimension";
    }

    @Override
    public String getUniqueName() {
        return "buildin.VectorSize";
    }

    @Override
    public String getIconName() {
        return "Vector-Size_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Vector m = (Vector) io.in0(0, null);
        Integer cs;
        Integer rs;
        if (m == null) {
            rs = 0;
        } else {
            rs = m.getRowCount();
        }

        io.out(0, rs);
    }

}
