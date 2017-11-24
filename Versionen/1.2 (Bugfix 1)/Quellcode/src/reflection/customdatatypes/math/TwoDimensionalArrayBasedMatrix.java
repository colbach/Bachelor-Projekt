package reflection.customdatatypes.math;

public class TwoDimensionalArrayBasedMatrix extends MutableMatrix {

    private final Number[][] values; // [Zeilen][Spalten]

    public TwoDimensionalArrayBasedMatrix(Number[][] values) {
        this.values = values;
    }
    
    public TwoDimensionalArrayBasedMatrix(Matrix prototyp) {
        this(prototyp.to2DArray());
    }
    
    public TwoDimensionalArrayBasedMatrix(Number[] diagonalValues) {
        this(diagonalValues.length, diagonalValues.length);
        for(int i=0; i<diagonalValues.length; i++) {
            set(i, i, diagonalValues[i]);
        }
    }

    public TwoDimensionalArrayBasedMatrix(int rows, int colums) {
        this.values = new Number[rows][colums];
        /*for (int row = 0; row < rows; row++) {
            for (int colum = 0; colum < colums; colum++) {
                values[row][colum] = DEFAULT_NUMBER;
            }
        }*/ // Nicht noetig da automatisch DEFAULT_NUMBER zuruek gegeben wird
    }
    
    public TwoDimensionalArrayBasedMatrix(int unityMatrixSize) {
        this(unityMatrixSize, unityMatrixSize);
        for(int i=0; i<unityMatrixSize; i++) {
            set(i, i, 1L);
        }
    }

    @Override
    public final int getRowCount() {
        return values.length;
    }

    @Override
    public final int getColumCount() {
        if (values.length <= 0) {
            return 0;
        } else {
            return values[0].length;
        }
    }

    @Override
    public final Number get(int row, int colum) {
        Number get = values[row][colum];
        if(get == null)
            return DEFAULT_NUMBER;
        else
        return get;
    }

    @Override
    public final void set(int row, int colum, Number value) {
        values[row][colum] = value;
    }

}
