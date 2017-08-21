package reflection.nodedefinitions.math;

import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;
import reflection.customdatatypes.math.MathObject;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.math.NumberMathObject;
import reflection.customdatatypes.math.Vector;
import utils.math.MatrixAndVectorOperations;

public class ModNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        return Integer.class;
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        if (inletIndex == 0) {
            return "A";
        } else if (inletIndex == 1) {
            return "B";
        }
        return null;
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        return false;
    }

    @Override
    public boolean isInletEngaged(int i) {
        return true;
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int outletIndex) {
        return Integer.class;
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        return "Resultat";
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return false;
    }

    @Override
    public String getName() {
        return "A mod B";
    }

    @Override
    public String getDescription() {
        return "Berechnet A Modulo B." + TAG_PREAMBLE + " [Math] Mod %";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Mod";
    }

    @Override
    public String getIconName() {
        return "Mod_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {
        Integer oA = io.inN(0, 0).intValue();
        Integer oB = io.inN(1, 0).intValue();

        io.out(0, oA % oB);

    }

}
