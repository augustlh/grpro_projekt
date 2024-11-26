import itumulator.world.World;

/**
 * Represents an entity that can be consumed within a certain context.
 */
public interface Consumable {
    void onConsume(World world);
    double getNutritionalValue();
}
