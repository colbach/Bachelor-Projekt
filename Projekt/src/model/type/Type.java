package model.type;

import java.util.Objects;

public class Type {
    
    private final Class typeClass;
    private final boolean array;
    
    private String name = null;

    public Type(Class typeClass, boolean array) {
        this.typeClass = typeClass;
        this.array = array;
    }

    public Class getTypeClass() {
        return typeClass;
    }

    public boolean isArray() {
        return array;
    }

    @Override
    public String toString() {
        if(name == null) {
            name = typeClass.getName();
            int punktPosition = name.lastIndexOf(".");
            if(punktPosition != -1) {
                name = name.substring(punktPosition + 1);
            }
            if(array) {
                name += "...";
            }
        }
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.typeClass);
        hash = 89 * hash + (this.array ? 1 : 0);
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
        final Type other = (Type) obj;
        if (this.array != other.array) {
            return false;
        }
        if (!Objects.equals(this.typeClass, other.typeClass)) {
            return false;
        }
        return true;
    }
    
    
}
