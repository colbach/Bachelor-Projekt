package main.componenthub;

import commandline.CommandLinePrompt;
import commandline.CommandLineThread;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Project;
import view.main.MainWindow;
import view.onrun.OnRunWindowManager;

public class ComponentHub {

    private static ComponentHub instance;

    private Project project;
    private final Object projectLock;

    private MainWindow gui;
    private final Object guiLock;

    private CommandLinePrompt commandLine;
    private final Object commandLineLock;

    public static synchronized ComponentHub getInstance() {
        if (instance == null) {
            instance = new ComponentHub();
        }
        return instance;
    }

    private ComponentHub() {
        projectLock = new Object();
        guiLock = new Object();
        commandLineLock = new Object();
    }

    public Project getProjectBlocking() {
        synchronized (projectLock) {
            while (project == null) {
                try {
                    projectLock.wait();
                } catch (InterruptedException ex) {
                }
            }
            return project;
        }
    }

    public Project getProject() {
        synchronized (projectLock) {
            return project;
        }
    }

    public void setProject(Project project) {
        synchronized (projectLock) {
            this.project = project;
            projectLock.notifyAll();
        }
    }

    public MainWindow getGUIBlocking() {
        synchronized (guiLock) {
            while (gui == null) {
                try {
                    guiLock.wait();
                } catch (InterruptedException ex) {
                }
            }
            return gui;
        }
    }

    public MainWindow getGUI() {
        synchronized (guiLock) {
            return gui;
        }
    }

    public void setGUI(MainWindow mainWindow) {
        synchronized (guiLock) {
            this.gui = mainWindow;
            guiLock.notifyAll();
        }
    }

    public synchronized CommandLinePrompt getCommandLinePromptBlocking() {
        synchronized (commandLineLock) {
            while (commandLine == null) {
                try {
                    commandLineLock.wait();
                } catch (InterruptedException ex) {
                }
            }
            return commandLine;
        }
    }

    public synchronized CommandLinePrompt getCommandLinePrompt() {
        synchronized (commandLineLock) {
            return commandLine;
        }
    }

    public void launchCommandLine() {
        synchronized (commandLineLock) {
            if (commandLine == null) {
                setCommandLine(CommandLineThread.launchThread());
            }
        }
    }

    public void setCommandLine(CommandLinePrompt commandLine) {
        synchronized (commandLineLock) {
            this.commandLine = commandLine;
            commandLineLock.notifyAll();
        }
    }
}
