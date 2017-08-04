package reflection.customdatatypes.math;

public class NumberMathObject implements MathObject {
    
    private final Number wrappedNumber;

    public NumberMathObject(Number wrappedNumber) {
        this.wrappedNumber = wrappedNumber;
    }

    public Number getWrappedNumber() {
        return wrappedNumber;
    }

    @Override
    public String toString() {
        return wrappedNumber.toString();
    }
    
}
