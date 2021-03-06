package settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import utils.ArrayHelper;

public class Settings {

    public static final String DEFAULT_VALUE = "default";
    public static final String TRUE = "true", FALSE = "false";
    protected static final Charset ENCODING = Charset.forName("UTF-8");
    protected final HashMap<String, String> values;

    public Settings(HashMap<String, String> values) {
        this.values = values;
    }
    
    public void reset() {
        values.clear();
    }

    public synchronized void setAndTryToWrite(String key, String value, String path) {
        this.values.put(key, value);
        tryToWriteSettingsToSettingsFile(path);
    }

    public synchronized void setAndTryToWrite(String key, int value, String path) {
        this.values.put(key, String.valueOf(value));
        tryToWriteSettingsToSettingsFile(path);
    }

    public synchronized void setAndTryToWrite(String key, boolean value, String path) {
        this.setAndTryToWrite(key, value ? TRUE : FALSE, path);
    }

    public synchronized void set(String key, String value) {
        this.values.put(key, value);
    }

    public synchronized void set(String key, int value) {
        this.values.put(key, String.valueOf(value));
    }

    public synchronized void set(String key, boolean value) {
        this.set(key, value ? TRUE : FALSE);
    }

    public synchronized int getInt(String key, int defaultInteger) {
        String value = this.values.get(key);
//            System.out.println("****** ------------------------------");
//            System.out.println("****** GGG " + this);
//            System.out.println("****** GGG& " + this.getClass());
//            System.out.println("****** GGG! " + ArrayHelper.arrayToString(values.keySet().toArray()));
//            System.out.println("****** GGG? " + value);
//            System.out.println("****** =============================");
        if (value == null) {
            return defaultInteger;
        } else {
            try {
                return Integer.valueOf(value);
            } catch (NumberFormatException numberFormatException) {
                return defaultInteger;
            }
        }
    }

    public synchronized boolean getBoolean(String key, boolean defaultBoolean) {
        String value = this.values.get(key);
        if (value == null) {
            return defaultBoolean;
        } else {
            return value.trim().equals(TRUE);
        }
    }

    public synchronized boolean getBoolean(String key) {
        String value = values.get(key);
        if (value == null) {
            return false;
        } else {
            return value.trim().equals(TRUE);
        }
    }

    public boolean isSet(String key) {
        return values.containsKey(key);
    }

    public synchronized String getString(String key, String defaultString) {
        String value = values.get(key);
        if (value == null) {
            return defaultString;
        } else {
            return value;
        }
    }

    public synchronized String getString(String key) {
        String value = values.get(key);
        return value;
    }

    @Override
    public synchronized String toString() {
        StringBuilder builder = new StringBuilder();
        values.entrySet().forEach((value) -> {
            builder.append(value.getKey()).append("=").append(value.getValue()).append("\n");
        });
        return builder.toString();
    }

    private static HashMap<String, String> fromString(String string) {
        HashMap<String, String> values = new HashMap<>();
        String[] lines = string.split("\n");
        for (String line : lines) {
            int indexOfEqual = line.indexOf("=");
            if (indexOfEqual != -1) {
                values.put(line.substring(0, indexOfEqual), line.substring(indexOfEqual + 1));
            }
        }
        return values;
    }

    private static synchronized String readStringFromSettingsFile(String path) throws IOException {
        File settingsFile = new File(path);
        if (settingsFile.exists()) {
            byte[] bytes = Files.readAllBytes(settingsFile.toPath());
            return new String(bytes, ENCODING);
        } else {
            return "";
        }
    }

    private synchronized void writeStringToSettingsFile(String string, String path) throws FileNotFoundException, IOException {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(string.getBytes(ENCODING));
        }
    }

    public static synchronized HashMap<String, String> readSettingsFromSettingsFile(String path) throws IOException {
        return fromString(readStringFromSettingsFile(path));
    }

    public synchronized void writeSettingsToSettingsFile(String path) throws IOException {
        writeStringToSettingsFile(toString(), path);
    }

    public static synchronized HashMap<String, String> tryToReadSettingsFromSettingsFile(String path) {
        if (path == null) {
            return new HashMap<>();
        }
        try {
            return readSettingsFromSettingsFile(path);
        } catch (IOException iOException) {
            System.err.println("Settings-File konnte nicht gelesen werden.");
            return new HashMap<>();
        }
    }

    public synchronized void tryToWriteSettingsToSettingsFile(String path) {
        try {
            writeStringToSettingsFile(toString(), path);
        } catch (IOException iOException) {
            System.err.println("Settings-File konnte nicht geschrieben werden.");
        }
    }
}
