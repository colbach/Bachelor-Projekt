package utils.files;

import java.io.File;
import java.util.*;
import logging.*;
import utils.structures.tuples.Pair;

public class FileGrabber {

    public static String[] getListOfFilenamesInDirectory(String directory, FileRule rule) {
        return getListOfFilenamesInDirectory(new File(directory), rule);
    }

    public static String[] getListOfFilenamesInDirectory(File directory, FileRule rule) {
        File[] listOfFiles = directory.listFiles();
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

    public static String[] getListOfDirectorynamesInDirectory(String directory, FileRule rule) {
        return getListOfDirectorynamesInDirectory(new File(directory), rule);
    }

    public static String[] getListOfDirectorynamesInDirectory(File directory, FileRule rule) {
        File[] listOfFiles = directory.listFiles();
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

    public static File[] getListOfFilesInDirectory(String directory, FileRule rule) {
        return getListOfFilesInDirectory(new File(directory), rule);
    }

    public static File[] getListOfFilesInDirectory(File directory, FileRule rule) {
        File[] listOfFiles = directory.listFiles();
        ArrayList<File> files = new ArrayList<>(listOfFiles.length);
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (rule.isValid(file.getName(), file.getPath())) {
                    files.add(file);
                }
            }
        }
        return files.toArray(new File[0]);
    }

    public static File[] getListOfDirectoriesInDirectory(String directory, FileRule rule) {
        return getListOfDirectoriesInDirectory(new File(directory), rule);
    }

    public static File[] getListOfDirectoriesInDirectory(File directory, FileRule rule) {
        File[] listOfFiles = directory.listFiles();
        ArrayList<File> directories = new ArrayList<>(listOfFiles.length);
        for (File file : listOfFiles) {
            if (file.isDirectory()) {
                if (rule.isValid(file.getName(), file.getPath())) {
                    directories.add(file);
                }
            }
        }
        return directories.toArray(new File[0]);
    }
    
    public static Pair<Set<File>, Set<File>> getListOfFilesAndSubdirectoriesInDirectory(File directory, FileRule rule, boolean applyFileRuleOnSubdirectories, boolean recursiv) {

        HashSet<File> foundFiles = new HashSet<>();
        HashSet<File> foundDirectories = new HashSet<>();

        File[] filesInThisDirectory = getListOfFilesInDirectory(directory, rule);
        foundFiles.addAll(Arrays.asList(filesInThisDirectory));

        File[] directoriesInThisDirectory = getListOfDirectoriesInDirectory(directory, applyFileRuleOnSubdirectories ? rule : new FileRule() {
            @Override
            public boolean isValid(String filename, String path) {
                return true;
            }
        });
        foundDirectories.addAll(Arrays.asList(directoriesInThisDirectory));

        if (recursiv) {
            for (File subdirectory : directoriesInThisDirectory) {
                Pair<Set<File>, Set<File>> listOfFilesAndSubdirectoriesInDirectory = getListOfFilesAndSubdirectoriesInDirectory(subdirectory, rule, applyFileRuleOnSubdirectories, true);
                foundFiles.addAll(listOfFilesAndSubdirectoriesInDirectory.l);
                foundDirectories.addAll(listOfFilesAndSubdirectoriesInDirectory.r);
            }
        }

        return new Pair<>(foundFiles, foundDirectories);
    }
}
