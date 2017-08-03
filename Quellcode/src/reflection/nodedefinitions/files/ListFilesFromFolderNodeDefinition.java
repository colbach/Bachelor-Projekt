package reflection.nodedefinitions.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import reflection.*;
import utils.files.FileGrabber;
import utils.files.FileRule;
import utils.structures.tuples.Pair;

public class ListFilesFromFolderNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 5;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return File.class;
            case 1:
                return Boolean.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Verzeichniss";
            case 1:
                return "Rekursiv";
            case 2:
                return "Prefix";
            case 3:
                return "Sufix";
            case 4:
                return "Pre-/Sufix nur bei Dateien";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        return false;
    }

    @Override
    public boolean isInletEngaged(int index) {
        if (index == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int getOutletCount() {
        return 2;
    }

    @Override
    public Class getClassForOutlet(int index) {
        return File.class;
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Dateien";
            case 1:
                return "Verzeichnisse";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        return true;
    }

    @Override
    public String getName() {
        return "Dateien aus Verzeichniss";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + "";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ListFilesFromFolder";
    }

    @Override
    public String getIconName() {
        return "Files-In-Folder_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        File verzeichniss = (File) io.in0(0, new File("."));
        Boolean rekursiv = (Boolean) io.in0(1, Boolean.TRUE);
        String prefix = (String) io.in0(2, null);
        String sufix = (String) io.in0(3, null);
        Boolean presufixFileOnly = (Boolean) io.in0(4, Boolean.FALSE);

        ArrayList<File> files = new ArrayList<>();

        Pair<Set<File>, Set<File>> listOfFilesAndSubdirectoriesInDirectory = FileGrabber.getListOfFilesAndSubdirectoriesInDirectory(verzeichniss, new FileRule() {
            @Override
            public boolean isValid(String filename, String path) {
                if(prefix != null) {
                    if(!filename.startsWith(prefix))return false;
                }
                if(sufix != null) {
                    if(!filename.endsWith(sufix))return false;
                }return true;
            }
        }, !presufixFileOnly, rekursiv);
                
        io.out(0, listOfFilesAndSubdirectoriesInDirectory.l.toArray(new File[0]));
        io.out(1, listOfFilesAndSubdirectoriesInDirectory.r.toArray(new File[0]));
    }

}
