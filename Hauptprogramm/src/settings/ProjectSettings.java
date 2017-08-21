package settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import main.MainClass;

public class ProjectSettings extends Settings {
    
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    
    // === Keys ===
    public static String CREATION_PROGRAM_VERSION_NAME_KEY = "creationprogramversion";
    public static String SAVE_PROGRAM_VERSION_NAMEE_KEY = "saveprogramversion";
    public static String PROGRAM_VERSION_NAME_INITIAL_VALUE = MainClass.VERSION_NAME;
    public static String FORMAT_VERSION_KEY = "formatversion";
    public static String PROGRAM_VERSION_NAME_KEY = "programversion";
    public static String PROJECT_VERSION_KEY = "projectversion";
    public static String VERSIONING_ENABLED_KEY = "versioningenabled";
    public static boolean VERSIONING_ENABLED_DEFAULT_VALUE = true;
    public static String SAVE_DATE_KEY = "savedate";
    
    // ===

    public String path;

    public ProjectSettings(String path) {
        super(tryToReadSettingsFromSettingsFile(path));
        this.path = path;
    }
    
    public synchronized void setPath(String path) {
        this.path = path;
    }
    
    public synchronized void setPathAndLoadIn(String path) {
        setPath(path);
        HashMap<String, String> rodeSettings = tryToReadSettingsFromSettingsFile(path);
        for (Map.Entry<String, String> entry : rodeSettings.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            set(key, value);
        }
    }
    
}