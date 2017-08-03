package generalexceptions;

import logging.AdditionalErr;
import logging.AdditionalLogger;

public class UserDisplayableException extends Exception {

    private final String userMessage;

    public UserDisplayableException(String message, String userMessage) {
        super(message);
        if (userMessage == null) {
            String automessage = "Ein unbekannter Fehler ist aufgetreten (" + message + ")";
            AdditionalLogger.err.println("userMessage sollte nicht null sein da diese in der UI angezeigt wird! Automatische Ergaenzung: " + automessage);
            this.userMessage = automessage;
        } else {
            this.userMessage = userMessage;
        }
    }

    public String getUserMessage() {
        return userMessage;
    }
}
