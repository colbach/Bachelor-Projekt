package reflection.customdatatypes;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import logging.AdditionalLogger;

public class SourceCode implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String javaCode;
    private transient Integer lineCount;

    public SourceCode(String javaCode) {
        this.javaCode = javaCode;
    }

    public String getJavaCode() {
        return javaCode;
    }

    @Override
    public String toString() {
        if (lineCount == null) {
            int lineCounter = 0;
            for (int i = 0; i < javaCode.length(); i++) {
                if (javaCode.charAt(i) == '\n') {
                    lineCounter++;
                }
            }
            lineCount = lineCounter;
        }
        return "Javacode (" + lineCount + " Zeilen)";
    }
}
