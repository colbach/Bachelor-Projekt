package commandline;

import java.io.InputStream;

public class CommandLineThread extends Thread {

    private static CommandLineThread instance;
    private CommandLinePrompt commandLinePrompt;

    public static synchronized CommandLineThread launchInstance() {
        if (instance == null || !instance.isAlive()) {
            instance = new CommandLineThread();
            instance.start();
            return instance;
        } else {
            System.out.println("CommandLineThread lauft bereits.");
            return instance;
        }
    }

    public static synchronized CommandLinePrompt getCommandLinePrompt() {
        if (instance != null) {
            return instance.commandLinePrompt;
        } else {
            return null;
        }
    }

    @Override
    public void run() {
        commandLinePrompt = new CommandLinePrompt();
        commandLinePrompt.runSystemPrompt();
    }
}
