package reflection.customdatatypes.math;

import static reflection.customdatatypes.math.TwoDimensionalArrayBasedMatrix.DEFAULT_NUMBER;

public class OneDimensionalArrayBasedMatrix extends MutableMatrix {

    private final Number[] values; // [ Spalte 1 + Spalte 2 + Spalte 3 ]
    private final int colums;

    public OneDimensionalArrayBasedMatrix(Number[] values, int colums) {
        if (values.length % colums != 0) {
            throw new IllegalArgumentException("Matrix aufgrund von 1D-Array kann nur erstellt werden wenn ((Anzahl Werte) mod (Anzahl Kolonnen) == 0).");
        }
        this.values = values;
        this.colums = colums;
    }

    public OneDimensionalArrayBasedMatrix(Matrix prototyp) {
        this(prototyp.to1DArray(), prototyp.getColumCount());
    }

    public OneDimensionalArrayBasedMatrix(int rows, int colums) {
        this.values = new Number[rows * colums];
        /*for (int i = 0; i < values.length; i++) {
            values[i] = DEFAULT_NUMBER;
        }*/ // Nicht noetig da automatisch DEFAULT_NUMBER zuruek gegeben wird
        this.colums = colums;
    }

    public OneDimensionalArrayBasedMatrix(int unityMatrixSize) {
        this(unityMatrixSize, unityMatrixSize);
        for (int i = 0; i < unityMatrixSize; i++) {
            set(i, i, 1L);
        }
    }

    @Override
    public final int getRowCount() {
        return values.length / colums;
    }

    @Override
    public final int getColumCount() {
        return colums;
    }

    @Override
    public final Number get(int row, int colum) {
        int index = row * colums + colum;
        if (index >= values.length) {
            throw new IndexOutOfBoundsException("Zeile: " + row + ", Spalte: " + colums + ", Anzahl Zeilen: " + getRowCount() + ", Anzahl Spalten: " + getColumCount());
        }
        Number get = values[index];
        if (get == null) {
            return DEFAULT_NUMBER;
        } else {
            return get;
        }
    }

    @Override
    public final void set(int row, int colum, Number value) {
        values[row * colums + colum] = value;
    }

}
