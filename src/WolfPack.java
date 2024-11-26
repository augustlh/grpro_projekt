import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;

public class WolfPack {

    private ArrayList<Wolf> wolves;
    private Location spawnLocation;
    private int quantity;
    private Wolf alpha;

    public WolfPack(World world, Location location, int quantity) {
        this.spawnLocation = location;
        this.quantity = quantity;
        this.wolves = new ArrayList<>();
        setupPack(world);
        setAlpha();
        for (Wolf wolf : wolves) {
            wolf.setPack(wolves);
        }
    }

    private void setupPack(World world) {
        for (int i = 0; i < quantity; i++) {
            wolves.add(new Wolf(world, Utils.getSurroundingEmptyTiles(spawnLocation, world)));
        }
    }

    public List getPack() {
        return wolves;
    }

    private void setAlpha() {
        this.alpha = wolves.getFirst();
        wolves.getFirst().setThisAlpha(true);
    }

    public Wolf getAlpha() {
        return alpha;
    }


}
