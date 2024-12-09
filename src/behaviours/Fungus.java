package behaviours;

import behaviours.plants.Grass;
import datatypes.Organism;
import datatypes.Species;
import help.Utils;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a Fungus, a non-blocking organism that can infest carcasses,
 * consume energy from them, and spread to adjacent carcasses within a certain radius.
 * The fungus consumes energy and spreads until energy is depleted or no carcasses remain to infest.
 */
public class Fungus extends Organism implements NonBlocking {

    private double energy;
    private double energyDecay;
    private int spreadCounter;
    private int spreadRadius;

    private Carcass carcass;

    /**
     * Constructs a new Fungus instance that can infest a carcass,
     * consume energy from it, and potentially spread to nearby carcasses.
     *
     * @param world the world in which the fungus resides.
     * @param carcass the carcass that this fungus will initially infest and consume energy from.
     * @param energyDecay the rate at which the fungus's energy depletes over time.
     */
    public Fungus(World world, Carcass carcass, double energyDecay) {
        super(Species.Carcass);
        this.energyDecay = energyDecay;
        this.carcass = carcass;
        this.carcass.infest();
        this.energy = 30;
        this.spreadCounter = 0;
        this.spreadRadius = 1;
        world.setTile(world.getLocation(carcass),this);
    }

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

        // Spread to other Carcasses
        spreadCounter++;
        if (spreadCounter >= 10) {
            // Infect other carcasses within certain radius
            infestOther(world);
            spreadCounter = 0;
            spreadRadius++;
        }

        // Subtract energy
        energy = energy - energyDecay;

        // Dies if out of energy
        if(energy <= 0) {
            die();
            Location temp = world.getLocation(this);
            world.delete(this);
            new Grass(world,temp);
        }

    }

    public void infestOther(World world) {
        ArrayList<Location> infestLocations = new ArrayList<>(world.getSurroundingTiles(world.getLocation(this), spreadRadius));
        // Call infest method
        for (Location location : infestLocations) {
            if(world.getTile(location) instanceof Carcass c) {
                if(!c.isInfested()){
                    c.infest();
                    c.setFungus(new Fungus(world,c, Utils.random.nextDouble()*2));
                    return;
                }
            }
        }
    }

    @Override
    public void onConsume(World world) {}

    @Override
    public double getNutritionalValue() {
        return 0;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.RED, "mc-mushroom-brown");
    }

    public void setCarcass(Carcass carcass) {
        this.carcass = carcass;
    }


}
