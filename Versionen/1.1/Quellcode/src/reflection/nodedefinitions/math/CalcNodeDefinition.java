package reflection.nodedefinitions.math;

import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;

public class CalcNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Number.class;
            case 1:
                return Number.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "A";
            case 1:
                return "B";
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
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
        return true;
    }

    @Override
    public int getOutletCount() {
        return 4;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Number.class;
            case 1:
                return Number.class;
            case 2:
                return Number.class;
            case 3:
                return Number.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "A + B";
            case 1:
                return "A - B";
            case 2:
                return "A * B";
            case 3:
                return "A / B";
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
        return "Rechner";
    }

    @Override
    public String getDescription() {
        return "Addiert, Subtrahiert, Multipliziert oder Dividiert zwei Werte." + TAG_PREAMBLE + " [Math] Rechner calculator addieren subtrahieren dividieren Math rechnen Taschenrechner mal plus minus geteilt divide times * / - + ^";
    }

    @Override
    public String getUniqueName() {
        return "buildin.calc";
    }

    @Override
    public String getIconName() {
        return "Calculator_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {
        Number oA = (Number) io.in0(0, 0);
        Number oB = (Number) io.in0(1, 1);

        // Input auf gleiche Klasse casten...
        if (oA instanceof Double && oB instanceof Float) {
            oB = new Double((Float) oB);
        } else if (oA instanceof Float && oB instanceof Long) {
            oB = new Float((Long) oB);
        } else if (oA instanceof Float && oB instanceof Integer) {
            oB = new Float((Integer) oB);
        } else if (oA instanceof Float && oB instanceof Short) {
            oB = new Float((Short) oB);
        } else if (oA instanceof Float && oB instanceof Byte) {
            oB = new Float((Byte) oB);
        } else if (oA instanceof Double && oB instanceof Long) {
            oB = new Double((Long) oB);
        } else if (oA instanceof Double && oB instanceof Integer) {
            oB = new Double((Integer) oB);
        } else if (oA instanceof Double && oB instanceof Short) {
            oB = new Double((Short) oB);
        } else if (oA instanceof Double && oB instanceof Byte) {
            oB = new Double((Byte) oB);
        } else if (oA instanceof Long && oB instanceof Integer) {
            oB = new Long((Integer) oB);
        } else if (oA instanceof Long && oB instanceof Short) {
            oB = new Long((Short) oB);
        } else if (oA instanceof Long && oB instanceof Byte) {
            oB = new Long((Byte) oB);
        } else if (oA instanceof Integer && oB instanceof Short) {
            oB = new Integer((Short) oB);
        } else if (oA instanceof Integer && oB instanceof Byte) {
            oB = new Integer((Byte) oB);
        } else if (oA instanceof Short && oB instanceof Byte) {
            oA = new Integer((Short) oA);
            oB = new Integer((Byte) oB);
        } // Umgekehrt
        else if (oB instanceof Double && oA instanceof Float) {
            oA = new Double((Float) oA);
        } else if (oB instanceof Float && oA instanceof Long) {
            oA = new Float((Long) oA);
        } else if (oB instanceof Float && oA instanceof Integer) {
            oA = new Float((Integer) oA);
        } else if (oB instanceof Float && oA instanceof Short) {
            oA = new Float((Short) oA);
        } else if (oB instanceof Float && oA instanceof Byte) {
            oA = new Float((Byte) oA);
        } else if (oB instanceof Double && oA instanceof Long) {
            oA = new Double((Long) oA);
        } else if (oB instanceof Double && oA instanceof Integer) {
            oA = new Double((Integer) oA);
        } else if (oB instanceof Double && oA instanceof Short) {
            oA = new Double((Short) oA);
        } else if (oB instanceof Double && oA instanceof Byte) {
            oA = new Double((Byte) oA);
        } else if (oB instanceof Long && oA instanceof Integer) {
            oA = new Long((Integer) oA);
        } else if (oB instanceof Long && oA instanceof Short) {
            oA = new Long((Short) oA);
        } else if (oB instanceof Long && oA instanceof Byte) {
            oA = new Long((Byte) oA);
        } else if (oB instanceof Integer && oA instanceof Short) {
            oA = new Integer((Short) oA);
        } else if (oB instanceof Integer && oA instanceof Byte) {
            oA = new Integer((Byte) oA);
        } else if (oB instanceof Short && oA instanceof Byte) {
            oB = new Integer((Short) oB);
            oA = new Integer((Byte) oA);
        }

        // Operation ausfueren...
        // +
        if (io.outConnected(0)) {
            if (oA instanceof Double && oB instanceof Double) {
                Double dA = (Double) oA;
                Double dB = (Double) oB;
                io.out(0, new Double(dA + dB));
            } else if (oA instanceof Float && oB instanceof Float) {
                Float fA = (Float) oA;
                Float fB = (Float) oB;
                io.out(0, new Float(fA + fB));
            } else if (oA instanceof Long && oB instanceof Long) {
                Long lA = (Long) oA;
                Long lB = (Long) oB;
                io.out(0, new Long(lA + lB));
            } else if (oA instanceof Integer && oB instanceof Integer) {
                Integer iA = (Integer) oA;
                Integer iB = (Integer) oB;
                io.out(0, new Integer(iA + iB));
            } else if (oA instanceof Short && oB instanceof Short) {
                Short sA = (Short) oA;
                Short sB = (Short) oB;
                io.out(0, new Integer(sA + sB));
            } else if (oA instanceof Byte && oB instanceof Byte) {
                Byte bA = (Byte) oA;
                Byte bB = (Byte) oB;
                io.out(0, new Integer(bA + bB));
            } else {
                System.err.println("Datentypen konnten nicht richtig gecastet werden. Fallback auf Double");
                Double dA = ((Number) oA).doubleValue();
                Double dB = ((Number) oB).doubleValue();
                io.out(0, dA + dB);
            }
        }

        // -
        if (io.outConnected(1)) {
            if (oA instanceof Double && oB instanceof Double) {
                Double dA = (Double) oA;
                Double dB = (Double) oB;
                io.out(1, new Double(dA - dB));
            } else if (oA instanceof Float && oB instanceof Float) {
                Float fA = (Float) oA;
                Float fB = (Float) oB;
                io.out(1, new Float(fA - fB));
            } else if (oA instanceof Long && oB instanceof Long) {
                Long lA = (Long) oA;
                Long lB = (Long) oB;
                io.out(1, new Long(lA - lB));
            } else if (oA instanceof Integer && oB instanceof Integer) {
                Integer iA = (Integer) oA;
                Integer iB = (Integer) oB;
                io.out(1, new Integer(iA - iB));
            } else if (oA instanceof Short && oB instanceof Short) {
                Short sA = (Short) oA;
                Short sB = (Short) oB;
                io.out(1, new Integer(sA - sB));
            } else if (oA instanceof Byte && oB instanceof Byte) {
                Byte bA = (Byte) oA;
                Byte bB = (Byte) oB;
                io.out(1, new Integer(bA - bB));
            } else {
                System.err.println("Datentypen konnten nicht richtig gecastet werden. Fallback auf Double");
                Double dA = ((Number) oA).doubleValue();
                Double dB = ((Number) oB).doubleValue();
                io.out(1, dA - dB);
            }
        }

        // *
        if (io.outConnected(2)) {
            if (oA instanceof Double && oB instanceof Double) {
                Double dA = (Double) oA;
                Double dB = (Double) oB;
                io.out(2, new Double(dA * dB));
            } else if (oA instanceof Float && oB instanceof Float) {
                Float fA = (Float) oA;
                Float fB = (Float) oB;
                io.out(2, new Float(fA * fB));
            } else if (oA instanceof Long && oB instanceof Long) {
                Long lA = (Long) oA;
                Long lB = (Long) oB;
                io.out(2, new Long(lA * lB));
            } else if (oA instanceof Integer && oB instanceof Integer) {
                Integer iA = (Integer) oA;
                Integer iB = (Integer) oB;
                io.out(2, new Integer(iA * iB));
            } else if (oA instanceof Short && oB instanceof Short) {
                Short sA = (Short) oA;
                Short sB = (Short) oB;
                io.out(2, new Integer(sA * sB));
            } else if (oA instanceof Byte && oB instanceof Byte) {
                Byte bA = (Byte) oA;
                Byte bB = (Byte) oB;
                io.out(2, new Integer(bA * bB));
            } else {
                System.err.println("Datentypen konnten nicht richtig gecastet werden. Fallback auf Double");
                Double dA = ((Number) oA).doubleValue();
                Double dB = ((Number) oB).doubleValue();
                io.out(2, dA * dB);
            }
        }

        // /
        if (io.outConnected(3)) {
            if (oB.doubleValue() == 0f) { // Spezialfall Null
                io.out(3, Double.NaN);
            } else if (oA instanceof Double && oB instanceof Double) {
                Double dA = (Double) oA;
                Double dB = (Double) oB;
                io.out(3, new Double(dA / dB));
            } else if (oA instanceof Float && oB instanceof Float) {
                Float fA = (Float) oA;
                Float fB = (Float) oB;
                io.out(3, new Float(fA / fB));
            } else if (oA instanceof Long && oB instanceof Long) {
                Long lA = (Long) oA;
                Long lB = (Long) oB;
                io.out(3, new Double(lA / (double) lB));
            } else if (oA instanceof Integer && oB instanceof Integer) {
                Integer iA = (Integer) oA;
                Integer iB = (Integer) oB;
                io.out(3, new Double(iA / (double) iB));
            } else if (oA instanceof Short && oB instanceof Short) {
                Short sA = (Short) oA;
                Short sB = (Short) oB;
                io.out(3, new Double(sA / (double) sB));
            } else if (oA instanceof Byte && oB instanceof Byte) {
                Byte bA = (Byte) oA;
                Byte bB = (Byte) oB;
                io.out(3, new Double(bA / (double) bB));
            } else {
                System.err.println("Datentypen konnten nicht richtig gecastet werden. Fallback auf Double");
                Double dA = ((Number) oA).doubleValue();
                Double dB = ((Number) oB).doubleValue();
                io.out(3, dA / dB);
            }
        }
    }

}
