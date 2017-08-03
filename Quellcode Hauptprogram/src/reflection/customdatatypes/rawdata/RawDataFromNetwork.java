package reflection.customdatatypes.rawdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class RawDataFromNetwork extends RawData {

    private final InetAddress sourceAddress;

    public RawDataFromNetwork(InetAddress sourceAddress, byte[] data) {
        super(data);
        this.sourceAddress = sourceAddress;
    }

    public InetAddress getSourceAddress() {
        return sourceAddress;
    }

}
