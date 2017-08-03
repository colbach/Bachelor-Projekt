package reflection.nodedefinitions.files;

import java.io.File;
import reflection.*;

public class MakeFolderNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return File.class;
            case 1:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Elternpfad";
            case 1:
                return "Name";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int index) {

        return false;
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return File.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Verzeichnisspfad";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Verzeichniss erstellen";
    }

    @Override
    public String getDescription() {
        return "Falls Name nicht angegeben ist wird nur der Elternpfad erstellt falls dieser nicht existiert." + TAG_PREAMBLE + "";
    }

    @Override
    public String getUniqueName() {
        return "buildin.MakeFolder";
    }

    @Override
    public String getIconName() {
        return "Make-Folder_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        File parent = (File) io.in0(0, new File("."));
        Object[] names = io.in(1, new Object[0]);

        File[] newFolders;

        if (names == null || names.length == 0) {
            newFolders = new File[]{parent};
        } else {
            newFolders = new File[names.length];
            for (int i = 0; i < names.length; i++) {
                newFolders[i] = new File(parent, (String) names[i]);
            }

        }

        for (File folder : newFolders) {
            folder.mkdirs();
        }

        io.out(0, newFolders);
    }

}
