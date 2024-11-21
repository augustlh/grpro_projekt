import itumulator.world.World;

public interface Eatable {
    enum Type {
        PLANT,
        ANIMAL,
    }

    double getEnergy();
    boolean canEat(Eatable other);
    Type getType();
    void eaten(World world);
}