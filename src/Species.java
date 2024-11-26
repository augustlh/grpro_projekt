/**
 * assa
 */
public enum Species {
    None(1, 1),
    Plant(1 << 1, 1),
    Rabbit(1 << 2, 1 << 1),
    Wolf(1 << 3, 1<< 2),
    Bear(1 << 4, 1 << 3 | 1 << 1);

    private final int value;
    private final int dietMask;

    Species(int value, int dietMask) {
        this.value = value;
        this.dietMask = dietMask;
    }

    public int getValue() {
        return this.value;
    }

    public int getDietMask() {
        return this.dietMask;
    }
}
