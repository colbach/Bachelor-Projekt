package reflection.nodedefinitions.time;

import java.util.logging.Level;
import java.util.logging.Logger;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class SleepNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Long.class;
            case 1:
                return Integer.class;
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
        return false;
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
            case 0:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Schlafen";
    }

    @Override
    public String getDescription() {
        return "Dieses Element schläft eine angegebene Zeit und gibt dann beliebiges Objekt weiter.\nAlternativ kann auch der Auslöser verwendet werden um ein weiteres Element anzustossen. Werden Zeiten in verschiedenen Einheiten angegeben so werden diese addiert." + TAG_PREAMBLE + " [Zeit] [Basics] Schlafen sleep wait timer Zeit";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Sleep";
    }

    @Override
    public String getIconName() {
        return "Sand-watch_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public synchronized void run(InOut io, API api) {

        long timestamp = System.currentTimeMillis();

        Long zeitInMS = (Long) io.in0(0, 0L);
        Integer zeitInS = (Integer) io.in0(1, 0);

        if (zeitInS != null) {
            zeitInMS += zeitInS * 1000;
        }
        io.terminatedTest();

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
    }

}
