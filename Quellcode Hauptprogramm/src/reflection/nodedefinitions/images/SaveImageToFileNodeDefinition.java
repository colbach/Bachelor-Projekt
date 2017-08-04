package reflection.nodedefinitions.images;

import java.awt.image.BufferedImage;
import java.io.File;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;
import utils.images.ImageSaving;

public class SaveImageToFileNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 4;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return BufferedImage.class;
            case 1:
                return File.class;
            case 2:
                return Boolean.class;
            case 3:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Bilder";
            case 1:
                return "Dateien";
            case 2:
                return "Überschreiben";
            case 3:
                return "Format";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            case 1:
                return true;
            case 2:
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
        return "Bilder speichern";
    }

    @Override
    public String getDescription() {
        return "Speichert Bilder in Datei" + TAG_PREAMBLE + " [Grafik] [Dateien] Image BufferedImage Speichern Schreiben Sichern Festplatte Persistent Sichern";
    }

    @Override
    public String getUniqueName() {
        return "buildin.SaveImageToFile";
    }

    @Override
    public String getIconName() {
        return "Save-Image-To-Disk_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        Object[] images = io.in(0, new BufferedImage[0]);
        Object[] files = io.in(1, new File[0]);
        Boolean overwrite = (Boolean) io.in0(2, true);
        String format = (String) io.in0(3);

        if (images.length != files.length) {
            throw new IllegalArgumentException("Bilder und Dateien muss gleiche Länge haben.");
        }
        for (int i = 0; i < images.length; i++) {
            if (format == null) {
                if (((File) files[i]).getAbsolutePath().toLowerCase().endsWith(".png")) {
                    format = "png";
                } else if (((File) files[i]).getAbsolutePath().toLowerCase().endsWith(".jpg")
                        || ((File) files[i]).getAbsolutePath().toLowerCase().endsWith(".jpeg")) {
                    format = "jpg";
                } else {
                    format = "png";
                }
            }

            File file = (File) files[i];
            if (!overwrite && file.exists()) {
                throw new Exception("Datei existiert bereits!");
            }
            ImageSaving.saveImageToFile((BufferedImage) images[i], format, (File) files[i]);
        }
    }

}
