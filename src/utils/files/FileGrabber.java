package utils.files;

import java.io.File;
import java.util.*;
import logging.*;

public class FileGrabber {

    public static String[] getListOfFilenamesInDirectory(String directory, Rule rule) {
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> filenames = new ArrayList<>(listOfFiles.length);
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (rule.isValid(file.getName(), file.getPath())) {
                    filenames.add(file.getName());
                }
            }
        }
        return filenames.toArray(new String[0]);
    }

    public static String[] getListOfDirectorynamesInDirectory(String directory, Rule rule) {
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> directorynames = new ArrayList<>(listOfFiles.length);
        for (File file : listOfFiles) {
            if (file.isDirectory()) {
                if (rule.isValid(file.getName(), file.getPath())) {
                    directorynames.add(file.getName());
                }
            }
        }
        return directorynames.toArray(new String[0]);
    }
}
