package datatypes;

import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * The abstract class Nest represents a generic container for animals within a world,
 * at a specific location. It allows adding, removing, and retrieving
 * animals, and implements DynamicDisplayInformationProvider in order for its
 * subclasses to appear visually in the simulation.
 *
 * @param <T> The type parameter that extends the Animal class.
 */
abstract public class Nest<T extends Animal> implements NonBlocking, DynamicDisplayInformationProvider {
    protected List<T> animals;

    /**
     * Constructs a new Nest at the specified location within the given world.
     * The nest is initially empty and is associated with a specific location in the world.
     *
     * @param world The world in which the nest exists.
     * @param location The location within the world where the nest is to be created.
     */
    public Nest(World world, Location location) {
        world.setTile(location, this);
        this.animals = new ArrayList<>();
    }

    /**
     * Adds an animal to the nest.
     *
     * @param animal The animal to be added to the nest.
     */
    public void addAnimal(T animal) {
        this.animals.add(animal);
    }

    /**
     * Removes the specified animal from the nest.
     *
     * @param animal The animal to be removed from the nest.
     */
    public void removeAnimal(T animal) {
        this.animals.remove(animal);
    }

    /**
     * Retrieves the list of animals contained in the nest.
     *
     * @return a list of animals currently in the nest.
     */
    public List<T> getAnimals() {
        return this.animals;
    }
}
