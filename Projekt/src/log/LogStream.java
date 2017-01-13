package log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import settings.GeneralSettings;

public class LogStream extends PrintStream {
    
    private final Logging logger;
    private final boolean err;
    protected final GeneralSettings settings = GeneralSettings.getInstance();
    
    public LogStream(OutputStream out, Logging logger, boolean err) {
        super(out);
        this.logger = logger;
        this.err = err;
    }

    @Override
    public synchronized void write(byte[] buf, int off, int len) {
        
        try {
            
            String outString = new String(buf, off, len);
            logger.logToRingStack(outString, err);
            logger.logToFileIfWanted(outString, err);
            super.write(buf, off, len);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
