package projectrunner.debugger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import logging.AdditionalLogger;
import model.Node;
import projectrunner.executionlogging.ExecutionLogger;

public class DebuggerMapForContexts {

    private final ExecutionLogger executionLogger;

    public final static Long PARENT_IDENTIFIER_FOR_CONTEXT_CREATOR = -1L;

    private final HashMap<Long, String> contextIdentifierToDescription;
    private final HashMap<Long, Boolean> contextIdentifierToIsActive;
    private final HashMap<Long, Long> contextIdentifierToParentIdentifier;
    private final HashMap<Long, Integer> contextCreatorIdentifiersToChildContextCount;
    private final HashMap<Long, Integer> contextCreatorIdentifiersToTerminatedChildContextCount;

    private ContextTable contextTable; // muss bei jeder Aenderung auf null zuruek gesetzt werden!

    public DebuggerMapForContexts(ExecutionLogger executionLogger) {
        this.executionLogger = executionLogger;
        this.contextIdentifierToDescription = new HashMap<>();
        this.contextIdentifierToIsActive = new HashMap<>();
        this.contextIdentifierToParentIdentifier = new HashMap<>();
        this.contextCreatorIdentifiersToChildContextCount = new HashMap<Long, Integer>();
        this.contextCreatorIdentifiersToTerminatedChildContextCount = new HashMap<Long, Integer>();
        this.contextTable = null;
    }
    
    public boolean containsContextIdentifier(long contextIdentifier) {
        return contextIdentifierToDescription.containsKey(contextIdentifier);
    }

    public synchronized void registerContextIdentifier(Long identifier, String description, Long parentIdentifier) {
        contextIdentifierToDescription.put(identifier, description);
        contextIdentifierToIsActive.put(identifier, Boolean.TRUE);
        contextIdentifierToParentIdentifier.put(identifier, parentIdentifier);
        if (Objects.equals(parentIdentifier, PARENT_IDENTIFIER_FOR_CONTEXT_CREATOR)) {
            if (!contextCreatorIdentifiersToChildContextCount.containsKey(identifier)) {
                contextCreatorIdentifiersToChildContextCount.put(identifier, 0);
                contextCreatorIdentifiersToTerminatedChildContextCount.put(identifier, 0);
            } else {
                System.err.println("identifier ist bereits in contextCreatorIdentifiersToChildContextCount vorhanden");
            }
        } else {
            if (contextCreatorIdentifiersToChildContextCount.containsKey(parentIdentifier)) {
                Integer count = contextCreatorIdentifiersToChildContextCount.get(parentIdentifier);
                if (count != null) {
                    count++;
                    contextCreatorIdentifiersToChildContextCount.put(parentIdentifier, count);
                } else {
                    AdditionalLogger.err.println("parentIdentifier nicht in contextCreatorIdentifiersToChildContextCount gefunden");
                }
            } else {
                AdditionalLogger.err.println("parentIdentifier nicht gefunden");
            }
        }
        contextTable = null;
    }

    public synchronized HashMap<Long, Integer> getContextCreatorIdentifiersToChildContextCount() {
        return (HashMap<Long, Integer>) contextCreatorIdentifiersToChildContextCount.clone();
    }

    public synchronized HashMap<String, Integer> getContextCreatorDescriptionsToChildContextCount() {
        HashMap<String, Integer> descriptionsToCounts = new HashMap<>(contextCreatorIdentifiersToChildContextCount.size());
        for (Map.Entry<Long, Integer> entry : contextCreatorIdentifiersToChildContextCount.entrySet()) {
            Long key = entry.getKey();
            Integer value = entry.getValue();
            String description = getDescription(key);
            descriptionsToCounts.put(description, value);
        }
        return descriptionsToCounts;
    }

    public synchronized HashMap<Long, Integer> getContextCreatorIdentifiersToTerminatedChildContextCount() {
        return (HashMap<Long, Integer>) contextCreatorIdentifiersToTerminatedChildContextCount.clone();
    }

    public synchronized HashMap<String, Integer> getContextCreatorDescriptionsToTerminatedChildContextCount() {
        HashMap<String, Integer> descriptionsToCounts = new HashMap<>(contextCreatorIdentifiersToTerminatedChildContextCount.size());
        for (Map.Entry<Long, Integer> entry : contextCreatorIdentifiersToTerminatedChildContextCount.entrySet()) {
            Long key = entry.getKey();
            Integer value = entry.getValue();
            String description = getDescription(key);
            descriptionsToCounts.put(description, value);
        }
        return descriptionsToCounts;
    }

    public synchronized boolean isCreatorContext(Long identifier) {
        Long parrentIdentifier = contextIdentifierToParentIdentifier.get(identifier);
        if (parrentIdentifier != null) {
            return parrentIdentifier.equals(PARENT_IDENTIFIER_FOR_CONTEXT_CREATOR);
        } else {
            System.err.println("identifier " + identifier + " nicht gefunden! return false");
            return false;
        }
    }

    public synchronized Long getParentIdentifier(Long identifier) {
        Long parrentIdentifier = contextIdentifierToParentIdentifier.get(identifier);
        if (parrentIdentifier != null) {
            return parrentIdentifier;
        } else {
            System.err.println("identifier " + identifier + " nicht gefunden! return -2L");
            return -2L;
        }
    }

    public synchronized String getDescription(Long identifier) {
        String description = contextIdentifierToDescription.get(identifier);
        if (description != null) {
            return description;
        } else {
            System.err.println("identifier " + identifier + " nicht gefunden! return \"\"");
            return "";
        }
    }

    public synchronized void setContextTerminated(Long identifier) {
        contextIdentifierToIsActive.put(identifier, Boolean.FALSE);
        Long parentIdentifier = contextIdentifierToParentIdentifier.get(identifier);
        if (parentIdentifier != null) {
            if (parentIdentifier != PARENT_IDENTIFIER_FOR_CONTEXT_CREATOR) {
                Integer count = contextCreatorIdentifiersToTerminatedChildContextCount.get(parentIdentifier);
                if (count != null) {
                    count++;
                    contextCreatorIdentifiersToTerminatedChildContextCount.put(parentIdentifier, count);
                } else {
                    AdditionalLogger.err.println("parentIdentifier nicht in contextCreatorIdentifiersToTerminatedChildContextCount gefunden");
                }
            }
        } else {
            System.err.println("identifier nicht in contextIdentifierToParentIdentifier gefunden");
        }
        contextTable = null;
    }

    public synchronized ContextState getContextState(Long identifier) {
        Long parrentIdentifier = contextIdentifierToParentIdentifier.get(identifier);
        Boolean active = contextIdentifierToIsActive.get(identifier);
        if (parrentIdentifier != null && active != null) {

            if (parrentIdentifier.equals(PARENT_IDENTIFIER_FOR_CONTEXT_CREATOR)) {

                if (active) {
                    return ContextState.RUNNING;
                } else {

                    return ContextState.FINISHED;
                }

            } else {
                if (active) {

                    for (Map.Entry<Long, Long> entry : contextIdentifierToParentIdentifier.entrySet()) {
                        Long otherIdentifier = entry.getKey();
                        Long otherParrentIdentifier = entry.getValue();
                        if (otherParrentIdentifier.equals(identifier)) {
                            Boolean otherActive = contextIdentifierToIsActive.get(otherIdentifier);
                            if (otherActive != null) {
                                if (otherActive) {
                                    return ContextState.RUNNING_WITH_RUNNING_CLILDREN;
                                }
                            } else {
                                System.err.println("otherIdentifier " + otherIdentifier + " nicht gefunden!");
                            }
                        }
                    }
                    return ContextState.RUNNING_WITHOUT_RUNNING_CLILDREN;

                } else {

                    for (Map.Entry<Long, Long> entry : contextIdentifierToParentIdentifier.entrySet()) {
                        Long otherIdentifier = entry.getKey();
                        Long otherParrentIdentifier = entry.getValue();
                        if (otherParrentIdentifier.equals(identifier)) {
                            Boolean otherActive = contextIdentifierToIsActive.get(otherIdentifier);
                            if (otherActive != null) {
                                if (otherActive) {
                                    return ContextState.FINISHED_WITH_RUNNING_CLILDREN;
                                }
                            } else {
                                System.err.println("otherIdentifier " + otherIdentifier + " nicht gefunden!");
                            }
                        }
                    }
                    return ContextState.FINISHED_WITHOUT_RUNNING_CLILDREN;
                }
            }

        } else {
            System.err.println("identifier " + identifier + " nicht gefunden! return ContextState.UNKNOWN");
            return ContextState.UNKNOWN;
        }
    }

    public synchronized void destroy() {
        contextIdentifierToDescription.clear();
        contextIdentifierToIsActive.clear();
        contextIdentifierToParentIdentifier.clear();
        contextTable = null;
    }

    public synchronized ContextTable getContextTable() {
        if (contextTable == null) {
            contextTable = new ContextTable(contextIdentifierToDescription.size());
            Set<Long> identifiers = contextIdentifierToDescription.keySet();
            HashSet<Long> creatorContextIdentifiers = new HashSet<>();
            for (Long identifier : identifiers) {
                if (isCreatorContext(identifier)) {
                    creatorContextIdentifiers.add(identifier);
                }
            }
            for (Long creatorContextIdentifier : creatorContextIdentifiers) {
                contextTable.add(getDescription(creatorContextIdentifier), creatorContextIdentifier, true, getContextState(creatorContextIdentifier));
                for (Long identifier : identifiers) {
                    if (getParentIdentifier(identifier).equals(creatorContextIdentifier)) {
                        contextTable.add(getDescription(identifier), identifier, false, getContextState(identifier));
                    }
                }
            }
        }
        return contextTable;
    }

    public synchronized Long getAnyContextIdentifier() {
        // Versuchen einen aktiven ContextIdentifier zu finden...
        for(Long key : contextIdentifierToIsActive.keySet()) {
            if(contextIdentifierToIsActive.get(key)) {
                return key;
            }
        }
        // Falls keinen gefunden, versuchen irgend einen ContextIdentifier zu finden...
        for(Long key : contextIdentifierToIsActive.keySet()) {
            return key;
        }
        // Fallback...
        System.err.println("Keinen ContextIdentifier gefunden. return -2L");
        return -2L;
    }

}
