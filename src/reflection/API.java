package reflection;

import java.awt.image.BufferedImage;
import java.io.File;

public interface API {

    public void cancelExecution(String reason, boolean error) throws TerminatedException;

    /**
     * Versucht data anzuzeigen. Wirft eine Exception falls der Typ nicht
     * unterstuetzt wird.
     */
    public void displayContentInWindow(Object data, SmartIdentifier smartIdentifier) throws Exception, TerminatedException;

    public void disposeWindow(SmartIdentifier smartIdentifier) throws TerminatedException;

    public void disposeAllWindows(boolean otherRunContexts) throws TerminatedException;

    public void saveImageToFileViaDialog(BufferedImage bufferedImage) throws TerminatedException;

    public void saveImageToFileViaDialog(BufferedImage bufferedImage, String path) throws TerminatedException;

    public void saveImageToFile(BufferedImage bufferedImage, String format, File file, boolean overwrite) throws Exception, TerminatedException;

    public BufferedImage loadImageFromFileViaDialog() throws Exception, TerminatedException;

    public BufferedImage[] loadImagesFromFileViaDialog() throws Exception, TerminatedException;

    public BufferedImage loadImageFromFileViaDialog(String path) throws Exception, TerminatedException;

    public BufferedImage[] loadImagesFromFileViaDialog(String path) throws Exception, TerminatedException;

    public BufferedImage loadImageFromFile(File file) throws Exception, TerminatedException;

    public void saveByteArrayToFile(byte[] bs, File file) throws Exception, TerminatedException;

    public void saveStringToFile(String s, File file) throws Exception, TerminatedException;

    public byte[] loadByteArrayFromFile(File file) throws Exception, TerminatedException;

    public String loadStringFromFile(File file) throws Exception, TerminatedException;

    public String getTimeStamp() throws TerminatedException;

    public String getTimeStampWithDate() throws TerminatedException;

    public SmartIdentifierContext getSmartIdentifierContext() throws TerminatedException;

    public void additionalPrintErr(Object o);

    public void additionalPrintOut(Object o);

    public void printErr(Object o);

    public void printOut(Object o);

    public void putSharedObject(String key, Object o) throws Exception, TerminatedException;

    public Object putSharedObjectIfNotAlreadyExists(String key, Object o) throws Exception, TerminatedException;

    public void removeSharedObject(String key, Object o) throws Exception, TerminatedException;

    public Object getSharedObject(String key) throws Exception, TerminatedException;

    public Object getSharedObjectBlocking(String key) throws Exception, TerminatedException;

}
