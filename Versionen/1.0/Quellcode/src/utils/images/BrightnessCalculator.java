package utils.images;

public class BrightnessCalculator {
    
    // https://en.wikipedia.org/wiki/Grayscale
    private static float COEFFICIENT_R = 0.299f;
    private static float COEFFICIENT_G = 0.587f;
    private static float COEFFICIENT_B = 0.114f;
            
    /**
     * Diese Funktion berechnet die Helligkeit eines RGB-Wertes. Das Ergebniss
     * dieser Funktion liegt im gleichen Werteberecih wie dessen Eingabe. Es
     * koennen also sowohl Werte aus dem Bereich [0,1[ wie auch Werte aus dem
     * Bereich [0, 256[ verwendet werden.
     */
    public static double calculateBrightness(float r, float g, float b) {
        return Math.sqrt(0.299 * Math.pow(r, 2) + 0.587 * Math.pow(g, 2) + 0.114 * Math.pow(b, 2));
    }
}
