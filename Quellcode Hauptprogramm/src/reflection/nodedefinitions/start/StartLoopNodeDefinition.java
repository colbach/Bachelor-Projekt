package reflection.nodedefinitions.start;

import reflection.API;
import reflection.ContextCreator;
import reflection.ContextCreatorInOut;
import reflection.InOut;
import reflection.NodeDefinition;

public class StartLoopNodeDefinition implements NodeDefinition, ContextCreator {

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
                return Long.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Maximum Wiederh.";
            case 1:
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
        return "Wiederholen";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Events] [Basics] repeat Loop immer while(true) Kontext Context Start Run Execute";
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

        long wiederh = (Long) io.in0(0, Long.MAX_VALUE);
        long interval = (Long) io.in0(1, 200L);

        while (wiederh > 0) {

            io.terminatedTest();
            io.startNewContext();
            api.additionalPrintOut("Neuer Kontext erzeugen (" + api.getTimeStamp() + ")");
            wiederh--;

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