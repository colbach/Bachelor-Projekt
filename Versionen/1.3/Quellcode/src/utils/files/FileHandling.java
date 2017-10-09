package utils.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import logging.AdditionalErr;
import logging.AdditionalLogger;
import logging.AdditionalOut;

public class FileHandling {
    
    // Synchronisierung ist noetig um zu verhindern dass mehrere Elemente auf
    // die gleiche Datei schreiben.
        
    public static synchronized void saveByteArrayToFile(byte[] bs, File file) throws IOException {
        
        try (FileOutputStream stream = new FileOutputStream(file)) {
            AdditionalLogger.out.println("Schreibe " + bs.length + " Bytes in " + file.getAbsolutePath());
            Files.write(file.toPath(), bs);
        } catch (IOException ex) {
            AdditionalLogger.err.println("Schreiben in " + file.getAbsolutePath() + " fehlgeschlagen (" + ex.getMessage() + ")");
            throw ex;
        }
    }

    public static void saveStringToFile(String s, File file) throws IOException {
        saveByteArrayToFile(s.getBytes("UTF-8"), file);
    }

    public static synchronized byte[] loadByteArrayFromFile(File file) throws IOException {
        
        byte[] bs = null;
        try {
            bs = Files.readAllBytes(file.toPath());
        } catch (IOException ex) {
            AdditionalLogger.err.println("Laden von " + file.getAbsolutePath() + " fehlgeschlagen (" + ex.getMessage() + ")");
            throw ex;
        }
        return bs;
    }


    public static String loadStringFromFile(File file) throws Exception {
        return new String(loadByteArrayFromFile(file), "UTF-8");
    }
}
