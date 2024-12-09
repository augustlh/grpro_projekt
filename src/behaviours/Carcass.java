package behaviours;

import behaviours.plants.Grass;
import datatypes.Organism;
import datatypes.Species;
import help.Utils;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;

/**
 * Represents a carcass within the world that provides energy and can be consumed
 * for a limited number of uses. It can be infested with fungus, which affects its behavior.
 */
public class Carcass extends Organism {
    private double energy;
    private int remainingUses;
    private Fungus fungus;

    /**
     * Handles carcasses constructed upon the death of an animal.
     * Constructs a new Carcass instance with specified energy.
     * Constructor also sets a fixed number of uses,
     * which represents how many times the carcass can be eaten from.
     * Whenever a carcass is instantiated, delete whatever non-blocking
     * object that occupies the same space.
     * The carcass is placed in the world at the specified location.
     *
     * @param world the world in which the carcass resides
     * @param location the location of the carcass in the world
     * @param energy the initial energy value of the carcass
     */
    public Carcass(World world, Location location, double energy) {
        super(Species.Carcass);
        this.energy = energy;
        remainingUses = 3;

        if(!world.isTileEmpty((location)) || world.containsNonBlocking(location)) {
            world.delete(world.getTile(location));
        }

        world.setTile(location, this);
    }

    /**
     * Handles carcasses constructed by order of the input files.
     * Constructs a new Carcass instance with a random energy value between 50 and 75.
     * Constructor also sets a fixed number of uses,
     * which represents how many times the carcass can be eaten from.
     * The carcass is placed in the world at the specified location.
     *
     * @param world the world in which the carcass resides
     * @param location the location of the carcass in the world
     */
    public Carcass(World world, Location location) {
        super(Species.Carcass);
        this.energy = Utils.random.nextDouble(50, 75);
        remainingUses = 3;
        world.setTile(location, this);
    }

    /**
     * Consumes the carcass, reducing its energy and remaining uses.
     * Deletes reference to the fungus living in the carcass (if there is any).
     * When it's uses are depleted, deletes the carcass from the world.
     *
     * @param world the world in which the carcass resides
     */
    @Override
    public void onConsume(World world) {
        this.energy = this.energy / remainingUses;
        remainingUses--;

        if(remainingUses == 0) {
            if(isInfested()){
                fungus.setCarcass(null);
                world.delete(this);
                return;
            }
            world.delete(this);
        }
    }

    /**
     * Returns the nutritional value of the carcass, based on its remaining uses.
     *
     * @return the nutritional value of the carcass
     */
    @Override
    public double getNutritionalValue() {
        return this.energy / remainingUses;
    }

    /**
     * Performs the action for the carcass in the world, including energy reduction
     * and deleting itself when energy reaches zero.
     * Deletes any grass that might have spread to its location.
     *
     * @param world the world in which the carcass resides
     */
    @Override
    public void act(World world) {
        if(this.isDead){
            return;
        }

        if(world.getNonBlocking(world.getLocation(this)) instanceof Grass g){
            world.delete(g);
        }

        this.energy--;
        if(this.energy <= 0) {
            super.die();
            onConsume(world);
        }
    }

    /**
     * Sets the fungus living in the carcass.
     *
     * @param fungus the fungus living in the carcass
     */
    public void setFungus(Fungus fungus){
        this.fungus=fungus;
    }

    /**
     * Gets the remaining uses of the carcass before it is fully consumed.
     *
     * @return the remaining uses of the carcass
     */
    public int getRemainingUses(){
        return remainingUses;
    }

    /**
     * Provides display information about the carcass, with specific color and image indications
     * if the carcass is infested with a fungus or not.
     *
     * @return the display information of the carcass
     */
    @Override
    public DisplayInformation getInformation() {
        if(isInfested()){
            return new DisplayInformation(Color.GRAY);
        }
        return new DisplayInformation(Color.CYAN, "carcass");
    }

}
