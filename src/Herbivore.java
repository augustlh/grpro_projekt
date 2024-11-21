public abstract class Herbivore extends Animal {
    public Herbivore(double metabolism, double energyDecay) {
        super(metabolism, energyDecay);
    }

    @Override
    public boolean canEat(Eatable other) {
        return other.getType() == Type.PLANT;
    }
}