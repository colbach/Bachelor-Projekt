package view.dialogs.vectorinput;

import view.dialogs.matrixinput.*;
import java.util.HashSet;
import reflection.customdatatypes.math.ArrayBasedVector;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.math.PrimitiveDoubleWrappingMatrix;
import reflection.customdatatypes.math.Vector;

public class VectorBuilder {

    private final String[] preparedValues; // [Zeilen]
    private int rows = 3;
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

    public VectorBuilder(Vector basedVector) {
        this.preparedValues = new String[20];
        rows = basedVector.getRowCount();
        if (rows > 20) {
            rows = 20;
        }
        for (int r = 0; r < rows; r++) {
            preparedValues[r] = basedVector.get(r).toString();
        }
    }

    public String getPreparedValue(int row) {
        String preparedValue = preparedValues[row];
        if (preparedValue == null) {
            return "0";
        } else {
            return preparedValue;
        }
    }

    public boolean isPreparedValueValid(int row) {
        try {
            Double.valueOf(getPreparedValue(row));
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void appendToPreparedValue(int row, Character letter) {

        if (LEGAL_LETTERS.contains(letter) || ((int) letter) == 8) {
            String preparedValue = preparedValues[row];
            if (((int) letter) == 8) {
                if (preparedValue != null && preparedValue.length() != 0) {
                    preparedValues[row] = preparedValue.substring(0, preparedValue.length() - 1);
                }
            } else {
                if (preparedValue == null) {
                    preparedValues[row] = "" + letter;
                } else {
                    if (preparedValue.equals("0")) {
                        preparedValues[row] = "" + letter;
                    } else {
                        preparedValues[row] += letter;
                    }
                }
            }
        }
    }

    public void setToPreparedValue(int row, String newPreparedValue) {
        preparedValues[row] = newPreparedValue;
    }

    public int getRowCount() {
        return rows;
    }

    public void setRowCount(int rows) {
        this.rows = rows;
    }

    public boolean isValidMatrix() {
        for (int r = 0; r < rows; r++) {
            if (!isPreparedValueValid(r)) {
                return false;
            }
        }
        return true;
    }

    public Vector getVector() {
        Double[] casted = new Double[rows];
        for (int r = 0; r < rows; r++) {
            casted[r] = Double.valueOf(getPreparedValue(r));
        }
        return new ArrayBasedVector(casted);
    }
}
