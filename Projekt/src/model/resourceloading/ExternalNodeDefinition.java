package model.resourceloading;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import reflection.NodeDefinition;

/**
 * Diese Klasse kappselt eine Instanz von NodeDefinition um zusaetzliche Informationen
 * hinzuzufuegen welche es erlaubt Operationen auf die Class-Datei durchzufuehren.
 * Die Klasse implementiert das NodeDefinition-Interface und unterscheidet sich dadurch
 * von aussen nicht von der gekappselten NodeDefinition.
 */
public class ExternalNodeDefinition implements NodeDefinition {
    
    private final NodeDefinition nodeDefinition;
    private final String classDirectory;
    private final String className;
    private final File classFile;
    
    public ExternalNodeDefinition(NodeDefinition nodeDefinition, String classDirectory, String className) {
        this.nodeDefinition = nodeDefinition;
        this.classDirectory = classDirectory;
        this.className = className;
        String path = classDirectory + "/" + className.replace('.', '/') + ".class";
        this.classFile = new File(path);
        if(!classFile.exists()) {
            System.err.println(path + " existiert nicht!");
        }
    }
    
    // === Hinzugefuegte Methoden ===
    
    public void copyClassFileToDirectory(String newClassDirectory) throws IOException {
        File newClassDirectoryFile = new File(newClassDirectory);
        if(!newClassDirectoryFile.exists()) {
            System.err.println(newClassDirectory + " existiert nicht!");
            throw new IllegalArgumentException("Ungueltiger Pfad");
        } else {
            if(classFile.exists()) {
                String filePath = newClassDirectory + "/" + className.substring(0, className.lastIndexOf(".")).replace(".", "/");
                File destinationFolder = new File(filePath);
                destinationFolder.mkdirs();
                File destinationFile = new File(filePath + "/" + className.substring(className.lastIndexOf(".")+1) + ".class");
                if(!destinationFile.exists()) {
                    Files.copy(classFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } else {
                System.err.println(classFile + " existiert nicht!");
                throw new RuntimeException("Datei kann nicht gefunden werden");
            }
        }
    }
    
    public void deleteClassFile() {
        if(classFile.exists()) {
            classFile.delete();
        } else {
            System.err.println(classFile + " existiert nicht und kann somit auch nicht geloescht werden");
        }
    }
    
    // === Delegate-Methoden ===
    
    @Override
    public int getInletCount() {
        return nodeDefinition.getInletCount();
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        return nodeDefinition.getClassForInlet(inletIndex);
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        return nodeDefinition.getNameForInlet(inletIndex);
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        return nodeDefinition.isInletForArray(inletIndex);
    }

    @Override
    public int getOutletCount() {
        return nodeDefinition.getOutletCount();
    }

    @Override
    public Class getClassForOutlet(int outletIndex) {
        return nodeDefinition.getClassForOutlet(outletIndex);
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        return nodeDefinition.getNameForOutlet(outletIndex);
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return nodeDefinition.isOutletForArray(outletIndex);
    }

    @Override
    public String getName() {
        return nodeDefinition.getName();
    }

    @Override
    public void run(NodeDefinition.InOut io) {
        nodeDefinition.run(io);
    }

    @Override
    public int getVersion() {
        return nodeDefinition.getVersion();
    }

    @Override
    public String getUniqueName() {
        return nodeDefinition.getUniqueName();
    }

    @Override
    public String getDescription() {
        return nodeDefinition.getDescription();
    }

    @Override
    public String getIconName() {
        return nodeDefinition.getIconName();
    }
    
}
