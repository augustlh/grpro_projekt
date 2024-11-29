package behaviours;

import datatypes.Animal;
import datatypes.Carnivore;
import datatypes.Organism;
import datatypes.Species;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

/**
 * The Wolf class represents a wolf, extending the Animal class. A wolf can form part of a pack,
 * has an alpha status, and interacts with the world.
 */
public class Wolf extends Carnivore {

    private boolean alpha;
    private List<Wolf> pack;
    private WolfPack wolfPack;

    /**
     * Constructs a new Wolf object within the given world at the specified location and
     * associates it with the provided wolf pack. Initializes the wolf with a random size
     * and energy level and sets default values for its alpha status and pack.
     *
     * @param world The world in which the wolf resides.
     * @param location The initial location of the wolf within the world.
     * @param wolfPack The wolf pack to which this wolf belongs.
     */
    public Wolf(World world, Location location, WolfPack wolfPack) {
        super(Species.Wolf, new Random().nextDouble(), new Random().nextDouble(1, 2), 1);
        world.setTile(location, this);
        this.energy = 25;
        alpha = false;
        pack = null;
        this.wolfPack = wolfPack;
    }

    /**
     * Performs the actions of the wolf in the given world. The wolf ages, eats, and moves according
     * to its role within the pack (alpha or non-alpha).
     *
     * @param world The world in which the wolf performs its actions.
     */
    @Override
    public void act(World world) {
        // Stops act if dead
        age(world);
        if(isDead){
            return;
        }

        // Eats nearby food
        List<Location> neighbours = new ArrayList<>(world.getSurroundingTiles());
        for (Location loc:neighbours){
            if(world.getTile(loc) instanceof Organism o) {
               if(this.canEat(o)){
                    this.consume(o,world);
                    world.move(this,loc);
                    return;
               }
            }
        }
        // DOESN'T WORK
        // Fight other wolf packs

        for (Location loc : neighbours) {
            if(world.getTile(loc) instanceof Wolf w && w.getPack() != this.wolfPack) {
                System.out.println("Fighting");
                if(new Random().nextDouble() < 0.5) {
                    w.onConsume(world);
                    w.wolfPack.updatePack();
                    return;
                }
                else {
                    this.onConsume(world);
                    wolfPack.updatePack();
                    return;
                }
            }
        }

        // Alpha moves
        if(alpha) {
            this.wander(world);
            return;
        } else {
            this.pursue(world, world.getLocation(wolfPack.getAlpha()));
            return;
        }


    }

    /**
     * Ages the wolf by incrementing its age and decrementing its energy. If the wolf's energy
     * depletes to zero or it reaches a certain age threshold, it will die. If the wolf is
     * the alpha, it updates the pack's status before dying.
     *
     * @param world The world in which the wolf resides.
     */
    private void age(World world){
        this.age ++;
        //this.energy -= energyDecay;
        if (energy <=0 || age >=100){
            if (alpha){
                wolfPack.updatePack();
            }
            die();
            world.delete(this);
        }
    }

    /**
     * Sets the alpha status of the wolf.
     *
     * @param b A boolean value indicating whether the wolf is the alpha (true) or not (false).
     */
    public void setThisAlpha(boolean b) {
        this.alpha = b;
    }

    /**
     * Sets the pack of wolves to which this wolf belongs.
     *
     * @param wolves The list of wolves representing the pack.
     */
    public void setPack(List<Wolf> wolves) {
        this.pack=wolves;
    }


    /**
     * Retrieves the wolf pack that this wolf belongs to.
     *
     * @return the WolfPack object representing the wolf's pack
     */
    public WolfPack getPack() {return this.wolfPack;}

    /**
     * Provides display information for the wolf object, including its color and the image key.
     *
     * @return a DisplayInformation object containing color as Color.GRAY and image key as "mc_wolf"
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.GRAY, "mc-wolf-large");
    }



}
