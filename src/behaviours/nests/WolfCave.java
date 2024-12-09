package behaviours.nests;

import behaviours.wolf.Wolf;
import datatypes.Nest;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;

/**
 * The WolfCave class represents a wolf cave in the world and extends the {@link Nest Nest class}.
 * Used by wolves to sleep and reproduce during nighttime.
 */
public class WolfCave extends Nest<Wolf> {
    Location entrance;

    /**
     * Constructs a WolfCave instance with the specified world and location.
     *
     * @param world The world in which the WolfCave exists.
     * @param location The location of the entrance of the WolfCave.
     */
    public WolfCave(World world, Location location) {
        super(world, location);
        this.entrance = location;
    }

    /**
     * Retrieves the entrance location of the WolfCave.
     *
     * @return The location of the entrance of the WolfCave.
     */
    public Location getEntrance() {
        return entrance;
    }

    /**
     * Provides display information for the WolfCave.
     *
     * @return The display information of the WolfCave.
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.BLACK, "pkmndp-cave");
    }

}
