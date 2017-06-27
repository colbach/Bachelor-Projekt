package utils.images;

import java.awt.Color;

public class RGBHelper {
    
    public static int rgb(byte red, byte green, byte blue) {
        Color c = new Color((red + 256) % 256, (green + 256) % 256, (blue + 256) % 256);
        return c.getRGB();
    }
    
    public static int rgba(byte red, byte green, byte blue, byte alpha) {
        Color c = new Color((red + 256) % 256, (green + 256) % 256, (blue + 256) % 256, (alpha + 256) % 256);
        return c.getRGB();
    }
    
    public static int rgba(int red, int green, int blue, int alpha) {
        return rgba((byte) red, (byte) green, (byte) blue, (byte) alpha);
    }
    public static int rgb(int red, int green, int blue) {
        return rgb((byte) red, (byte) green, (byte) blue);
    }
    
    public static byte r(int rgb) {
        Color c = new Color(rgb);
        return (byte) c.getRed();
    }
    
    public static byte g(int rgb) {
        Color c = new Color(rgb);
        return (byte) c.getGreen();
    }
    
    public static byte b(int rgb) {
        Color c = new Color(rgb);
        return (byte) c.getBlue();
    }
    
    public static byte a(int rgb) {
        Color c = new Color(rgb);
        return (byte) c.getAlpha();
    }
}
