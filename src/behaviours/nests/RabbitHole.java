package behaviours.nests;

import behaviours.rabbit.Rabbit;
import datatypes.Nest;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The RabbitHole class represents a rabbit hole in a world tile that can contain rabbits.
 * Rabbit holes are connected, and a rabbit can travel between connected holes.
 *
 * This class implements NonBlocking, allowing other actors/objects to exist on top of it,
 * and DynamicDisplayInformationProvider to dynamically control its display information.
 */
public class RabbitHole extends Nest<Rabbit> {
    private final List<RabbitHole> holes;
    private final Location location;

    /**
     * Creates a new RabbitHole at the specified location in the given world.
     * The RabbitHole is added to the world's tile at the specified location
     * and initializes its own list of connected holes and rabbits.
     *
     * @param world The world where the RabbitHole is created.
     * @param location The specific location in the world where the RabbitHole is placed.
     */
    public RabbitHole(World world, Location location) {
        super(world, location);
        this.holes = new ArrayList<>();
        this.holes.add(this);
        this.location = location;
    }

    /**
     * Creates a new RabbitHole at the specified location in the given world and links it with
     * a list of connected RabbitHoles. The RabbitHole is added to the world's tile at the
     * specified location and initializes its rabbits and connection to other RabbitHoles.
     *
     * @param world The world where the RabbitHole is created.
     * @param location The specific location in the world where the RabbitHole is placed.
     * @param holes A list of already connected RabbitHoles to which this RabbitHole will be linked.
     */
    public RabbitHole(World world, Location location, List<RabbitHole> holes) {
        super(world, location);
        this.holes = holes;
        this.location = location;
        this.holes.add(this);
        this.animals = this.holes.getFirst().getAnimals();
    }

    /**
     * Selects a random connected RabbitHole and returns its location.
     *
     * @param world The world in which the RabbitHole exists.
     * @return The location of one of the connected RabbitHoles.
     */
    public Location getRandomExit(World world) {
        return this.holes.get(new Random().nextInt(this.holes.size())).getLocation();
    }


    /**
     * Retrieves the location of the rabbit hole.
     *
     * @return The location of the rabbit hole.
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Retrieves the list of holes connected to this rabbit hole.
     *
     * @return A list of RabbitHole objects that are connected to this rabbit hole.
     */
    public List<RabbitHole> getHoles() {
        return holes;
    }

    /**
     * Provides display information for the RabbitHole.
     *
     * @return DisplayInformation that includes the color (black) and the image key ("hole") for the RabbitHole.
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.BLACK, "hole");
    }
}
