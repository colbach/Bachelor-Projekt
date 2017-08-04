package reflection.customdatatypes.math;

import java.io.Serializable;
import utils.text.CharacterRepeateHelper;

public abstract class Matrix implements MathObject {

    public static final Matrix NULL_MATRIX = new Matrix() {
        @Override
        public int getRowCount() {
            return 0;
        }

        @Override
        public int getColumCount() {
            return 0;
        }

        @Override
        public Number get(int row, int colum) {
            return DEFAULT_NUMBER;
        }
    };

    public static final Number DEFAULT_NUMBER = new Double(0);

    public boolean isSquare() {
        return getRowCount() == getColumCount();
    }

    public abstract int getRowCount();

    public abstract int getColumCount();

    public abstract Number get(int row, int colum);

    public Number[] getColum(int colum) {
        int rowCount = getRowCount();
        Number[] numbers = new Number[rowCount];
        for (int row = 0; row < rowCount; row++) {
            numbers[row] = get(row, colum);
        }
        return numbers;
    }

    public Number[] getRow(int row) {
        int columCount = getColumCount();
        Number[] numbers = new Number[columCount];
        for (int colum = 0; colum < columCount; colum++) {
            numbers[colum] = get(row, colum);
        }
        return numbers;
    }

    public Number[][] to2DArray() {
        Number[][] array = new Number[getRowCount()][getColumCount()];
        for (int row = 0; row < getRowCount(); row++) {
            for (int colum = 0; colum < getColumCount(); colum++) {
                array[row][colum] = get(row, colum);
            }
        }
        return array;
    }

    public Number[] to1DArray() {
        Number[] array = new Number[getRowCount() * getColumCount()];
        int i = 0;
        for (int row = 0; row < getRowCount(); row++) {
            for (int colum = 0; colum < getColumCount(); colum++) {
                array[i++] = get(row, colum);
            }
        }
        return array;
    }

    public TwoDimensionalArrayBasedMatrix getMutableCopy() {
        return new TwoDimensionalArrayBasedMatrix(to2DArray());
    }

    @Override
    public String toString() {
        int rowCount = getRowCount();
        int columCount = getColumCount();

        String description = rowCount + "x" + columCount + " Matrix: ";
        StringBuilder sb = new StringBuilder(description);
        String whiteSpace = CharacterRepeateHelper.repeateSpaceCharacter(description.length());
        for (int row = 0; row < getRowCount(); row++) {
            if (row != 0) {
                sb.append(whiteSpace);
            }
            sb.append("[ ");
            sb.append(row);
            sb.append("][*]: ");
            for (int colum = 0; colum < getColumCount(); colum++) {
                if (colum != 0) {
                    sb.append(", ");
                }
                sb.append(get(row, colum));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
