package model.runproject.executioncontrol;

public enum TerminationReason {
    FINISHED, // Erfolgreich ausgefuehrt
    CANCELED, // Durch den Benutzer abgebrochen
    RUNTIME_ERROR, // Laufzeit-Fehler
    START_FAILED, // Projekt konnte nicht gestartet werden
    UNKNOWN; // Unbekannter Grund
}
