package reflection.nodedefinitions.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class LoadImageViaDialogNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return File.class;
            case 1:
                return Boolean.class;
            case 2:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Ausgangs Pfad";
            case 1:
                return "Mehrere";
            case 2:
                return "Ausnahme bei Abbruch";
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
                return BufferedImage.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Bild";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Bild-Eingabe-Dialog";
    }

    @Override
    public String getDescription() {
        return "Öffnet Dateieingabe-Dialog welcher es dem Benutzer erlaubt Bilddateien im JPG-, PNG- oder BMP-Format zu laden und gibt diese als Bitmap weiter. Falls der Benutzer der Eingabe abbricht wird ein Bitmap-Array mit der Länge 0 weitergegeben." + TAG_PREAMBLE + " [Grafik] [Dateien] [UI] Eingabe Input File JPEG JPG PNG BMP Bitmap Photo Grafik";
    }

    @Override
    public String getUniqueName() {
        return "buildin.LoadImageViaDialog";
    }

    @Override
    public String getIconName() {
        return "Image-From-Dialog_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        File ausgangspfad = (File) io.in0(0, new File("."));
        Boolean mehrere = (Boolean) io.in0(1, false);
        Boolean abbruch = (Boolean) io.in0(2, true);

        try {
            if (mehrere) {
                io.out(0, api.loadImagesFromFileViaDialog(ausgangspfad.getPath()));
            } else {
                io.out(0, new BufferedImage[]{api.loadImageFromFileViaDialog(ausgangspfad.getPath())});
            }
        } catch (Exception exception) {
            if (abbruch) {
                throw exception;
            } else {
                api.additionalPrintErr(exception.getMessage());
            }
        }

    }

}
