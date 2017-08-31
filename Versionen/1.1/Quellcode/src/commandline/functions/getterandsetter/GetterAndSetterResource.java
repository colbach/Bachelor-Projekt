/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandline.functions.getterandsetter;

import commandline.CommandLine;
import main.componenthub.ComponentHub;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.MainClass;
import settings.GeneralSettings;
import static settings.GeneralSettings.*;
import utils.format.TimeFormat;
import view.main.MainWindow;

public class GetterAndSetterResource {

    private static GetterAndSetterResource instance;

    private final GetterAndSetterMap getterAndSetterMap;

    public static boolean stringToBoolean(String string) {
        String lowerString = string.toLowerCase();
        if (lowerString.equals("0")) {
            return false;
        } else if (lowerString.equals("1")) {
            return true;
        } else if (lowerString.equals("true")) {
            return true;
        } else if (lowerString.equals("false")) {
            return false;
        } else if (lowerString.equals("false")) {
            return false;
        } else if (lowerString.equals("t")) {
            return true;
        } else if (lowerString.equals("f")) {
            return false;
        }
        throw new IllegalArgumentException(string + " kann nicht als Wahrheitswert interpretiert werden.");
    }

    public GetterAndSetterResource(CommandLine commandLine) {

        this.getterAndSetterMap = new GetterAndSetterMap(commandLine);

        /*getterAndSetterMap.put("totalmemory", "Fuer die JVM verfuegbarer Hauptspeicherpeicher", new Getter() {
            @Override
            public String get() {
                return Runtime.getRuntime().totalMemory() + " (~ " + (Runtime.getRuntime().totalMemory()/1024/1024) + " MB)";
            }
        });
        getterAndSetterMap.put("freememory", "Fuer die JVM verfuegbarer Hauptspeicherpeicher", new Getter() {
            @Override
            public String get() {
                return Runtime.getRuntime().freeMemory() + " (~ " + (Runtime.getRuntime().freeMemory()/1024/1024) + " MB)";
            }
        });
        getterAndSetterMap.put("usedmemory", "Fuer die JVM verfuegbarer Hauptspeicherpeicher", new Getter() {
            @Override
            public String get() {
                long usedMemory = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
                return usedMemory + " (~ " + (usedMemory/1024/1024) + " MB)";
            }
        });*/
        getterAndSetterMap.put("version", "vers", "Version des Builds",
                () -> MainClass.VERSION_NAME);
        getterAndSetterMap.put("pathforassets", "Pfad zu Assetdateien",
                () -> new File(MainClass.PATH_FOR_ASSETS).getAbsolutePath());
        getterAndSetterMap.put("workingdirectory", "pwd wd", "Arbeits-Pfad",
                () -> new File("").getAbsolutePath());
        getterAndSetterMap.put("threadcount", "treadcount tc jtc javathreadcount", "Gesammtanzahl Java-Threads",
                () -> "" + ManagementFactory.getThreadMXBean().getThreadCount());
        getterAndSetterMap.put("pathfornodes", "pathfornodesdefinitions pathfornodedefinitions", "Pfad zu eingebauten Nodedefinitionen",
                () -> new File(MainClass.pathForBuildinNodeClasses).getAbsolutePath());
        getterAndSetterMap.put("showstate", "Zustand des Hauptfensters (wenn verfuegbar)",
                () -> {
                    MainWindow lastRegisteredMainWindow = ComponentHub.getInstance().getGUI();
                    if (lastRegisteredMainWindow == null) {
                        return "nicht verfuegbar";
                    } else {
                        return lastRegisteredMainWindow.getMainPanel().getShowState().toString();
                    }
                });
        getterAndSetterMap.put("runtime", "Laufzeit",
                () -> {
                    return TimeFormat.format(System.currentTimeMillis() - MainClass.START_TIME.getTime());
                });
        getterAndSetterMap.put("starttime", "startime startzeit", "Startzeit",
                () -> {
                    return MainClass.START_TIME.toString();
                });
        getterAndSetterMap.put("pref_logmore", "logmore", "Mehr Ausgaben", new GeneralSettingsGetterSetter(DEVELOPER_LOG_MORE_KEY, Boolean.FALSE));
        getterAndSetterMap.put("pref_invisibleprompt", "invisibleprompt", "Unsichtbarer Prompt", new GeneralSettingsGetterSetter(INVISIBLE_PROMPT_KEY, Boolean.FALSE));
        getterAndSetterMap.put("pref_logtofile", "logtofile", "Ausgaben in Datei loggen", new GeneralSettingsGetterSetter(DEVELOPER_LOG_TO_FILE_KEY, Boolean.FALSE));
        getterAndSetterMap.put("pref_logonlyerrorstofile", "logonlyerrorstofile", "Nur Fehler in Datei loggen (setzt vorraus dass logtofile aktiv ist)", new GeneralSettingsGetterSetter(DEVELOPER_LOG_ONLY_ERRORS_TO_FILE_KEY, false));
        getterAndSetterMap.put("pref_startguionstartup", "startguionstartup startuionstartup startguionstart startuionstart autostartgui autostartui", "GUI automatisch bei Programmstart oeffnen", new GeneralSettingsGetterSetter(START_GUI_ON_STARTUP_KEY, START_GUI_ON_STARTUP_DEFAULT_VALUE));
        getterAndSetterMap.put("pref_startpromptonstartup", "startpromptonstartup startpromptonstart autostartprompt", "Prompt automatisch bei Programmstart starten", new GeneralSettingsGetterSetter(START_PROMPT_ON_STARTUP_KEY, START_PROMPT_ON_STARTUP_DEFAULT_VALUE));
        getterAndSetterMap.put("pref_nodecollectionautodisposeondoubleclick", "nodecollectionautodisposeondoubleclick nodecollectionautodispose", "Nodecollection-Fenster automatisch schliessen bei Doppelklick", new GeneralSettingsGetterSetter(VIEW_NODECOLLECTION_AUTODISPOSE_OND_OUBLECLICK_KEY, true));
        getterAndSetterMap.put("pref_usenimbuslaf", "usenimbuslaf", "Nimbus als LAF verwenden (nur fuer Kompatibilitaet, Nimbus is haesslich)", new GeneralSettingsGetterSetter(VIEW_NODECOLLECTION_AUTODISPOSE_OND_OUBLECLICK_KEY, true));
        getterAndSetterMap.put("pref_advancedtesting", "advancedtesting", "Erweiterte Funktionen zum Testen in GUI anzeigen", new GeneralSettingsGetterSetter(DEVELOPER_ADVANCED_TESTING_KEY, false));
        getterAndSetterMap.put("pref_redrawcounter", "redrawcounter", "Redraw Counter anzeigen (nur zum Testen)", new GeneralSettingsGetterSetter(DEVELOPER_REDRAW_COUNTER_KEY, false));
        getterAndSetterMap.put("pref_logredraw", "logredraw", "Redraw loggen (nur zum Testen)", new GeneralSettingsGetterSetter(DEVELOPER_REDRAW_COUNTER_KEY, false));
        getterAndSetterMap.put("pref_targetcoordinates", "targetcoordinates", "Zeigt Koordinaten von Target (nur zum Testen)", new GeneralSettingsGetterSetter(DEVELOPER_TARGET_COORDINATES_KEY, false));
        getterAndSetterMap.put("pref_mousecoordinates", "mousecoordinates", "Zeit Maus-Koordinaten (nur zum Testen)", new GeneralSettingsGetterSetter(DEVELOPER_MOUSE_COORDINATES_KEY, false));
        getterAndSetterMap.put("pref_nodecount", "nodecount", "Node-Zaehler anzeigen (nur zum Testen)", new GeneralSettingsGetterSetter(DEVELOPER_NODE_COUNT_KEY, false));
        getterAndSetterMap.put("pref_additionalchecks", "additionalchecks", "Erweiterte Tests machen (aehnlich asserts, nur zum Testen)", new GeneralSettingsGetterSetter(DEVELOPER_ADDITIONAL_CHECKS_KEY, DEVELOPER_ADDITIONAL_CHECKS_DEFAULT));
        getterAndSetterMap.put("pref_executionringstacksize", "executionringstacksize", "Groesse von Execution-Ringstack (moeglicherweise erst aktiv ab Neustart)", new GeneralSettingsGetterSetter(DEVELOPER_RING_STACK_SIZE_FOR_EXECUTION_LOGGER, (Integer) 2000));
        getterAndSetterMap.put("pref_generalringstacksize", "generalringstacksize", "Groesse von Execution-Ringstack (moeglicherweise erst aktiv ab Neustart)", new GeneralSettingsGetterSetter(DEVELOPER_RING_STACK_SIZE_FOR_GENERAL_LOGGER, (Integer) 200));
        getterAndSetterMap.put("pref_additionalhelpingui", "additionalhelpingui additionalhelpinui", "Hilfe unten Rechts in UI anzeigen", new GeneralSettingsGetterSetter(VIEW_ADDITIONAL_HELP_KEY, true));
        getterAndSetterMap.put("pref_warnifprojectnotsaved", "warnifprojectnotsaved", "Nachfragen ob Projekt gespeichert werden soll", new GeneralSettingsGetterSetter(WARN_IF_PROJECT_NOT_SAVED_KEY, true));
        getterAndSetterMap.put("pref_checkprojectsbeforstart", "checkprojectsbeforstart checkprojects checkprojectsbeonstart checkprojektsbeforstart", "Projekte auf Fehler ueberpruefen vor Start", new GeneralSettingsGetterSetter(CHECK_PROJECTS_BEFOR_RUN_KEY, true));

    }

    public Getter getGetter(String key) {
        if (key == null) {
            return null;
        }
        return getterAndSetterMap.getGetter(key);
    }

    public Setter getSetter(String key) {
        if (key == null) {
            return null;
        }
        return getterAndSetterMap.getSetter(key);
    }

    public String getDescription(String key) {
        return getterAndSetterMap.getDescription(key);
    }

    public String getInfo(String key) {
        return getterAndSetterMap.getInfo(key);
    }

    public String[] getGetInfos() {
        return getterAndSetterMap.getGetInfos();
    }

    public String[] getSetInfos() {
        return getterAndSetterMap.getSetInfos();
    }

    public String[] getInfos() {
        return getterAndSetterMap.getInfos();
    }

    public String getInfosSeparatedByLineBreaks() {
        return getterAndSetterMap.getInfosSeparatedByLineBreaks();
    }

    public String getSetInfosSeparatedByLineBreaks() {
        return getterAndSetterMap.getSetInfosSeparatedByLineBreaks();
    }

    public String getGetInfosSeparatedByLineBreaks() {
        return getterAndSetterMap.getGetInfosSeparatedByLineBreaks();
    }

}
