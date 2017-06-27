package utils.images;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import logging.AdditionalLogger;
import logging.AdditionalOut;

public class ImageSaving {

    public static void saveImageToFileViaDialogOnSwingThread(BufferedImage bufferedImage, Component component, String path) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                saveImageToFileViaDialog(bufferedImage, component, path);
            }
        });
    }

    public static void saveImageToFileViaDialog(BufferedImage bufferedImage, Component component, String path) {
        JFileChooser chooser = new JFileChooser(path);
        int rueckgabeWert = chooser.showSaveDialog(component);
        if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                if (file.getAbsolutePath().toLowerCase().endsWith(".png")) {

                } else if (file.getAbsolutePath().toLowerCase().endsWith(".jpg") || file.getAbsolutePath().toLowerCase().endsWith(".jpeg")) {

                } else {
                    String[] options = {"PNG", "JPG", "Abbrechen"};
                    int n = JOptionPane.showOptionDialog(
                            null,
                            "Dateiendung nicht unterstützt oder nicht angegeben. In welchem Format soll das Bild gespeichert werden?",
                            "Unbekanntes Format",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
                    if (n == 0 || n == 1) {
                        saveImageToFile(bufferedImage, options[n].toLowerCase(), file);
                    } else {
                        AdditionalLogger.out.println("Speichern abgebrochen");
                    }
                }
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
    
    public static void saveImageToFile(BufferedImage bufferedImage, String format, File file, boolean overwrite) throws IOException {

        if(overwrite || !file.exists()) { // Fall: ueberschreiben aktiviert oder Datei existiert noch nicht
            saveImageToFile(bufferedImage, format, file);
        }
    }

    public static void saveImageToFile(BufferedImage bufferedImage, String format, File file) throws IOException {

        AdditionalLogger.out.println("Schreibe: " + file.getAbsolutePath());
        ImageIO.write(bufferedImage, format, file);
    }

    public static void saveImageToFileAndAskOnOverwriteOnSwingThread(BufferedImage bufferedImage, String format, File file, Component component) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    saveImageToFileAndAskOnOverwrite(bufferedImage, format, file, component);
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

    public static void saveImageToFileAndAskOnOverwrite(BufferedImage bufferedImage, String format, File file, Component component) throws IOException {

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
                saveImageToFile(bufferedImage, format, file);
            } else {
                AdditionalLogger.out.println("Speichern abgebrochen");
            }
        }
    }
}
