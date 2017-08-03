package reflection.customdatatypes.math;

public class PrimitiveFloatWrappingMatrix extends Matrix {

    private final float [][] values; // [Zeilen][Spalten]

    public PrimitiveFloatWrappingMatrix(float[][] values) {
        this.values = values;
    }

    @Override
    public int getRowCount() {
        return values.length;
    }

    @Override
    public int getColumCount() {
        if (values.length <= 0) {
            return 0;
        } else {
            return values[0].length;
        }
    }

    @Override
    public Float get(int row, int colum) {
        return (Float) values[row][colum];
    }

}
