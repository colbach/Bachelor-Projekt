package utils.structures;

import java.util.Objects;

public class ReferentedValue<T> {
    
    private T value;

    public ReferentedValue(T value) {
        this.value = value;
    }
    
    public ReferentedValue() {
        this.value = null;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this.value) {
            return true;
        }
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReferentedValue<?> other = (ReferentedValue<?>) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }
}
