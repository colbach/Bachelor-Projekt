package projectrunner.executionlogging;

import logging.AdditionalLogger;
import logging.AdditionalOut;
import logging.AdvancedLogger;
import projectrunner.debugger.Debugger;
import settings.FastAccessibleSettings;

public class ExecutionLoggerOut {

    private final AdvancedLogger debuggerExecutionLogger;

    protected ExecutionLoggerOut(AdvancedLogger debuggerExecutionLogger) {
        this.debuggerExecutionLogger = debuggerExecutionLogger;
    }

    public void println(String s) {
        
        AdditionalLogger.out.println(s);
        
        if (debuggerExecutionLogger != null) {
            debuggerExecutionLogger.outPrintln(s);
        }
    }
}
