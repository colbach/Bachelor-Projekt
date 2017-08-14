package settings;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import logging.AdditionalLogger;
import logging.AdditionalOut;
import model.Project;
import model.resourceloading.projectserialization.CorruptedProjectException;
import settings.GeneralSettings;
import startuptasks.ProgressIndicator;

public class LastProjectMemory {

    public static void setLastProjectPath(String path) {
        GeneralSettings settings = GeneralSettings.getInstance();
        settings.set(GeneralSettings.LAST_PROJECT_PATH_KEY, path);
        try {
            settings.writeSettingsToSettingsFile();
        } catch (IOException e) {
            System.err.println("Konnte Settings nicht in Datei schreiben! (" + e.getMessage() + ")");
        }
    }

    public static String getLastProjectPath() {
        GeneralSettings settings = GeneralSettings.getInstance();
        String projectPath = settings.getString(GeneralSettings.LAST_PROJECT_PATH_KEY);
        return projectPath;
    }

    public static Project getLastProject(ProgressIndicator progressIndicator) {
        progressIndicator.addProgress(0.10, "Nach letztem Projekt-Pfad suchen.");
        String projectPath = getLastProjectPath();
        if (projectPath != null) {
            File projectFolder = new File(projectPath);
            if (projectFolder.exists() && projectFolder.isDirectory()) {
                try {
                    progressIndicator.addProgress(0.10, "Letztes Projekt laden.");
                    Project lastProject = Project.loadProject(projectFolder);
                    return lastProject;
                } catch (Exception e) {
                    AdditionalLogger.err.println("Zuletzt geoeffnetes Projekt konnte nicht geoefnet werden! (" + e.getMessage() + ")");
                }
            } else {
                AdditionalLogger.err.println("Zuletzt geoeffnetes Projekt existiert nicht mehr unter dem gespeicherten Pfad!");
            }
        }
        return null;
    }

    public static Project getLastOrNewProject(ProgressIndicator progressIndicator) {
        Project lastProject = getLastProject(progressIndicator);
        if (lastProject == null) {
            return new Project();
        } else {
            return lastProject;
        }
    }

}
