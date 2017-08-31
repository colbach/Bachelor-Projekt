package reflection.nodedefinitionsupport.functions;

import java.util.logging.Level;
import java.util.logging.Logger;
import reflection.common.TerminatedTestable;

public class FunctionCallToken {

    private Object[] result = null;

    public synchronized Object[] collectResult(TerminatedTestable testable) {
        while (result == null) {
            testable.terminatedTest();
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        return result;
    }

    public synchronized void deliever(Object[] result) {
        this.result = result;
        notifyAll();
    }
    
    public synchronized void finish() {
        if(result == null) result = new Object[0];
        notifyAll();
    }
    
}
