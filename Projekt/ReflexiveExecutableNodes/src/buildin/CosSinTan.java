package buildin;

import reflection.NodeDefinition;

public class CosSinTan implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Number.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "x";
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
    public int getOutletCount() {
        return 4;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Double.class;
            case 1:
                return Double.class;
            case 2:
                return Double.class;
            case 3:
                return Double.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "cos";
            case 1:
                return "sin";
            case 2:
                return "tan";
            case 3:
                return "tanh";
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
            case 2:
                return false;
            case 3:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Cos-Sin-Tan";
    }

    @Override
    public String getDescription() {
        return "Berechnet Cos, Sin, Tan und Tanh von x. " + TAG_PREAMBLE + " Cosinus Sinus Kurve Dreieck Berechnen x cos(x) sin(x) tan(x) tanh(x) cotan";
    }

    @Override
    public String getUniqueName() {
        return "buildin.CosSinTan";
    }

    @Override
    public String getIconName() {
        return "cos-sin-tan_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io) {

        Number x = (Number) io.in0(0, Math.PI);

        if (io.outConnected(0)) {
            io.out(0, Math.cos(x.doubleValue()));
        }
        if (io.outConnected(1)) {
            io.out(1, Math.sin(x.doubleValue()));
        }
        if (io.outConnected(2)) {
            io.out(2, Math.tan(x.doubleValue()));
        }
        if (io.outConnected(3)) {
            io.out(3, Math.tanh(x.doubleValue()));
        }

    }

}
