package utils.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import logging.AdditionalErr;
import logging.AdditionalLogger;
import logging.AdditionalOut;
import reflection.NodeDefinition;
import utils.structures.ReferentedValue;
import utils.structures.tuples.Single;

public class ImageLoading {
    
    public static BufferedImage loadImageFromFileViaDialogOnSwingThread(String path) throws Exception {
        return loadImagesFromFileViaDialogOnSwingThread(path, false)[0];
    }
    
    public static BufferedImage[] loadImagesFromFileViaDialogOnSwingThread(String path, boolean multiple) throws Exception {
        
        ArrayList<BufferedImage> images = new ArrayList<>();

        final JFileChooser chooser = new JFileChooser(".");
        chooser.setMultiSelectionEnabled(multiple);
        chooser.setDialogTitle("Bild auswählen");
        chooser.setFileFilter(new ImageFileFilter());
        chooser.setAcceptAllFileFilterUsed(false);

        final ReferentedValue<Integer> value = new ReferentedValue<>(-1);
    
        // Dialog auf SwingThread schieben...
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                value.set(chooser.showOpenDialog(null));
            }
        });

        if (value.get() == JFileChooser.APPROVE_OPTION) { // Fall: Benutzer hat nicht abgebrochen

            File[] files;
            if ((boolean) multiple) {
                files = chooser.getSelectedFiles();
            } else {
                files = new File[] {
                    chooser.getSelectedFile()
                };
            }
            
            AdditionalLogger.out.println(files.length + " Dateien ausgewaehlt");
            for (File file : files) {
                AdditionalLogger.out.println(file.getAbsolutePath() + " laden");
                if (file.exists()) {
                    BufferedImage bi = loadImageFromFile(file);
                    images.add(bi);
                } else {
                    throw new Exception(file.getAbsolutePath() + " existiert nicht");
                }
            }
        } else { // Fall: Benutzer hat abgebrochen
            throw new Exception("Benutzer hat Dateiauswahldialog abgebrochen");
        }
        return images.toArray(new BufferedImage[0]);
    }
    
    public static BufferedImage loadImageFromFile(File file) throws Exception {
        return ImageIO.read(file);
    }

}
