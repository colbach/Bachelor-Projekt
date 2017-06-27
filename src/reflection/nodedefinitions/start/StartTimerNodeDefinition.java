package reflection.nodedefinitions.start;

import reflection.API;
import reflection.ContextCreator;
import reflection.ContextCreatorInOut;
import reflection.InOut;
import reflection.NodeDefinition;

public class StartTimerNodeDefinition implements NodeDefinition, ContextCreator {

    @Override
    public int getInletCount() {
        return 4;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Long.class;
            case 1:
                return Integer.class;
            case 2:
                return Boolean.class;
            case 3:
                return Long.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Zeit in ms";
            case 1:
                return "Zeit in s";
            case 2:
                return "Wiederholen";
            case 3:
                return "Maximum Wiederh.";
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
            default:
                return false;
        }
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
        return "Timer";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Events] [Zeit] Ausführen Timer Wiederholen Repeate Repeating Kontext Context Start Run Execute";
    }

    @Override
    public String getUniqueName() {
        return "buildin.StartTimer";
    }

    @Override
    public String getIconName() {
        return "Start-Timer_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public synchronized void run(InOut uio, API api) throws Exception {
        ContextCreatorInOut io = (ContextCreatorInOut) uio;

        Long zeitInMS = (Long) io.in0(0, 0L);
        {
            Integer zeitInS = (Integer) io.in0(1, 0);
            Object data = io.in0(2, new Object[0]);
            if (zeitInS != null) {
                zeitInMS += zeitInS * 1000;
            }
        }
        Boolean repeat = (Boolean) io.in0(2, true);
        io.terminatedTest();

        Long wiederh = (Long) io.in0(3, Long.MAX_VALUE);

        long starttime = System.currentTimeMillis();
        long counter = 0;

        do {
            io.terminatedTest();

            wiederh--;
            if (wiederh == 0) {
                return;
            }

            long timestamp = starttime + counter++ * zeitInMS;
            if (System.currentTimeMillis() > timestamp + zeitInMS) {
                api.additionalPrintErr("Beeilung!... (counter=" + counter + ", " + (System.currentTimeMillis() - (timestamp + zeitInMS)) + "ms Verspaetung)");
            }

            while (System.currentTimeMillis() < timestamp + zeitInMS) {
                long remainingTime = (timestamp + zeitInMS) - System.currentTimeMillis();
                if (remainingTime > 0) {
                    io.terminatedTest();
                    try {
                        wait(remainingTime);
                    } catch (InterruptedException ex) {
                    }
                }
            }
            io.terminatedTest();

            api.additionalPrintOut("Neuer Kontext erzeugen (" + api.getTimeStamp() + ")");

            io.startNewContext();

            io.terminatedTest();

        } while (repeat);

    }

}
