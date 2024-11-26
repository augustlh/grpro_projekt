import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * The WolfPack class represents a pack of wolves in a given world. It handles the
 * creation of the pack, assigns an alpha wolf, and updates the pack when necessary.
 */
public class WolfPack {

    private ArrayList<Wolf> wolves;
    private Location spawnLocation;
    private int quantity;
    private Wolf alpha;

    /**
     * Constructs a new WolfPack instance.
     *
     * @param world the world in which the wolf pack resides
     * @param location the spawn location for the wolf pack
     * @param quantity the number of wolves to be included in the pack
     */
    public WolfPack(World world, Location location, int quantity) {
        this.spawnLocation = location;
        this.quantity = quantity;
        this.wolves = new ArrayList<>();
        setupPack(world);
        setAlpha();
    }

    /**
     * Initializes the wolf pack by creating the specified quantity of wolves and
     * adding them to the pack.
     *
     * @param world the world in which the wolves will be spawned
     */
    private void setupPack(World world) {
        for (int i = 0; i < quantity; i++) {
            wolves.add(new Wolf(world, Utils.getSurroundingEmptyTiles(spawnLocation, world),this));
        }
    }

    /**
     * Retrieves the list of wolves that form the pack.
     *
     * @return a list of Wolf objects representing the wolves in the pack
     */
    public List getPack() {
        return wolves;
    }

    /**
     * Sets the alpha wolf within the wolf pack. The first wolf in the list of wolves is assigned as the alpha.
     * The alpha status of this wolf is set to true. Additionally, every wolf in the pack has its reference to
     * the pack updated.
     */
    public void setAlpha() {
        this.alpha = wolves.getFirst();
        wolves.getFirst().setThisAlpha(true);
        for (Wolf wolf : wolves) {
            wolf.setPack(wolves);
        }
    }

    /**
     * Retrieves the alpha wolf of the wolf pack.
     *
     * @return the Wolf object representing the alpha wolf of the pack
     */
    public Wolf getAlpha() {
        return alpha;
    }

    /**
     * Updates the status of the wolf pack by removing the current alpha wolf. If there are
     * remaining wolves in the pack after removal, assigns a new alpha wolf from the remaining pack.
     */
    public void updatePack() {
        wolves.remove(alpha);
        if(wolves.isEmpty()) {
            return;
        }
        setAlpha();
    }

}
