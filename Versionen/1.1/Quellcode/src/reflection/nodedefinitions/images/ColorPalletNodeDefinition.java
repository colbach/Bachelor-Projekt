package reflection.nodedefinitions.images;

import reflection.common.InOut;
import reflection.common.API;
import reflection.common.NodeDefinition;
import java.awt.Color;

public class ColorPalletNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 0;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
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
        return 12;
    }

    @Override
    public Class getClassForOutlet(int index) {
        return Color.class;
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Schwarz";
            case 1:
                return "Blau";
            case 2:
                return "Cyan";
            case 3:
                return "Dunkel Grau";
            case 4:
                return "Grün";
            case 5:
                return "Hell Grau";
            case 6:
                return "Magenta";
            case 7:
                return "Orange";
            case 8:
                return "Pink";
            case 9:
                return "Rot";
            case 10:
                return "Weiß";
            case 11:
                return "Gelb";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
                return false;
    }

    @Override
    public String getName() {
        return "Farbpalette";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + "[Grafik]";
    }

    @Override
    public String getUniqueName() {
        return "buildin.ColorPallet";
    }

    @Override
    public String getIconName() {
        return "Color-Pallet_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {
        io.out(0, Color.BLACK);
        io.out(1, Color.BLUE);
        io.out(2, Color.CYAN);
        io.out(3, Color.DARK_GRAY);
        io.out(4, Color.GREEN);
        io.out(5, Color.LIGHT_GRAY);
        io.out(6, Color.MAGENTA);
        io.out(7, Color.ORANGE);
        io.out(8, Color.PINK);
        io.out(9, Color.RED);
        io.out(10, Color.WHITE);
        io.out(11, Color.RED);
    }

}
