package commandline;

import java.io.InputStream;

public class CommandLineThread extends Thread {

    private final CommandLinePrompt commandLinePrompt;

    public static synchronized CommandLinePrompt launchThread() {
        CommandLineThread newInstance = new CommandLineThread(new CommandLinePrompt());
        newInstance.start();
        return newInstance.commandLinePrompt;
    }

    public CommandLineThread(CommandLinePrompt commandLinePrompt) {
        this.commandLinePrompt = commandLinePrompt;
    }

    @Override
    public void run() {
        commandLinePrompt.runSystemPrompt();
    }
}
