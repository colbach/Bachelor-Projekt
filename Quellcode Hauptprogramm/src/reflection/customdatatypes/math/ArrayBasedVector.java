package reflection.customdatatypes.math;

import static reflection.customdatatypes.math.OneDimensionalArrayBasedMatrix.DEFAULT_NUMBER;

public class ArrayBasedVector extends MutableVector {

    private final Number[] values;

    public ArrayBasedVector(Number[] values) {
        this.values = values;
    }

    public ArrayBasedVector(int rows) {
        this.values = new Number[rows];
        /*for (int i = 0; i < values.length; i++) {
            values[i] = new Double(0);
        }*/ // Nicht noetig da automatisch DEFAULT_NUMBER zuruek gegeben wird
    }

    @Override
    public int getRowCount() {
        return values.length;
    }
    
    @Override
    public Number get(int row) {
        Number get = values[row];
        if(get == null)
            return DEFAULT_NUMBER;
        else
        return get;
    }

    public void set(int row, Number value) {
        values[row] = value;
    }

}
