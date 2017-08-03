package reflection.nodedefinitions.files;

import reflection.*;

public class DataUnitConverterNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 10;
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
            case 7:
                return Double.class;
            case 8:
                return Double.class;
            case 9:
                return Double.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Bit";
            case 1:
                return "Byte";
            case 2:
                return "Kilobyte ";
            case 3:
                return "Kibibyte ";
            case 4:
                return "Megabyte";
            case 5:
                return "Mebibyte";
            case 6:
                return "Gigabyte";
            case 7:
                return "Gibibyte";
            case 8:
                return "Terabyte";
            case 9:
                return "Tebibyte";
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
            case 7:
                return false;
            case 8:
                return false;
            case 9:
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
        return 10;
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
            case 7:
                return Double.class;
            case 8:
                return Double.class;
            case 9:
                return Double.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Bit";
            case 1:
                return "Byte";
            case 2:
                return "Kilobyte";
            case 3:
                return "Kibibyte";
            case 4:
                return "Megabyte";
            case 5:
                return "Mebibyte";
            case 6:
                return "Gigabyte";
            case 7:
                return "Gibibyte";
            case 8:
                return "Terabyte";
            case 9:
                return "Tebibyte";
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
            case 7:
                return false;
            case 8:
                return false;
            case 9:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Dateneinheiten umwandeln";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + "umwandeln convert";
    }

    @Override
    public String getUniqueName() {
        return "buildin.DataUnitConverter";
    }

    @Override
    public String getIconName() {
        return "Data-Unit-Convert_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        long bit = ((Number) io.in0(0, 0L)).longValue();

        double bytes = ((Number) io.in0(1, 0D)).doubleValue();
        bytes += bit / 8D;
        bytes += ((Number) io.in0(2, 0D)).doubleValue() * 1000D;
        bytes += ((Number) io.in0(3, 0D)).doubleValue() * 1024D;
        bytes += ((Number) io.in0(4, 0D)).doubleValue() * 1000D * 1000D;
        bytes += ((Number) io.in0(5, 0D)).doubleValue() * 1024D * 1024D;
        bytes += ((Number) io.in0(6, 0D)).doubleValue() * 1000D * 1000D * 1000D;
        bytes += ((Number) io.in0(7, 0D)).doubleValue() * 1024D * 1024D * 1024D;
        bytes += ((Number) io.in0(8, 0D)).doubleValue() * 1000D * 1000D * 1000D * 1000D;
        bytes += ((Number) io.in0(9, 0D)).doubleValue() * 1024D * 1024D * 1024D * 1024D;

        io.out(0, new Long((long) (bytes * 8D)));
        io.out(1, new Double(bytes));
        io.out(2, new Double(bytes / 1000D));
        io.out(3, new Double(bytes / 1024D));
        io.out(4, new Double(bytes / 1000D / 1000D));
        io.out(5, new Double(bytes / 1024D / 1024D));
        io.out(6, new Double(bytes / 1000D / 1000D / 1000D));
        io.out(7, new Double(bytes / 1024D / 1024D / 1024D));
        io.out(8, new Double(bytes / 1000D / 1000D / 1000D / 1000D));
        io.out(8, new Double(bytes / 1024D / 1024D / 1024D / 1024D));
    }

}
