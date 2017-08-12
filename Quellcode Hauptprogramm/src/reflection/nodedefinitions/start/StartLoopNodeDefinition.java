package reflection.nodedefinitions.start;

import reflection.API;
import reflection.ContextCreator;
import reflection.ContextCreatorInOut;
import reflection.InOut;
import reflection.NodeDefinition;

public class StartLoopNodeDefinition implements NodeDefinition, ContextCreator {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Long.class;
            case 1:
                return Long.class;
            case 2:
                return Long.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Start i";
            case 1:
                return "Solange i<";
            case 2:
                return "Check I. in ms.";
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
    public boolean isInletEngaged(int i) {
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
                return Long.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "i";
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
        return "Wiederholen";
    }

    @Override
    public String getDescription() {
        return "Startet wiederhollt Kontexte bis i eine bestimmte Grösse erreicht hat. der Jeweils nächste Kontext wird erst gestartet wenn der jeweils vorhergehende terminiert ist." + TAG_PREAMBLE + " [Events] [Basics] repeat Loop immer while(true) Kontext Context Start Run Execute";
    }

    @Override
    public String getUniqueName() {
        return "buildin.StartLoop";
    }

    @Override
    public String getIconName() {
        return "Start-Loop_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut uio, API api) throws Exception {
        ContextCreatorInOut io = (ContextCreatorInOut) uio;

        long i = (Long) io.in0(0, 0);
        long maxi = (Long) io.in0(1, Long.MAX_VALUE);
        long interval = (Long) io.in0(2, 100L);

        while (maxi > i) {

            io.terminatedTest();
            io.out(0, i);
            io.startNewContext();
            api.additionalPrintOut("Neuer Kontext erzeugen (" + api.getTimeStamp() + ")");
            i++;

            while (io.getRunningContextCount() > 0) {
                io.terminatedTest();                                                        // Code optimieren ohne aktives warten
                try {                                                                       //
                    Thread.sleep(interval);                                                 //
                } catch (InterruptedException interruptedException) {                       //
                    api.additionalPrintErr("InterruptedException in StartLoop empfangen."); //
                }                                                                           //
            }                                                                               //

        }
    }

}
