package commandline;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Timer;
import logging.AdditionalLogger;
import logging.AdvancedLogger;
import settings.GeneralSettings;

public class CommandLinePrompt {

    private static final long NO_PROMPT_TIMEOUT = 200;
    private static final String PROMPT = "> ";

    private boolean promptCanceled = false;
    private Timer timer;
    private CommandLine commandLine;
    boolean executing = false;
    boolean interpreting = false;

    public CommandLinePrompt() {
        commandLine = new CommandLine(this);
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (promptCanceled) {
                    AdditionalLogger.err.println("promptCanceled, Prompt muss nicht mehr ausgegeben werden.");
                } else {
                    checkAndRePrintPromptIfNeeded();
                }
            }
        });
        timer.start();
    }

    private void checkAndRePrintPromptIfNeeded() {
        AdvancedLogger generalInstance = AdvancedLogger.getGeneralInstance();
        if (!executing && !interpreting && System.currentTimeMillis() - generalInstance.getLastOutputMillis() > NO_PROMPT_TIMEOUT) {
            String get = generalInstance.get(0);
            String getM1 = generalInstance.get(-1);
            if (get == null) {
                AdditionalLogger.err.println("generalInstance.get(0) ist null!");
            } else {
                if (!get.contains(PROMPT)) {
                    printPrompt();
                }
            }
        }
    }

    private void printPrompt() {
        if (!GeneralSettings.getInstance().getBoolean(GeneralSettings.INVISIBLE_PROMPT_KEY, false)) {
            System.out.print(PROMPT);
        } else {
            System.out.print("");
        }
    }

    public void cancelPrompt() {
        AdditionalLogger.out.println("Prompt anhalten");
        promptCanceled = true;
        if (timer != null) {
            timer.stop();
        }
    }

    public void executeLineOnOwnThread(String input) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                interpretLine(input);
            }
        }).start();
    }

    public synchronized void interpretLine(String input) {
        interpreting = true;
        if (commandLine.taskNeedsConfirmation()) {
            commandLine.answerForConfimableTask(input);
        } else {
            String[] args = new String[0];
            if (input.indexOf(" ") == -1) {
                args = new String[]{input};
            } else {
                ArrayList<String> argList = new ArrayList<String>();
                try {
                    Pattern pattern = Pattern.compile("(?i)((?:(['|\"]).+\\2)|(?:\\w+\\\\\\s\\w+)+|\\b(?=\\w)\\w+\\b(?!\\w))");
                    Matcher matcher = pattern.matcher(input);
                    while (matcher.find()) {
                        String match = matcher.group(0).replaceAll(input, "$1");
                        if (match.startsWith("\"") && match.endsWith("\"") && match.length() >= 2) {
                            match = match.substring(1, match.length() - 1);
                        }
                        argList.add(match);
                    }
                    args = argList.toArray(new String[0]);
                } catch (Exception e) {
                    System.err.println("Eingabe nicht interpretierbar. (" + e.getMessage() + ")");
                }
            }
            execute(args);
        }
        printPrompt();
        interpreting = false;
    }

    public synchronized void execute(String[] args) {
        executing = true;
        if (args.length == 1) {
            try {
                boolean found = commandLine.executeFunction(args[0], null);
                if (!found) {
                    System.out.println("Funktion nicht gefunden.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (args.length > 1) {
            String[] param = new String[args.length - 1];
            System.arraycopy(args, 1, param, 0, args.length - 1);
            try {
                boolean found = commandLine.executeFunction(args[0], param);
                if (!found) {
                    System.out.println("Funktion nicht gefunden.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executing = false;
    }

    public void runSystemPrompt() {
        Scanner s = new Scanner(System.in);

        System.out.println("help eingeben um Hilf-Seite auszugeben.");
        printPrompt();

        while (true) {
            try {
                if (promptCanceled) {
                    return;
                }

                // Input einlesen...
                String input = null;
                try {
                    input = s.nextLine();
                } catch (NoSuchElementException e) {
                    AdditionalLogger.err.println("Kein Systeminput verfuegbar. Beende SystemPrompt.");
                    return;
                }

                // Input in RingStack loggen...
                AdvancedLogger.getGeneralInstance().logToRingStack(input + "\n", false);

                // Zeile ausfuehren...
                interpretLine(input);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
