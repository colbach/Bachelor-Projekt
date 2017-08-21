package utils.files;

import java.util.ArrayList;
import logging.AdditionalLogger;
import static utils.files.FileGrabber.*;
import utils.files.FileRule;

public class ClassGrabber {

    private static FileRule classRule = new FileRule() {
        @Override
        public boolean isValid(String filename, String path) {
            if (!filename.startsWith(".")) {
                if (filename.endsWith(".class")) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }
    };
    private static FileRule directoryRule = new FileRule() {
        @Override
        public boolean isValid(String filename, String path) {
            return true;
        }
    };

    public static String[] getListOfClassnamesInDirectory(String directory) {
        ArrayList<String> classnames = new ArrayList<>();
        for (String filename : getListOfFilenamesInDirectory(directory, classRule)) {
            classnames.add(filename.substring(0, filename.lastIndexOf(".")));
        }

        for (String sub : getListOfDirectorynamesInDirectory(directory, directoryRule)) { // Fuer Klassen in einem Paket
            String[] subclasses = getListOfClassnamesInDirectory(directory + "/" + sub);
            for (String subclass : subclasses) {
                classnames.add(sub + "." + subclass);
            }
        }

        return classnames.toArray(new String[0]);
    }
}
