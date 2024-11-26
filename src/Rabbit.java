import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;
import java.util.List;
import java.util.Random;

/**
 * The Rabbit class represents a rabbit that inherits from the Animal class.
 * It defines behaviors and properties specific to rabbits, such as burrowing, reproducing, and acting during different times of the day.
 */
public class Rabbit extends Animal {
    private RabbitHole hole;
    private boolean insideHole;
    private boolean canBreed;

    /**
     * Constructs a Rabbit instance in the specified world at the given location.
     *
     * @param world The world in which the rabbit is to be placed.
     * @param location The location within the world where the rabbit will be placed.
     */
    public Rabbit(World world, Location location) {
        super(Species.Rabbit, new Random().nextDouble(), new Random().nextDouble(0, 0.8), 1);
        world.setTile(location, this);
        this.canBreed = true;
        this.insideHole = false;
        this.hole = null;
    }

    /**
     * Constructs a Rabbit instance in the specified world and assigns it to a given rabbit hole.
     *
     * @param world The world in which the rabbit is to be placed.
     * @param hole The RabbitHole instance where the rabbit will reside.
     */
    public Rabbit(World world, RabbitHole hole) {
        super(Species.Rabbit, new Random().nextDouble(), new Random().nextDouble(0, 0.8), 1);
        this.insideHole = true;
        this.hole = hole;
        this.canBreed = false;
        this.hole.addRabbit(this);
        world.add(this);
        System.out.println("sex happened");
    }

    /**
     * Executes the actions of the rabbit within the given world, including aging,
     * and performing appropriate behavior based on the time of day.
     *
     * @param world The world in which the rabbit behaves.
     */
    @Override
    public void act(World world) {
        age(world);
        if(isDead) {
            return;
        }

        if(world.isDay()) {
            dayTimeBehaviour(world);
        }

        if(world.isNight()) {
            nightTimeBehaviour(world);
        }
    }

    /**
     * Increases the rabbit's age and decreases its energy by a specific decay factor.
     * If the rabbit's energy falls to 0 or below, or its age reaches 100, the rabbit dies.
     *
     * @param world The world in which the rabbit exists.
     */
    private void age(World world) {
        this.age++;
        this.energy -= this.energyDecay * 0.2;

        if(energy <= 0 || age >= 100) {
            die(world);
        }
    }

    /**
     * Defines the behavior of the rabbit during the night. The rabbit engages in different activities
     * depending on its current state, such as reproducing if it is inside a hole, or looking for and
     * digging a hole if it is not.
     *
     * @param world The world in which the rabbit exists and performs its night time behavior.
     */
    private void nightTimeBehaviour(World world) {
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
     * Defines the behavior of the rabbit during the day. The rabbit will engage in different
     * activities such as exiting a hole if it is inside one, setting a hole, eating, seeking
     * nearby food, or wandering randomly if no targets are found.
     *
     * @param world The world in which the rabbit exists and performs its daytime behavior.
     */
    private void dayTimeBehaviour(World world) {
        if(this.insideHole) {
            exitHole(world);
            return;
        }

        setHole(world);
        eat(world);

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
     * Sets the rabbit's hole if it does not already have one and if the current
     * location in the*/
    private void setHole(World world) {
        if(this.hole == null && world.getNonBlocking(world.getLocation(this)) instanceof RabbitHole e) {
            this.hole = e;
            this.hole.addRabbit(this);
        }
    }

    /**
     * Determines whether the rabbit is standing on an edible organism and consumes it if possible.
     *
     * @param world The world in which the rabbit resides and interacts with the environment.
     */
    private void eat(World world) {
        // If stands on eatable organism, eat it
        if (world.getNonBlocking(world.getLocation(this)) instanceof Organism o) {
            if(canEat(o)) {
                this.consume(o, world);
            }
        }
    }

    /**
     * Facilitates the reproduction process for the rabbit if the conditions
     * for breeding are met, including being inside a hole and having the
     * breeding capability enabled.
     *
     * @param world The world in which the rabbit resides and attempts to reproduce.
     */
    private void reproduce(World world) {
        if(!this.canBreed || !this.insideHole || new Random().nextDouble() > 1) {
            return;
        }

        List<Rabbit> possiblePartners = this.hole.getRabbits();
        possiblePartners.remove(this);
        System.out.println(possiblePartners);

        if(possiblePartners.isEmpty()) {
            return;
        }

        for(Rabbit rabbit: possiblePartners) {
            if (rabbit.canBreed && rabbit.insideHole) {
                System.out.println("Sex should happen");
                new Rabbit(world, this.hole);

                this.energy /= 2;
                this.canBreed = false;

                rabbit.energy /= 2;
                rabbit.canBreed = false;

                break;
            }
        }
    }

    /**
     * Handles the death of a rabbit by removing it from its hole and the world.
     *
     * @param world The world from which the rabbit is to be removed.
     */
    private void die(World world) {
        super.die();
        this.hole.removeRabbit(this);
        world.remove(this);
    }

    /**
     * Attempts to dig a hole at the specified location within the provided world.
     * If the rabbit already has a hole or if the location contains a non-blocking entity,
     * the method will return without making any changes.*/
    private void digHole(World world, Location location) {
        if(this.hole != null || world.containsNonBlocking(location)) {
            return;
        }

        this.hole = new RabbitHole(world, location);
        this.hole.addRabbit(this);
    }

    /**
     * Attempts to dig an exit at the specified location within the provided world.
     * If the location contains a non-blocking entity or is null, the method will return without making any changes.
     *
     * @param world The world in which the exit is to be dug.
     * @param*/
    private void digExit(World world, Location location) {
        if(world.containsNonBlocking(location) || location == null) {
            return;
        }

        this.hole = new RabbitHole(world, location, this.hole.getHoles());
    }

    /**
     * Enables the rabbit to enter its hole if it is present and the rabbit is currently
     * located at the hole's position. If these conditions are met, the rabbit is removed
     * from the world and its state is updated to indicate it is inside the hole.
     *
     * @param world The world in which the rabbit exists.
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
     * Handles the rabbit's attempt to exit its hole within the provided world. If the exit location is invalid or
     * obstructed, a new valid random location is determined and an exit is dug there.
     *
     * @param world The world in which the rabbit resides and attempts to exit its hole.
     */
    private void exitHole(World world){
        Location holeLocation = this.hole.getRandomExit(world);
        if(world.getTile(holeLocation) != null /*|| new Random().nextDouble() < 0.2*/) {
            Location newLocation = Utils.getValidRandomLocation(world);
            digExit(world, newLocation);
            holeLocation = newLocation;
        }

        if(holeLocation == null) {
            System.out.println("Forced to stay in hole: " + this);
            return;
        }

        world.setTile(holeLocation, this);
        insideHole = false;
    }

    /**
     * Retrieves display information for the rabbit, including its color and image key.
     *
     * @return a DisplayInformation object representing the rabbit with specific color and image key.
     */
    @Override
    public DisplayInformation getInformation() {
        if(age > 6) {
            return new DisplayInformation(Color.WHITE, "mc_rabbit");
        }
        return new DisplayInformation(Color.WHITE, "mc_rabbit");
    }
}
