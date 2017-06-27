package model.runproject.debugger;

import java.util.ArrayList;

public class ContextTable {
        
    private final ArrayList<String> descriptions;
    private final ArrayList<Long> identifiers;
    private final ArrayList<Boolean> creators;
    private final ArrayList<ContextState> states;
    private int size;

    public ContextTable() {
        this.descriptions = new ArrayList<>();
        this.identifiers = new ArrayList<>();
        this.creators = new ArrayList<>();
        this.states = new ArrayList<>();
    }
    
    public ContextTable(int supposedSize) {
        this.descriptions = new ArrayList<>(supposedSize);
        this.identifiers = new ArrayList<>(supposedSize);
        this.creators = new ArrayList<>(supposedSize);
        this.states = new ArrayList<>(supposedSize);
    }
    
    protected void add(String description, Long identifier, boolean creator, ContextState state) {
        identifiers.add(identifier);
        descriptions.add(description);
        creators.add(creator);
        states.add(state);
        size++;
    }

    public int size() {
        return size;
    }

    public String getDescription(int index) {
        return descriptions.get(index);
    }

    public Long getIdentifier(int index) {
        return identifiers.get(index);
    }
    
    public boolean isCreator(int index) {
        return creators.get(index);
    }
    
}
