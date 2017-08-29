package reflection.customdatatypes.rawdata;

import java.net.InetAddress;

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
