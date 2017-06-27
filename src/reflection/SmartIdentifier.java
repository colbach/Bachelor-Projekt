package reflection;

import java.io.Serializable;
import java.util.Objects;

public class SmartIdentifier implements Serializable {
    
    private final String identifier;

    public SmartIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifierName() {
        return identifier;
    }
    
    @Override
    public String toString() {
        return "# " + identifier;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.identifier);
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
        final SmartIdentifier other = (SmartIdentifier) obj;
        if (!Objects.equals(this.identifier, other.identifier)) {
            return false;
        }
        return true;
    }
}