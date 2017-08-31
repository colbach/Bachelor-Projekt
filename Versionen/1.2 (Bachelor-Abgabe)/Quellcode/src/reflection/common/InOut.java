package reflection.common;

import reflection.customdatatypes.math.MathObject;

public interface InOut extends TerminatedTestable {

    public Object[] in(int i) throws TerminatedException;

    public Object in0(int i) throws TerminatedException;

    public Object[] in(int i, Object[] def) throws TerminatedException;

    public Object in0(int i, Object def) throws TerminatedException;
    
    public Number inN(int i, Number def) throws TerminatedException;
    
    public MathObject inM(int i, MathObject def) throws TerminatedException;
    
    public Boolean inB(int i, Boolean def) throws TerminatedException;

    public void out(int i, Object ausgabe) throws TerminatedException;

    public void out(int i, Object[] ausgabe) throws TerminatedException;

    /**
     * Pruefft ob out ueberhaupt verbunden ist. Ist out nicht verbunden muss
     * kein Ergebnis an diesen out geliefert werden.
     */
    public boolean outConnected(int i) throws TerminatedException;

    public long getContextIdentifier() throws TerminatedException;

}
