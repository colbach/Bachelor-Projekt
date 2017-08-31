package projectrunner.debugger;

public enum ContextState {
    RUNNING_WITH_RUNNING_CLILDREN, // fuer CreatorContext
    RUNNING_WITHOUT_RUNNING_CLILDREN, // fuer CreatorContext
    FINISHED_WITH_RUNNING_CLILDREN, // fuer CreatorContext
    FINISHED_WITHOUT_RUNNING_CLILDREN, // fuer CreatorContext
    RUNNING, // fuer Context
    FINISHED, // fuer Context
    FAILED,
    UNKNOWN
}
