package reflection;

import java.util.Set;

public interface SmartIdentifierContext {
    
    public void put(SmartIdentifier smartIdentifier);
    
    public SmartIdentifier getOrCreate(String identifier);
    
    public Set<SmartIdentifier> getSmartIdentifiers();
    
    public boolean doesSmartIdentifierExist(String identifier);
    
    public SmartIdentifier createNew();
    
}
