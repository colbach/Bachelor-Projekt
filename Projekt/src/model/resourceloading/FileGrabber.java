package model.resourceloading;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FileGrabber {
    
    private static String[] getListOfFilenamesInDirectory(String directory) {
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> filenames = new ArrayList<>(listOfFiles.length);
        for (File file : listOfFiles) {
            if(file.isFile()) {
                filenames.add(file.getName());
            }
        }
        return filenames.toArray(new String[0]);
    }
    
    private static String[] getListOfDirectorynamesInDirectory(String directory) {
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> directorynames = new ArrayList<>(listOfFiles.length);
        for (File file : listOfFiles) {
            if(file.isDirectory()) {
                directorynames.add(file.getName());
            }
        }
        return directorynames.toArray(new String[0]);
    }
    
    public static String[] getListOfClassnamesInDirectory(String directory) {
        ArrayList<String> classnames = new ArrayList<>();
        for(String filename : getListOfFilenamesInDirectory(directory)) {
            if(!filename.startsWith(".")) {
                if(filename.endsWith(".class")) {
                    classnames.add(filename.substring(0, filename.lastIndexOf(".")));
                } else {
                    System.err.println(filename + " ist keine Klasse!");
                }
            }
        }
        for(String sub : getListOfDirectorynamesInDirectory(directory)) { // Fuer Klassen in einem Paket
            String[] subclasses = getListOfClassnamesInDirectory(directory + "/" + sub);
            for(String subclass : subclasses) {
                classnames.add(sub + "." + subclass);
            }
        }
        return classnames.toArray(new String[0]);
    }
}
