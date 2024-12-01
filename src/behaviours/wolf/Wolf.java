package behaviours.wolf;

import datatypes.Carnivore;
import datatypes.Species;
import help.Utils;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Wolf class represents a wolf, extending the Animal class. A wolf can form part of a pack,
 * has an alpha status, and interacts with the world.
 */
public class Wolf extends Carnivore {
    protected WolfType type;
    protected WolfPack pack;
    protected double strength;


    /**
     * Constructs a new Wolf object within the given world at the specified location and
     * associates it with the provided wolf pack. Initializes the wolf with a random size
     * and energy level and sets default values for its alpha status and pack.
     *
     * @param world The world in which the wolf resides.
     * @param location The initial location of the wolf within the world.
     * @param pack The wolf pack to which this wolf belongs.
     */
    public Wolf(World world, Location location, WolfPack pack) {
        super(Species.Wolf, Utils.random.nextDouble(), new Random().nextDouble(), 2, 75.0);
        this.type = WolfType.Normal;
        this.pack = pack;
        this.strength = Utils.random.nextDouble();

        world.setTile(location, this);
    }

    @Override
    protected void dayTimeBehaviour(World world) {
        this.pack.updatePack();

        eat(world);

        if(this.type == WolfType.Alpha) {
            hunt(world);
        } else if(this.type == WolfType.Normal) {
            Wolf alpha = this.pack.getAlphaWolf();

            if(alpha != null && Utils.random.nextDouble() > 0.75) {
                this.pursue(world, world.getLocation(this.pack.getAlphaWolf()));
            } else {
                wander(world);
            }
        }

        fight(world);

    }

    public void toggleAlpha() {
        if(this.type == WolfType.Alpha) {
            this.type = WolfType.Normal;
        } else {
            this.type = WolfType.Alpha;
        }
    }

    @Override
    protected void nightTimeBehaviour(World world) {
        this.pack.updatePack();

        if(this.pack.getCave() != null) {
            //this.pursue(this);
        }
    }

    //    private void buildCave(World world, Location location) {
//        if(this.wolfPack.getCave() == null) {
//            if (world.getNonBlocking(location) == null) {
//                if(new Random().nextDouble() < 0.1) {
//                    this.wolfPack.setCave(new Cave(world, location));
//
//                }
//            }
//        }
//    }

    public double getStrength() {
        return this.strength * (this.energy/this.maxEnergy);
    }

    public void kill(World world, Wolf other) {
       this.consume(other, world);
    }

    private void fight(World world) {
        List <Location> neighbours = new ArrayList<>(world.getSurroundingTiles(this.searchRadius));
        for(Location location : neighbours) {
            if(world.getTile(location) instanceof Wolf w && w.pack != this.pack) {
                System.out.println("Fight");
                if(this.strength > w.getStrength()) {
                    this.kill(world, w);
                } else {
                    w.kill(world, this);
                }
            }
        }
    }

    /**
     * Handles the event when the wolf is consumed by another organism.
     * This method will delete the current instance of the animal from the world.
     *
     * @param world the world in which the animal exists
     */
    @Override
    public void onConsume(World world) {
        this.die();
        super.onConsume(world);
    }

    /**
     * Handles logic for when the wolf dies. The wolf is responsible for telling its pack its dead.
     */
    @Override
    public void die() {
        super.die();
        this.pack.updatePack();

    }

    /**
     * Provides display information for the wolf object, including its color and the image key.
     *
     * @return a DisplayInformation object containing color as Color.GRAY and image key as "mc_wolf"
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.GRAY, "mc-wolf-large");
    }
}

