package utils.structures;

public class Single<T> {
    
    public final T v;

    public Single(T v) {
        this.v = v;
    }

    public T get() {
        return v;
    }

    @Override
    public String toString() {
        return "(" + v + ")";
    }
}
