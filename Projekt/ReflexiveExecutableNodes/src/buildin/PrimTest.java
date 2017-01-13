package buildin;

import reflection.NodeDefinition;

public class PrimTest implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Long.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "n";
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
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Ist Primzahl";
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
        return "Primzahltest";
    }

    @Override
    public String getDescription() {
        return "Testet ob es sich bei einer Zahl n um eine Primzahl handelt. Dies kann bei grossen Zahlen eine längere Zeit in Anspruch nehmen." + TAG_PREAMBLE + " Prim Primzahl Test Probieren ";
    }

    @Override
    public String getUniqueName() {
        return "buildin.PrimTest";
    }

    @Override
    public String getIconName() {
        return "Is-Prim_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io) {

        Long n = (Long) io.in0(0, 1);

        io.out(0, isPrim(n));
    }

    public static boolean isPrim(long n) {
        if (n < 0) {
            System.err.println("Primzahlen sind immer positiv");
            return false;
        }
        if (n <= 2) {
            return true;
        }
        for (long i = 2; i <= n / 2; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

}
