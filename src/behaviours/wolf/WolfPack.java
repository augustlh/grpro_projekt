package behaviours.wolf;

import behaviours.nests.WolfCave;
import datatypes.Organism;
import help.Utils;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The WolfPack class represents a pack of wolves in a given world. It handles the
 * creation of the pack, assigns an alpha wolf, and updates the pack when necessary.
 */
public class WolfPack {
    private WolfCave cave;
    private final List<Wolf> wolves;
    private Wolf alphaWolf;

    public WolfPack(World world, Location location, int quantity) {
        this.wolves = new ArrayList<>();
        this.cave = null;
        this.alphaWolf = null;

        for (int i = 0; i < quantity; i++) {
            wolves.add(new Wolf(world, Utils.getSurroundingEmptyTiles(location, world),this));
        }

        updatePack();
    }

    public void updatePack() {
        wolves.removeIf(wolf -> !wolf.isAlive());

        if(this.alphaWolf != null) {
            this.alphaWolf.toggleAlpha();
        }

        this.alphaWolf = wolves.stream()
                .filter(Organism::isAlive)
                .max(Comparator.comparingDouble(Wolf::getStrength))
                .orElse(null);

        if (this.alphaWolf != null) {
            this.alphaWolf.toggleAlpha();
        }
    }

    public void addWolf(Wolf wolf) {
        this.wolves.add(wolf);

        if(this.cave != null) {
            this.cave.addAnimal(wolf);
        }
    }

    public Wolf getAlphaWolf() {
        return this.alphaWolf;
    }

    public void setCave(WolfCave cave) {
        this.cave = cave;

        for(Wolf wolf : wolves) {
            cave.addAnimal(wolf);
        }
    }

    public WolfCave getCave() {
        return this.cave;
    }

    //    private Cave cave;
//
//    private final ArrayList<Wolf> wolves;
//    private Wolf alpha;
//
//    /**
//     * Constructs a new WolfPack instance.
//     *
//     * @param world the world in which the wolf pack resides
//     * @param location the spawn location for the wolf pack
//     * @param quantity the number of wolves to be included in the pack
//     */
//    public WolfPack(World world, Location location, int quantity) {
//        this.wolves = new ArrayList<>();
//        this.cave = null;
//        setupPack(world, location, quantity);
//        setAlpha();
//    }
//
//    /**
//     * Initializes the wolf pack by creating the specified quantity of wolves and
//     * adding them to the pack.
//     *
//     * @param world the world in which the wolves will be spawned
//     */
//    private void setupPack(World world, Location location, int quantity) {
//        for (int i = 0; i < quantity; i++) {
//            wolves.add(new Wolf(world, Utils.getSurroundingEmptyTiles(location, world),this));
//        }
//    }
//
//    public Cave getCave() {
//        return this.cave;
//    }
//
//    public void setCave(Cave cave) {
//        if (this.cave == null) {
//            return;
//        }
//
//        this.cave = cave;
//    }
//
   /**
    * Retrieves the list of wolves that formS the pack.
   *
    * @return a list of Wolf objects representing the wolves in the pack
    */
   public List<Wolf> getPack() {
       return wolves;
   }
//
//    /**
//     * Sets the alpha wolf within the wolf pack. The first wolf in the list of wolves is assigned as the alpha.
//     * The alpha status of this wolf is set to true. Additionally, every wolf in the pack has its reference to
//     * the pack updated.
//     */
//    public void setAlpha() {
//        this.alpha = wolves.getFirst();
//        wolves.getFirst().setThisAlpha(true);
//        for (Wolf wolf : wolves) {
//            wolf.setPack(wolves);
//        }
//    }
//
//    /**
//     * Retrieves the alpha wolf of the wolf pack.
//     *
//     * @return the Wolf object representing the alpha wolf of the pack
//     */
//    public Wolf getAlpha() {
//        return alpha;
//    }
//
//    /**
//     * Updates the status of the wolf pack by removing the current alpha wolf. If there are
//     * remaining wolves in the pack after removal, assigns a new alpha wolf from the remaining pack.
//     */
//    public void updatePack() {
//        wolves.remove(alpha);
//        if(wolves.isEmpty()) {
//            return;
//        }
//        setAlpha();
//    }

}
