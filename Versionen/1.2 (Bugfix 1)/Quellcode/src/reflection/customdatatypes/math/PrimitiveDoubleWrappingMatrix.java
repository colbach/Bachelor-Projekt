package reflection.customdatatypes.math;

public class PrimitiveDoubleWrappingMatrix extends Matrix {

    private final double[][] values; // [Zeilen][Spalten]

    public PrimitiveDoubleWrappingMatrix(double[][] values) {
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
    public Double get(int row, int colum) {
        return (Double) values[row][colum];
    }

}
