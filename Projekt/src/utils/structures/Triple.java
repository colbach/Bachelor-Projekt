package utils.structures;

import java.util.Objects;

/*
 * Diese Klasse beschreibt ein Triple.
 */
public class Triple<L, M, R> {

    public final L l;
    public final M m;
    public final R r;

    public Triple(L l, M m, R r) {
        this.l = l;
        this.m = m;
        this.r = r;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.l);
        hash = 89 * hash + Objects.hashCode(this.m);
        hash = 89 * hash + Objects.hashCode(this.r);
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
        final Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
        if (!Objects.equals(this.l, other.l)) {
            return false;
        }
        if (!Objects.equals(this.m, other.m)) {
            return false;
        }
        if (!Objects.equals(this.r, other.r)) {
            return false;
        }
        return true;
    }
    
    
}
