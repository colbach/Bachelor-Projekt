package reflection.customdatatypes.math;

public class NumberMathObject extends Number implements MathObject {

    private final Number wrappedNumber;

    public NumberMathObject(Number wrappedNumber) {
        if (wrappedNumber == null) {
            throw new IllegalArgumentException("wrappedNumber darf nicht null sein!");
        }
        this.wrappedNumber = wrappedNumber;
    }

    public Number getWrappedNumber() {
        return wrappedNumber;
    }

    @Override
    public String toString() {
        return wrappedNumber.toString();
    }

    @Override
    public int intValue() {
        return wrappedNumber.intValue();
    }

    @Override
    public long longValue() {
        return wrappedNumber.longValue();
    }

    @Override
    public float floatValue() {
        return wrappedNumber.floatValue();
    }

    @Override
    public double doubleValue() {
        return wrappedNumber.doubleValue();
    }

    @Override
    public byte byteValue() {
        return wrappedNumber.byteValue();
    }

    @Override
    public short shortValue() {
        return wrappedNumber.shortValue();
    }

}
