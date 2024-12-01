package behaviours.wolf;

import datatypes.BreedingGround;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;

public class Cave extends BreedingGround {
    Location entrance;

    public Cave(World world, Location location) {
        world.setTile(location, this);
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
        return new DisplayInformation(Color.BLACK, "cave");
    }

}
