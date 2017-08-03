package utils;

public class MathHelper {
    
    public static int roundUp(double d) {
        int r = (int)d;
        if(r - d < 0)
            r++;
        return r;
    }
    
    public static int roundDown(double d) {
        return (int)d;
    }
}
