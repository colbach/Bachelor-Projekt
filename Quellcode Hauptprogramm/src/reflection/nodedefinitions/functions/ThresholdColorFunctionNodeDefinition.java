package reflection.nodedefinitions.functions;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import reflection.nodedefinitionsupport.functions.FunctionNotAdoptableException;
import reflection.customdatatypes.Function;
import java.awt.Color;
import utils.images.BrightnessCalculator;

public class ThresholdColorFunctionNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 8;
    }

    @Override
    public Class getClassForInlet(int index) {
        return Float.class;
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Minimum Rot";
            case 1:
                return "Minimum Grün";
            case 2:
                return "Minimum Blau";
            case 3:
                return "Minimum Helligkeit";
            case 4:
                return "Maximum Rot";
            case 5:
                return "Maximum Grün";
            case 6:
                return "Maximum Blau";
            case 7:
                return "Maximum Helligkeit";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        return false;
    }

    @Override
    public boolean isInletEngaged(int i) {
        return false;
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        return Function.class;
    }

    @Override
    public String getNameForOutlet(int index) {
        return "(Farbe) -> Wahrheitswert";
    }

    @Override
    public boolean isOutletForArray(int index) {
        return false;
    }

    @Override
    public String getName() {
        return "Schwellenwert-Farb-Funktion erstellen";
    }

    @Override
    public String getDescription() {
        return "Verwendete Werte müssen im Wertebereich 0 und 1 liegen." + TAG_PREAMBLE + " [Funktionen] min max Farbe Color";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ThresholdColorFunction";
    }

    @Override
    public String getIconName() {
        return "Threshold-Color-Function_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        float minR = (Float) io.in0(0, 0F);
        float minG = (Float) io.in0(1, 0F);
        float minB = (Float) io.in0(2, 0F);
        float minL = (Float) io.in0(3, 0F);

        float maxR = (Float) io.in0(4, 1F);
        float maxG = (Float) io.in0(5, 1F);
        float maxB = (Float) io.in0(6, 1F);
        float maxL = (Float) io.in0(7, 1F);

        io.terminatedTest();
        if (io.outConnected(0)) {
            io.out(0, new Function() {
                @Override
                public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {

                    Color c = Function.parameterToColor(parameter);
                    float r = c.getRed() / 256f;
                    float g = c.getGreen() / 256f;
                    float b = c.getBlue() / 256f;
                    double l = 0.5f;
                    if (minL > 0 || maxL < 1) {
                        l = BrightnessCalculator.calculateBrightness(r, g, b);
                    }

                    boolean returnValue = true;
                    if (r < minR) {
                        returnValue = false;
                    } else if (r > maxR) {
                        returnValue = false;
                    } else if (g < minG) {
                        returnValue = false;
                    } else if (g > maxG) {
                        returnValue = false;
                    } else if (b < minB) {
                        returnValue = false;
                    } else if (b > maxB) {
                        returnValue = false;
                    } else if (l < minL) {
                        returnValue = false;
                    } else if (l > maxL) {
                        returnValue = false;
                    }

                    return new Object[]{(Boolean) returnValue};
                }
            });
        }
    }
}
