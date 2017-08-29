package reflection.nodedefinitions.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;
import utils.images.ImageLoading;

public class LoadImageFromFileNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return File.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Datei";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
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
                return "Bilder";
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
        return "Bilder laden";
    }

    @Override
    public String getDescription() {
        return "Lädt Bitmap aus File-Objekt." + TAG_PREAMBLE + " [Grafik] [Dateien] laden Datei Festplatte";
    }

    @Override
    public String getUniqueName() {
        return "buildin.LoadImageFromFile";
    }

    @Override
    public String getIconName() {
        return "Load-Image-From-Disk_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        Object[] files = io.in(0, new File[0]);

        ArrayList<BufferedImage> images = new ArrayList<>();

        for (Object file : files) {
            images.add(ImageLoading.loadImageFromFile((File) file));
        }

        io.out(0, images.toArray());
    }

}
