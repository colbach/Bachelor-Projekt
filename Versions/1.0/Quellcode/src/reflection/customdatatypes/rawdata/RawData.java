package reflection.customdatatypes.rawdata;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

public class RawData {

    protected byte[] data;
    protected boolean changed;

    public RawData(byte[] data) {
        this.data = data;
        this.changed = false;
    }

    public RawData() {
        this.data = new byte[0];
    }

    public synchronized byte[] getData() throws Exception {
        return data;
    }

    public synchronized void setData(byte[] data) {
        this.data = data;
        changed = true;
    }

    public synchronized RawDataFromFile writeToFile(File file) throws Exception {
        byte[] data = getData();
        FileUtils.writeByteArrayToFile(file, data);
        return new RawDataFromFile(file, data);
    }

    public boolean isChanged() {
        return changed;
    }

    public int size() throws Exception {
        byte[] data = getData();
        if (data == null) {
            return 0;
        } else {
            return data.length;
        }
    }

    @Override
    public String toString() {
        try {
            int sampleLength = 10;
            String c;
            if (changed) {
                c = "";
            } else {
                c = " unchanged";
            }
            return size() + " bytes of" + c + " Raw Data starting with " + getSampleString(sampleLength) + " (first " + sampleLength + " Bits)";
        } catch (Exception ex) {
            return "Undefined Raw Data";
        }
    }

    public String getSampleString(int bytes) throws Exception {
        byte[] data = getData();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes && data.length > i; i++) {
            sb.append(String.format("%02X", data[i]));
        }
        return sb.toString();
    }
    
    public String getMime() {
        return "text/plain";
    }

}
