package settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;

public class GeneralSettings extends Settings {
    
    // === Keys ===
    public static String VIEW_ADDITIONAL_HELP_KEY = "view.additionalhelp";
    public static String VIEW_BACKGROUND_KEY = "view.background";
    public static String VIEW_BACKGROUND_VALUE_DEFAULT = DEFAULT_VALUE;
    public static String VIEW_BACKGROUND_VALUE_BLUEPRINT = "blueprint";
    public static String DEVELOPER_REDRAW_COUNTER_KEY = "developer.redrawcounter";
    public static String DEVELOPER_LOG_REDRAW_KEY = "developer.logredraw";
    public static String DEVELOPER_TARGET_COORDINATES_KEY = "developer.targetcoordinates";
    public static String DEVELOPER_MOUSE_COORDINATES_KEY = "developer.mousecoordinates";
    public static String DEVELOPER_LOG_TO_FILE_KEY = "developer.logtofile";
    public static String DEVELOPER_LOG_ONLY_ERRORS_TO_FILE_KEY = "developer.logonlyerrorstofile";
    public static String DEVELOPER_LOG_MORE_KEY = "developer.logmore";
    public static String DEVELOPER_NODE_COUNT_KEY = "developer.nodecount";
    public static String DEVELOPER_ADVANCED_TESTING_KEY = "developer.advancedtesting";
    
    // ===

    private static GeneralSettings instance = null;
    public static String SETTINGS_PATH = "./settings.txt";

    private GeneralSettings(HashMap<String, String> values) {
        super(values);
    }

    public static synchronized GeneralSettings getInstance() {
        if (instance == null) {
            instance = new GeneralSettings(tryToReadSettingsFromSettingsFile(SETTINGS_PATH));
        }
        return instance;
    }
    
    public synchronized void setAndTryToWrite(String key, String value) {
        super.setAndTryToWrite(key, value, SETTINGS_PATH);
    }
    
    public synchronized void setAndTryToWrite(String key, boolean value) {
        super.setAndTryToWrite(key, value, SETTINGS_PATH);
    }

    public void writeSettingsToSettingsFile() throws IOException {
        super.writeSettingsToSettingsFile(SETTINGS_PATH);
    }


    
}