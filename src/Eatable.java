import itumulator.world.World;

public interface Eatable {
    enum Type {
        PLANT,
        ANIMAL,
    }

    double onEaten();
    boolean canEat(Eatable other);
    Type getType();
}