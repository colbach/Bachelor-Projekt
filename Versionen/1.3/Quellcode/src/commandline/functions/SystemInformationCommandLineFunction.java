package commandline.functions;

import commandline.CommandLine;
import commandline.CommandLineFooterHeaderFormat;
import commandline.CommandLineFunction;
import java.io.File;
import java.util.Map;
import main.MainClass;
import static utils.text.CharacterRepeateHelper.*;
import view.assets.ImageAsset;

public class SystemInformationCommandLineFunction implements CommandLineFunction {

    @Override
    public void execute(String[] param, CommandLine commandLine) {

        if (param.length != 0) {
            System.err.println("Diese Funktion nimmt keine Argumente.");

        } else {
            
            System.out.println("JAVA");
            System.out.println("   JVM-Version: " + System.getProperty("java.version"));
            System.out.println("   JVM-Anbieter: " + System.getProperty("java.vendor"));
            System.out.println("   JVM-Path: " + System.getProperty("java.home"));
            
            System.out.println("");
            System.out.println("SYSTEM");
            System.out.println("   System-Architektur: " + System.getProperty("os.arch"));
            System.out.println("   System-Name: " + System.getProperty("os.name"));
            System.out.println("   System-Version: " + System.getProperty("os.version"));
            System.out.println("   Benutzername: " + System.getProperty("user.name"));
            System.out.println("   Arbeits-Pfad: " + System.getProperty("user.dir"));
            
            System.out.println("");
            System.out.println("HAUPTSPEICHER");
                    
            System.out.println("   Verfuegbare Prozessoren (Kerne): "
                    + Runtime.getRuntime().availableProcessors() + " (Moeglicherweise virtuell)");

            System.out.println("   Freier Speicher (Bytes): "
                    + Runtime.getRuntime().freeMemory() + " (~ " + (Runtime.getRuntime().freeMemory()/1024/1024) + " MB)");

            long maxMemory = Runtime.getRuntime().maxMemory();
            System.out.println("   Maximaler Speicher (Bytes): "
                    + (maxMemory == Long.MAX_VALUE ? "keine Begrenzung" : maxMemory + " (~ " + (maxMemory/1024/1024) + " MB)"));

            System.out.println("   Insgesammt verfuegbarer Speicher fuer JVM (Bytes): "
                    + Runtime.getRuntime().totalMemory() + " (~ " + (Runtime.getRuntime().totalMemory()/1024/1024) + " MB)");
            
            System.out.println("");
            System.out.println("DATEISYSTEM");

            File[] roots = File.listRoots();
            for (File root : roots) {
                System.out.println("   File system root: " + root.getAbsolutePath());
                System.out.println("   -> Insgesammt (bytes): " + root.getTotalSpace() + " (~ " + (root.getTotalSpace()/1024/1024) + " MB)");
                System.out.println("   -> davon frei (bytes): " + root.getFreeSpace() + " (~ " + (root.getFreeSpace()/1024/1024) + " MB)");
                System.out.println("   -> Verwendbar (bytes): " + root.getUsableSpace() + " (~ " + (root.getUsableSpace()/1024/1024) + " MB)");
            }
       }
    }

    @Override
    public String getDescription() {
        return "Gibt Systeminformationen aus.";
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }

    @Override
    public String getUsage() {
        return getName();
    }

    @Override
    public String getName() {
        return "systeminfo";
    }

    @Override
    public String getAliases() {
        return "system systeminformation vm vminfo vminformation java";
    }
}
