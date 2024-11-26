import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RabbitHole implements NonBlocking, DynamicDisplayInformationProvider {
    private final List<RabbitHole> holes;
    private final Location location;

    public RabbitHole(World world, Location location) {
        world.setTile(location, this);
        this.holes = new ArrayList<>();
        this.holes.add(this);
        this.location = location;
    }

    public RabbitHole(World world, Location location, List<RabbitHole> holes) {
        world.setTile(location, this);
        this.holes = holes;
        this.location = location;
        this.holes.add(this);
    }

    public Location getRandomExit(World world) {
        return this.holes.get(new Random().nextInt(this.holes.size())).getLocation();
    }

    public Location getLocation() {
        return this.location;
    }

    public List<RabbitHole> getHoles() {
        return holes;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.BLACK, "hole");
    }
}
