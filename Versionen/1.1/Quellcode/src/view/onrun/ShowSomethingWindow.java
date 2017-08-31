package view.onrun;

import javax.swing.JFrame;

public abstract class ShowSomethingWindow extends JFrame {
    
    public abstract boolean canShow(Object o);
    
    public abstract void show(Object o) throws UnsupportedShowTypeException;
    
    public abstract boolean isClosed();
    
    public abstract void releaseMemory();
    
}
