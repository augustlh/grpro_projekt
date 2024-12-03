package behaviours;

import datatypes.Organism;
import datatypes.Species;
import itumulator.executable.DisplayInformation;
import itumulator.simulator.Actor;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Fungus extends Organism implements NonBlocking {

    private double energy;
    private double energydecay;

    public Fungus() {
        super(Species.Carcass);

    }

    @Override
    public void act(World world) {

    }

    @Override
    public void onConsume(World world) {

    }

    @Override
    public double getNutritionalValue() {
        return 0;
    }

    @Override
    public DisplayInformation getInformation() {
        return null;
    }

    


}
