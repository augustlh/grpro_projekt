package behaviours.wolf;

import behaviours.nests.WolfCave;
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
import java.util.Set;

/**
 * The Wolf class represents a wolf, extending the Animal class. A wolf can form part of a pack,
 * has an alpha status, and interacts with the world.
 */
public class Wolf extends Carnivore {
    protected WolfType type;
    protected WolfPack pack;
    protected double strength;
    protected boolean insideCave;
    protected boolean canBreed;


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

    public Wolf(World world, WolfPack pack) {
        super(Species.Wolf, Utils.random.nextDouble(), new Random().nextDouble(), 2, 75.0);
        this.type = WolfType.Normal;
        this.pack = pack;
        this.strength = Utils.random.nextDouble();
        this.pack.addWolf(this);
        this.insideCave = true;
        world.add(this);
        System.out.println("Wolf breeding occured!");
    }

    @Override
    protected void dayTimeBehaviour(World world) {
        if(this.insideCave) {
            exitCave(world);
            return;
        }

        this.pack.updatePack();

        if(this.pack.getCave() == null) {
            if(Utils.random.nextDouble() < 0.2) {
                buildCave(world, world.getLocation(this));
            }
        }

        eat(world);

        if(this.energy/2 > this.maxEnergy / 88 && this.age > 4) {
            this.canBreed = true;
        }

        if(this.type == WolfType.Alpha) {
            hunt(world);
        } else if(this.type == WolfType.Normal) {
            Wolf alpha = this.pack.getAlphaWolf();

            if(alpha != null && Utils.random.nextDouble() > 0.75 && !this.pack.getAlphaWolf().insideCave) {
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
        if(this.insideCave) {
            reproduce(world);
            return;
        }

        if(this.pack.getCave() == null) {
            if(Utils.random.nextDouble() < 0.4) {
                buildCave(world, world.getLocation(this));
                enterCave(world, world.getLocation(this));
                return;
            }
        }

        if(this.pack.getCave() != null) {
            if(world.getSurroundingTiles(world.getLocation(this)).contains(this.pack.getCave().getEntrance())) {
                enterCave(world, world.getLocation(this));
            } else {
                this.pursue(world, this.pack.getCave().getEntrance());
            }
        }
    }

    private void reproduce(World world) {
        if(this.pack.getCave() == null || !this.canBreed || !this.insideCave || new Random().nextDouble() < 0.4) {
            return;
        }

        List<Wolf> possiblePartners = this.pack.getCave().getAnimals();
        possiblePartners.remove(this);

        if(possiblePartners.isEmpty()) {
            return;
        }

        for(Wolf wolf: possiblePartners) {
            if (wolf.canBreed && wolf.insideCave) {
                new Wolf(world, this.pack);

                this.energy -= energy / 4;
                this.canBreed = false;

                wolf.energy -= wolf.energy / 4;
                wolf.canBreed = false;
                break;
            }
        }
    }


    private void enterCave(World world, Location currentLocation) {
        if(this.pack.getCave() == null) {
            return;
        }

        Location caveEntrance = this.pack.getCave().getEntrance();
        if (!world.getSurroundingTiles(currentLocation).contains(caveEntrance)) {
            return;
        }

        world.remove(this);
        this.insideCave = true;
    }

    private void exitCave(World world) {
        Location caveEntrance = this.pack.getCave().getEntrance();
        Set<Location> validTiles = world.getEmptySurroundingTiles(caveEntrance);

        if(!validTiles.isEmpty()) {
            world.setTile(validTiles.iterator().next(), this);
            this.insideCave = false;
        }

    }

    private void buildCave(World world, Location location) {
        if(this.pack.getCave() == null && !world.containsNonBlocking(location)) {
            this.pack.setCave(new WolfCave(world, location));
        }
    }

    public double getStrength() {
        return this.strength * (this.energy/this.maxEnergy);
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
        if(this.pack.getCave() != null) {
            this.pack.getCave().removeAnimal(this);
        }

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
     * Retrieves display information for the wolf, including its color and image key.
     *
     * @return a DisplayInformation object representing the rabbit with specific color and image key.
     */
    @Override
    public DisplayInformation getInformation() {
        if(this.isInfected()) {
            return new DisplayInformation(Color.WHITE, "mc-wolf-large-infested");
        }

        if(age > 6) {
            return new DisplayInformation(Color.WHITE, "mc-wolf-large");
        }
        return new DisplayInformation(Color.WHITE, "mc-wolf-small");
    }

}

