package utils.structures.tuples;

import java.util.Objects;

/*
 * Diese Klasse beschreibt ein Tupel.
 */
public class Pair<L, R> {

    public final L l;
    public final R r;

    public Pair(L l, R r) {
        this.l = l;
        this.r = r;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.l);
        hash = 17 * hash + Objects.hashCode(this.r);
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
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        if (!Objects.equals(this.l, other.l)) {
            return false;
        }
        if (!Objects.equals(this.r, other.r)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "(" + l + ", " + r + ")";
    }
    
}
