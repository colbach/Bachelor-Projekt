package reflection.customdatatypes.rawdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class RawDataFromFile extends RawData {

    private final File sourceFile;
    private boolean read;

    public RawDataFromFile(File sourceFile, boolean lazy) throws IOException {
        this.sourceFile = sourceFile;
        if (lazy) {
            this.read = false;
        } else {
            read();
        }
    }
    
    public RawDataFromFile(File sourceFile, byte[] data) {
        this.sourceFile = sourceFile;
        this.data = data;
        this.read = true;
    }

    public synchronized File getSourceFile() {
        return sourceFile;
    }

    public final synchronized void read() throws FileNotFoundException, IOException {
        if (!read) {
            data = IOUtils.toByteArray(new FileInputStream(sourceFile));
            read = true;
        }
    }

    @Override
    public synchronized byte[] getData() throws IOException {
        read();
        return data;
    }

    public boolean isRead() {
        return read;
    }

    public synchronized void writeBack() throws IOException {
        if (read) {
            FileUtils.writeByteArrayToFile(sourceFile, data);
        }
    }

    @Override
    public String toString() {
        return "";
    }
    
    public String getFileName() {
        return sourceFile.getName();
    }
    
}
