package behaviours;

import behaviours.plants.Grass;
import datatypes.Organism;
import datatypes.Species;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a Fungus, a non-blocking organism that can infest carcasses,
 * consume energy from them, and spread to adjacent carcasses within a certain radius.
 * The fungus consumes energy and spreads until energy is depleted or no carcasses remain to infest.
 */
public class Fungus extends Organism {

    private double energy;
    private double energyDecay;
    private int spreadCounter;
    private int spreadRadius;

    private Carcass carcass;

    /**
     * Constructs a new Fungus instance that can infest a carcass,
     * consume energy from it, and potentially spread to nearby carcasses.
     *.
     * @param carcass the carcass that this fungus will initially infest and consume energy from.
     * @param energyDecay the rate at which the fungus's energy depletes over time.
     */
    public Fungus(Carcass carcass, double energyDecay) {
        super(Species.Fungus);
        this.energyDecay = energyDecay;
        this.carcass = carcass;
        this.energy = 30;
        this.spreadCounter = 0;
        this.spreadRadius = 1;
    }

    /**
     * The act method of the fungus. Consumes a carcass if it is infecting a carcass, else it decays nad tries to spread. If it dies, it spawns grass.
     *
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void act(World world) {
        // Stop act if dead
        if(this.isDead){
            return;
        }

        // Gain energy if in carcass
        if(this.carcass != null) {
            energy = energy + carcass.getNutritionalValue();
            carcass.onConsume(world);
            return;
        }

        infestOther(world);


        // Subtract energy
        energy = energy - energyDecay;

        // Dies if out of energy
        if(energy <= 0) {
            die();
            onConsume(world);
        }

    }

    public void infestOther(World world) {
        ArrayList<Location> infestLocations = new ArrayList<>(world.getSurroundingTiles(world.getLocation(this), spreadRadius));
        // Call infest method
        for (Location location : infestLocations) {
            if(world.getTile(location) instanceof Carcass c) {
                if(!c.isInfested()){
                    c.infest();
                    return;
                }
            }
        }

        spreadCounter++;
        if (spreadCounter % 5 == 0) {
            spreadRadius++;
        }
    }

    /**
     * Implement logic for when the fungys is consumed / dies in the simulation.
     *
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void onConsume(World world) {
        Location temp = world.getLocation(this);
        world.delete(this);

        if(!world.containsNonBlocking(temp)) {
            new Grass(world,temp);
        }
    }

    /**
     * Retrieves the nutritional value of this object.
     *
     * @return the nutritional value, which is 0 for this implementation.
     */
    @Override
    public double getNutritionalValue() {
        return 0;
    }

    /**
     * Provides the display information for this object.
     *
     * @return a {@link DisplayInformation} object containing color and icon details,
     *         with the color set to red and the icon identifier "mc-mushroom-brown".
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.RED, "mc-mushroom-brown");
    }


    /**
     * Sets the carcass associated with this object.
     *
     * @param carcass the {@link Carcass} object to associate with this object.
     */
    public void setCarcass(Carcass carcass) {
        this.carcass = carcass;
    }

    /**
     * Spawns fungus on carcass position when carcass is consumed
     * @param world the world in which the fungus resides
     * @param location the initial location of the fungus within the world.
     */
    public void spawn(World world, Location location) {
        this.carcass = null;
        world.setTile(location, this);
    }


    /**
     * Determines if this organism can be eaten by another organism.
     * Acts as a helping method for certain subclasses.
     * @return true if the organism can be eaten, false otherwise.
     */
    @Override
    public boolean canBeEaten() {
        return false;
    }
}