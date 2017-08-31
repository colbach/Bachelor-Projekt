package reflection.nodedefinitions.time;

import java.math.BigInteger;
import reflection.*;

public class TimeUnitConverterNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 7;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Long.class;
            case 1:
                return Double.class;
            case 2:
                return Double.class;
            case 3:
                return Double.class;
            case 4:
                return Double.class;
            case 5:
                return Double.class;
            case 6:
                return Double.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Nanosekunden";
            case 1:
                return "Millisekunden";
            case 2:
                return "Sekunden";
            case 3:
                return "Minuten";
            case 4:
                return "Stunden";
            case 5:
                return "Tage";
            case 6:
                return "Jahre";
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
            case 4:
                return false;
            case 5:
                return false;
            case 6:
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
        return 7;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Long.class;
            case 1:
                return Double.class;
            case 2:
                return Double.class;
            case 3:
                return Double.class;
            case 4:
                return Double.class;
            case 5:
                return Double.class;
            case 6:
                return Double.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Nanosekunden";
            case 1:
                return "Millisekunden";
            case 2:
                return "Sekunden";
            case 3:
                return "Minuten";
            case 4:
                return "Stunden";
            case 5:
                return "Tage";
            case 6:
                return "Jahre";
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
            case 2:
                return false;
            case 3:
                return false;
            case 4:
                return false;
            case 5:
                return false;
            case 6:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Zeiteinheiten umwandeln";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + "";
    }

    @Override
    public String getUniqueName() {
        return "buildin.TimeUnitConverter";
    }

    @Override
    public String getIconName() {
        return "Time-Unit-Convert_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        double sekunden = 0;

        sekunden += ((Number) io.in0(0, 0L)).longValue() / 1000000000D;
        sekunden += ((Number) io.in0(1, 0D)).doubleValue() / 1000D;
        sekunden += ((Number) io.in0(2, 0D)).doubleValue();
        sekunden += ((Number) io.in0(3, 0D)).doubleValue() * 60D;
        sekunden += ((Number) io.in0(4, 0D)).doubleValue() * 60D * 60D;
        sekunden += ((Number) io.in0(5, 0D)).doubleValue() * 60D * 60D * 24D;
        sekunden += ((Number) io.in0(6, 0D)).doubleValue() * 60D * 60D * 24D * 365D;

        io.out(0, new Long((long) (sekunden * 1000000000D)));
        io.out(1, new Double(sekunden * 1000D));
        io.out(2, new Double(sekunden));
        io.out(3, new Double(sekunden / 60D));
        io.out(4, new Double(sekunden / 60D / 60D));
        io.out(5, new Double(sekunden / 60D / 60D / 24D));
        io.out(6, new Double(sekunden / 60D / 60D / 24D / 356D));
    }

}
