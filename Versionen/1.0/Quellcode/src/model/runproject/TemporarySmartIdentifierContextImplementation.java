package model.runproject;

import model.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import reflection.customdatatypes.SmartIdentifier;
import reflection.SmartIdentifierContext;

public class TemporarySmartIdentifierContextImplementation implements SmartIdentifierContext {

    private final HashMap<String, SmartIdentifier> smartIdentifiers = new HashMap<String, SmartIdentifier>();
    private final SmartIdentifierContext parent;

    public TemporarySmartIdentifierContextImplementation(SmartIdentifierContext parent) {
        this.parent = parent;
    }

    public synchronized void put(SmartIdentifier smartIdentifier) {
        if (!parent.doesSmartIdentifierExist(smartIdentifier.getIdentifierName())) {
            smartIdentifiers.put(smartIdentifier.getIdentifierName(), smartIdentifier);
        }
    }

    public synchronized SmartIdentifier getOrCreate(String identifier) {
        if (identifier.startsWith("# ")) {
            identifier = identifier.substring(2);
        }
        if(parent.doesSmartIdentifierExist(identifier)) {
            return parent.getOrCreate(identifier);
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
        HashSet hashSet = new HashSet();
        hashSet.add(smartIdentifiers.values());
        hashSet.add(parent.getSmartIdentifiers());
        return hashSet;
    }

    public synchronized boolean doesSmartIdentifierExist(String identifier) {
        if (identifier.startsWith("# ")) {
            identifier = identifier.substring(2);
        }
        return smartIdentifiers.containsKey(identifier) || parent.doesSmartIdentifierExist(identifier);
    }
    
    private long counter = 0; 

    @Override
    public synchronized SmartIdentifier createNew() {
        String identifiername;
        final String PREFIX = "New Identfier ";
        do {
            identifiername = PREFIX + ++counter;
        } while (doesSmartIdentifierExist(identifiername));
        SmartIdentifier smartIdentifier = getOrCreate(identifiername);
        return smartIdentifier;
    }
}
