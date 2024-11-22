import itumulator.world.World;

/**
 * The Eatable interface represents entities that can be eaten
 * in a simulation world. It provides methods to get the energy
 * content of the entity, check if it can eat another entity,
 * retrieve the type of the entity, and handle the entity being eaten.
 */
public interface Eatable {
    /**
     * The Type enum represents the different types of entities
     * that can exist in the simulation world, specifically distinguishing
     * between plants and animals.
     */
    enum Type {
        PLANT,
        ANIMAL,
    }

    /**
     * Retrieves the energy content of the entity.
     *
     * @return the energy value associated with the entity.
     */
    double getEnergy();
    /**
     * Determines if the current entity can eat another 'Eatable' entity.
     *
     * @param other the entity that is being checked if it can be eaten by the current entity.
     * @return true if the current entity can eat the other entity, otherwise false.
     */
    boolean canEat(Eatable other);
    /**
     * Retrieves the type of the entity.
     *
     * @return the type of the entity, which can be either PLANT or ANIMAL.
     */
    Type getType();
    /**
     * Handles the action to be taken when the entity is eaten within the simulation world.
     *
     * @param world the simulation world where the entity exists and the eating event occurs.
     */
    void eaten(World world);
}