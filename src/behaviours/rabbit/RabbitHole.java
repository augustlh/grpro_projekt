package behaviours.rabbit;

import datatypes.BreedingGround;
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
public class RabbitHole extends BreedingGround {
    private final List<RabbitHole> holes;
    private final List<Rabbit> rabbits;
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
        world.setTile(location, this);
        this.holes = new ArrayList<>();
        this.holes.add(this);
        this.location = location;
        rabbits = new ArrayList<>();
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
        world.setTile(location, this);
        this.holes = holes;
        this.location = location;
        this.holes.add(this);
        this.rabbits = this.holes.getFirst().getRabbits();
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
     * Retrieves the list of rabbits currently in the rabbit hole.
     *
     * @return A list of Rabbit objects present in the rabbit hole.
     */
    public List<Rabbit> getRabbits() {
        return this.rabbits;
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
     * Adds a Rabbit to the current list of rabbits in the RabbitHole.
     *
     * @param rabbit The Rabbit to be added to the RabbitHole.
     */
    public void addRabbit(Rabbit rabbit) {
        this.rabbits.add(rabbit);
    }

    /**
     * Removes the specified Rabbit from the list of rabbits in the RabbitHole.
     *
     * @param rabbit The Rabbit to be removed from the RabbitHole.
     */
    public void removeRabbit(Rabbit rabbit) {
        this.rabbits.remove(rabbit);
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
