package projectrunner.executionlogging;

import logging.AdvancedLogger;
import projectrunner.debugger.Debugger;

public class ExecutionLogger {

    public final ExecutionLoggerOut out;
    public final ExecutionLoggerErr err;
    
    private AdvancedLogger debuggerExecutionLogger;

    public AdvancedLogger getDebuggerExecutionLogger() {
        return debuggerExecutionLogger;
    }
    
    public ExecutionLogger(Debugger debugger) {
        
        debuggerExecutionLogger = new AdvancedLogger(Debugger.DEBUGGER_STATE_LOGGER_SIZE);

        this.out = new ExecutionLoggerOut(debuggerExecutionLogger);
        this.err = new ExecutionLoggerErr(debuggerExecutionLogger);
    }
}
