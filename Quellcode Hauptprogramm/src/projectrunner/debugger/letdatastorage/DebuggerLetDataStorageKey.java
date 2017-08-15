package projectrunner.debugger.letdatastorage;

import java.util.Objects;
import model.Inlet;

public class DebuggerLetDataStorageKey {
    
    private Inlet inlet;
    private Integer index;
    private long contextIdentifier;

    protected DebuggerLetDataStorageKey(Inlet inlet, Integer index, long contextIdentifier) {
        this.inlet = inlet;
        this.index = index;
        this.contextIdentifier = contextIdentifier;
    }

    public Inlet getInlet() {
        return inlet;
    }

    public void setInlet(Inlet inlet) {
        this.inlet = inlet;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public long getContextIdentifier() {
        return contextIdentifier;
    }

    public void setContextIdentifier(long contextIdentifier) {
        this.contextIdentifier = contextIdentifier;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.inlet);
        hash = 97 * hash + Objects.hashCode(this.index);
        hash = 97 * hash + (int) (this.contextIdentifier ^ (this.contextIdentifier >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DebuggerLetDataStorageKey other = (DebuggerLetDataStorageKey) obj;
        if (this.contextIdentifier != other.contextIdentifier) {
            return false;
        }
        if (!Objects.equals(this.inlet, other.inlet)) {
            return false;
        }
        if (!Objects.equals(this.index, other.index)) {
            return false;
        }
        return true;
    }
    
}
