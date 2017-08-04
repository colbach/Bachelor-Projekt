package model.type;

import model.LetConnectException;

public class WrongTypException extends LetConnectException {

    public WrongTypException() {
        super("Falscher Typ");
    }
    
}
