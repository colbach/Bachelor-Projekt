package model.runproject.api;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import logging.AdditionalErr;
import logging.AdditionalLogger;
import logging.AdditionalOut;
import logging.AdvancedLogger;
import model.Project;
import model.runproject.executioncontrol.ExecutionControl;
import model.runproject.ProjectExecution;
import model.runproject.SharedObjectSpace;
import model.runproject.TemporarySmartIdentifierContextImplementation;
import model.runproject.executionlogging.ExecutionLogger;
import reflection.API;
import reflection.NodeDefinition;
import reflection.SmartIdentifier;
import reflection.SmartIdentifierContext;
import reflection.TerminatedException;
import utils.files.FileHandling;
import utils.images.ImageLoading;
import utils.images.ImageSaving;
import view.main.MainWindow;
import view.onrun.OnRunWindowManager;
import view.onrun.UnsupportedShowTypeException;

public class APIImplementation implements API {

    private final AdvancedLogger printer;
    private final ExecutionLogger executionLogger;
    private final OnRunWindowManager onRunWindowManager;
    private final Component viewOrNull;
    private final ExecutionControl executionControl;
    private final Project project;
    private final TemporarySmartIdentifierContextImplementation temporarySmartIdentifierContext;
    private final SharedObjectSpace sharedObjectSpace;

    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    private static SimpleDateFormat timeFormatWithDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public APIImplementation(ExecutionControl executionControl, AdvancedLogger printer, ExecutionLogger executionLogger, OnRunWindowManager onRunWindowManager, Component viewOrNull, Project project, TemporarySmartIdentifierContextImplementation temporarySmartIdentifierContextImplementation, SharedObjectSpace sharedObjectSpace) {
        this.printer = printer;
        this.onRunWindowManager = onRunWindowManager;
        this.viewOrNull = viewOrNull;
        this.executionControl = executionControl;
        this.executionLogger = executionLogger;
        this.project = project;
        this.temporarySmartIdentifierContext = temporarySmartIdentifierContextImplementation;
        this.sharedObjectSpace = sharedObjectSpace;
    }

    private boolean stillActive() {
        return executionControl.isRunning();
    }

    @Override
    public void displayContentInWindow(Object data, SmartIdentifier smartIdentifier) throws Exception {
        if (stillActive()) {
            try {
                onRunWindowManager.show(data, smartIdentifier, temporarySmartIdentifierContext);
            } catch (UnsupportedShowTypeException unsupportedShowTypeException) {
                throw new Exception(unsupportedShowTypeException.getMessage()); // (Workaround wegen Problemen bei weitergabe unbekannter Klassen ueber Reflektion)
            }
        } else {
            throw new TerminatedException();
        }
    }

    @Override
    public void disposeWindow(SmartIdentifier smartIdentifier) throws TerminatedException {
        if (stillActive()) {
            onRunWindowManager.dispose(smartIdentifier);
        } else {
            throw new TerminatedException();
        }
    }

    @Override
    public void disposeAllWindows(boolean otherRunContexts) throws TerminatedException {
        if (stillActive()) {
            if (otherRunContexts) {
                HashSet<OnRunWindowManager> instances = OnRunWindowManager.getInstances();
                for (OnRunWindowManager instance : instances) {
                    instance.disposeAll();
                }
            } else {
                onRunWindowManager.disposeAll();
            }
        } else {
            throw new TerminatedException();
        }
    }

    @Override
    public void printErr(Object o) {
        if (stillActive()) {
            executionLogger.err.println("API: Print err \"" + o + "\"");
            printer.errPrintln(o.toString());
            System.err.println(o);
        } else {
            printer.errPrintln("---" + o.toString() + "---");
            System.err.println("---" + o + "---");
        }
    }

    @Override
    public void printOut(Object o) {
        if (stillActive()) {
            executionLogger.err.println("API: Print out \"" + o + "\"");
            printer.outPrintln(o.toString());
            System.out.println(o);
        } else {
            printer.outPrintln("---" + o.toString() + "---");
            System.out.println("---" + o + "---");
        }
    }

    @Override
    public void additionalPrintErr(Object o) {
        if (stillActive()) {
            executionLogger.err.println("API: Print additional err \"" + o + "\"");
        } else {
            executionLogger.err.println("---API: Print additional err \"" + o + "\"---");
        }
    }

    @Override
    public void additionalPrintOut(Object o) {
        if (stillActive()) {
            executionLogger.out.println("API: Print additional out \"" + o + "\"");
        } else {
            executionLogger.out.println("---API: Print additional out \"" + o + "\"---");
        }
    }

    @Override
    public void cancelExecution(String reason, boolean error) {
        if (error) {
            executionControl.setFailed(reason);
        } else {
            executionControl.setFinished(reason);
        }
    }

    @Override
    public String getTimeStamp() throws TerminatedException {
        if (stillActive()) {
            return timeFormat.format(new Date());
        } else {
            throw new TerminatedException();
        }
    }

    @Override
    public String getTimeStampWithDate() throws TerminatedException {
        if (stillActive()) {
            return timeFormatWithDate.format(new Date());
        } else {
            throw new TerminatedException();
        }
    }

    @Override
    public SmartIdentifierContext getSmartIdentifierContext() throws TerminatedException {
        if (stillActive()) {
            return temporarySmartIdentifierContext;
        } else {
            throw new TerminatedException();
        }
    }

    @Override
    public void putSharedObject(String key, Object object) {
        sharedObjectSpace.putObject(key, object);
    }

    @Override
    public Object putSharedObjectIfNotAlreadyExists(String key, Object object) {
        return sharedObjectSpace.putIfNotAlreadyExistsObject(key, object);
    }

    @Override
    public void removeSharedObject(String key, Object object) {
        sharedObjectSpace.removeObject(key);
    }

    @Override
    public Object getSharedObject(String key) {
        return sharedObjectSpace.getObject(key);
    }

    @Override
    public Object getSharedObjectBlocking(String key) {
        return sharedObjectSpace.getObject(key);
    }
}
