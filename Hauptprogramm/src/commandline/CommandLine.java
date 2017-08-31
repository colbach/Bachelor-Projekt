package commandline;

import commandline.functions.analyse.RepairProjectCommandLineFunction;
import commandline.functions.getterandsetter.*;
import commandline.functions.tests.*;
import commandline.functions.*;
import commandline.functions.analyse.AnalyseProjectCommandLineFunction;
import commandline.functions.getterandsetter.*;
import commandline.functions.tests.*;
import java.util.*;
import logging.AdditionalLogger;
import projectrunner.ProjectExecutionRemote;

public class CommandLine {
    
    private final CommandLinePrompt commandLinePrompt;

    private final TreeMap<String, CommandLineFunction> functions;
    
    private final Aliases aliases;
    
    private final GetterAndSetterResource getterAndSetterResource;
    
    private ConfimableTask confimableTask;
    
    private ProjectExecutionRemote latestProjectExecutionRemote;

    public CommandLine(CommandLinePrompt commandLinePrompt) {
        this.functions = new TreeMap<>();
        this.commandLinePrompt = commandLinePrompt;
        this.aliases = new Aliases();
        this.getterAndSetterResource = new GetterAndSetterResource(this);

        // Funktionen hinzufuegen...
        addFunction(new AliasesCommandLineFunction());
        addFunction(new HelpCommandLineFunction());
        addFunction(new ExitCommandLineFunction());
        addFunction(new SystemInformationCommandLineFunction());
        addFunction(new GetCommandLineFunction());
        addFunction(new SetCommandLineFunction());
        addFunction(new SetGetCommandLineFunction());
        addFunction(new ResetGeneralSettingsCommandLineFunction());
        addFunction(new FileExistsCommandLineFunction());
        addFunction(new PWDCommandLineFunction());
        addFunction(new CancelPromptCommandLineFunction());
        addFunction(new PrintArgumentsCommandLineFunction());
        addFunction(new TestCommandLineFunction());
        addFunction(new RunCommandLineFunction());
        addFunction(new CancelCommandLineFunction());
        addFunction(new RepairProjectCommandLineFunction());
        addFunction(new RedrawGUIProjectCommandLineFunction());
        addFunction(new AnalyseProjectCommandLineFunction());
        addFunction(new DisposeOnRunWindowsCommandLineFunction());
        addFunction(new CountOnRunWindowsCommandLineFunction());
        addFunction(new GenerateNodeDefinitionLibraryMarkdownTableCommandLineFunction());
        addFunction(new GenerateNodeDefinitionLibraryLatexTableCommandLineFunction());
   
//        System.out.println("");
//        for (CommandLineFunction f : functions.values()) {
//            System.out.print("`" + f.getClass().getName() + "`");
//        }
//        System.out.println("");
    }

    private void addFunction(CommandLineFunction commandLineFunction) {
        String name = commandLineFunction.getName();
        if(functions.get(name) != null) {
            System.err.println("Achtung die Funktion " + name + " ist bereits angelegt!");
        }
        functions.put(name.toLowerCase(), commandLineFunction);
        if (commandLineFunction.getAliases() != null && commandLineFunction.getAliases().length()>0) {
            aliases.put(commandLineFunction.getAliases().split(" "), commandLineFunction.getName());
        }
    }

    public CommandLineFunction getFunction(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name darf nicht null sein!");
        } else {
            name = name.toLowerCase();
            CommandLineFunction get = functions.get(name);
            if (get != null) {
                return get;
            } else {
                String alias = aliases.get(name);
                if (alias == null) {
                    return null;
                } else {
                    return functions.get(alias);
                }
            }
        }
    }

    /**
     * Fuehrt Funktion mit gegebenen Parametern aus.
     *
     * @return true false Funktion gefunden wurde und false falls nicht.
     */
    public boolean executeFunction(String functionName, String[] parameter) {
        if (parameter == null) {
            parameter = new String[0];
        }
        if (functionName == null) {
            throw new IllegalArgumentException("functionName darf nicht null sein!");
        }
        CommandLineFunction function = getFunction(functionName);
        if (function == null) {
            return false;
        } else {
            function.execute(parameter, this);
            return true;
        }
    }

    public TreeMap<String, CommandLineFunction> getFunctions() {
        return functions;
    }

    public void answerForConfimableTask(String answer) {
        if (answer.equals("ja") || answer.equals("j") || answer.equals("yes") || answer.equals("y")) {
            System.out.println("Bestaetigt");
            confimableTask.confirmed();
            confimableTask = null;
        } else {
            System.out.println("Abbruch");
            confimableTask.canceled();
            confimableTask = null;
        }
    }

    public void success() {
        System.out.println("Erfolgreich durchgefuehrt");
    }

    public CommandLinePrompt getCommandLinePrompt() {
        return commandLinePrompt;
    }

    public Aliases getAliases() {
        return aliases;
    }

    public GetterAndSetterResource getGetterAndSetterResource() {
        return getterAndSetterResource;
    }
    
    public void putConfimableTask(ConfimableTask confimableTask) {
        if(confimableTask != null) {
            AdditionalLogger.err.println("Unbestaetigter Task wird verworfen und durch neuen ersetzt.");
        }
        System.out.println("Sind sie sicher? (ja/nein)");
        this.confimableTask = confimableTask;
    }
    
    public boolean taskNeedsConfirmation() {
        return confimableTask != null;
    }

    public ProjectExecutionRemote getLatestProjectExecutionRemote() {
        return latestProjectExecutionRemote;
    }

    public void setLatestProjectExecutionRemote(ProjectExecutionRemote latestProjectExecutionRemote) {
        this.latestProjectExecutionRemote = latestProjectExecutionRemote;
    }
    
}
