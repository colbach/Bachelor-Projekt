package utils.math;

import java.security.SecureRandom;
import java.util.Random;

public class RandomHelper {

    private static final String LETTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static Random random = new Random();

    public static String randomReadableString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }
        return sb.toString();
    }

    public static String randomNumberString(int base10length) {
        if (base10length > 15) {
            return randomNumberString(15) + randomNumberString(base10length - 15);
        } else {
            Double randomDouble = Math.random();
            String randomString = randomDouble.toString().substring(2) + "000000000000000";
            return randomString.substring(0, base10length);
        }
    }

    /**
     * Gibt eine zufaellige Zahl zwischen [min und max] zurueck.
     */
    public static int randomNumber(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

}
