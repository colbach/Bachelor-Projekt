package view.main.saveandloadproject;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import logging.AdditionalLogger;
import logging.AdditionalOut;
import model.NoProjectLocationSetException;
import model.Project;
import org.apache.commons.io.FileUtils;
import settings.GeneralSettings;
import settings.LastProjectMemory;

public class ProjectSaver {

    public static void askToSave(JFrame frame, Project project) {
        askToSave(frame, "Möchten sie das Projekt speichern?", project);
    }

    public static void askToSave(JFrame frame, String message, Project project) {
        String[] yesNoOptions = {"Nicht speichern", "Speichern"};
        int n = JOptionPane.showOptionDialog(frame,
                message,
                "Speichern?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                yesNoOptions,
                yesNoOptions[1]);

        if (n == 1) {
            if (project.isProjectLocationSet()) {
                save(frame, project);
            } else {
                saveAs(frame, project);
            }
        }
    }

    public static void save(Component frame, Project project) {
        try {
            project.saveProject();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                    "Projekt konnte nicht gespeichert werden!\n(" + ex.getMessage() + ")",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void saveAs(JFrame frame, Project project) {

        // Speicherort anfragen...
        File file = showSaveProjectDialog(frame);
        if (file == null) { // Abgebrochen...
            AdditionalLogger.err.println("Speichern abbrechen");
            return;
        }

        // Datei/Verzeichniss existiert bereits...
        if (file.exists()) {

            // Ist Verzeichniss...
            if (file.isDirectory()) {
                Object[] options = {"Abbrechen", "Verzeichniss leeren", "Zusammenführen"};
                int n1 = JOptionPane.showOptionDialog(frame,
                        "Das Verzeichniss " + file.getName() + " existiert bereits.\nWie möchten sie fortfahren?",
                        "Uberschreiben?",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[2]);
                if (n1 == 2) { // Zusammenfuehren...
                    // Mach nichts...

                } else if (n1 == 1) {
                    // Verzeichniss leeren...
                    String[] yesNoOptions = {"Abbrechen", "Ja"};
                    int n2 = JOptionPane.showOptionDialog(frame,
                            "Sind sie sicher dass der Inhalt des Verzeichnis unwiederruflich gelöscht werden soll?\n" + file.getAbsolutePath(),
                            "Verzeichnis " + file.getName() + " leeren",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            yesNoOptions,
                            yesNoOptions[1]);
                    if (n2 == 1) {
                        try {
                            System.out.println(file.getAbsolutePath() + " wird geleert.");
                            FileUtils.cleanDirectory(file);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame,
                                    "Verzeichniss " + file.getName() + " kann nicht geleert werden!\n(" + ex.getMessage() + ")",
                                    "Fehler",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                } else { // Abgebrochen...
                    AdditionalLogger.err.println("Speichern abbrechen");
                    return;
                }
            }

            // Ist Datei...
            if (file.isFile()) {
                Object[] options = {"Datei löschen", "Abbrechen"};
                int n = JOptionPane.showOptionDialog(frame,
                        "Es existiert bereits eine Datei mit dem Namen " + file.getName() + " existiert bereits.\nWie möchten sie fortfahren?",
                        "Uberschreiben?",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[1]);
                if (n == 1) { // Zusammenfuehren...
                    // Mach nichts...

                } else if (n == 0) {
                    try {// Datei loeschen...
                        Files.delete(file.toPath());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame,
                                "Datei " + file.getName() + " kann nicht gelöscht werden!\n(" + ex.getMessage() + ")",
                                "Fehler",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                } else { // Abgebrochen...
                    AdditionalLogger.err.println("Speichern abbrechen");
                    return;
                }
            }

        }

        // Ordner erzeugen...
        file.mkdirs();

        // Projekt speichern...
        project.setProjectLocation(file);
        save(frame, project);

        // LastProjectMemory setzen...
        LastProjectMemory.setLastProjectPath(file.getAbsolutePath());

    }

    public static File showSaveProjectDialog(Component frame) {

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return true;
            }

            @Override
            public String getDescription() {
                return "Projekt-Ordner";
            }
        });
        fc.setAcceptAllFileFilterUsed(true);

        int returnVal = fc.showSaveDialog(frame);

        if (returnVal == JFileChooser.APPROVE_OPTION) { // Verzeichniss ausgewaeht
            File projectFolder = fc.getSelectedFile();
            AdditionalLogger.err.println("Speicherort von Benutzer angegeben: " + projectFolder.getAbsolutePath());
            return projectFolder;
        } else { // Vom Benutzer abgebrochen
            AdditionalLogger.err.println("Speicherortsangabe vom Benutzer abgebrochen");
            return null;
        }
    }

    private static boolean showOverwriteDialogIfNeeded(File file, Component frame) {
        if (file == null) {
            return false;
        }
        if (!file.exists()) {
            return true;
        } else {
            String phrase;
            if (file.isFile()) {
                phrase = "Die Datei " + file.getName() + " existiert bereits.\nSind sie sicher dass diese überschrieben werden soll?";
            } else {
                phrase = "Das Verzeichniss " + file.getName() + " existiert bereits.\nSind sie sicher dass dieses überschrieben werden soll?";
            }
            Object[] options = {"Überschreiben",
                "Abbrechen"};
            int n = JOptionPane.showOptionDialog(frame,
                    phrase,
                    "Uberschreiben?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[2]);
            return n == 0;
        }
    }
}
