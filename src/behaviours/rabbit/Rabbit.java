package behaviours.rabbit;

import behaviours.nests.RabbitHole;
import datatypes.Animal;
import datatypes.Herbivore;
import datatypes.Species;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import help.Utils;

/**
 * The Rabbit class represents a rabbit that inherits from the Animal class.
 * It defines behaviors and properties specific to rabbits, such as burrowing, reproducing,
 * and acting during different times of the day.
 */
public class Rabbit extends Herbivore {
    private RabbitHole hole;
    private boolean insideHole;
    private boolean canBreed;

    /**
     * Handles rabbits constructed as specified by the input files
     * Constructs a Rabbit instance in the specified world at the given location.
     * By default, the rabbit is set to be able to breed and not be inside a hole.
     *
     * @param world The world in which the rabbit resides.
     * @param location The location within the world where the rabbit will be placed.
     */
    public Rabbit(World world, Location location) {
        super(Species.Rabbit, new Random().nextDouble(), new Random().nextDouble(1, 2), 1, 50.0);
        world.setTile(location, this);
        this.canBreed = true;
        this.insideHole = false;
        this.hole = null;
    }

    /**
     * Handles rabbits constructed as a result of reproduction.
     * Constructs a Rabbit instance in the specified world and assigns it to a given rabbit hole.
     *
     * @param world The world in which the rabbit resides.
     * @param hole The RabbitHole instance where the rabbit will reside.
     */
    public Rabbit(World world, RabbitHole hole) {
        super(Species.Rabbit, new Random().nextDouble(), new Random().nextDouble(0, 0.8), 1, 50.0);
        this.insideHole = true;
        this.hole = hole;
        this.canBreed = false;
        this.hole.addAnimal(this);
        world.add(this);
        //System.out.println("Breeding happened! :)");
    }

    /**
     * Defines the behavior of the rabbit during the day. The rabbit will exit a hole if it is inside one,
     * set a hole, eat, and/or seek nearby food, or wander randomly if no targets are found.
     *
     * @param world The world in which the rabbit resides.
     */
    protected void dayTimeBehaviour(World world) {
        if(this.insideHole) {
            exitHole(world);
            return;
        }

        setHole(world);
        eat(world);

        if(this.energy/2 > this.maxEnergy / 4 && this.age > 4 && !this.isInfected()) {
            this.canBreed = true;
        }

        // Seeks nearby food or wander randomly
        Location target = Utils.closestConsumableEntity(world,this, this.searchRadius);
        if(target == null && this.hole == null){
            target = Utils.getClosestRabbitHole(world, world.getLocation(this), this.searchRadius);
        }

        if (target == null) {
            wander(world);
        } else {
            pursue(world, target);
        }
    }

    /**
     * Defines the behavior of the rabbit during the night. If not already inside a hole,
     * the rabbit will try to find one or dig a new one.
     * If the rabbit is inside a hole, it will attempt to reproduce.
     *
     * @param world The world in which the rabbit resides.
     */
    protected void nightTimeBehaviour(World world) {
        // If in hole, reproduce
        if(insideHole) {
            reproduce(world);
            return;
        }

        if(this.hole == null) {
            setHole(world);


            if(new Random().nextDouble() < 0.3) {
                digHole(world, world.getLocation(this));
            } else {
                Location nearestHole = Utils.getClosestRabbitHole(world, world.getLocation(this), this.searchRadius);

                if(nearestHole != null) {
                    pursue(world, nearestHole);
                    return;
                }
            }

            if(this.hole == null) {
                wander(world);
            }

            return;
        }

        pursue(world, this.hole.getLocation());
        enterHole(world, world.getCurrentLocation());
    }

    /**
     * Associates the rabbit with a RabbitHole in the world.
     * If the rabbit does not already have a hole and
     * the rabbit will be added to the RabbitHole.
     *
     * @param world The world in which the rabbit resides.
     */
    private void setHole(World world) {
        if(this.hole == null && world.getNonBlocking(world.getLocation(this)) instanceof RabbitHole e) {
            this.hole = e;
            this.hole.addAnimal(this);
        }
    }

    /**
     * Performs reproduction for the rabbit if the conditions
     * for breeding are met, including being inside a hole and having the
     * breeding capability enabled.
     *
     * @param world The world in which the rabbit resides.
     */
    private void reproduce(World world) {
        if(!this.canBreed || !this.insideHole || new Random().nextDouble() < 0.4) {
            return;
        }

        List<Rabbit> possiblePartners = this.hole.getAnimals();
        possiblePartners.remove(this);

        if(possiblePartners.isEmpty()) {
            return;
        }

        for(Rabbit rabbit: possiblePartners) {
            if (rabbit.canBreed && rabbit.insideHole) {
                new Rabbit(world, this.hole);

                this.energy -= energy / 4;
                this.canBreed = false;

                rabbit.energy -= rabbit.energy / 4;
                rabbit.canBreed = false;

                break;
            }
        }
    }

    /**
     * Handles the event when the rabbit is consumed by another organism.
     * First calls the die method and removes the rabbit's association with the hole.
     * Afterward calls the super class {@link Animal Animal's} onConsume method.
     *
     * @param world the world in which the wolf resides.
     */
    @Override
    public void onConsume(World world) {
        this.die();
        if(this.hole != null) {
            this.hole.removeAnimal(this);
        }
        super.onConsume(world);
    }

    /**
     * Attempts to dig a hole at the specified location.
     * If the rabbit already has a hole or if the location contains a non-blocking entity,
     * the method will return without making any changes.
     *
     * @param world The world in which the rabbit resides.
     * @param location The location the hole is to be dug.
     */
    private void digHole(World world, Location location) {
        if(this.hole != null || world.containsNonBlocking(location)) {
            return;
        }
        this.hole = new RabbitHole(world, location);
        this.hole.addAnimal(this);
    }

    /**
     * Attempts to dig an exit at the specified location.
     * If the location contains a non-blocking entity or is null, the method will return without making any changes.
     *
     * @param world The world in which the rabbit resides.
     * @param location The location the hole is to be dug.
     */
    private void digExit(World world, Location location) {
        if(location == null || world.getTile(location) != null) {
            return;
        }
        this.hole = new RabbitHole(world, location, this.hole.getHoles());
    }

    /**
     * Enables the rabbit to enter its hole if it is present and the rabbit is currently
     * located at the hole's position. If these conditions are met, the rabbit is removed
     * from the world and its state is updated to indicate it is inside the hole.
     *
     * @param world The world in which the rabbit resides.
     * @param currentLocation The current location of the rabbit.
     */
    private void enterHole(World world, Location currentLocation) {
        if(this.hole == null) {
            return;
        }

        Location holeLocation = this.hole.getLocation();
        if (currentLocation.getX() != holeLocation.getX() || currentLocation.getY() != holeLocation.getY()) {
            return;
        }

        world.remove(this);
        this.insideHole = true;
    }

    /**
     * Handles the rabbit's attempt to exit its hole. If the exit location is invalid or
     * another blocking abject occupies the hole's location,
     * a new valid random location is determined and an exit is dug there.
     *
     * @param world The world in which the rabbit resides.
     */
    private void exitHole(World world){
        Location holeLocation = this.hole.getRandomExit(world);
        if(!(world.getTile(holeLocation) instanceof NonBlocking)) {
            Location newLocation = Utils.getValidRandomLocation(world);

            if(newLocation != null) {
                digExit(world, newLocation);
            }
            holeLocation = newLocation;
        }

        if(holeLocation != null) {
            world.setTile(holeLocation, this);
            insideHole = false;
        }
    }

    /**
     * Provides display information for the rabbit, including its color and image key.
     * Display information changes depending on the rabbit's age and whether
     * it's infested with a cordyceps.
     *
     * @return a DisplayInformation object representing the rabbit with specific color and image key.
     */
    @Override
    public DisplayInformation getInformation() {
        if(this.isInfected() && age > 6) {
            return new DisplayInformation(Color.WHITE, "mc-rabbit-large-infested");
        }
        if(this.isInfected() && age <= 6) {
            return new DisplayInformation(Color.WHITE, "mc-rabbit-small-infested");
        }
        if(!this.isInfested() && age > 6) {
            return new DisplayInformation(Color.WHITE, "mc-rabbit-large");
        }
        if(!this.isInfested() && age <= 6) {
            return new DisplayInformation(Color.WHITE, "mc-rabbit-small");
        }
        else {
            return new DisplayInformation(Color.WHITE, "mc-rabbit-large");
        }
    }

}