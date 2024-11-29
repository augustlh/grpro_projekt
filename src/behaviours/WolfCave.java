package behaviours;

import datatypes.BreedingGround;
import itumulator.executable.DisplayInformation;

import java.awt.*;

public class WolfCave extends BreedingGround {

    /**
     * Provides display information for the WolfCave.
     *
     * @return DisplayInformation that includes the color (black) and the image key ("big-hole") for the BreedingGround.
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.BLACK, "hole-big");
    }

}
