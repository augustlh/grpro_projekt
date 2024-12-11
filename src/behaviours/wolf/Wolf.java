package behaviours.wolf;

import behaviours.nests.WolfCave;
import datatypes.Animal;
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
 * The Wolf class represents a wolf, extending the Carnivore class.
 * A wolf sticks to its designated wolf pack. Together they eat, hunt and wander,
 * with the alpha taking the lead and the other wolves in the pack following.
 */
public class Wolf extends Carnivore {
    private WolfType type;
    private final WolfPack pack;
    private final double strength;
    private boolean insideCave;
    private double sexChance = 0.2;

    /**
     * Handles wolves constructed by order of the input files.
     * Constructs a new Wolf in the specified world at the given location and
     * associates it with the provided wolf pack.
     * Calls super class to specify species, metabolism, energy decay, search radius and max energy,
     * which are defined in the {@link Animal Animal class}.
     *
     * @param world the world in which the wolf resides.
     * @param location the initial location of the wolf within the world.
     * @param pack the wolf pack to which this wolf belongs.
     */
    public Wolf(World world, Location location, WolfPack pack) {
        super(Species.Wolf, Utils.random.nextDouble(), new Random().nextDouble()*2.5, 2, 75.0);
        this.type = WolfType.Normal;
        this.pack = pack;
        this.strength = Utils.random.nextDouble();

        world.setTile(location, this);
    }

    /**
     * Handles wolves constructed as a result of reproduction.
     * Constructs a new Wolf in the specified world, associates it with the provided wolf pack,
     * and places it inside the cave.
     * Calls super class to specify species, metabolism, energy decay, search radius and max energy,
     * which are defined in the {@link Animal Animal class}.
     *
     * @param world the world in which the wolf resides.
     * @param pack the wolf pack to which this wolf belongs.
     */
    public Wolf(World world, WolfPack pack) {
        super(Species.Wolf, Utils.random.nextDouble(), new Random().nextDouble()*2.5, 2, 75.0);
        this.type = WolfType.Normal;
        this.pack = pack;
        this.strength = Utils.random.nextDouble();
        this.pack.addWolf(this);
        this.insideCave = true;
        world.add(this);
    }

    /**
     * Defines the behavior of a wolf during daytime. In the morning, the wolf exits its cave
     * and the pack it belongs to is updated. Furthermore, a cave is constructed if the wolf
     * does not yet belong to one. The wolf eats nearby animals and the method checks if the wolf can
     * breed depending on certain conditions. Alpha wolves will hunt for nearby food and the pack will follow.
     * If no food can be found, the pack wanders randomly.
     * The wolf will fight other nearby wolves that do not belong to its own pack.
     *
     * @param world the world in which the wolf resides.
     */
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

    /**
     * Toggles the type of the wolf between Alpha and Normal.
     * If the current type of the wolf is Alpha, it changes to Normal.
     * Conversely, if the current type is Normal, it changes to Alpha.
     */
    public void toggleAlpha() {
        if(this.type == WolfType.Alpha) {
            this.type = WolfType.Normal;
        } else {
            this.type = WolfType.Alpha;
        }
    }

    /**
     * Defines the behavior of a wolf during nighttime.
     * The wolf may reproduce if it's in a cave, attempt to build or enter a cave,
     * or pursue its pack's cave entrance.
     *
     * @param world the world in which the wolf resides.
     */
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

    /**
     * Facilitates the reproduction of a wolf if certain conditions are met.
     * If the wolf can breed and is inside a cave with its pack,
     * it attempts to find a breeding partner. When a partner is found,
     * a new wolf is created within the same world and pack. Energy is consumed
     * by both the wolf and its partner, and both wolves become temporarily unable to breed again.
     *
     * @param world the world in which the wolf resides.
     */
    private void reproduce(World world) {
        if(this.pack.getCave() == null || !this.canBreed() || !this.insideCave) {
            return;
        }

        List<Wolf> possiblePartners = this.pack.getCave().getAnimals();
        possiblePartners.remove(this);

        if(possiblePartners.isEmpty()) {
            return;
        }

        for(Wolf wolf: possiblePartners) {
            if (wolf.canBreed() && wolf.insideCave && Utils.random.nextDouble() < sexChance) {
                new Wolf(world, this.pack);

                this.energy/=3;

                wolf.energy/=3;
                break;
            }
        }
    }


    /**
     * Attempts to move the wolf into its pack's cave.
     * If the wolf's pack has a cave and the cave's entrance is adjacent
     * to the wolf's current location, the wolf is moved inside the cave.
     *
     * @param world the world in which the wolf resides
     * @param currentLocation the current location of the wolf within the world
     */
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

    /**
     * Attempts to move the wolf out of its pack's cave.
     * If there are empty tiles surrounding the cave entrance,
     * the wolf will occupy one of these tiles. The wolf's state is then updated
     * to indicate that it is no longer inside the cave.
     *
     * @param world the world in which the wolf resides.
     */
    private void exitCave(World world) {
        Location caveEntrance = this.pack.getCave().getEntrance();
        Set<Location> validTiles = world.getEmptySurroundingTiles(caveEntrance);

        if(!validTiles.isEmpty()) {
            world.setTile(validTiles.iterator().next(), this);
            this.insideCave = false;
        }

    }

    /**
     * Constructs a cave for the wolf's pack in the specified location within the world,
     * if the pack currently does not have a cave and the location is non-blocking.
     *
     * @param world the world in which the wolf resides.
     * @param location the location in the world where the cave will be built.
     */
    private void buildCave(World world, Location location) {
        if(this.pack.getCave() == null && !world.containsNonBlocking(location)) {
            this.pack.setCave(new WolfCave(world, location));
        }
    }

    /**
     * Generates the wolf's fighting strength based on its current energy
     * as a proportion of its maximum energy.
     *
     * @return the wolf's fighting strength
     */
    public double getStrength() {
        return this.strength * (this.energy/this.maxEnergy);
    }

    /**
     * Performs a fight between wolves from different packs located within the world.
     * The method iterates over all neighboring tiles to find potential enemy wolves.
     * When a wolf from a different pack is found, a fight ensues. The wolf with higher
     * fighting strength will kill the weaker one.
     *
     * @param world the world in which the wolf resides.
     */
    private void fight(World world) {
        List <Location> neighbours = new ArrayList<>(world.getSurroundingTiles(this.searchRadius));
        for(Location location : neighbours) {
            if(world.getTile(location) instanceof Wolf w && (w.pack != this.pack)) {
                if(this.strength > w.getStrength() || w.isInfected()) {
                    this.kill(world, w);
                } else {
                    w.kill(world, this);
                }
            }
        }
    }

    /**
     * Handles the event when the wolf is consumed by another organism.
     * First calls the die method and removes the wolf's cave's association with the wolf.
     * Afterward calls the super class {@link Animal Animal's} onConsume method.
     *
     * @param world the world in which the wolf resides.
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
     * Handles logic for when the wolf dies.
     * The wolf is responsible for telling its pack that it's dead.
     */
    @Override
    public void die() {
        super.die();
        this.pack.updatePack();
    }

    /**
     * Provides display information for the wolf, including its color and image key.
     * Display information changes depending on the wolf's age and whether
     * it's infested with a cordyceps.
     *
     * @return a DisplayInformation object representing the wolf with specific color and image key.
     */
    @Override
    public DisplayInformation getInformation() {
        if(this.isInfected() && age > 6) {
            return new DisplayInformation(Color.WHITE, "mc-wolf-large-infested");
        }
        if(this.isInfected() && age <= 6) {
            return new DisplayInformation(Color.WHITE, "mc-wolf-small-infested");
        }
        if(!this.isInfected() && age > 6) {
            return new DisplayInformation(Color.WHITE, "mc-wolf-large");
        }
        if(!this.isInfected() && age <= 6) {
            return new DisplayInformation(Color.WHITE, "mc-wolf-small");
        }
        else {
            return new DisplayInformation(Color.WHITE, "mc-wolf-large");
        }
    }

    public boolean canBreed() {
        return (this.energy/2 > this.maxEnergy / 8 && this.age > 4);

    }

    public void setSexChance(double sexChance) {
        this.sexChance = sexChance;
    }
}

