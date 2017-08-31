package reflection.customdatatypes.math;

import utils.text.CharacterRepeateHelper;

public abstract class Vector implements MathObject {

    public static final Vector NULL_VECTOR = new Vector() {
        @Override
        public int getRowCount() {
            return 0;
        }
        @Override
        public Number get(int row) {
            return Vector.DEFAULT_NUMBER;
        }
    };

    public static final Number DEFAULT_NUMBER = new Double(0);

    public abstract int getRowCount();

    public abstract Number get(int row);

    public Number[] toArray() {
        Number[] array = new Number[getRowCount()];
        for (int i = 0; i < array.length; i++) {
            array[i] = get(i);
        }
        return array;
    }

    public ArrayBasedVector getMutableCopy() {
        return new ArrayBasedVector(toArray());
    }

    @Override
    public String toString() {
        int height = getRowCount();
        String description = height + " Row Vector: ";
        StringBuilder sb = new StringBuilder(description);
        String whiteSpace = CharacterRepeateHelper.repeateSpaceCharacter(description.length());
        for (int i = 0; i < height; i++) {
            sb.append("\n[ " + i + "]: " + get(i) + ", ");
        }
        return sb.toString();
    }

}
