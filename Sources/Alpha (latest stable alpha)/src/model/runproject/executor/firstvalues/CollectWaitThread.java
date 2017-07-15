package model.runproject.executor.firstvalues;

import model.Inlet;
import model.runproject.inputdata.CollectRequest;
import model.runproject.inputdata.InletInputData;
import model.runproject.inputdata.InletInputDataSource;
import utils.structures.Tuple;

public class CollectWaitThread extends Thread {
    
    @SafeVarargs
    public static <T> T[] toGenericArray(T ... elems) {
        return elems;
    }
    
    private final CollectRequest collectRequest;
    private final CollectWaitCallback collectWaitCallback;
    private final InletInputDataSource inletInputDataSource;
    private final boolean pushInlets;

    public CollectWaitThread(CollectRequest collectRequest, CollectWaitCallback collectWaitCallback, InletInputDataSource inletInputDataSource, boolean push) {
        this.collectRequest = collectRequest;
        this.collectWaitCallback = collectWaitCallback;
        this.inletInputDataSource = inletInputDataSource;
        this.pushInlets = push;
    }

    public CollectWaitThread(Tuple<Inlet, Integer>[] connectedInlets, CollectWaitCallback collectWaitCallback, InletInputDataSource inletInputDataSource, boolean push) {
        this(new CollectRequest(connectedInlets), collectWaitCallback, inletInputDataSource, push);
    }

    public CollectWaitThread(Tuple<Inlet, Integer> connectedInlet, CollectWaitCallback collectWaitCallback, InletInputDataSource inletInputDataSource, boolean push) {
        this(toGenericArray(connectedInlet), collectWaitCallback, inletInputDataSource, push);
    }
    
    @Override
    public void run() {
        InletInputData collected = inletInputDataSource.collect(collectRequest, pushInlets);
        collectWaitCallback.takeCollectedInletInputData(collected);
    }
}
