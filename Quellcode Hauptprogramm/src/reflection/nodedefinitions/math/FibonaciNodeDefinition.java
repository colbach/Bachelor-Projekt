package reflection.nodedefinitions.math;

import java.util.ArrayList;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class FibonaciNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Integer.class;
            case 1:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "n";
            case 1:
                return "rekursiv";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
        return false;
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
    public int getOutletCount() {
        return 2;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Long.class;
            case 1:
                return Long.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "fibo(n)";
            case 1:
                return "Folge bis n";
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
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Fibonaci";
    }

    @Override
    public String getDescription() {
        return "Berechnet die Fibonacci-Folge bis zum Wert n." + TAG_PREAMBLE + " [Math] fib()";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Fibonaci";
    }

    @Override
    public String getIconName() {
        return "Fibonacci_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Integer n = (Integer) io.in0(0, 0);
        Boolean rekursiv = (Boolean) io.in0(1, false);

        if (rekursiv) { // (sehr unefizient)

            if (io.outConnected(0)) {
                Long f = recursiv(n, io);
                io.out(0, f);

            }

            if (io.outConnected(1)) {
                ArrayList<Long> folge = new ArrayList<>();
                for (int i = 0; i <= n; i++) {
                    if (i % 1000 == 0) {
                        io.terminatedTest();
                    }
                    folge.add(recursiv(n, io));
                }
                io.out(1, folge.toArray());
            }

        } else {

            if (io.outConnected(0)) {
                long f = 0, e = 1;
                for (int i = 0; i < n; i++) {
                    if (i % 1000 == 0) {
                        io.terminatedTest();
                    }
                    e = f + (f = e);
                }
                io.out(0, f);
            }

            if (io.outConnected(1)) {
                ArrayList<Long> folge = new ArrayList<>();
                long f = 0, e = 1;
                for (int i = 0; i < n; i++) {
                    if (i % 1000 == 0) {
                        io.terminatedTest();
                    }
                    e = f + (f = e);
                    folge.add(e);
                }
                io.out(1, f);
            }

        }

    }

    public static long recursiv(int n, InOut io) {
        if (n % 1000 == 0) {
            io.terminatedTest();
        }
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else {
            return recursiv(n - 1, io) + recursiv(n - 2, io);
        }
    }
}
