package reflection.common;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface NodeDefinition {

    public int getInletCount();

    public Class getClassForInlet(int inletIndex);

    public String getNameForInlet(int inletIndex);

    public boolean isInletForArray(int inletIndex);
    
    public boolean isInletEngaged(int inletIndex);

    public int getOutletCount();

    public Class getClassForOutlet(int outletIndex);

    public String getNameForOutlet(int outletIndex);

    public boolean isOutletForArray(int outletIndex);

    public static String TAG_PREAMBLE = "//tags//";

    /**
     * Dies ist der Name welcher dem Benutzer angezeigt wird.
     */
    public String getName();

    /**
     * Beschreibung welche dem Benutzer angezeigt wird oder null (default).
     * Unsichtbare Tags koennen hinter den String mit der Einleitung //tags//
     * (Konstante NodeDefinition.TAG_PREAMBLE) angefuegt werden.
     */
    public String getDescription();

    /**
     * Eindeutige Bezeichnung der NodeDefinition. Diese wird nie dem Benutzer
     * angezeigt sondern dient der internen Kennung.
     */
    public String getUniqueName();

    /**
     * Name der Icon-Datei oder null (default).
     */
    public String getIconName();

    /**
     * Versionsnummer der NodeDefinition. Diese Kennzeichnung ist von besonderer
     * Bedeutung wenn es Aenderungen an der Funktionalitaet der NodeDefinition
     * gibt oder wenn die Reihenfolde von In-/Outlets aendert.
     */
    public int getVersion();
    
    /**
     * Diese Methode wird beim ausfueren des Projektes ausgefuert. Am Ende der
     * Methode muessen alle Outlets gefuellt sein. io erfuellt immer mindestends
     * das io-Interface, jedoch falls es sich um einen ContextCreator handelt
     * auch noch die zusaetzlichen Methoden von ContextCreatorInOut auf welche
     * in diesen Faellen gecastet werden kann.
     */
    public void run(InOut io, API api) throws Exception;

}
