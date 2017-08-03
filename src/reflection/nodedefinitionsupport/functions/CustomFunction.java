package reflection.nodedefinitionsupport.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import reflection.Function;
import reflection.FunctionNotAdoptableException;
import reflection.SmartIdentifier;
import reflection.TerminatedTestable;
import utils.structures.Tuple;

public class CustomFunction extends Function {

    private final CustomFunctionManager manager;
    private final SmartIdentifier smartIdentifier;
    private final TerminatedTestable terminatedTestable;

    private final ArrayList<Tuple<FunctionCallToken, Object[]>> newTokens;

    public CustomFunction(CustomFunctionManager manager, SmartIdentifier smartIdentifier, TerminatedTestable terminatedTestable) {
        this.manager = manager;
        this.smartIdentifier = smartIdentifier;
        this.newTokens = new ArrayList<>();
        this.terminatedTestable = terminatedTestable;
    }

    @Override
    public Object[] evaluate(Object[] parameter) throws FunctionNotAdoptableException {
        FunctionCallToken token = new FunctionCallToken();
        synchronized (newTokens) {
            newTokens.add(new Tuple<>(token, parameter));
            newTokens.notifyAll();
        }
        return token.collectResult(terminatedTestable);
    }

    public Tuple<FunctionCallToken, Object[]> start() {
        Tuple<FunctionCallToken, Object[]> token;
        synchronized (newTokens) {
            while (newTokens.isEmpty()) {
                try {
                    newTokens.wait();
                } catch (InterruptedException ex) {
                }
            }
            token = newTokens.remove(0);
        }
        return token;
    }
}
