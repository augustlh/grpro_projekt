import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

import java.util.List;

enum FoodType {
    PLANT,
    ANIMAL,
}

interface Eatable {
    double eat();
    FoodType getType();

    //Eatable grass = new Grass();
    //energy += grass.eat();
}

abstract class Plant implements NonBlocking, Actor, Spreadable, Eatable, DynamicDisplayInformationProvider {
    protected List<Location> neighbours;
    protected double spreadProbability;
}

abstract class Animal implements Eatable, Actor, DynamicDisplayInformationProvider {
    protected int age;
    protected double energy;

    abstract void eatEatable();
    abstract void reproduce();
    abstract void die();

    // Methods for pathfinding or similarly
}

abstract class Herbivore extends Animal {
    //Can only eat plants
}

abstract class Carnivore extends Animal {
    //Can only eat meat
}