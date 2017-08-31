package reflection.nodedefinitions.system;

import java.awt.MouseInfo;
import java.awt.Point;
import javax.swing.JOptionPane;
import reflection.*;
import utils.view.ThreeSecondsDisplayCounterFrame;

public class DefineDisplayPositionNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return String.class;
            case 1:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Mittteilung";
            case 1:
                return "Mittteilung anzeigen";
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
        return 2;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Integer.class;
            case 1:
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "x";
            case 1:
                return "y";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            case 1:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Bildschirmpositon definieren";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + "";
    }

    @Override
    public String getUniqueName() {
        return "buildin.DefineDisplayPosition";
    }

    @Override
    public String getIconName() {
        return "Define-Mouse-Position_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        String mittteilung = (String) io.in0(0, "Bewegen sie den Mauszeiger auf eine Position welche sie definieren möchten.\nNachdem sie auf Ok geklickt haben, läuft ein Timer von 3 Sekunden.");
        Boolean mittteilunganzeigen = (Boolean) io.in0(1, Boolean.TRUE);

        JOptionPane.showMessageDialog(null,
                mittteilung,
                "Mauszeiger auf Position bewegen",
                JOptionPane.PLAIN_MESSAGE);
        
        ThreeSecondsDisplayCounterFrame.showCounter(true);
        
        Point location = MouseInfo.getPointerInfo().getLocation();

        Integer x = (int) location.getX();
        Integer y = (int) location.getY();

        io.out(0, x);
        io.out(1, y);
    }

}
