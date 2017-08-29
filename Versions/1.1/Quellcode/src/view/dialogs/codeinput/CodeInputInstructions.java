package view.dialogs.codeinput;

public interface CodeInputInstructions {
    
    public String getTemplate();
    
    public boolean isTestable();
        
    public CodeInputTestResult performTest(Object testInstance);
    
    public String getTitle();
    
}
