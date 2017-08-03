package model.runproject.executor;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import model.*;
import model.runproject.executioncontext.ExecutionContext;
import model.runproject.executioncontrol.ExecutionControl;
import model.runproject.inputdata.InletInputData;
import model.runproject.outputdata.OutletOutputData;
import reflection.*;
import reflection.NodeDefinition.*;
import utils.images.ImageLoading;
import utils.images.ImageSaving;
import view.onrun.*;

public class InOutImplementation implements InOut {
    
    private final InletInputData inletInputData;
    private final OutletOutputData outletOutputData;
    private final Node node;
    private final ExecutionControl executionControl;
    private final long contextIdentifier;

    public InOutImplementation(InletInputData inletInputData, Node node, ExecutionControl executionControl, long contextIdentifier) {
        this.inletInputData = inletInputData;
        this.outletOutputData = new OutletOutputData();
        this.node = node;
        this.executionControl = executionControl;
        this.contextIdentifier = contextIdentifier;
    }

    private boolean stillActive() {
        return executionControl.isRunning();
    }

    @Override
    public Object[] in(int i) {
        if (stillActive()) {
            Inlet inlet = node.getInlet(i);
            return inletInputData.get(inlet);
        } else {
            throw new TerminatedException();
        }
    }

    @Override
    public Object in0(int i) {
        if (stillActive()) {
            Object[] data = in(i);
            if (data == null) {
                return null;
            }
            if (data.length > 0) {
                return in(i)[0];
            } else {
                return null;
            }
        } else {
            throw new TerminatedException();
        }
    }

    @Override
    public void out(int i, Object output) {
        if (stillActive()) {
            out(i, new Object[]{output});
        } else {
            throw new TerminatedException();
        }
    }

    @Override
    public void out(int i, Object[] data) {
        if (stillActive()) {
            Outlet outlet = node.getOutlet(i);
            outletOutputData.put(outlet, data);
        } else {
            throw new TerminatedException();
        }
    }

    @Override
    public boolean isTerminated() {
        return executionControl.isCanceled();
    }

    @Override
    public void terminatedTest() throws TerminatedException {
        if (!stillActive()) {
            throw new TerminatedException();
        }
    }

    @Override
    public boolean outConnected(int i) {
        if (stillActive()) {
            return node.getOutlet(i).isConnected();
        } else {
            throw new TerminatedException();
        }
    }

    @Override
    public Object[] in(int i, Object[] def) {
        if (stillActive()) {
            Object[] os = in(i);
            if (os == null) {
                return def;
            } else {
                return os;
            }
        } else {
            throw new TerminatedException();
        }
    }

    @Override
    public Object in0(int i, Object def) {
        if (stillActive()) {
            Object o = in0(i);
            if (o == null) {
                return def;
            } else {
                return o;
            }
        } else {
            throw new TerminatedException();
        }

    }

    public OutletOutputData getOutletOutputData() {
        return outletOutputData;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public String toString() {
        return "InOutImplementation (" + node.getName() + ")";
    }

    @Override
    public long getContextIdentifier() throws TerminatedException {
        return contextIdentifier;
    }
}
