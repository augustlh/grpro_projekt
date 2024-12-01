package datatypes;

import itumulator.world.World;

/**
 * By implementing the {@link Consumable} interface, a class defines the behavior for objects that can be consumed. These objects must
 * provide implementations for two key methods:
 */
public interface Consumable {
    /**
     * This method is invoked when the consumable object is consumed within a {@link World}.
     * This method should define the effects of being consumed, such as being deleted from the {@link World}.
     *
     * @param world
     */
    void onConsume(World world);

    /**
     * Returns the nutritional value of the consumable object, typically used
     * to determine the benefit of consuming the object, such as health or energy.
     *
     * @return the nutritional value of the consumable object
     */
    double getNutritionalValue();
}