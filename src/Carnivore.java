/**
 * The Carnivore class represents a type of animal that exclusively eats other animals.
 * It extends the base Animal class and provides specific implementations related to
 * the carnivorous dietary habits.
 */
public abstract class Carnivore extends Animal {
    public Carnivore(double metabolism, double energyDecay) {
        super(metabolism, energyDecay);
    }

    @Override
    public boolean canEat(Eatable other) {
        return other.getType() == Type.ANIMAL;
    }
}