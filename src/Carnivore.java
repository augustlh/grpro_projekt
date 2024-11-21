public abstract class Carnivore extends Animal {
    public Carnivore(double metabolism, double energyDecay) {
        super(metabolism, energyDecay);
    }

    @Override
    public boolean canEat(Eatable other) {
        return other.getType() == Type.ANIMAL;
    }
}