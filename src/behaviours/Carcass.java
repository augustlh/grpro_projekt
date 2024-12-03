package behaviours;

import datatypes.Organism;
import datatypes.Species;
import help.Utils;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;

public class Carcass extends Organism {
    private double energy;
    private int remainingUses;


    public Carcass(World world, Location location, double energy) {
        super(Species.Carcass);
        this.energy = energy;
        remainingUses = 3;

        if(!world.isTileEmpty((location))) {
            world.delete(world.getTile(location));
        }

        world.setTile(location, this);
    }

    public Carcass(World world, Location location) {
        super(Species.Carcass);
        this.energy = Utils.random.nextDouble(50, 75);
        remainingUses = 3;
        world.setTile(location, this);
    }


    @Override
    public void onConsume(World world) {
        remainingUses--;
        this.energy = this.energy / remainingUses;

        if(remainingUses == 0) {
            Location temp = world.getLocation(this);
            world.remove(this);
        }
    }

    @Override
    public double getNutritionalValue() {
        return this.energy / remainingUses;
    }


    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.CYAN, "carcass");
    }


    @Override
    public void act(World world) {
        this.energy--;
        if(this.energy <= 0) {
            onConsume(world);
        }
    }
}
