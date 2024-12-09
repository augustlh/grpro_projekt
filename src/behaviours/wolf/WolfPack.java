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
 * The WolfPack class represents a pack of wolves. It handles the creation
 * of the wolves in the pack, assigns an alpha wolf, and updates the pack when necessary.
 */
public class WolfPack {
    private WolfCave cave;
    private final List<Wolf> wolves;
    private Wolf alphaWolf;

    /**
     * Constructs a new WolfPack in the world at a specified location with a specified number of wolves.
     *
     * @param world the world in which the wolf pack resides.
     * @param location the initial location where the wolves in the pack will be spawned.
     * @param quantity the number of wolves to be included in the pack.
     */
    public WolfPack(World world, Location location, int quantity) {
        this.wolves = new ArrayList<>();
        this.cave = null;
        this.alphaWolf = null;

        for (int i = 0; i < quantity; i++) {
            wolves.add(new Wolf(world, Utils.getSurroundingEmptyTiles(location, world),this));
        }

        updatePack();
    }

    /**
     * Updates the state of the wolf pack by doing the following:
     * 1. Removes any wolves from the pack that are not alive.
     * 2. Toggles the alpha status of the current alpha wolf, if present.
     * 3. Selects the strongest living wolf from the pack as the new alpha wolf.
     * 4. Toggles the alpha status of the newly selected alpha wolf, if one has been selected.
     * The method ensures that only living wolves remain in the pack and the wolf with the highest strength
     * becomes the new alpha.
     */
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

    /**
     * Adds a wolf to the wolf pack and, if the wolf pack is associated with a cave,
     * also adds the wolf to the cave.
     *
     * @param wolf the Wolf to be added to the wolf pack.
     */
    public void addWolf(Wolf wolf) {
        this.wolves.add(wolf);

        if(this.cave != null) {
            this.cave.addAnimal(wolf);
        }
    }

    /**
     * Retrieves the current alpha wolf of the wolf pack.
     *
     * @return the alpha Wolf of the pack.
     */
    public Wolf getAlphaWolf() {
        return this.alphaWolf;
    }

    /**
     * Sets the specified cave as the nest for this wolf pack.
     * Additionally, registers all wolves in the pack to the given cave.
     *
     * @param cave the WolfCave to be associated with this wolf pack.
     */
    public void setCave(WolfCave cave) {
        this.cave = cave;

        for(Wolf wolf : wolves) {
            cave.addAnimal(wolf);
        }
    }

    /**
     * Retrieves the cave associated with the wolf pack.
     *
     * @return the WolfCave associated with this wolf pack.
     */
    public WolfCave getCave() {
        return this.cave;
    }

    /**
     * Retrieves the list of wolves that formS the pack.
     *
     * @return a list of Wolf objects representing the wolves in the pack
     */
    public List<Wolf> getPack() {
        return wolves;
    }


    /*
    ****************
    *              *
    *   Old code:  *
    *              *
    ****************
     */

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
