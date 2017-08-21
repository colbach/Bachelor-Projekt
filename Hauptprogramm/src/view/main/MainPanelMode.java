package view.main;

public enum MainPanelMode {
    
    SHOW_OVERVIEW,                                            // Normalle Ansicht
    SHOW_OVERVIEW_AND_DRAG_CONNECTION_BETWEEN_NODES,          // Normalle Ansicht und Nodes werden miteinander verbunden
    SHOW_OVERVIEW_AND_DRAG_NODE,                              // Normalle Ansicht und Node wird verschoben
    SHOW_CONNECTION_OVERLAY,                                  // Connect-Overlay wird gezeigt
    SHOW_CONNECTION_OVERLAY_AND_DRAG_CONNECTION_BETWEEN_LETS, // Connect-Overlay wird gezeigt und Lets werden miteinander verbunden
    PROJECT_RUNNING,                                          // Projekt wird gerade ausgefuehrt
    DEBUGGER_RUNNING,                                         // Projekt wird ausgefuehrt und Debugger laeuft
    DEBUGGER_RUNNING_AND_SHOW_NODE                            // Projekt wird ausgefuehrt, Debugger laeuft und Overlay fuer Node wird angezeigt
    
}
