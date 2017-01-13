package reflection;

public interface NodeDefinition {

    public interface InOut {

        public Object[] in(int i);

        public Object in0(int i);

        public Object[] in(int i, Object[] def);

        public Object in0(int i, Object def);

        public void out(int i, Object ausgabe);

        public void out(int i, Object[] ausgabe);

        public boolean outConnected(int i);

        public boolean canceled();
    }

    public int getInletCount();

    public Class getClassForInlet(int inletIndex);

    public String getNameForInlet(int inletIndex);

    public boolean isInletForArray(int inletIndex);

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
     * angefuegt werden.
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
     * Methode muessen alle Outlets gefuellt sein.
     */
    public void run(InOut io);

}
