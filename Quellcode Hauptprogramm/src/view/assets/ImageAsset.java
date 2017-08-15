package view.assets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static main.MainClass.PATH_FOR_ASSETS;
import settings.GeneralSettings;
import main.startuptasks.ProgressIndicator;
import utils.files.FileGrabber;
import utils.files.FileRule;

/**
 * Diese Klasse representiert eine Bildresource.
 */
public class ImageAsset {

    private static final HashMap<String, ImageAsset> IMAGE_ASSETS = new HashMap<>();

    private final String assetName;
    private final BufferedImage image;
    private BufferedImage invertedImageOrNull;

    public static final String SWAP_SIDES_TO_WHITE = "ui" + File.separator + "Swap_Sides_To_White_24px.png";
    public static final String SWAP_SIDES_BACK_WHITE = "ui" + File.separator + "Swap_Sides_Back_White_24px.png";
    public static final String CLEAR = "ui" + File.separator + "Clear.png";
    
    public static final String START_ICON = "Start_30px.png";
    public static final String REPORT_ICON = "Receipt_30px.png";
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
    public static final String TWO_DISK_ICON = "2-Disks_30px.png";

    public static final HashSet<String> notExistent = new HashSet<>();

    /**
     * Konstruktor um ImageAsset zu erzeugen. Dieser Konstruktor ist privat da
     * die Methode getImageAssetForName(String) benutzt werden soll.
     *
     * @param assetName
     * @throws IOException
     */
    private ImageAsset(String assetName) throws IOException {
        
        assetName = assetName.replaceAll("/", File.separator);

        this.assetName = assetName;

        File file = new File(PATH_FOR_ASSETS + File.separator + assetName);

        // Test ob Bild existiert...
        if (!file.exists()) { // Bild existiert nicht

            // Dateiendungen testen...
            {
                File pngFile = new File(PATH_FOR_ASSETS + File.separator + assetName + ".png");
                File jpegFile = new File(PATH_FOR_ASSETS + File.separator + assetName + ".jpeg");
                File jpgFile = new File(PATH_FOR_ASSETS + File.separator + assetName + ".jpg");
                if (pngFile.exists()) {
                    file = pngFile;
                } else if (jpegFile.exists()) {
                    file = jpegFile;
                } else if (jpegFile.exists()) {
                    file = jpegFile;
                }
            }

            // Alternative Asset-Directory testen...
            String alternativeAssetsDirectory = GeneralSettings.getInstance().getString(GeneralSettings.ALTERNATIV_ASSET_DIRECTORY_KEY);
            if (alternativeAssetsDirectory != null) {
                if (!alternativeAssetsDirectory.endsWith(File.separator)) {
                    alternativeAssetsDirectory += File.separator;
                }
                file = new File(alternativeAssetsDirectory + assetName);
                if (!file.exists()) { // Bild existiert nicht
                    // Dateiendungen testen...
                    File pngFile = new File(alternativeAssetsDirectory + assetName + ".png");
                    File jpegFile = new File(alternativeAssetsDirectory + assetName + ".jpeg");
                    File jpgFile = new File(alternativeAssetsDirectory + assetName + ".jpg");
                    if (pngFile.exists()) {
                        file = pngFile;
                    } else if (jpegFile.exists()) {
                        file = jpegFile;
                    } else if (jpegFile.exists()) {
                        file = jpegFile;
                    }
                }
            }
        }

        if (!file.exists() && !notExistent.contains(assetName)) { // Warnung ausgeben
            System.err.println(file.getAbsolutePath() + " existiert nicht und wurde auch in keinem alternativem Verzeichniss gefunden!");
            notExistent.add(assetName);
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
    public void draw(Graphics g, int x, int y) {
        if (!assetName.equals(CLEAR)) {
            g.drawImage(image, x, y, null);
        }
    }

    /**
     * Zeichnet Bild zentriert.
     */
    public void drawCentered(Graphics g, int x, int y) {
        if (!assetName.equals(CLEAR)) {
            g.drawImage(image, x - image.getWidth() / 2, y - image.getHeight() / 2, null);
        }
    }

    public void drawCenteredInverted(Graphics g, int x, int y) {
        if (!assetName.equals(CLEAR)) {
            BufferedImage inverted = getInvertedImage();
            g.drawImage(inverted, x - inverted.getWidth() / 2, y - inverted.getHeight() / 2, null);
        }
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

    public static void preload(ProgressIndicator progressIndicator) {
        FileRule imageRule = new FileRule() {
            @Override
            public boolean isValid(String filename, String path) {
                if (filename.startsWith(".")) {
                    return false;
                } else if (path.toLowerCase().endsWith(".png") || path.toLowerCase().endsWith(".jpg") || path.toLowerCase().endsWith(".jpeg")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        if (progressIndicator != null) {
            progressIndicator.addProgress(0.00, "Asset-Verzeichnisse durchsuchen.");
        }
        List<String> listOfFilenamesInAssetPath = Arrays.asList(FileGrabber.getListOfFilenamesInDirectory(PATH_FOR_ASSETS, imageRule));
        String alternativeAssetsDirectory = GeneralSettings.getInstance().getString(GeneralSettings.ALTERNATIV_ASSET_DIRECTORY_KEY);
        if (alternativeAssetsDirectory != null && alternativeAssetsDirectory.length() > 0) {
            if (new File(alternativeAssetsDirectory).exists()) {
                listOfFilenamesInAssetPath.addAll(Arrays.asList(FileGrabber.getListOfFilenamesInDirectory(alternativeAssetsDirectory, imageRule)));
            } else {
                System.err.println("alternativeAssetsDirectory ist gesetzt, wird in Dateisystem aber nicht gefunden.");
            }
        }

        if (progressIndicator != null) {
            progressIndicator.addProgress(0.10, "Asset-Verzeichnisse durchsuchen.");
        }
        String[] listOfFilenamesInAssetPathForUI = FileGrabber.getListOfFilenamesInDirectory(PATH_FOR_ASSETS + File.separator + "ui", imageRule);
        if (progressIndicator != null) {
            progressIndicator.addProgress(0.10, "Grafiken laden.");
        }
        int total = listOfFilenamesInAssetPath.size() + listOfFilenamesInAssetPathForUI.length;
        double steps = 0.80 / total;

        int i = 0;
        int maxI = 1000;
        for (String assetName : listOfFilenamesInAssetPath) {
            if (i >= maxI) {
                i = Integer.MAX_VALUE;
                break;
            }
            if (progressIndicator != null) {
                progressIndicator.addProgress(steps, assetName + " laden.");
            }
            try {
                IMAGE_ASSETS.put(assetName, new ImageAsset(assetName));
            } catch (IOException ex) {
                System.err.println("Asset: " + assetName + " konnte nicht geladen werden!");
            }
            i++;
        }
        for (String assetName : listOfFilenamesInAssetPathForUI) {
            if (i >= maxI) {
                i = Integer.MAX_VALUE;
                break;
            }
            assetName = "ui/" + assetName;
            if (progressIndicator != null) {
                progressIndicator.addProgress(steps, assetName + " laden.");
            }
            try {
                IMAGE_ASSETS.put(assetName, new ImageAsset(assetName));
            } catch (IOException ex) {
                System.err.println("Asset: " + assetName + " konnte nicht geladen werden!");
            }
            i++;
        }
        if (i == Integer.MAX_VALUE) {
            System.err.println("Aus Speichergruenden wurden nur " + maxI + " Grafiken vorgeladen");
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public synchronized BufferedImage getInvertedImage() {

        if (invertedImageOrNull == null) {

            // Abmasse...
            int width = image.getWidth();
            int height = image.getHeight();

            // neues BufferedImage erstellen...
            invertedImageOrNull = new BufferedImage(width, height, image.getType());

            // Pixel kopieren...
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    int rgbValue = image.getRGB(x, y);
                    Color color = new Color(rgbValue);
                    int red = 255 - color.getRed();
                    int green = 255 - color.getGreen();
                    int blue = 255 - color.getBlue();
                    color = new Color(red, green, blue);
                    invertedImageOrNull.setRGB(x, y, color.getRGB());
                }
            }

            // Alpha-Raster kopieren...
            invertedImageOrNull.getAlphaRaster().setRect(image.getAlphaRaster());

        }
        return invertedImageOrNull;
    }

}
