package commandline.functions;

import commandline.CommandLine;
import commandline.CommandLineFooterHeaderFormat;
import commandline.CommandLineFunction;
import java.io.File;
import java.util.Map;
import main.MainClass;
import static utils.text.CharacterRepeateHelper.*;
import view.assets.ImageAsset;

public class PWDCommandLineFunction implements CommandLineFunction {

    @Override
    public void execute(String[] param, CommandLine commandLine) {

        if (param.length != 0) {
            System.err.println("Diese Funktion nimmt keine Argumente.");

        } else {
            System.out.println(new File("").getAbsolutePath());
            
        }
    }

    @Override
    public String getDescription() {
        return "Gibt Arbeits-Pfad aus.";
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
        return "pwd";
    }

    @Override
    public String getAliases() {
        return "printworkingdirectory";
    }
}
