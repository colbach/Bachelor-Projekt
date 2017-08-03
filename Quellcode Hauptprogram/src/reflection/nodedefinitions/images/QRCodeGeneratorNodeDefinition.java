package reflection.nodedefinitions.images;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import reflection.*;

public class QRCodeGeneratorNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return String.class;
            case 1:
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Inhalt";
            case 1:
                return "Grösse";
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
    public boolean isInletEngaged(int index) {
        if (index == 0) {
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
                return "QR-Code";
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
        return "QR-Code erzeugen";
    }

    @Override
    public String getDescription() {
        return "Erzeugt QR-Code" + TAG_PREAMBLE + "";
    }

    @Override
    public String getUniqueName() {
        return "buildin.QRCodeGenerator";
    }

    @Override
    public String getIconName() {
        return "QR-Code_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws IOException {

        String content = (String) io.in0(0, "");
        Integer size = (Integer) io.in0(1, 250);

        ByteArrayOutputStream bout
                = QRCode.from(content)
                        .withSize(size, size)
                        .to(ImageType.PNG)
                        .stream();
        InputStream in = new ByteArrayInputStream(bout.toByteArray());
        BufferedImage qrcode = ImageIO.read(in);

        io.out(0, qrcode);
    }

}
