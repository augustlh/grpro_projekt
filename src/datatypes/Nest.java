package datatypes;

import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;

abstract public class Nest<T extends Animal> implements NonBlocking, DynamicDisplayInformationProvider {
    protected List<T> animals;

    public Nest(World world, Location location) {
        world.setTile(location, this);
        this.animals = new ArrayList<>();
    }

    public void addAnimal(T animal) {
        this.animals.add(animal);
    }

    public void removeAnimal(T animal) {
        this.animals.remove(animal);
    }

    public List<T> getAnimals() {
        return this.animals;
    }
}
