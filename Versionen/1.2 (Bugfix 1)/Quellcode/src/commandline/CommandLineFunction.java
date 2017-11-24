package commandline;

public interface CommandLineFunction {
    
    public void execute(String[] param, CommandLine commandLine);
    
    public String getDescription();
    
    public String getLongDescription();
    
    public String getUsage();
    
    public String getName();
    
    public String getAliases();
}
