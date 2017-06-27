package reflection.nodedefinitions.show;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;
import reflection.SmartIdentifier;

public class ShowHistogramWindowNodeDefinition implements NodeDefinition {
    
    @Override
    public int getInletCount() {
        return 2;
    }
    
    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Number.class;
            case 1:
                return SmartIdentifier.class;
            default:
                return null;
        }
    }
    
    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Werte";
            case 1:
                return "Id";
            default:
                return null;
        }
    }
    
    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            case 1:
                return false;
            default:
                return false;
        }
    }@Override
    public boolean isInletEngaged(int i) {
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
                return SmartIdentifier.class;
            default:
                return null;
        }
    }
    
    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Id";
            default:
                return null;
        }
    }
    
    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            default:
                return false;
        }
    }
    
    @Override
    public String getName() {
        return "Histogram anzeigen";
    }
    
    @Override
    public String getDescription() {
        return "Zeigt Werte-Array als Histogram in Fenster an. Die Angabe eines Kontextes gibt an ob jedes mal ein neues Fenster geöffnet werden soll oder ob die neuen Werte die alten ersetzen soll. Der Kontext \"test\" öffnet ein neues Fenster falls noch keines mit dieser Bezeichnung angelegt ist oder ersetzt dieses falls dieser Kontext bereits geöffnet ist." + TAG_PREAMBLE + " [UI] [Arrays] Histogram Zahlen anzeigen Fenster";
    }
    
    @Override
    public String getUniqueName() {
        return "buildin.ShowHistogramWindow";
    }
    
    @Override
    public String getIconName() {
        return "Show-Histogram_30px.png";
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public void run(InOut io, API api) throws Exception {
        
        Object[] values = io.in(0, new Object[0]);
        SmartIdentifier smartIdentifier = (SmartIdentifier) io.in0(1);
        if(smartIdentifier == null) {
            smartIdentifier = api.getSmartIdentifierContext().createNew();
        }
        
        if (values != null) {
            Number[] numbers = new Number[values.length];
            for(int i=0; i<numbers.length; i++) {
                numbers[i] = (Number) values[i];
            }
            try {
                api.displayContentInWindow(numbers, smartIdentifier);
            } catch (Exception ex) {
                Logger.getLogger(ShowHistogramWindowNodeDefinition.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            api.printErr("Bild ist null. Es wird nichts angezeigt.");
        }
        
        io.out(0, smartIdentifier);
        
    }
    
}
