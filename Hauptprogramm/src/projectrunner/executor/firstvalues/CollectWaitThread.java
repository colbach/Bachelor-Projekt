package projectrunner.executor.firstvalues;

import model.Inlet;
import projectrunner.inputdata.CollectRequest;
import projectrunner.inputdata.InletInputData;
import projectrunner.inputdata.InletInputDataSource;
import utils.structures.NotifyValue;
import utils.structures.tuples.Pair;

public class CollectWaitThread extends Thread {

    @SafeVarargs
    public static <T> T[] toGenericArray(T... elems) {
        return elems;
    }

    private final CollectRequest collectRequest;
    private final CollectWaitCallback collectWaitCallback;
    private final InletInputDataSource inletInputDataSource;
    private final boolean pushInlets;
    private final NotifyValue<Boolean> cancelator;

    public CollectWaitThread(CollectRequest collectRequest, CollectWaitCallback collectWaitCallback, InletInputDataSource inletInputDataSource, boolean push, NotifyValue<Boolean> cancelator) {
        this.collectRequest = collectRequest;
        this.collectWaitCallback = collectWaitCallback;
        this.inletInputDataSource = inletInputDataSource;
        this.pushInlets = push;
        this.cancelator = cancelator;
    }

    public CollectWaitThread(Pair<Inlet, Integer>[] connectedInlets, CollectWaitCallback collectWaitCallback, InletInputDataSource inletInputDataSource, boolean push, NotifyValue<Boolean> cancelator) {
        this(new CollectRequest(connectedInlets), collectWaitCallback, inletInputDataSource, push, cancelator);
    }

    public CollectWaitThread(Pair<Inlet, Integer> connectedInlet, CollectWaitCallback collectWaitCallback, InletInputDataSource inletInputDataSource, boolean push, NotifyValue<Boolean> cancelator) {
        this(toGenericArray(connectedInlet), collectWaitCallback, inletInputDataSource, push, cancelator);
    }

    @Override
    public void run() {
        InletInputData collected = inletInputDataSource.collect(collectRequest, pushInlets, cancelator);
        if (collected != null) {
            cancelator.set(true);
            collectWaitCallback.takeCollectedInletInputData(collected);
        }
    }
}
