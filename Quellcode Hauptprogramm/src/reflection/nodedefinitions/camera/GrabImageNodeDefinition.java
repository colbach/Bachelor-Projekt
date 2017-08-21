package reflection.nodedefinitions.camera;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.image.BufferedImage;
import logging.AdditionalLogger;
import reflection.additionalnodedefinitioninterfaces.CompatibilityTestable;
import reflection.customdatatypes.camera.Camera;
import reflection.customdatatypes.camera.Webcam;

public class GrabImageNodeDefinition implements NodeDefinition, CompatibilityTestable {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Camera.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Kamera";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int index) {
        return true;
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return BufferedImage.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Snapshot";
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
        return "Bild aufnehmen";
    }

    @Override
    public String getDescription() {
        return "Nimmt Bild über Kamera auf." + TAG_PREAMBLE + " [Kamera] aufnehmen capture grab take machen foto photo";
    }

    @Override
    public String getUniqueName() {
        return "buildin.GrabImage";
    }

    @Override
    public String getIconName() {
        return "Capture-Image_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        Camera kamera = (Camera) io.in0(0, null);
        if (kamera == null) {
            kamera = new Webcam(0);
        }

        BufferedImage snapshot = kamera.grabImage();

        io.out(0, snapshot);
    }

    @Override
    public String testForCompatibility() {
        AdditionalLogger.err.println("testForCompatibility() fuer WebcamNodeDefinition ist noch nicht implementiert.");
        return null;
    }

}
