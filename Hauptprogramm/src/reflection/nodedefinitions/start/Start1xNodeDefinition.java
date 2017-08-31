package reflection.nodedefinitions.start;

import reflection.common.API;
import reflection.common.ContextCreator;
import reflection.common.ContextCreatorInOut;
import reflection.common.InOut;
import reflection.common.NodeDefinition;

public class Start1xNodeDefinition implements ContextCreator {

    @Override
    public int getInletCount() {
        return 0;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
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
        return "Einfacher Start";
    }

    @Override
    public String getDescription() {
        return "Startet genau einen Kontext." + TAG_PREAMBLE + " [Events] [Basics] 1x Kontext Context Start Run Execute";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Start1x";
    }

    @Override
    public String getIconName() {
        return "Start-1x_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut uio, API api) throws Exception {
        ContextCreatorInOut io = (ContextCreatorInOut) uio;
        io.terminatedTest();
        io.startNewContext();
    }

}
