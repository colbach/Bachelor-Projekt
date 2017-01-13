package view.assets;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static main.MainClass.PATH_FOR_ASSETS;

/**
 * Diese Klasse representiert eine Bildresource.
 */
public class ImageAsset {

    private static final HashMap<String, ImageAsset> IMAGE_ASSETS = new HashMap<>();

    private final String assetName;
    private final BufferedImage image;
    
    public static final String SWAP_SIDES_TO_WHITE = "ui/Swap_Sides_To_White_24px.png";
    public static final String SWAP_SIDES_BACK_WHITE = "ui/Swap_Sides_Back_White_24px.png";
    public static final String START_ICON = "Start_30px.png";
    public static final String REPORT = "Receipt_30px.png";
    public static final String DEBUG_ICON = "Debug_30px.png";
    public static final String STOP_ICON = "Stop_30px.png";
    public static final String CONTINUE_STEP_ICON = "Continue_Step_30px.png";
    public static final String CONTINUE_DEBUG_ICON = "Continue_Debug_30px.png";
    public static final String CONTINUE_ABORT_DEBUG_ICON = "Continue_Abort_Debug_30px.png";
    public static final String RECONTINUE_DEBUG_ICON = "Recontinue_Debug_30px.png";
    public static final String CLOSE_ICON = "Close_30px.png";
    public static final String BUG_ICON = "Bug_30px.png";
    public static final String DEBUG_WINDOW_ICON = "Debug-Window_30px.png";
    public static final String ON_ICON = "On_30px.png";
    public static final String OFF_ICON = "Off_30px.png";
    public static final String MANAGEMENT_ICON = "Management_30px.png";
    public static final String PROJECT_ICON = "Project_30px.png";
    public static final String MONITOR_ICON = "Monitor-3_30px.png";
    public static final String DISK_ICON = "Disk_30px.png";
    public static final String FOLDER_ICON = "Folder_30px.png";
    public static final String PAPER_ICON = "Paper_30px.png";
    public static final String PREFERENCES_ICON = "Preferences_30px.png";
    
    private static final String[] TO_PRELOAD = new String[] {
        SWAP_SIDES_TO_WHITE,
        SWAP_SIDES_BACK_WHITE,
        START_ICON,
        REPORT,
        STOP_ICON,
        CONTINUE_STEP_ICON,
        CONTINUE_DEBUG_ICON,
        CONTINUE_ABORT_DEBUG_ICON,
        RECONTINUE_DEBUG_ICON,
        CLOSE_ICON,
        BUG_ICON,
        DEBUG_WINDOW_ICON,
        ON_ICON,
        OFF_ICON,
        MANAGEMENT_ICON,
        PROJECT_ICON,
        MONITOR_ICON,
        DISK_ICON,
        FOLDER_ICON,
        PAPER_ICON,
        PREFERENCES_ICON
    };

    /**
     * Konstruktor um ImageAsset zu erzeugen. Dieser Konstruktor ist privat da
     * die Methode getImageAssetForName(String) benutzt werden soll.
     *
     * @param assetName
     * @throws IOException
     */
    private ImageAsset(String assetName) throws IOException {

        this.assetName = assetName;

        File file = new File(PATH_FOR_ASSETS + "/" + assetName);

        // Test ob Bild existiert...
        if (!file.exists()) { // Bild existiert nicht

            // Dateiendungen testen...
            File pngFile = new File(PATH_FOR_ASSETS + "/" + assetName + ".png");
            File jpegFile = new File(PATH_FOR_ASSETS + "/" + assetName + ".jpeg");
            File jpgFile = new File(PATH_FOR_ASSETS + "/" + assetName + ".jpg");
            if (pngFile.exists()) {
                file = pngFile;
            } else if (jpegFile.exists()) {
                file = jpegFile;
            } else if (jpegFile.exists()) {
                file = jpegFile;
            }
        }

        if (!file.exists()) { // Warnung ausgeben
            System.err.println(file.getAbsolutePath() + " existiert nicht!");
        }

        // Buffered Image in den Speicher laden...
        image = ImageIO.read(file);
    }

    /**
     * Gibt ImageAsset fuer Dateiname zurueck. Diese werden nur ein mal von der
     * Festplatte geladen und dann im Speicher gehalten.
     */
    public static ImageAsset getImageAssetForName(String assetName) throws IOException {

        // Nach bereits geladenem ImageAsset mit diesem Namen suchen... 
        ImageAsset asset = IMAGE_ASSETS.get(assetName);

        if (asset != null) { // Asset existiert bereits im Speicher
            return asset;
        } else { // Asset existiert noch nicht
            asset = new ImageAsset(assetName);
            IMAGE_ASSETS.put(assetName, asset);
            return asset;
        }
    }

    /**
     * Zeichnet Bild.
     */
    public void draw(Graphics2D g, int x, int y) {
        g.drawImage(image, x, y, null);
    }

    /**
     * Zeichnet Bild zentriert.
     */
    public void drawCentered(Graphics2D g, int x, int y) {
        g.drawImage(image, x - image.getWidth() / 2, y - image.getHeight() / 2, null);
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public String getName() {
        return assetName;
    }
    
    public static void preload() {
        for(String assetName : TO_PRELOAD) {
            try {
                IMAGE_ASSETS.put(assetName, new ImageAsset(assetName));
            } catch (IOException ex) {
                System.err.println("Asset: " + assetName + " konnte nicht geladen werden!");
            }
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}