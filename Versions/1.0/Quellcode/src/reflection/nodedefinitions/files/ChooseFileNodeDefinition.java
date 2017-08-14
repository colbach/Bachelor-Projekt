package reflection.nodedefinitions.files;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import logging.AdditionalLogger;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;
import utils.images.ImageFileFilter;
import static utils.images.ImageLoading.loadImageFromFile;
import utils.structures.ReferentedValue;

public class ChooseFileNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 4;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return File.class;
            case 1:
                return Boolean.class;
            case 2:
                return Boolean.class;
            case 3:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Pfad";
            case 1:
                return "Mehere Dateien";
            case 2:
                return "Speicher Dialog";
            case 3:
                return "Titel";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:
                return false;
            case 3:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int index) {

        return false;
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return File.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Ausgewählt";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Datei auswählen Dialog";
    }

    @Override
    public String getDescription() {
        return "Öffnet Dateiauswahl-Dialog." + TAG_PREAMBLE + " [Dateien] Dialog File Datei auswählen speichern öffnen";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ChooseFile";
    }

    @Override
    public String getIconName() {
        return "File-Via-Dialog_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        final File pfad = (File) io.in0(0, new File("."));
        final Boolean meheredateien = (Boolean) io.in0(1, false);
        final Boolean speicherdialog = (Boolean) io.in0(2, false);
        final String titel = (String) io.in0(3, "");

        final JFileChooser chooser = new JFileChooser(".");
        chooser.setMultiSelectionEnabled(meheredateien);
        chooser.setDialogTitle(titel);

        final ReferentedValue<Integer> value = new ReferentedValue<>(-1);

        // Dialog auf SwingThread schieben...
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                if (speicherdialog) {
                    value.set(chooser.showSaveDialog(null));
                } else {
                    value.set(chooser.showOpenDialog(null));
                }
            }
        });

        io.terminatedTest();

        if (value.get() == JFileChooser.APPROVE_OPTION) { // Fall: Benutzer hat nicht abgebrochen

            File[] files;
            if ((boolean) meheredateien) {
                files = chooser.getSelectedFiles();
            } else {
                files = new File[]{
                    chooser.getSelectedFile()
                };
            }

            io.terminatedTest();

            io.out(0, files);

        } else { // Fall: Benutzer hat abgebrochen
            throw new Exception("Benutzer hat Dateiauswahldialog abgebrochen");
        }

    }

}
