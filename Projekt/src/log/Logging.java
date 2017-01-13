package log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import settings.GeneralSettings;
import view.generallog.LogWindow;

public class Logging {

    private static Logging instance = null;

    private int stackSize;
    private final String[] ringStack;
    private final boolean[] errRingStack;
    private int pointer = 0;
    private boolean systemStreamsSetted = false;
    private long counter = 0;
    protected final static GeneralSettings settings = GeneralSettings.getInstance();
    private final String formatedDate;
    private boolean cantWriteToLogToFileStream = false;
    private PrintStream logToFileStream;
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public Logging(int stackSize) {
        this.stackSize = stackSize;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        formatedDate = dateFormat.format(date);
        ringStack = new String[stackSize];
        errRingStack = new boolean[stackSize];
    }

    /**
     * Genereller Logger welcher im gesammten Programm verwendet wird und in
     * System.out sowie System.in geleitet wird.
     */
    public static synchronized Logging getGeneralInstance() {
        final int STACK_SIZE_FOR_GENERAL_INSTANCE = 200;
        if (instance == null) {
            instance = new Logging(STACK_SIZE_FOR_GENERAL_INSTANCE);
        }
        return instance;
    }

    public PrintStream getLogToFileStream() {
        if (logToFileStream == null && cantWriteToLogToFileStream == false) {
            String filename = "log_" + formatedDate + ".txt";
            try {
                logToFileStream = new PrintStream(new FileOutputStream(filename, true));
            } catch (FileNotFoundException ex) {
                cantWriteToLogToFileStream = true;
                System.err.println(filename + " konnte nicht geoeffnet werden.");
            }
        }
        return logToFileStream;
    }

    /**
     * Leitet System-Streams (System.err und System.out) auf eigene Streams um.
     */
    public synchronized void setupSystemStreams() {
        if (!systemStreamsSetted) {
            System.setOut(new LogStream(System.out, this, false));
            System.setErr(new LogStream(System.err, this, true));
            systemStreamsSetted = true;
        } else {
            System.err.println("Streams sind bereits initialisiert.");
        }
    }

    /**
     * 0 ist das neuste.
     */
    public synchronized String get(int i) {
        if (i == stackSize) {
            return "(" + (counter - stackSize) + " weitere Zeilen)";
        } else if (i > stackSize) {
            throw new IllegalArgumentException("i (" + i + ") muss kleiner als STACK_SIZE (" + stackSize + ") sein.");
        }
        String s = ringStack[(pointer + stackSize - i - 1) % stackSize];
        if (s == null) {
            return "";
        } else {
            return s;
        }
    }

    public synchronized int getAvailableCount() {
        if (counter <= stackSize) {
            return (int) counter;
        } else {
            return stackSize + 1;
        }
    }

    public synchronized long getCounter() {
        return counter;
    }

    public synchronized boolean isError(int i) {
        if (i == stackSize) {
            return false;
        } else if (i > stackSize) {
            throw new IllegalArgumentException("i (" + i + ") muss kleiner als STACK_SIZE (" + stackSize + ") sein.");
        }
        return errRingStack[(pointer + stackSize - i - 1) % stackSize];
    }

    /**
     * Diese Methode printet Text als Ausgabe in den RingStack. Er erzeugt aber
     * KEINE Konsolen-Ausgabe. Hierfuer System.out.println benutzen dieser wird
     * in den Generellen RingStack umgeleitet (als Kopie).
     */
    public synchronized void outPrintln(String s) {
        logToRingStack(s + "\n", false);
    }

    /**
     * Diese Methode printet Text als Fehler-Ausgabe in den RingStack. Er
     * erzeugt aber KEINE Konsolen-Ausgabe. Hierfuer System.out.println benutzen
     * dieser wird in den Generellen RingStack umgeleitet (als Kopie).
     */
    public synchronized void errPrintln(String s) {
        logToRingStack(s + "\n", true);
    }

    protected synchronized void logToRingStack(String string, boolean err) {

        if (string.trim().length() == 0) {
            pointerPlusPlus();
        } else {

            if (ringStack[pointer] == null) {
                ringStack[pointer] = "";
            }

            boolean isFirst = true;
            for (String s : string.split("\n")) {
                if (!isFirst) {
                    pointerPlusPlus();
                } else {
                    isFirst = false;
                }
                ringStack[pointer] += s;
                errRingStack[pointer] = err;

            }

            if (string.endsWith("\n")) {
                pointerPlusPlus();
            }

        }
    }

    protected synchronized void logToFileIfWanted(String string, boolean err) {

        if (this.settings.getBoolean(GeneralSettings.DEVELOPER_LOG_TO_FILE_KEY, false)) {

            if (err || !this.settings.getBoolean(GeneralSettings.DEVELOPER_LOG_ONLY_ERRORS_TO_FILE_KEY, false)) {

                PrintStream logToFileStream = getLogToFileStream();

                if (string.trim().length() == 0) {
                    logToFileStream.println();
                } else {

                    boolean isFirst = true;
                    for (String s : string.split("\n")) {
                        if (!isFirst) {
                            logToFileStream.println();
                        } else {
                            isFirst = false;
                        }
                        String pre = err ? "[ERR " + timeFormat.format(new Date()) + "] " : "[OUT " + timeFormat.format(new Date()) + "] ";
                        logToFileStream.print(pre + s);
                    }

                    if (string.endsWith("\n")) {
                        logToFileStream.println();
                    }

                }
            }
        }
    }

    private void pointerPlusPlus() {
        pointer = (pointer + 1) % stackSize;
        counter++;
        ringStack[pointer] = "";
    }

    /*public static void additionalOutPrint(String s) {
        if (settings.getBoolean(GeneralSettings.DEVELOPER_LOG_MORE_KEY, false)) {
            System.out.print("(" + s + ")");
        }
    }*/
}
