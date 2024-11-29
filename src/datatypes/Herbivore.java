package datatypes;

public abstract class Herbivore extends Animal {

    public Herbivore(Species species, double metabolism, double energyDecay, int searchRadius) {
        super(species, metabolism, energyDecay, searchRadius);
    }

}
