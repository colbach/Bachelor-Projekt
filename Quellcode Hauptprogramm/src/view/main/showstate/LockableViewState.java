package view.main.showstate;

public interface LockableViewState extends ShowState {

    public void lockBack();

    public void unlockBack();
    
    public void unlockAndGoBack();
}
