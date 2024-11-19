import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

import java.awt.Color;
import java.util.List;

public class Hole implements NonBlocking, DynamicDisplayInformationProvider {
    private Location entrance;
    private Location exit;
    List<Object> contains;

    public Hole(Location entrance) {
        this.entrance = entrance;
    }

    public Hole(Location entrance, Location exit) {
        this.entrance = entrance;
        this.exit = exit;
    }
    
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.gray, "hole");
    }
}
