package projectrunner.executionlogging;

import logging.AdditionalLogger;
import logging.AdditionalOut;
import logging.AdvancedLogger;
import projectrunner.debugger.Debugger;

public class ExecutionLoggerErr {

    private final AdvancedLogger debuggerExecutionLogger;

    protected ExecutionLoggerErr(AdvancedLogger debuggerExecutionLogger) {
        this.debuggerExecutionLogger = debuggerExecutionLogger;
    }

    public void println(String s) {

        AdditionalLogger.err.println(s);

        if (debuggerExecutionLogger != null) {
            debuggerExecutionLogger.errPrintln(s);
        }
    }

}
