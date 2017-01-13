package utils.structures;

import java.util.Objects;
import model.InOutlet;

/*
 * Diese Klasse beschreibt ein Triple.
 */
public class Quadrupel<A, B, C, D> {

    public final A a;
    public final B b;
    public final C c;
    public final D d;

    public Quadrupel(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.a);
        hash = 23 * hash + Objects.hashCode(this.b);
        hash = 23 * hash + Objects.hashCode(this.c);
        hash = 23 * hash + Objects.hashCode(this.d);
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
        final Quadrupel<?, ?, ?, ?> other = (Quadrupel<?, ?, ?, ?>) obj;
        if (!Objects.equals(this.a, other.a)) {
            return false;
        }
        if (!Objects.equals(this.b, other.b)) {
            return false;
        }
        if (!Objects.equals(this.c, other.c)) {
            return false;
        }
        if (!Objects.equals(this.d, other.d)) {
            return false;
        }
        return true;
    }
    
    
}
