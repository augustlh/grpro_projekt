package behaviours.nests;

import behaviours.wolf.Wolf;
import datatypes.Nest;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;

public class WolfCave extends Nest<Wolf> {
    Location entrance;

    public WolfCave(World world, Location location) {
        super(world, location);
        this.entrance = location;
    }

    public Location getEntrance() {
        return entrance;
    }

    /**
     * Provides display information for the WolfCave.
     *
     * @return DisplayInformation that includes the color (black) and the image key ("big-hole") for the BreedingGround.
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.BLACK, "pkmndp-cave");
    }

}
