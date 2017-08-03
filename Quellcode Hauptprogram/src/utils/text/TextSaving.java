package utils.text;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import logging.AdditionalLogger;

public class TextSaving {

    public static void saveTextToFileViaDialogOnSwingThread(String text, Component component, String path) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                saveTextToFileViaDialog(text, component, path);
            }
        });
    }

    public static void saveTextToFileViaDialog(String text, Component component, String path) {
        JFileChooser chooser = new JFileChooser(path);
        int rueckgabeWert = chooser.showSaveDialog(component);
        if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                saveTextToFile(text, file);
                
            } catch (Exception exception) {
                System.err.println(file + " konnte nicht gespeichert werden.");
                System.err.println(exception.getMessage());
                JOptionPane.showMessageDialog(
                        null,
                        "Fehler: " + exception.getMessage(),
                        "Fehler",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

    }

    public static void saveTextToFile(String text, File file, boolean overwrite) throws IOException {

        if (overwrite || !file.exists()) { // Fall: ueberschreiben aktiviert oder Datei existiert noch nicht
            saveTextToFile(text, file);
        }
    }

    public static void saveTextToFile(String text, File file) throws IOException {

        AdditionalLogger.out.println("Schreibe: " + file.getAbsolutePath());
        try (PrintWriter out = new PrintWriter(file)) {
            out.println(text);
        }
    }

    public static void saveTextToFileAndAskOnOverwriteOnSwingThread(String text, String format, File file, Component component) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    saveTextToFileAndAskOnOverwrite(text, file, component);
                } catch (IOException exception) {
                    System.err.println(file + " konnte nicht gespeichert werden.");
                    System.err.println(exception.getMessage());
                    JOptionPane.showMessageDialog(
                            null,
                            "Fehler: " + exception.getMessage(),
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    public static void saveTextToFileAndAskOnOverwrite(String text, File file, Component component) throws IOException {

        if (file.exists()) {
            Object[] options = {"Ja", "Nein"};
            int n = JOptionPane.showOptionDialog(
                    component,
                    "Die Datei " + file.getName() + " existiert bereits. Wollen sie diese Datei überschreiben?",
                    "Datei überschreiben",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (n == 0) {
                saveTextToFile(text, file);
            } else {
                AdditionalLogger.out.println("Speichern abgebrochen");
            }
        }
    }

}
