package reflection.nodedefinitions.camera;

import logging.AdditionalErr;
import logging.AdditionalLogger;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import reflection.additionalnodedefinitioninterfaces.CompatibilityTestable;
import reflection.*;
import reflection.customdatatypes.camera.Camera;
import reflection.customdatatypes.camera.Webcam;

public class WebcamNodeDefinition implements NodeDefinition, CompatibilityTestable {

    @Override
    public int getInletCount() {
        return 1; // Wegen Bug hier 1 und nicht 3. Breite und Höhe nicht definierbar
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Integer.class;
//            case 1:
//                return Integer.class;
//            case 2:
//                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Geräte Nummer";
//            case 1:
//                return "Breite"; // Bug
//            case 2:
//                return "Höhe"; // Bug
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        return false;
    }

    @Override
    public boolean isInletEngaged(int index) {

        return false;
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Camera.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Webcam";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Webcam";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Kamera] webcam foto photo";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Webcam";
    }

    @Override
    public String getIconName() {
        return "Webcam_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String testForCompatibility() {
        AdditionalLogger.err.println("testForCompatibility() fuer WebcamNodeDefinition ist noch nicht implementiert.");
        return null;
    }

    @Override
    public void run(InOut io, API api) {

        Integer device = (Integer) io.in0(0, 0);
//        Integer w = (Integer) io.in0(1, 0);
//        Integer h = (Integer) io.in0(2, 0);

        Webcam webcam = new Webcam(device);
//        if(w!= null) {
//            webcam.setImageWidth(w);
//        }
//        if(h!= null) {
//            webcam.setImageHeight(h);
//        }

        io.out(0, webcam);
    }

}
