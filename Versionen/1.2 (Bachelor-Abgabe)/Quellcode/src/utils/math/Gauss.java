package utils.math;

/**
 * Loesen von linearen Gls nach Gauss Berechnung von inversen Matrizen.
 * Berechnung von Determinanten. Diese Klasse arbeitet nicht mit Objekten der
 * Klasse Matrix und Vektor, sondern nur mit dem primitiven Datentyp double.
 * (Verwendung: [Zeile][Spalte]).
 * 
 * Diese Klasse ist leicht abgewandelt, bassiert aber auf der Klasse "Gauss" aus
 * der Vorlesung Computergrafik von Prof. Dr. F.N. Rudolph welche unter seinem
 * Copyright (2008-2012) steht.
 *
 */
public class Gauss {

    /**
     * Legt die Kopie einer Matrix an
     *
     * @param matrix
     * @return clone neue Matrix
     */
    public static double[][] cloneMatrix(double[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        double[][] clone = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                clone[i][j] = matrix[i][j];
            }
        }
        return clone;
    }

    /**
     * Loest ein lineares GLS nach dem Gaussverfahren
     *
     * @param m Koeffizientenmatrix mit rechter Seite
     * @return Loesungsvektor x[]
     */
    public static double[] gauss(double[][] m) {
        return rueckwaertsSubstitution(dreiecksElimination(m));
    }

    /**
     * Loest ein lineares GLS mit mehreren rechten Seiten nach dem
     * Gaussverfahren.
     *
     * @param m Koeffizientenmatrix mit mehreren rechten Seiten
     * @return Loesungsvektoren x[][]
     */
    public static double[][] gaussMehrereRechteSeiten(double[][] m) {
        return rueckwaertsSubstitutionMehrereGLS(dreiecksElimination(m));
    }

    /**
     * Fuehrt die Rueckwaertssubstitution des Gaussverfahrens durch. Es ist eine
     * rechte Seite erlaubt.
     *
     * @param m n * (n+1) Matrix obere Dreiecksmatrix
     * @return Loesungsvektor x
     */
    public static double[] rueckwaertsSubstitution(double[][] m) {
        int n = m.length;
        double[] x = new double[n];
        x[n - 1] = m[n - 1][n] / m[n - 1][n - 1];
        for (int i = n - 2; i > -1; i--) {
            double tmp = m[i][n];
            for (int j = i + 1; j < n; j++) {
                tmp -= m[i][j] * x[j];
            }
            x[i] = tmp / m[i][i];
        }
        return x;
    }

    /**
     * Fuehrt die Rueckwaertssubstitution des Gaussverfahrens durch. Es sind k
     * rechte Seiten erlaubt.
     *
     * @param m n * (n+k) Matrix obere Dreiecksmatrix
     * @return Loesungsvektor x
     */
    public static double[][] rueckwaertsSubstitutionMehrereGLS(double[][] m) {
        int n = m.length;
        int nGls = m[0].length - n;
        double[][] x = new double[n][nGls];
        for (int k = 0; k < nGls; k++) {
            x[n - 1][k] = m[n - 1][n + k] / m[n - 1][n - 1];
            for (int i = n - 2; i > -1; i--) {
                double tmp = m[i][n + k];
                for (int j = i + 1; j < n; j++) {
                    tmp -= m[i][j] * x[j][k];
                }
                x[i][k] = tmp / m[i][i];
            }
        }
        return x;
    }

    /**
     * Fuehrt eine Dreieckszerlegung nach Gauss durch.
     *
     * @param m n*(n+k) Matrix Koeffizienten und rechte Seite(n).
     * @return Matrix, die Elemente der unteren Dreiecksmatrix haben den Wert 0
     */
    public static double[][] dreiecksElimination(double[][] m) {
        double[][] r = cloneMatrix(m);
        int n = r.length;
        // fuer alle zu eliminierenden Spalten...
        for (int j = 0; j < n - 1; j++) {
            double max = Math.abs(r[j][j]);
            int pivotZeile = j;
            // Pivotsuche...
            for (int i = j + 1; i < n; i++) {
                if (Math.abs(r[i][j]) > max) {
                    max = Math.abs(r[i][j]);
                    pivotZeile = i;
                }
            }
            // Zeilentausch...
            if (pivotZeile != j) {
                for (int k = j; k < r[j].length; k++) {
                    double tmp = r[j][k];
                    r[j][k] = r[pivotZeile][k];
                    r[pivotZeile][k] = tmp;
                }
            }
            // Elimination...
            for (int i = j + 1; i < n; i++) {
                double faktor = r[i][j] / r[j][j];
                for (int k = j; k < r[j].length; k++) {
                    r[i][k] -= faktor * r[j][k];
                }
            }
        }
        return r;
    }

    /**
     * Berechnet die Determinante einer quadratischen Matrix. Es wird zunaechst
     * eine Dreieckselimination durchgefuehrt. Die Determinante berechnet sich
     * als Produkt der Hauptdiagonalelemente.
     *
     * @param m n*n Matrix
     * @return r Wert der Determinante
     */
    public static double determinante(double[][] m) {
        double[][] tmp = dreiecksElimination(m);
        // Produkt der Hauptdiagonalelemente...
        double r = tmp[0][0];
        for (int i = 1; i < tmp.length; i++) {
            r *= tmp[i][i];
        }
        return r;
    }

    /**
     * Berechnet das Hadamardsche Konditionsmass Kh < 0.01 schlechte Kondition Kh
     * > 0.1 gute Kondition
     *
     * @param m quadratische Matrix
     * @return r Konditionsmass nach Hadamard
     */
    public static double hadamardKondition(double[][] m) {
        double r = Math.abs(determinante(m));
        int n = m.length;
        for (int i = 0; i < n; i++) {
            double alpha = 0d;
            for (int j = 0; j < n; j++) {
                alpha += m[i][j] * m[i][j];
            }
            r /= Math.sqrt(alpha);
        }
        return r;
    }

}
