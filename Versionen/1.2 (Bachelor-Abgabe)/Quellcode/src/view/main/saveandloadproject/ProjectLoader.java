package view.main.saveandloadproject;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import logging.AdditionalLogger;
import model.Project;
import model.resourceloading.projectserialization.CorruptedProjectException;
import model.resourceloading.projectserialization.ProjectStructureBuilder;
import settings.LastProjectMemory;

public class ProjectLoader {

    public static Project load(JFrame frame) {

        File projectFolder = showOpenProjectDialog(frame);
        if (projectFolder == null) {
            return null;
        }
        
        try {
            Project project = Project.loadProject(projectFolder);

            // LastProjectMemory setzen...
            LastProjectMemory.setLastProjectPath(projectFolder.getAbsolutePath());

            return project;

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                    "Projekt-Dateien " + projectFolder.getName() + " können nicht geladen werden!\n(" + ex.getMessage() + ")",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            return null;

        } catch (CorruptedProjectException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                    "Projekt-Struktur " + projectFolder.getName() + " scheint defekt zu sein!\n(" + ex.getMessage() + ")",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            return null;

        }
    }

    private static File projectFolderFor(File source) {
        if (source == null) {
            return null;
        } else {
            String path = source.getAbsolutePath();
            if (source.isDirectory()) {
                File structureFile = new File(path + "/" + Project.FILE_NAME_FOR_PROJECT_STRUCTURE);
                File settingsFile = new File(path + "/" + Project.FILE_NAME_FOR_PROJECT_SETTINGS);
                if (structureFile.exists() && settingsFile.exists()) {
                    return source.getAbsoluteFile();
                }
            }
            if (source.isFile()) {
                if (source.getName().equals(Project.FILE_NAME_FOR_PROJECT_STRUCTURE)) {
                    File parent = source.getParentFile();
                    return projectFolderFor(parent);
                } else if (source.getName().equals(Project.FILE_NAME_FOR_PROJECT_SETTINGS)) {
                    File parent = source.getParentFile();
                    return projectFolderFor(parent);
                }
            }
            return null;
        }
    }

    private static File showOpenProjectDialog(Component frame) {

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return projectFolderFor(file) != null;
            }

            @Override
            public String getDescription() {
                return "Projekt-Ordner";
            }
        });
        fc.setAcceptAllFileFilterUsed(true);

        int returnVal = fc.showOpenDialog(frame);

        if (returnVal == JFileChooser.APPROVE_OPTION) { // Verzeichniss ausgewaeht
            File projectFolder = projectFolderFor(fc.getSelectedFile());
            AdditionalLogger.err.println("Projekt vom Benutzer ausgewaehlt: " + projectFolder.getAbsolutePath());
            return projectFolder;
        } else { // Vom Benutzer abgebrochen
            AdditionalLogger.err.println("Projekt-Auswahl vom Benutzer abgebrochen");
            return null;
        }
    }

}
