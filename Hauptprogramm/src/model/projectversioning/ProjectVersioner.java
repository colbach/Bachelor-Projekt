package model.projectversioning;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import logging.AdditionalLogger;
import model.Project;
import org.apache.commons.io.FileUtils;

public class ProjectVersioner {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");

    public static void saveProjectVersion(Project project) throws IOException {

        String folderPathForVersioning = project.getFolderForVersioning().getAbsolutePath();
        if (!folderPathForVersioning.endsWith(File.separator)) {
            folderPathForVersioning += File.separator;
        }
        File fileForStructure = project.getFileForProjectStructure();
        File fileForSettings = project.getFileForProjectSettings();
        String folderNameForVersion = project.getProjectVersion() + " (" + DATE_FORMAT.format(new Date()) + ")" + File.separator;
        String folderPathForVersion = folderPathForVersioning + folderNameForVersion;
        File folderForVersion = new File(folderPathForVersion);
        File fileForStructureCopy = new File(folderPathForVersion + Project.FILE_NAME_FOR_PROJECT_STRUCTURE);
        File fileForSettingsCopy = new File(folderPathForVersion + Project.FILE_NAME_FOR_PROJECT_SETTINGS);

        if (fileForStructure.exists()) {
            folderForVersion.mkdirs();
            FileUtils.copyFile(fileForStructure, fileForStructureCopy);
        } else {
            AdditionalLogger.err.println(fileForStructure.getAbsolutePath() + " existiert nicht!");
        }
        
        if (fileForSettings.exists()) {
            folderForVersion.mkdirs();
            FileUtils.copyFile(fileForSettings, fileForSettingsCopy);
        } else {
            AdditionalLogger.err.println(fileForSettings.getAbsolutePath() + " existiert nicht!");
        }
    }

}
