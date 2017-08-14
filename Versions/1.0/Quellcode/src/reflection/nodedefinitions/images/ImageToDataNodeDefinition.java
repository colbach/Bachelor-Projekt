package reflection.nodedefinitions.images;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;
import reflection.customdatatypes.rawdata.RawData;
import reflection.customdatatypes.rawdata.RawData;
import utils.images.ImageLoading;

public class ImageToDataNodeDefinition implements NodeDefinition {

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
                return String.class;
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
                return RawData.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Data";
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
        return "Bild zu Daten";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + " [Grafik] Bild Raw Data Daten umwandeln convert";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ImageToData";
    }

    @Override
    public String getIconName() {
        return "Image-To-Data_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        Object[] bilder = io.in(0, new BufferedImage[0]);
        String format = (String) io.in0(0, "png");

        ArrayList<RawData> raws = new ArrayList<>();

        for (Object bild : bilder) {
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write((BufferedImage) bild, format, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            RawData raw = new RawData(imageInByte);
            raws.add(raw);
            
        }

        io.out(0, raws);
    }

}
