package projectrunner.outputdata;

import model.Node;

public interface OutletOutputDataDestination {

    public void deliver(Node node, OutletOutputData outletOutputData);
}
