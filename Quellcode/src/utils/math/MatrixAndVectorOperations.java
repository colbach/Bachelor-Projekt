package utils.math;

import reflection.customdatatypes.math.*;

/**
 * Stellt Funktionen zur Loesung von Matrixoperationen zur Verfuegung.
 *
 * Diese Klasse ist stark abgewandelt (einige Methoden wurden hinzugefuegt,
 * bestehende abgeaendert, Variabeln umbenannt, Objektorientierung
 * hinzugefuegt), bassiert aber auf der Klasse "Matrix" aus der Vorlesung
 * Computergrafik von Prof. Dr. F.N. Rudolph welche unter seinem Copyright
 * steht.
 */
public class MatrixAndVectorOperations {

    /**
     * Berechnet das Produkt eines Vektors mit einem Skalar. Vektor wird nicht
     * veraendert.
     *
     * @param s Faktor
     * @param vector Vektor
     * @return s*vector
     */
    public static ArrayBasedVector multiply(Number s, Vector vector) {
        int vectorRows = vector.getRowCount();
        ArrayBasedVector newVec = new ArrayBasedVector(vectorRows);
        for (int r = 0; r < vectorRows; ++r) {
            newVec.set(r, s.doubleValue() * vector.get(r).doubleValue());
        }
        return newVec;
    }

    /**
     * Berechnet das Produkt einer Matrix mit einem Skalar. Matrix wird nicht
     * veraendert.
     *
     * @param s Faktor
     * @param matrix Matrix
     * @return newMatrix s*Matrix
     */
    public static TwoDimensionalArrayBasedMatrix multiply(Number s, Matrix matrix) {
        int matrixRows = matrix.getRowCount();
        int matrixColums = matrix.getColumCount();

        TwoDimensionalArrayBasedMatrix newMatrix = new TwoDimensionalArrayBasedMatrix(matrixRows, matrixColums);
        for (int r = 0; r < matrixRows; ++r) {
            for (int c = 0; c < matrixColums; ++c) {
                newMatrix.set(r, c, s.doubleValue() * matrix.get(r, c).doubleValue());
            }
        }
        return newMatrix;
    }

    /**
     * Matrixmultiplikation matrix3Values = matrix1 * matrix2
     *
     * Matrix2 muss so viele Zeilen haben wie Matrix1 Spalten hat.
     *
     * @param matrix1 linke Matrix
     * @param matrix2 rechte Matrix
     * @return matrix3Values Ergebnismatrix
     */
    public static TwoDimensionalArrayBasedMatrix multiply(Matrix matrix1, Matrix matrix2) {

        // Dimensionen und Schleifengrenzen...
        int matrix1Rows = matrix1.getRowCount(); // Anzahl Zeilen Matrix1
        int matrix2Rows = matrix2.getRowCount(); // Anzahl Zeilen Matris2
        int matrix1Colums = matrix1.getColumCount(); // Anzahl Spalten Matrix1
        int matrix2Colums = matrix2.getColumCount(); // Anzahl Spalten Matrix2

        if (matrix2Rows != matrix1Colums) {
            throw new IllegalArgumentException("Anzahl Kolonen von matrix1 muss gleich Anzahl Zeilen von matrix2 sein.");

        } else {
            Double[][] matrix3Values = new Double[matrix1Rows][matrix2Colums];
            for (int c = 0; c < matrix2Colums; c++) { // fuer alle Spalten von matrix2 und matrix3Values...
                for (int r = 0; r < matrix1Rows; r++) { // fuer alle Zeilen von matrix1 und matrix3Values...
                    matrix3Values[r][c] = 0.0d;
                    for (int k = 0; k < matrix2Rows; k++) { // summiere fuer alle Spalten von matrix1 (Zeilen von matrix2)...
                        matrix3Values[r][c] += matrix1.get(r, k).doubleValue() * matrix2.get(k, c).doubleValue();
                    }
                }
            }
            return new TwoDimensionalArrayBasedMatrix(matrix3Values);
        }
    }
    
    /**
     * Siehe: add(matrix1, matrix2, 1.0).
     */
    public static TwoDimensionalArrayBasedMatrix add(Matrix matrix1, Matrix matrix2) {
        return add(matrix1, matrix2, 1.0);
    }
    
    /**
     * Siehe: add(matrix1, matrix2, -1.0).
     */
    public static TwoDimensionalArrayBasedMatrix subtract(Matrix matrix1, Matrix matrix2) {
        return add(matrix1, matrix2, -1.0);
    }

    /**
     * Matrixmultiplikation matrix3Values = matrix1 + (factor * matrix2)
     *
     * Matrix2 muss gleiche Groesse wie Matrix1 haben.
     *
     * @param matrix1 linke Matrix
     * @param matrix2 rechte Matrix
     * @param factor multiplikator matrix2 (hiermit kann Bsp. leicht Subtraktion
     * umgesetzt werden)
     * @return matrix3Values Ergebnismatrix
     */
    public static TwoDimensionalArrayBasedMatrix add(Matrix matrix1, Matrix matrix2, double factor) {

        // Dimensionen und Schleifengrenzen...
        int matrix1Rows = matrix1.getRowCount(); // Anzahl Zeilen Matrix1
        int matrix2Rows = matrix2.getRowCount(); // Anzahl Zeilen Matris2
        int matrix1Colums = matrix1.getColumCount(); // Anzahl Spalten Matrix1
        int matrix2Colums = matrix2.getColumCount(); // Anzahl Spalten Matrix2

        if (matrix1Rows != matrix2Rows || matrix1Colums != matrix2Colums) {
            throw new IllegalArgumentException("matrix1 muss die gleiche Grösse wie matrix2 haben.");

        } else {
            Double[][] matrix3Values = new Double[matrix1Rows][matrix2Rows];
            for (int c = 0; c < matrix1Colums; c++) { // fuer alle Spalten von matrix2 und matrix3Values...
                for (int r = 0; r < matrix1Rows; r++) { // fuer alle Zeilen von matrix1 und matrix3Values...
                    matrix3Values[r][c] = matrix1.get(r, c).doubleValue() + factor * matrix2.get(r, c).doubleValue();
                }
            }

            return new TwoDimensionalArrayBasedMatrix(matrix3Values);
        }
    }
    
    public static ArrayBasedVector add(Vector vector1, Vector vector2) {
        return add(vector1, vector2, 1.0);
    }
    
    public static ArrayBasedVector subtract(Vector vector1, Vector vector2) {
        return add(vector1, vector2, -1.0);
    }
    
    public static ArrayBasedVector add(Vector vector1, Vector vector2, double factor) {

        // Dimensionen und Schleifengrenzen...
        int vector1Rows = vector1.getRowCount(); // Anzahl Zeilen Vector1
        int vector2Rows = vector2.getRowCount(); // Anzahl Zeilen Vector2

        if (vector1Rows != vector2Rows) {
            throw new IllegalArgumentException("vector1 muss die gleiche Grösse wie vector2 haben.");

        } else {
            Double[] vector3Values = new Double[vector1Rows];
            for (int r = 0; r < vector1Rows; r++) {
                vector3Values[r] = vector1.get(r).doubleValue() + factor * vector2.get(r).doubleValue();
            }

            return new ArrayBasedVector(vector3Values);
        }
    }

    /**
     * Matrixmultiplikation vektor2 = matrix * vektor. Matrix wird nicht
     * veraendert.
     *
     * Vektor muss so viele Zeilen haben wie Matrix1 Spalten hat.
     *
     * @param matrix linkes Element
     * @param vektor rechtes Element
     * @return vektor2 Ergebnisvektor
     */
    public static ArrayBasedVector multiply(Matrix matrix, Vector vektor) {

        // Dimensionen und Schleifengrenzen...
        int matrixRows = matrix.getRowCount(); // Anzahl Zeilen Matrix1
        int matrixColums = matrix.getColumCount(); // Anzahl Spalten Matrix1
        int vectorRows = vektor.getRowCount(); // Anzahl Zeilen Matris2

        if (vectorRows != matrixColums) {
            throw new IllegalArgumentException("Anzahl Zeilen von vektor muss gleich Anzahl Kolonen von matrix sein.");

        } else {
            Double[] matrix3Values = new Double[matrixRows];
            for (int j = 0; j < matrixRows; j++) {
                matrix3Values[j] = 0.0d;
                for (int k = 0; k < vectorRows; k++) {
                    matrix3Values[j] += matrix.get(j, k).doubleValue() * vektor.get(k).doubleValue();
                }
            }
            return new ArrayBasedVector(matrix3Values);
        }
    }

    /**
     * Invertiere die quadratische Matrix m. Matrix wird nicht veraendert.
     *
     * @param matrix zu invertierende Matrix
     * @return r inverse Matrix
     */
    public static PrimitiveDoubleWrappingMatrix invert(Matrix matrix) {
        int matrixRows = matrix.getRowCount();
        double[][] tmp1 = new double[matrixRows][2 * matrixRows];

        // Kopie der Matrix mit Einheitsmatrix rechts erstellen...
        for (int r = 0; r < matrixRows; r++) {
            for (int c = 0; c < matrixRows; c++) {
                tmp1[r][c] = matrix.get(r, c).doubleValue();
            }
            tmp1[r][r + matrixRows] = 1d;
        }

        // Dreieckselimination...
        double[][] tmp2 = Gauss.dreiecksElimination(tmp1);

        // Rueckwaertssubstitution...
        return new PrimitiveDoubleWrappingMatrix(Gauss.rueckwaertsSubstitutionMehrereGLS(tmp2));

    }

}
