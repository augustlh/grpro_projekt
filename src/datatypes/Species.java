package datatypes;

/**
 * The Species enum represents various species of organisms.
 * Each species is characterized by a unique value and a diet mask
 * which defines what other species it can consume.
 */
public enum Species {
    /**
     * Represents no species or a null value.
     * Value: 1, Diet Mask: 1
     */
    None(1, 1),

    /**
     * Represents the grass species.
     * Value: 2, Diet Mask: 1
     */
    Grass(1 << 1, 1),

    /**
     * Represents the grass species.
     * Value: 2, Diet Mask: 1
     */
    BerryBush(1 << 2, 1),

    /**
     * Represents the rabbit species.
     * Value: 4, Diet Mask: 2
     */
    Rabbit(1 << 3, 1 << 1),

    //rabbit: id: 4, 0100,
    //wolf id: 8, 0100  & 0100

    /**
     * Represents the wolf species.
     * Value: 8, Diet Mask: 4
     */
    Wolf(1 << 4, 1 << 3),

    /**
     * Represents the bear species.
     * Value: 16, Diet Mask: 10 (4 | 2)
     */
    Bear(1 << 5, 1 << 4 | 1 << 3 | 1 << 2);

    private final int value;
    private final int dietMask;

    /**
     * Constructs a new Species enum constant with the specified value and diet mask.
     * @param value the numeric representation of the species.
     * @param dietMask the bit mask representing the diet of the species.
     */
    Species(int value, int dietMask) {
        this.value = value;
        this.dietMask = dietMask;
    }

    /**
     * Retrieves the numeric value associated with this species.
     * @return the numeric representation of the species.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Retrieves the dietary mask representing the species that this species can consume.
     * @return the dietary mask as an integer.
     */
    public int getDietMask() {
        return this.dietMask;
    }
}

