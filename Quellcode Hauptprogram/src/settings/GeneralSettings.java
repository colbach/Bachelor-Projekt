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
    
    public static String WARN_IF_PROJECT_NOT_SAVED_KEY = "warnifprojectnotsaved";
    
    public static String CHECK_PROJECTS_BEFOR_RUN_KEY = "checkprojectsbeforrun";
    public static String CHECK_FOR_UNREACHABLE_NODES_KEY = "checkforunreachablenodes";
    
    public static String VIEW_ADDITIONAL_HELP_KEY = "view.additionalhelp";
    public static String VIEW_BACKGROUND_KEY = "view.background";
    public static String VIEW_BACKGROUND_VALUE_DEFAULT = DEFAULT_VALUE;
    public static String VIEW_BACKGROUND_VALUE_BLUEPRINT = "blueprint";
    public static String VIEW_BACKGROUND_VALUE_BLANK = "blank";
    public static String VIEW_NODECOLLECTION_AUTODISPOSE_OND_OUBLECLICK_KEY = "view.nodecollectionautodisposeondoubleclick";
    public static String VIEW_USE_NIMBUS_LAF_KEY = "view.usenimbuslaf";

    public static String DEVELOPER_REDRAW_COUNTER_KEY = "developer.redrawcounter";
    public static String DEVELOPER_LOG_REDRAW_KEY = "developer.logredraw";
    public static String DEVELOPER_TARGET_COORDINATES_KEY = "developer.targetcoordinates";
    public static String DEVELOPER_MOUSE_COORDINATES_KEY = "developer.mousecoordinates";
    public static String DEVELOPER_LOG_TO_FILE_KEY = "developer.logtofile";
    public static String DEVELOPER_LOG_ONLY_ERRORS_TO_FILE_KEY = "developer.logonlyerrorstofile";
    public static String DEVELOPER_LOG_MORE_KEY = "developer.logmore";
    public static String DEVELOPER_ADDITIONAL_CHECKS_KEY = "developer.additionalchecks";
    public static boolean DEVELOPER_ADDITIONAL_CHECKS_DEFAULT = false;
    public static String DEVELOPER_NODE_COUNT_KEY = "developer.nodecount";
    public static String DEVELOPER_ADVANCED_TESTING_KEY = "developer.advancedtesting";
    public static String DEVELOPER_RING_STACK_SIZE_FOR_GENERAL_LOGGER = "developer.ringstacksizeforgeneralLogger";
    public static String DEVELOPER_RING_STACK_SIZE_FOR_EXECUTION_LOGGER = "developer.ringstacksizeforexecutionLogger";
    
    public static String LAST_PROJECT_PATH_KEY = "lastprojectpath";
    
    public static String START_PROMPT_ON_STARTUP_KEY = "startpromptonstartup";
    public static boolean START_PROMPT_ON_STARTUP_DEFAULT_VALUE = true;
    
    public static String INVISIBLE_PROMPT_KEY = "invisibleprompt";
    
    public static String START_GUI_ON_STARTUP_KEY = "startguionstartup";
    public static boolean START_GUI_ON_STARTUP_DEFAULT_VALUE = true;
    
    public static String EXPERIMENTAL_NODE_DEFINITIONS_KEY = "experimentalnodedefinitions";
    public static boolean EXPERIMENTAL_NODE_DEFINITIONS_VALUE = true;
    
    public static String COMPOSED_NODE_DEFINITIONS_KEY = "composednodedefinitions";
    public static boolean COMPOSED_NODE_DEFINITIONS_VALUE = true;
    
    public static String ALTERNATIV_ASSET_DIRECTORY_KEY = "alternativeassetdirectory";
    
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

    public synchronized void setAndTryToWrite(String key, int value) {
        super.setAndTryToWrite(key, value, SETTINGS_PATH);
    }
    
    public synchronized void setAndTryToWrite(String key, boolean value) {
        
        if (key.equals(DEVELOPER_LOG_MORE_KEY)) {
            FastAccessibleSettings.setLogMore(value);
        } else if (key.equals(DEVELOPER_ADDITIONAL_CHECKS_KEY)) {
            FastAccessibleSettings.setAdditionalchecks(value);
        } else {
            super.setAndTryToWrite(key, value, SETTINGS_PATH);
        }
    }
    
    @Override
    public synchronized void set(String key, boolean value) {
        if (key.equals(DEVELOPER_LOG_MORE_KEY)) {
            FastAccessibleSettings.setLogMore(value);
        } else if (key.equals(DEVELOPER_ADDITIONAL_CHECKS_KEY)) {
            FastAccessibleSettings.setAdditionalchecks(value);
        } else {
            super.set(key, value);
        }
        this.set(key, value ? TRUE : FALSE);
    }

    public void writeSettingsToSettingsFile() throws IOException {
        super.writeSettingsToSettingsFile(SETTINGS_PATH);
    }

}
