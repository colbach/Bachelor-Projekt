package view.dialogs.matrixinput;

import java.util.HashSet;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.math.PrimitiveDoubleWrappingMatrix;

public class MatrixBuilder {

    private final String[][] preparedValues; // [Zeilen][Spalten]
    private int rows = 3;
    private int cols = 3;
    private static final HashSet<Character> LEGAL_LETTERS = new HashSet<>();

    static {
        LEGAL_LETTERS.add('0');
        LEGAL_LETTERS.add('1');
        LEGAL_LETTERS.add('2');
        LEGAL_LETTERS.add('3');
        LEGAL_LETTERS.add('4');
        LEGAL_LETTERS.add('5');
        LEGAL_LETTERS.add('6');
        LEGAL_LETTERS.add('7');
        LEGAL_LETTERS.add('8');
        LEGAL_LETTERS.add('9');
        LEGAL_LETTERS.add('-');
        LEGAL_LETTERS.add('+');
        LEGAL_LETTERS.add('.');
        LEGAL_LETTERS.add('E');
    }

    public MatrixBuilder(Matrix basedMatrix) {
        this.preparedValues = new String[20][20];
        rows = basedMatrix.getRowCount();
        cols = basedMatrix.getColumCount();
        if (rows > 20) {
            rows = 20;
        }
        if (cols > 20) {
            cols = 20;
        }
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                preparedValues[r][c] = basedMatrix.get(r, c).toString();
            }
        }
    }

    public String getPreparedValue(int row, int colum) {
        String preparedValue = preparedValues[row][colum];
        if (preparedValue == null) {
            return "0";
        } else {
            return preparedValue;
        }
    }

    public boolean isPreparedValueValid(int row, int colum) {
        try {
            Double.valueOf(getPreparedValue(row, colum));
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void appendToPreparedValue(int row, int colum, Character letter) {
        
        if (LEGAL_LETTERS.contains(letter) || ((int) letter) == 8) {
            String preparedValue = preparedValues[row][colum];
            if (((int) letter) == 8) {
                if (preparedValue != null && preparedValue.length() != 0) {
                    preparedValues[row][colum] = preparedValue.substring(0, preparedValue.length() - 1);
                }
            } else {
                if (preparedValue == null) {
                    preparedValues[row][colum] = "" + letter;
                } else {
                    if (letter != '.' && preparedValue.equals("0")) {
                        preparedValues[row][colum] = "" + letter;
                    } else {
                        preparedValues[row][colum] += letter;
                    }
                }
            }
        }
    }

    public void setToPreparedValue(int row, int colum, String newPreparedValue) {
        preparedValues[row][colum] = newPreparedValue;
    }

    public int getRowCount() {
        return rows;
    }

    public int getColumCount() {
        return cols;
    }

    public void setRowCount(int rows) {
        this.rows = rows;
    }

    public void setColumCount(int cols) {
        this.cols = cols;
    }

    public void setToUnitMatrix() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == c) {
                    preparedValues[r][c] = "1";
                } else {
                    preparedValues[r][c] = "0";
                }
            }
        }
    }

    public boolean isValidMatrix() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!isPreparedValueValid(r, c)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Matrix getMatrix() {
        double[][] casted = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                casted[r][c] = Double.valueOf(getPreparedValue(r, c));
            }
        }
        return new PrimitiveDoubleWrappingMatrix(casted);
    }
}
