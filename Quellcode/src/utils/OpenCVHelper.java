package utils;

import java.awt.image.BufferedImage;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacv.*;

public class OpenCVHelper {
    
    public static BufferedImage convertIplImageToBufferedImage(opencv_core.IplImage src) {
        OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter paintConverter = new Java2DFrameConverter();
        Frame frame = grabberConverter.convert(src);
        return paintConverter.getBufferedImage(frame, 1);
    }
}
