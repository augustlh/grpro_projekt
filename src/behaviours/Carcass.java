package behaviours;

import behaviours.plants.Grass;
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
    private Fungus fungus;


    public Carcass(World world, Location location, double energy) {
        super(Species.Carcass);
        this.energy = energy;
        remainingUses = 3;

        if(!world.isTileEmpty((location)) || world.containsNonBlocking(location)) {
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

    @Override
    public double getNutritionalValue() {
        return this.energy / remainingUses;
    }


    @Override
    public DisplayInformation getInformation() {
        if(isInfested()){
            return new DisplayInformation(Color.GRAY);
        }
        return new DisplayInformation(Color.CYAN, "carcass");
    }




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

    public void setFungus(Fungus fungus){
        this.fungus=fungus;
    }

    public int getRemainingUses(){
        return remainingUses;
    }

}
