package reflection.nodedefinitionsupport.camera;

import java.awt.image.BufferedImage;
import reflection.*;
import view.onrun.showimage.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacv.*;
import static utils.OpenCVHelper.*;

public class Webcam implements Camera {
    
    private final int deviceNumber;
    private final OpenCVFrameGrabber grabber;
    private final OpenCVFrameConverter.ToIplImage converter;

    public Webcam(int deviceNumber) {
        this.deviceNumber = deviceNumber;
        this.grabber = new OpenCVFrameGrabber(deviceNumber);
        this.converter = new OpenCVFrameConverter.ToIplImage();
    }
    
    @Override
    public BufferedImage grabImage() throws FrameGrabber.Exception {
        grabber.start();
        opencv_core.IplImage frame = converter.convert(grabber.grab());
        BufferedImage bufferedImage = convertIplImageToBufferedImage(frame);
        grabber.stop();
        return bufferedImage;
    }
    
}
