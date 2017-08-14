package reflection;

public interface TerminatedTestable {

    public boolean isTerminated();

    /**
     * Diese Methode soll in regelmaessig in run-Methode aufgerufen werden falls
     * eine laengere Arbeit verrichtet wird. Die Methode loesst im Falle dass
     * das die Ausfuerung abgebrochen wurde eine TerminatedException aus. Diese
     * soll NICHT abgefangen werden!
     */
    public void terminatedTest() throws TerminatedException;
    
}
