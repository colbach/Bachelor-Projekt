package projectrunner.inputdata;

import java.util.*;
import model.*;
import utils.structures.*;

public interface InletInputDataSource {

    /**
     * Diese Methode sammelt Input-Daten fuer Inlets. Diese Methode blockiert
     * solange bis Request erfuellt werden kann. Wenn push gesetzt ist werden
     * alle Inlets (wenn dies nicht bereits geschehen ist) angestossen.
     */
    public InletInputData collect(CollectRequest collectRequest, boolean push);

    /**
     * Diese Methode sammelt Input-Daten fuer Inlets. Diese Methode blockiert
     * solange bis Request erfuellt werden kann. Wenn push gesetzt ist werden
     * alle Inlets (wenn dies nicht bereits geschehen ist) angestossen.
     * cancelator muss false sein. Wenn cancelator im nachhinein auf true
     * gesetzt wird, wird collect abgebrochen und null zuruck gegeben.
     */
    public InletInputData collect(CollectRequest collectRequest, boolean push, NotifyValue<Boolean> cancelator);

    /**
     * Diese Methode dient zum Anstossen von Inlets ohne an dessen Resultat
     * interessiert zu sein.
     */
    public void push(CollectRequest collectRequest);

    /**
     * Stoesst Node an.
     */
    public void submitExecutorIfNotSubmited(Node node);
}
