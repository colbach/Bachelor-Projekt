package model.runproject.executor.firstvalues;

import model.runproject.inputdata.InletInputData;

public interface CollectWaitCallback {
    public void takeCollectedInletInputData(InletInputData inletInputData);
}
