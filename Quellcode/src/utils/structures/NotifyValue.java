package utils.structures;

import java.util.ArrayList;
import java.util.Objects;

public class NotifyValue<T> {

    private T value;
    private ArrayList<Object> notifyListenders;

    public NotifyValue(T value) {
        this.value = value;
        this.notifyListenders = new ArrayList<>();
    }

    public NotifyValue() {
        this.value = null;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
        synchronized (notifyListenders) {
            for (Object notifyListender : notifyListenders) {
                synchronized (notifyListender) {
                    notifyListender.notifyAll();
                }
            }
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public void addListener(Object notifyListender) {
        if (notifyListender == null) {
            throw new IllegalArgumentException("notifyListender darf nicht null sein.");
        } else {
            synchronized (notifyListenders) {
                notifyListenders.add(notifyListender);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this.value) {
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
        final NotifyValue<?> other = (NotifyValue<?>) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }
}
