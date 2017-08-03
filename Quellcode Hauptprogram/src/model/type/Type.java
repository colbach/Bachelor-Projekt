package model.type;

import java.util.Objects;

public class Type {

    private final Class typeClass;
    private final boolean array;

    private String name = null;
    
    private TypeNameTranslation germanTypeNameTranslation = TypeNameTranslation.getGermanInstance();

    public Type(Class typeClass, boolean array) {
        if (typeClass == null) {
            System.err.println("typeClass von Type soll nicht null sein!");
        }
        this.typeClass = typeClass;
        this.array = array;
    }

    public Type(String reconstructableTypeString) throws ClassNotFoundException {
        int linePosition = reconstructableTypeString.indexOf("-");
        if (linePosition != -1) { // Array...
            String classString = reconstructableTypeString.substring(0, linePosition);
            Class typeClass = Class.forName(classString);
            this.typeClass = typeClass;
            this.array = true;
        } else {
            Class typeClass = Class.forName(reconstructableTypeString);
            this.typeClass = typeClass;
            this.array = false;
        }
    }

    public Class getTypeClass() {
        return typeClass;
    }

    public boolean isArray() {
        return array;
    }

    /**
     * Gibt Beschreibung von Type zuruek. Achtung: Um einen String zu bekommen
     * mit welchem spaeter ein neues Type-Object instanziiert werden kann muss
     * getReconstructableTypeString() verwendet werden!
     */
    @Override
    public String toString() {
        if (name == null) {
            if (typeClass == null) {
                return "?";
            } else {
                name = germanTypeNameTranslation.get(typeClass);
                if (array) {
                    name += "...";
                }
            }
        }
        return name;
    }

    public String getReconstructableTypeString() {
        return typeClass.getName() + (array ? "-Array" : "");
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
