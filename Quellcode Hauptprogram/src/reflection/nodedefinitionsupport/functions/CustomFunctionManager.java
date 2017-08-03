package reflection.nodedefinitionsupport.functions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import reflection.customdatatypes.SmartIdentifier;
import reflection.TerminatedTestable;

public class CustomFunctionManager {
    
    public static final String SHARED_OBJECT_KEY = "buildin.CustomFunctionManager";

    private final HashMap<SmartIdentifier, CustomFunction> functions;

    public CustomFunctionManager() {
        this.functions = new HashMap<>();
    }

    public synchronized CustomFunction getAnyCustomFunction() {
        for (CustomFunction function : functions.values()) {
            return function;
        }
        return null;
    }

    public synchronized CustomFunction getCustomFunctionBlocking(SmartIdentifier id, TerminatedTestable testable) {
        if (id == null) {
            return null;
        } else {
            while(!functions.containsKey(id)) {
                testable.terminatedTest();
                try {
                    wait();
                } catch (InterruptedException ex) {
                }
            }
            return functions.get(id);
        }
    }

    public synchronized CustomFunction getCustomFunction(SmartIdentifier id) {
        if (id == null) {
            return null;
        } else {
            return functions.get(id);
        }
    }

    public synchronized CustomFunction createOrGetCustomFunction(SmartIdentifier id, TerminatedTestable testable) {
        if (functions.containsKey(id)) {
            return functions.get(id);
        } else {
            CustomFunction newCustomFunction = new CustomFunction(this, id, testable);
            functions.put(id, newCustomFunction);
            notifyAll();
            return newCustomFunction;
        }
    }

    public synchronized void remove(SmartIdentifier id) {
        functions.remove(id);
    }

}
