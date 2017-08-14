package model.resourceloading.projectserialization.pools;

import java.util.HashMap;
import model.InOutlet;
import model.Inlet;
import model.Node;
import model.Outlet;

public class LetPool {

    private HashMap<Long, InOutlet> lets;

    public LetPool() {
        this.lets = new HashMap<>();

    }

    public Inlet getOrCreateInlet(Long id) throws WrongLetKindException {

        InOutlet got = lets.get(id);
        if (got == null) { // erzeuge Node...
            Inlet newLet = new Inlet(id);
            lets.put(id, newLet);
            return newLet;

        } else { // Zurueck geben...
            if (got instanceof Inlet) {
                return (Inlet) got;
            } else {
                System.err.println("Let " + id + " ist kein Inlet! (" + got.getClass() + ")");
                throw new WrongLetKindException();
            }
        }
    }

    public Outlet getOrCreateOutlet(Long id) throws WrongLetKindException {

        InOutlet got = lets.get(id);
        if (got == null) { // erzeuge Node...
            Outlet newLet = new Outlet(id);
            lets.put(id, newLet);
            return newLet;

        } else { // Zurueck geben...
            if (got instanceof Outlet) {
                return (Outlet) got;
            } else {
                System.err.println("Let " + id + " ist kein Outlet! (" + got.getClass() + ")");
                throw new WrongLetKindException();
            }
        }
    }

}
