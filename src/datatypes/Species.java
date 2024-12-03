package datatypes;

/**
 * The {@link Species} enum represents different species with their unique values and dietary preferences.
 * Each species is associated with a specific value (used for categorization) and a diet mask (indicating what it can consume).
 * The diet mask uses bit masks to represent dietary options for each species.
 */
public enum Species {
    None(1, 1),
    Grass(1 << 1, 1),
    BerryBush(1 << 2, 1),
    Carcass(1 << 3, 1),
    Rabbit(1 << 4, 1 << 1),
    Wolf(1 << 5, 1 << 4 | 1 << 3),
    Bear(1 << 6, 1 << 4 | 1 << 5 | 1 << 3);

    private final int value;
    private final int dietMask;

    /**
     * Constructs a Species with a specified value and diet mask.
     *
     * @param value    a unique integer representing the species
     * @param dietMask a bitmask indicating the dietary preferences
     */
    Species(int value, int dietMask) {
        this.value = value;
        this.dietMask = dietMask;
    }

    /**
     * Returns the unique value representing this species.
     *
     * @return the integer value associated with this species
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Returns the dietary mask indicating what this species can consume.
     *
     * @return the dietary mask as an integer bitmask
     */
    public int getDietMask() {
        return this.dietMask;
    }
}

