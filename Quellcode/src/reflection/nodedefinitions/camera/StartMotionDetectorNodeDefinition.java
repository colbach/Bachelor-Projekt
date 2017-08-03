package reflection.nodedefinitions.camera;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import logging.AdditionalLogger;
import org.bytedeco.javacpp.Loader;
import static org.bytedeco.javacpp.helper.opencv_imgproc.cvFindContours;
import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvAbsDiff;
import static org.bytedeco.javacpp.opencv_core.cvClearMemStorage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_GAUSSIAN;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_LIST;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvMinAreaRect2;
import static org.bytedeco.javacpp.opencv_imgproc.cvSmooth;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import reflection.nodedefinitions.start.*;
import reflection.API;
import reflection.ContextCreator;
import reflection.ContextCreatorInOut;
import reflection.InOut;
import reflection.NodeDefinition;
import reflection.additionalnodedefinitioninterfaces.CompatibilityTestable;
import reflection.additionalnodedefinitioninterfaces.Experimental;
import reflection.customdatatypes.BooleanGrid;
import utils.OpenCVHelper;

public class StartMotionDetectorNodeDefinition implements NodeDefinition, ContextCreator, CompatibilityTestable, Experimental {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Double.class;
            case 1:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Empfindlichkeit";
            case 1:
                return "Echtzeitmonitor";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
        return false;
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            default:
                return false;
        }
    }

    @Override
    public int getOutletCount() {
        return 2;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return BufferedImage.class;
            case 1:
                return BooleanGrid.class;
            case 2:
                return Double.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Ganzes Bild";
            case 1:
                return "Aktiver Bereich";
            case 2:
                return "Gemessene Bewegung";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Bewegungsmelder";
    }

    @Override
    public String getDescription() {
        return "Erzeugt bei jeder erkannten Bewegung einen neuen Kontext. Empfindlichkeit muss zwischen 0.0 (nicht empfindlich) und 1.0 (sehr empfindlich). Es ist zu empfehlen sehr kleine Werte wie z.B. 0.0001 zu verwenden" + TAG_PREAMBLE + " [Events] [Kamera] webcam foto photo detector Detektor Bewegung Motion";
    }

    @Override
    public String getUniqueName() {
        return "buildin.StartMotionDetector";
    }

    @Override
    public String getIconName() {
        return "Motion-Detect_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public synchronized void run(InOut uio, API api) throws Exception {
        ContextCreatorInOut io = (ContextCreatorInOut) uio;
        double sensitivity = (Double) io.in0(0, 0.0001);
        boolean monitor = (Boolean) io.in0(1, true);

        double threshold = 1 - sensitivity;

        final NumberFormat ratioFormatter = new DecimalFormat("#0.0000");

        OpenCVFrameGrabber grabber = null;
        CanvasFrame canvasFrame = null;
        try {
            grabber = new OpenCVFrameGrabber(0);
            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            grabber.start();

            opencv_core.IplImage frame = converter.convert(grabber.grab());
            opencv_core.IplImage image = null;
            opencv_core.IplImage prevImage = null;
            opencv_core.IplImage diff = null;

            if (monitor) {
                canvasFrame = new CanvasFrame("Echtzeitmonitor");
            }
            if (canvasFrame != null) {
                canvasFrame.setCanvasSize(frame.width(), frame.height());
            }

            opencv_core.CvMemStorage storage = opencv_core.CvMemStorage.create();

            while ((canvasFrame == null || canvasFrame.isVisible()) && (frame = converter.convert(grabber.grab())) != null) {
                cvClearMemStorage(storage);

                io.terminatedTest();
                if (uio.outConnected(0)) {
                    uio.out(0, OpenCVHelper.convertIplImageToBufferedImage(frame));
                }

                cvSmooth(frame, frame, CV_GAUSSIAN, 9, 9, 2, 2);
                if (image == null) {
                    image = opencv_core.IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
                    cvCvtColor(frame, image, CV_RGB2GRAY);
                } else {
                    //prevImage = opencv_core.IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
                    prevImage = image;
                    image = opencv_core.IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
                    cvCvtColor(frame, image, CV_RGB2GRAY);
                }

                if (diff == null) {
                    diff = opencv_core.IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
                }

                if (prevImage != null) {
                    cvAbsDiff(image, prevImage, diff);
                    cvThreshold(diff, diff, 64, 255, CV_THRESH_BINARY); // Schwellenwert auf Bild anwenden
                    Frame divFrame = converter.convert(diff);
                    if (canvasFrame != null) {
                        canvasFrame.showImage(divFrame);
                    }
                    opencv_core.IplImage divIplImage = converter.convert(divFrame);

                    final int STEPS = 1;
                    long sum = 0;

                    final ByteBuffer imageData = divIplImage.createBuffer();

                    final int capacity = imageData.capacity();
                    for (int i = 0; i < capacity; i += STEPS) {
                        byte get = imageData.get(i);
                        if (get != 0) {
                            sum += STEPS;
                        }
                    }
                    double ratio;
                    if (capacity != 0) {
                        ratio = sum / (double) capacity;
                    } else {
                        api.additionalPrintErr("capacity ist 0.");
                        if (sum > 0) {
                            ratio = 1f;
                        } else {
                            ratio = 0f;
                        }
                    }
                    api.additionalPrintOut("Ratio: " + ratioFormatter.format(ratio));

                    if (ratio > sensitivity) {
                        api.additionalPrintOut("Bewegung erkannt");
                        io.terminatedTest();
                        if (uio.outConnected(1)) {
                            boolean[] values = new boolean[imageData.capacity()];
                            for (int i = 0; i < values.length; i++) {
                                values[i] = imageData.get(i) != 0;
                            }
                            int width = divFrame.imageWidth;
                            int height = divFrame.imageHeight;
                            uio.out(1, new BooleanGrid() {
                                @Override
                                public boolean getBoolean(int x, int y) {
                                    return values[y * width + x];
                                }

                                @Override
                                public int getWidth() {
                                    return width;
                                }

                                @Override
                                public int getHeight() {
                                    return height;
                                }
                            });
                        }
                        if (uio.outConnected(2)) {
                            uio.out(2, (Double) ratio);
                        }
                        io.startNewContext();
                    }
                }
            }

        } finally {
            if (grabber != null) {
                grabber.stop();
            }
            if (canvasFrame != null) {
                canvasFrame.dispose();
            }
        }

    }

    @Override
    public String testForCompatibility() {
        AdditionalLogger.err.println("testForCompatibility() fuer WebcamNodeDefinition ist noch nicht implementiert.");
        return null;
    }

    @Override
    public String getExperimentalNote() {
        return "Kann hohe Auslastung des Hauptspeicher verursachen.";
    }

}
