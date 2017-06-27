package reflection.nodedefinitions.images;

import java.awt.image.BufferedImage;
import java.io.File;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class SaveImageViaDialogNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return BufferedImage.class;
            case 1:
                return File.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Bild";
            case 1:
                return "Ausgangspunkt";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            case 1:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
        if (i == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int getOutletCount() {
        return 0;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
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
        return "Bild-Ausgabe-Dialog";
    }

    @Override
    public String getDescription() {
        return "Öffnet Dateiausgabe-Dialog welcher es dem Benutzer erlaubt ein Bild im JPG- oder PNG-Format zu speichern." + TAG_PREAMBLE + " [Grafik] [Dateien] [UI] Ausgabe Output File JPEG JPG PNG Bitmap Photo Grafik";
    }

    @Override
    public String getUniqueName() {
        return "buildin.SaveImageViaDialog";
    }

    @Override
    public String getIconName() {
        return "Image-To-Dialog_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        BufferedImage bild = (BufferedImage) io.in0(0);

        if (bild != null) {
            File ausgangspunkt = (File) io.in0(1, ".");
            api.saveImageToFileViaDialog(bild, ausgangspunkt.getAbsolutePath());
        }
    }

}
