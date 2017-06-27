package model.runproject.executionlogging;

import logging.AdvancedLogger;
import model.runproject.debugger.Debugger;

public class ExecutionLogger {

    public final ExecutionLoggerOut out;
    public final ExecutionLoggerErr err;

    public ExecutionLogger(Debugger debugger) {
        
        AdvancedLogger debuggerExecutionLogger;
        if (debugger != null) {
            debuggerExecutionLogger = debugger.getExecutionLogger();
        } else {
            debuggerExecutionLogger = null;
        }

        this.out = new ExecutionLoggerOut(debuggerExecutionLogger);
        this.err = new ExecutionLoggerErr(debuggerExecutionLogger);
    }
}
