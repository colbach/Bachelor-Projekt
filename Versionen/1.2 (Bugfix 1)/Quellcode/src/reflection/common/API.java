package reflection.common;

import reflection.customdatatypes.SourceCode;
import reflection.customdatatypes.SmartIdentifier;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFrame;

public interface API {

    public void cancelExecution(String reason, boolean error) throws TerminatedException;

    /**
     * Versucht data anzuzeigen. Wirft eine Exception falls der Typ nicht
     * unterstuetzt wird.
     */
    public void displayContentInWindow(Object data, SmartIdentifier smartIdentifier) throws Exception, TerminatedException;

    public void disposeWindow(SmartIdentifier smartIdentifier) throws TerminatedException;

    public void disposeAllWindows(boolean otherRunContexts) throws TerminatedException;
    
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
    
    public Object createInstanceFromSourceCode(SourceCode sourceCode) throws Exception, TerminatedException;
    
    public void compileSourceCode(SourceCode sourceCode) throws Exception, TerminatedException;
    
    public Component getView() throws Exception, TerminatedException;

}
