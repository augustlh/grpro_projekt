import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

import java.awt.Color;

public class Hole implements NonBlocking, DynamicDisplayInformationProvider {
    Location location;
    RabbitHole rabbitHole;

    public Hole(Location location, RabbitHole rabbitHole) {
        this.location = location;
        this.rabbitHole = rabbitHole;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.BLACK, "hole");
    }

    public Location getLocation() {
        return location;
    }
}