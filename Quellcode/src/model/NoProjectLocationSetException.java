package model;

public class NoProjectLocationSetException extends Exception {

    public NoProjectLocationSetException() {
        super("Kein Projekt-Ordner definiert");
    }
    
}
