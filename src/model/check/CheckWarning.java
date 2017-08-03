package model.check;

public interface CheckWarning {
    
    public String getMessage();
    
    public String getUserMessage();
    
    public boolean projectIsStillRunnable();
    
}