package utils.images;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ImageDuplicator {
    
    public static BufferedImage duplicate(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean alphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, alphaPremultiplied, null);
    }
}
