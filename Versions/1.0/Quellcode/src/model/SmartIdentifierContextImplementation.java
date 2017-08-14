package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import reflection.customdatatypes.SmartIdentifier;
import reflection.SmartIdentifierContext;

public class SmartIdentifierContextImplementation implements SmartIdentifierContext {

    private final HashMap<String, SmartIdentifier> smartIdentifiers = new HashMap<String, SmartIdentifier>();

    public synchronized void put(SmartIdentifier smartIdentifier) {
        smartIdentifiers.put(smartIdentifier.getIdentifierName(), smartIdentifier);
    }

    public synchronized SmartIdentifier getOrCreate(String identifier) {
        if (identifier.startsWith("# ")) {
            identifier = identifier.substring(2);
        }
        SmartIdentifier get = smartIdentifiers.get(identifier);
        if (get != null) {
            return get;
        } else {
            SmartIdentifier newSmartIdentifier = new SmartIdentifier(identifier);
            smartIdentifiers.put(identifier, newSmartIdentifier);
            return newSmartIdentifier;
        }
    }

    public synchronized Set<SmartIdentifier> getSmartIdentifiers() {
        return new HashSet(smartIdentifiers.values());
    }

    public synchronized boolean doesSmartIdentifierExist(String identifier) {
        if (identifier.startsWith("# ")) {
            identifier = identifier.substring(2);
        }
        return smartIdentifiers.containsKey(identifier);
    }
    
    private long counter = 0; 

    @Override
    public synchronized SmartIdentifier createNew() {
        String identifiername;
        final String PREFIX = "New temporary Identfier ";
        do {
            identifiername = PREFIX + ++counter;
        } while (doesSmartIdentifierExist(identifiername));
        SmartIdentifier smartIdentifier = getOrCreate(identifiername);
        return smartIdentifier;
    }
}
