package reflection.nodedefinitions.images;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;
import reflection.customdatatypes.rawdata.RawData;
import reflection.customdatatypes.rawdata.RawData;
import utils.images.ImageLoading;

public class DataToImageNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return RawData.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Data";
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
        if (i == 0) {
            return true;
        } else {
            return false;
        }
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
        return "Daten zu Bild";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Grafik] Bild Raw Data Daten umwandeln convert";
    }

    @Override
    public String getUniqueName() {
        return "buildin.DataToImage";
    }

    @Override
    public String getIconName() {
        return "Data-To-Image_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        Object[] raws = io.in(0, new RawData[0]);

        ArrayList<BufferedImage> bilder = new ArrayList<>();

        for (Object raw : raws) {

            InputStream in = new ByteArrayInputStream(((RawData) raw).getData());
            BufferedImage image = ImageIO.read(in);
            bilder.add(image);

        }

        io.out(0, bilder);
    }

}
